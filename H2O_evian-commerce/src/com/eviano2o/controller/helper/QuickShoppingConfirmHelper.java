package com.eviano2o.controller.helper;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.eviano2o.bean.weixin.QuickShoppingGoods;
import com.eviano2o.bean.weixin.QuickShoppingListDiscountModel;
import com.eviano2o.bean.weixin.QuickShoppingListModel;
import com.eviano2o.util.DoubleFormatUtil;
import com.eviano2o.util.SessionConstantDefine;
import com.eviano2o.util.ShopBuyTimeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QuickShoppingConfirmHelper extends BaseControllerHelper {
	private static final Logger logger = LoggerFactory.getLogger(QuickShoppingConfirmHelper.class);
	
	public QuickShoppingConfirmHelper(Model model, HttpServletRequest request) {
		super(model, request);
	}
	
	@Override
	public void Init() {
		//_request.getSession().removeAttribute(SessionConstantDefine.QUICKCART);
		String quickSpJson = _request.getParameter("quickSpJson");
		if(StringUtils.isEmpty(quickSpJson)){
			_model.addAttribute("msg", "没有选择任何商品");
			_result = formatJsonResult("E90000", "没有选择任何商品");
			return;
		}		
		//用户提交的商品
		List<QuickShoppingListModel> userSubmitSp = new Gson().fromJson(quickSpJson, new TypeToken<List<QuickShoppingListModel>>() {}.getType());
		if(userSubmitSp == null || userSubmitSp.size() == 0){
			_model.addAttribute("msg", "购物车中没有商品!");
			//logger.error("（1）QuickShoppingConfirmHelper："+quickSpJson);
			_result = formatJsonResult("E90000", "购物车中没有商品!");
			return;
		}
		//用户数据保存的商品
		List<QuickShoppingListModel> userQuickSp =weiXinService.getQuickShoppingInfo(getSessionClientIdentityCode(),getSessionWXAppId());
		if(userQuickSp == null || userQuickSp.size() == 0){
			_model.addAttribute("msg", "没有快速订购商品!");
			_result = formatJsonResult("E90000", "没有快速订购商品!");
			return;
		}
		checkUserSubmitSp(userSubmitSp, userQuickSp);
		_model.addAttribute("userSPInfo", quickSpJson);
	}
	
	private void checkUserSubmitSp(List<QuickShoppingListModel> userSubmitSp, List<QuickShoppingListModel> userQuickSp){
		Double totalMoney = 0.00;
		Integer totalTicket = 0;
		//购物车总电子票数
		Integer totalETicket = 0;
		
		//是否有电子票套餐的购买
		Boolean hashTicket = false;
		
		//是否必须在线支付
		Boolean isOnlinePay = false;
		
		//电子票计算，防止同一商品在不同的店铺购买，加起来的数量超出用户的电子票数量
		Hashtable<Integer, Integer> checkDianZiPiaoNum = new Hashtable<Integer, Integer>();
		
		List<QuickShoppingListModel> userCheckSp = new ArrayList<QuickShoppingListModel>();
		for(QuickShoppingListModel ssp : userSubmitSp){
			for(QuickShoppingListModel qsp : userQuickSp){
				if(ssp.getShopId().intValue() == qsp.getShopId().intValue()){
					qsp.setShopProductMoney(qsp.getShopProductMoney() == null ? 0.00 : qsp.getShopProductMoney());
					qsp.setShopProductTicket(qsp.getShopProductTicket() == null ? 0 : qsp.getShopProductTicket());
					qsp.setShopProductETicket(qsp.getShopProductETicket() == null ? 0 : qsp.getShopProductETicket());
					qsp.setShopProductQuantity(qsp.getShopProductQuantity() == null ? 0 : qsp.getShopProductQuantity());
					
					List<QuickShoppingGoods> carSP = new ArrayList<QuickShoppingGoods>();
					String code_no = "none";
					for(QuickShoppingGoods shopSSP : ssp.getHabits()){
						for(QuickShoppingGoods shopQSP : qsp.getHabits()){
							if(shopSSP.getPid().intValue() == shopQSP.getPid().intValue()){
								shopQSP.setFpid(shopSSP.getFpid());
								shopQSP.setMaxnum(shopSSP.getMaxnum());
								shopQSP.setBuyType(shopSSP.getBuyType());
								//System.out.println(shopSSP.getIfTicket());
								//if((shopSSP.getIfTicket().intValue() != shopQSP.getIfTicket().intValue() && shopQSP.getIfTicket().intValue() != 2) 
								//		|| (shopSSP.getIfTicket().intValue()!= 1 && shopSSP.getIfTicket().intValue()!= 3))
								//{
									//_model.addAttribute("msg", shopQSP.getPname() + " 结算方式错误!");
									//_result = formatJsonResult("E90000", "结算方式错误!");
									//return;
								//}
								
								if(shopQSP.getLinepay() == true){
									isOnlinePay = true;
								}

								if(shopSSP.getBuyType().intValue() == 2)
								{
									if(shopQSP.getSurplusNum() == 0 && shopSSP.getFpid() == 0){
										_model.addAttribute("msg", shopQSP.getPname() + " 结算方式不能是电子票结算!");
										_result = formatJsonResult("E90000", "结算方式不能是电子票结算!");
										return;
									}
									
									if(checkDianZiPiaoNum.contains(shopQSP.getPid())){
										if(shopSSP.getNumber().intValue() + checkDianZiPiaoNum.get(shopQSP.getPid()).intValue() > shopQSP.getSurplusNum().intValue()){
											_model.addAttribute("msg", shopQSP.getPname() + " 选购数量超出已有电子票数量!!");
											_result = formatJsonResult("E90000", "选购数量超出已有电子票数量!!");
											return;
										}
										
										checkDianZiPiaoNum.put(shopQSP.getPid(), shopSSP.getNumber().intValue() + checkDianZiPiaoNum.get(shopQSP.getPid()).intValue());
									}else{
										if((shopSSP.getNumber().intValue() > shopQSP.getSurplusNum().intValue() && shopSSP.getFpid() == 0)
												&& (shopSSP.getNumber().intValue() > shopQSP.getMaxnum() && shopSSP.getFpid() > 0) ){
											_model.addAttribute("msg", shopQSP.getPname() + " 选购数量超出已有电子票数量!");
											_result = formatJsonResult("E90000", "选购数量超出已有电子票数量!");
											return;
										}
										
										checkDianZiPiaoNum.put(shopQSP.getPid(), shopSSP.getNumber().intValue());
									}
								}
								
								if(shopSSP.getBuyType().intValue() == 3 && shopQSP.getIfTicket() == false)
								{
									_model.addAttribute("msg", shopQSP.getPname() + " 结算方式不能是水票结算!");
									_result = formatJsonResult("E90000", "结算方式不能是水票结算!");
									return;
								}
								
								
								
								//if(shopSSP.getNumber().intValue() > shopQSP.getRepertoryNum().intValue()){
								//	_model.addAttribute("msg", shopQSP.getPname() + " 数量超过库存!");
								//	_result = formatJsonResult("E90000", "数量超过库存!");
								//	return;
								//}
								
								shopQSP.setIfTicket(shopSSP.getIfTicket());
								shopQSP.setNumber(shopSSP.getNumber());
								
								if(shopSSP.getBuyType().intValue() == 1){
									totalMoney = DoubleFormatUtil.sum(totalMoney, shopQSP.getNumber() * shopQSP.getVipPrice());
									qsp.setShopProductMoney((qsp.getShopProductMoney() == null ? 0.00 : qsp.getShopProductMoney()) + (shopQSP.getNumber() * shopQSP.getVipPrice()));
								}
								
								if(shopSSP.getBuyType().intValue() == 2){
									totalETicket += shopQSP.getNumber();
									qsp.setShopProductETicket((qsp.getShopProductETicket() == null ? 0 : qsp.getShopProductETicket()) + shopQSP.getNumber());
								}
								
								if(shopSSP.getBuyType().intValue() == 3){
									totalTicket += shopQSP.getNumber();
									qsp.setShopProductTicket((qsp.getShopProductTicket() == null ? 0 : qsp.getShopProductTicket()) + shopQSP.getNumber());
								}
								
								qsp.setShopProductQuantity((qsp.getShopProductQuantity() == null ? 0 : qsp.getShopProductQuantity()) + shopQSP.getNumber());
								
								if(shopSSP.getBuyType().intValue() != 1 && !StringUtils.isEmpty(shopSSP.getCode_no())){
									_model.addAttribute("msg", shopQSP.getPname() + " 使用优惠券后结算方式不是现金!");
									_result = formatJsonResult("E90000", shopQSP.getPname() + " 使用优惠券后结算方式不是现金!");
									return;
								}
								
								if(shopSSP.getBuyType().intValue() == 1 && shopSSP.getCode_no() != null && !StringUtils.isEmpty(shopSSP.getCode_no())){
									if(code_no.equals("none"))
										code_no = shopSSP.getCode_no();
									else{
										_model.addAttribute("msg", shopQSP.getPname() + " 同一个水店只能使用一张优惠券!");
										_result = formatJsonResult("E90000", shopQSP.getPname() + " 同一个水店只能使用一张优惠券!");
										return;
									}
								}
								
								carSP.add(shopQSP);
							}
						}
					}
					if(carSP.size() > 0){
						totalMoney = DoubleFormatUtil.sum(totalMoney, qsp.getFreight());
						qsp.setShopTotalMoney(DoubleFormatUtil.sum(qsp.getShopProductMoney() == null ? 0.00 : qsp.getShopProductMoney(), qsp.getFreight()));
						qsp.setHabits(carSP);
						//时间段选择
						new ShopBuyTimeUtil(qsp);
						userCheckSp.add(qsp);
					}
				}
			}
		}
		if(userCheckSp.size() > 0){
			checkDiscount(userCheckSp);
			_model.addAttribute("model", userCheckSp);
			_model.addAttribute("isOnlinePay", isOnlinePay);
			_model.addAttribute("totalMoney", totalMoney);
			_model.addAttribute("totalTicket", totalTicket);
			_model.addAttribute("totalETicket", totalETicket);
			_model.addAttribute("hashTicket", hashTicket);
			_request.getSession().setAttribute(SessionConstantDefine.QUICKCART, userCheckSp);
			_result = formatJsonResult("E00000", "成功");
		}
		else{
			_model.addAttribute("msg", "购物车中没有商品!");
			_result = formatJsonResult("E90000", "购物车中没有商品.");
		}
	}
	
	//查询折扣
	private void checkDiscount(List<QuickShoppingListModel> userCheckSp){
		//查询购物车商品活动/优惠券
		JSONArray forDiscount = new JSONArray();
		for(QuickShoppingListModel shop : userCheckSp){
			//单个水店的JSON
			JSONObject curShop = new JSONObject();
			//单个水店的现金结算商品JSON集合
			JSONArray spList = new JSONArray();
			for(QuickShoppingGoods shopSP : shop.getHabits()){
				if(shopSP.getBuyType().intValue() == 1){
					//单个水店的现金结算商品JSON
					JSONObject spJson = new JSONObject();
					spJson.put("pid", shopSP.getPid());
					spJson.put("number", shopSP.getNumber());
					spList.add(spJson);
				}
			}
			if(spList.size() > 0){
				curShop.put("shopId", shop.getShopId());
				curShop.put("eid", shop.getEid());
				curShop.put("products", spList);
				forDiscount.add(curShop);
			}
		}
		if(forDiscount.size() > 0){
			String discountJsonStr = weiXinService.shopCartDiscount(getSessionClientIdentityCode(), forDiscount.toString(), getSessionWeiXinId(),getSessionWXAppId());
			if(StringUtils.isEmpty(discountJsonStr)){
				_result = formatJsonResult("E90000", "获取店铺折扣错误");
				logger.info("获取店铺折扣错误! 接口shopCartDiscount[clientId:"+getSessionClientIdentityCode()+"][shopCarts:"+forDiscount.toString()+"][deviceId:"+getSessionWeiXinId()+"]");
				return;
			}
			JSONObject discountJson = JSONObject.fromObject(discountJsonStr);
			if(!discountJson.has("code") || !discountJson.getString("code").equals("E00000")){
				_result = formatJsonResult("E90000", "获取店铺折扣失败");
				logger.info("获取店铺折扣失败! 接口shopCartDiscount[clientId:"+getSessionClientIdentityCode()+"][shopCarts:"+forDiscount.toString()+"][deviceId:"+getSessionWeiXinId()+"][返回结果:"+discountJsonStr+"]");
				return;
			}
			
			if(discountJson.getJSONArray("data").size() == 0) return;
			
			for(QuickShoppingListModel shop : userCheckSp){
				List<QuickShoppingListDiscountModel> discounts = new ArrayList();
				for (java.util.Iterator tor=discountJson.getJSONArray("data").iterator(); tor.hasNext();) {
					JSONObject job = (JSONObject)tor.next();
					//logger.info(discountJsonStr);
					
					if(job.getInt("shopId") == shop.getShopId().intValue()){
						for(java.util.Iterator tor2=job.getJSONArray("activityList").iterator(); tor2.hasNext();){
							JSONObject job2 = (JSONObject)tor2.next();
							//logger.info(job2.toString());
							QuickShoppingListDiscountModel curDiscount = new QuickShoppingListDiscountModel();
							curDiscount.setActivityId(job2.getInt("activityId"));
							curDiscount.setActivityName(job2.getString("activityName"));
							curDiscount.setPid(job2.getInt("pid"));
							curDiscount.setEid(job2.getInt("eid"));
							curDiscount.setDiscount(job2.getDouble("discount"));
							curDiscount.setPrice(job2.getDouble("price"));
							curDiscount.setCodeNo(job2.getString("codeNo"));
							curDiscount.setCodeMoney(job2.getDouble("codeMoney"));
							curDiscount.setOverDate(job2.getString("overDate"));
							curDiscount.setDefalutItem(job2.getBoolean("defalutItem"));
							curDiscount.setLimitSaleQuantity(job2.getInt("limitSaleQuantity"));
							discounts.add(curDiscount);
						}
					}
				}
				if(discounts.size() > 0){
					shop.setDiscounts(discounts);
				}
			}
		}
	}
	
	@Override
	public String getResult() {
		return _result;
	}
}
