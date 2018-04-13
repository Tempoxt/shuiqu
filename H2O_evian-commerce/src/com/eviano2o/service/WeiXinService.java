package com.eviano2o.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eviano2o.bean.weixin.ClientModel;
import com.eviano2o.bean.weixin.QuickShoppingListModel;
import com.eviano2o.bean.weixin.WxCityInfoListModel;
import com.eviano2o.bean.weixin.WxCityInfoModel;
import com.eviano2o.bean.weixin.WxIndexAdsModel;
import com.eviano2o.bean.weixin.WxProductVouchers;
import com.eviano2o.bean.weixin.WxUserAddressModel;
import com.eviano2o.bean.weixin.WxUserEntInfoModel;
import com.eviano2o.util.DES3_CBCUtil;
import com.eviano2o.util.HttpClientUtilOkHttp;
import com.eviano2o.util.MD5Util;
import com.eviano2o.util.WebStyle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class WeiXinService {
	
	private static final Logger logger = LoggerFactory.getLogger(WeiXinService.class);
	//logger.error("[msg:{}]", new Object[] { "用户登录！"});
	/** 接口域名 */
	// String WebStyle.getApiUrl() = "http://183.238.231.82:18080/whp-api-1.0.0";
	//String apiPath = "https://www.shuiqoo.com/shuiqoo_api";
	
	
	/** 1、手机注册验证码 */
	public String getCode(String cellphone, String weixinId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("weixinId", weixinId));
		params.add(new BasicNameValuePair("cellphone", cellphone));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/register/getCode.action", params);
		return webContent;
	}

	/** 2.手机注册码验证，生成账号 */
	public String saveBind(String cellphone, String code, Integer cityId, String weiXinId, String location,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("cellphone", cellphone));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("passWord", MD5Util.convertMD5(code)));
		params.add(new BasicNameValuePair("cityId", cityId.toString()));
		params.add(new BasicNameValuePair("weixinId", weiXinId));
		params.add(new BasicNameValuePair("location", (StringUtils.isEmpty(location) ? "" : location)));
		params.add(new BasicNameValuePair("sdkType", "3"));
		params.add(new BasicNameValuePair("citycode", "0"));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/register/verifyCode.action", params);
		return webContent;
	}

	
	/** 3.定位获取范围内的品牌信息 Json */
	public String getLocationBrandJson(String lat, String lon, String cityCode,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("location", lat +"," + lon));
		params.add(new BasicNameValuePair("citycode", (StringUtils.isEmpty(cityCode) ? "0" : cityCode)));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/query/searchBrand.action", params);
		return webContent;
	}

	/** 4.获取品牌下的水店列表 Json */
	public String getBrandShopListJson(String shopMap, Integer cityId, String location,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shopMap", shopMap));
		params.add(new BasicNameValuePair("cityId", cityId.toString()));
		params.add(new BasicNameValuePair("location", location));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shop/brandShopList.action", params);

		return webContent;
	}
	
	/** 21.根据订水电话检索水店 Json */
	public String getTelSearchShopsJson(String lat, String lon, String tel, String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("location", lat +"," + lon));
		params.add(new BasicNameValuePair("tel", tel));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/query/telSearchShops.action", params);
		return webContent;
	}	
	
	/** 22.根据产品品牌检索水店 Json */
	public String getBrandSearchShopsJson(String lat, String lon, String brand,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("location", lat +"," + lon));
		params.add(new BasicNameValuePair("brand", brand));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/query/brandSearchShops.action", params);
		//System.out.println(webContent);
		return webContent;
	}	
	
	
	
	
	/** 11.获取水店产品类别、产品信息 Json */
	public String getShopProductJson(Integer shopId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shopId", shopId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shop/getShopProduct.action", params);
		return webContent;
	}
	
	/** 26.获取商户信息 Json */
	public String getShopByIdJson(Integer shopId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shopId", shopId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shop/getShopById.action", params);
		return webContent;
	}
	
	/** 67.企业的客服信息 Json */
	public String getdEnterpriseServiceJson(Integer eid,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/getdEnterpriseService.action", params);
		return webContent;
	}
	
	/** 67.我的评价<分页> Json */
	public String getMyEvaluates(String clientId, Integer pageIndex,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("pageIndex", pageIndex.toString()));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/evaluate/getMyEvaluates.action", params);
		return webContent;
	}
	
	/** 31.获取客户收藏的水店信息 json字符串  */
	public String getCollectShopsJson(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/collect/getCollectShops.action", params);
		//System.out.println("addressJson："+ webContent);
		return webContent;
	}
	
	/** 32.添加收藏产品/水店 json字符串  */
	public String addCollect(String clientId, Integer collectId, Integer type,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("collectId", collectId.toString()));
		params.add(new BasicNameValuePair("type", type.toString()));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/collect/addCollect.action", params);
		//System.out.println("addressJson："+ webContent);
		return webContent;
	}	
	
	/** 33.删除收藏产品/水店 json字符串  */
	public String delCollect(String clientId, Integer collectId, Integer type,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("collectId", collectId.toString()));
		params.add(new BasicNameValuePair("type", type.toString()));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/collect/delCollect.action", params);
		return webContent;
	}
	
	/** 30.获取客户收藏的产品信息 json字符串  */
	public String getCollectProductsJson(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/collect/getCollectProducts.action", params);
		return webContent;
	}
	
	
	/** 27.获取商户评价 Json字符串  */
	public String getShopEvaluate(Integer shopId,Integer evaluateId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shopId", shopId.toString()));
		params.add(new BasicNameValuePair("evaluateId", evaluateId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shop/getShopEvaluate.action", params);
		return webContent;
	}
	
	
	/** 28.获取产品信息 Json字符串  */
	public String getProductDetailJson(Integer pid, Integer shopId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shopId", shopId.toString()));
		params.add(new BasicNameValuePair("pid", pid.toString()));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/product/getProductDetail.action", params);
		return webContent;
	}
	
	/** 38.获取客户对产品单个消费习惯信息 Json字符串  */
	public String getProductHabitJson(String clientId, Integer shopId, Integer pid,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("shopId", shopId.toString()));
		params.add(new BasicNameValuePair("pid", pid.toString()));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/habit/getProductHabit.action", params);
		return webContent;
	}
	
	/** 39.新增/修改单个产品的消费习惯  */
	public String addClientHabit(String clientId, String habit,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("habit", habit));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/habit/addClientHabit.action", params);
		return webContent;
	}	
	
	/** 29.获取产品评价集合(分页) Json字符串  */
	public String getProductEvaluatesJson(Integer pid, Boolean isPic, Integer grade, Integer pageIndex,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("pid", pid.toString()));
		params.add(new BasicNameValuePair("isPic", isPic.toString()));
		params.add(new BasicNameValuePair("grade", grade.toString()));
		params.add(new BasicNameValuePair("pageIndex", pageIndex.toString()));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/evaluate/getProductEvaluates.action", params);
		return webContent;
	}
	
	
	
	

	/** 60.获取(同步)购物车商品 Json */
	public String getShopCartProductJson(String clientId, Integer eid,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shopcart/getShopCartProducts_v2.action", params);
		logger.info("购物车----------------------------------------------------" + webContent);
		return webContent;
	}
	
	/** 60.获取(同步)购物车商品 */
	public List<QuickShoppingListModel> getShopCartProducts(String clientId, Integer eid,String authorizer_appid) {
		String webContent = getShopCartProductJson(clientId, eid,authorizer_appid);
		JSONObject objJson = JSONObject.fromObject(webContent);
		if (objJson.optString("code").equals("E00000") && objJson.get("data") != null) {
			//System.out.println("result -> " + JSONObject.fromObject(objJson.opt("data")).optString("quickWaterList"));
			String dataStr = JSONObject.fromObject(objJson.opt("data")).optString("shopcarts");
			if (StringUtils.isEmpty(dataStr))
				return null;

			List<QuickShoppingListModel> result = new Gson().fromJson(dataStr, new TypeToken<List<QuickShoppingListModel>>() {}.getType());

			for(QuickShoppingListModel cur :result){
				for(java.util.Iterator tor=JSONObject.fromObject(objJson.opt("data")).getJSONArray("shopcarts").iterator(); tor.hasNext();){
					JSONObject job = (JSONObject)tor.next();
					if(cur.getShopId().intValue() == job.getInt("shopId"))
					cur.setSendLocations(job.getString("sendLocation"));
				}
			}
			
			return result;
		}
		return null;
	}
	
	/** 61.编辑购物车商品 json字符串  */
	public String editShopCart(String clientId, String products, String tag,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("products", products));
		params.add(new BasicNameValuePair("tag", tag == null ? "" : tag));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shopcart/editShopCart_v2.action", params);
		return webContent;
	}
	
	/** 94.单据确认结算  */
	public String shopCartDiscount(String clientId, String shopCarts, String openid,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("shopCarts", shopCarts));
		params.add(new BasicNameValuePair("deviceId", openid));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shopcart/shopCartDiscount.action", params);
		System.out.print(webContent);
		return webContent;
	}
	
	
	
	
	
	/** 14.获取客户地址 对象 */
	public List<WxUserAddressModel> getUserAddressList(String clientId,String authorizer_appid) {
		String dataStr = getUserAddressListJsonStr(clientId,authorizer_appid);
		if(dataStr != null && !StringUtils.isEmpty(dataStr)){
			JSONObject addressObject = JSONObject.fromObject(dataStr);
			if(addressObject.has("code") && addressObject.getJSONArray("data").size() > 0){
				List<WxUserAddressModel> result = new Gson().fromJson(addressObject.getString("data"), new TypeToken<List<WxUserAddressModel>>() { }.getType());
				return result;
			}
		}
		return null;
	}
	
	/** 14.获取客户地址 json字符串  */
	public String getUserAddressListJsonStr(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/address/getAddress.action", params);
		return webContent;
	}
	
	/** 15.新增客户地址  */
	public String addAddress(List<BasicNameValuePair> addressParam){
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/address/addAddress.action", addressParam);
		return webContent;
	}

	/** 149.根据坐标点新增地址V2  */
	public String addAddress_v2(List<BasicNameValuePair> addressParam){
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/address/addAddress_v2.action", addressParam);
		return webContent;
	}
	
	/** 16.编辑客户地址  */
	public String editAddress(List<BasicNameValuePair> addressParam){
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/address/editAddress.action", addressParam);
		return webContent;
	}
	
	/** 17.设置默认客户地址 */
	public String defaultAddress(String clientId, Integer did,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("did", did.toString()));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/address/defaultAddress.action", params);
		return webContent;
	}
	
	/** 18.删除客户地址  */
	public String delAddress(String clientId, Integer did,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("did", did.toString()));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/address/deleteAddress.action", params);
		return webContent;
	}	
	
	/** 90.获取客户的地址标签集合  */
	public String getAddressTags(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/address/getAddressTags.action", params);
		return webContent;
	}
	
	/** 91.新增客户的地址标签  */
	public String addAddressTags(String clientId, String tag,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("tag", tag));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/address/insertAddressTag.action", params);
		return webContent;
	}
	
	
	
	/** 23.保存订单  */
	public String saveOrder(List<BasicNameValuePair> orderParam){
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/order/saveNewOrder.action", orderParam);
		return webContent;
	}
	
	
	
	
	/** 24.获取组单据列表 Json */
	public String wxToPayMoney(String clientId, String group,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("group", group));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/order/wxToPayMoney.action", params);
		return webContent;
	}

	
	
	
	/** 37.根据状态获取单据列表  Json  */
	public String getStatusOrdersJson(String clientId, Integer statusId, Integer pageIndex,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("pageIndex", pageIndex.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = "";
		if(statusId == null){
			webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/order/getAllOrders.action", params);
		}else{
			params.add(new BasicNameValuePair("statusId", statusId.toString()));
			webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/order/getStatusOrders.action", params);
		}
		//System.out.print(webContent);
		return webContent;
	}
	
	/** 61.取消订货单据 json字符串  */
	public String cancelOrder(String clientId, Integer orderId, String reason,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("orderId", orderId.toString()));
		params.add(new BasicNameValuePair("reason", reason));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/order/cancelOrder.action", params);
		return webContent;
	}
	
	/** 62.后台设置的订单取消原因  */
	public String orderCancelReason(String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/system/orderCancelReason.action", params);
		return webContent;
	}
	
	/** 47.删除完成的单据  Json  */
	public String delOrder(String clientId, Integer orderId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("orderId", orderId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/order/delOrder.action", params);
		
		return webContent;
	}
	
	/** 48.单据确认收货  Json  */
	public String reapOrder(String clientId, Integer orderId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("orderId", orderId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/order/reapOrder.action", params);
		return webContent;
	}
	
	/** 49.获取订单明细  Json  */
	public String getOrderDetailJson(String clientId, Integer orderId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("orderId", orderId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/order/getOrderDetail.action", params);
		
		return webContent;
	}
	
	/** 25.获取单据物流信息  Json  */
	public String getOrderLogJson(String clientId, Integer orderId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("orderId", orderId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/logistics/getOrderLog.action", params);
		
		return webContent;
	}
	
	/** 50.订单继续支付  Json  */
	public String orderAgainPay(String clientId, Integer orderId, String orderGroup,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("orderId", orderId.toString()));
		params.add(new BasicNameValuePair("orderGroup", orderGroup));
		params.add(new BasicNameValuePair("sdkType", "3"));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/pay/orderAgainPay.action", params);
		
		return webContent;
	}
	
	/** 44.在线支付参数  Json  */
	public String getPayParams(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("sdkType", "3"));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/pay/getPayParams.action", params);
		
		return webContent;
	}
	
	
	
	/** 46.首页广告栏  Json  */
	public String getSysAds(String location,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("location", location));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/ad/getSysAds.action", params);
		
		return webContent;
	}
	
	/** 46.首页广告栏 */
	public List<WxIndexAdsModel> getIndexAds(String location,String authorizer_appid) {
		String webContent = getSysAds(location,authorizer_appid);
		JSONObject objJson = JSONObject.fromObject(webContent);
		if (objJson.optString("code").equals("E00000") && objJson.get("data") != null) {
			String dataStr = JSONObject.fromObject(objJson.opt("data")).optString("ads");
			if (StringUtils.isEmpty(dataStr))
				return null;

			List<WxIndexAdsModel> result = new Gson().fromJson(dataStr, new TypeToken<List<WxIndexAdsModel>>() {}.getType());
			return result;
		}
		return null;
	}
	
	/** 64.企业的品牌故事 Json */
	public String getBrandStoryJson(Integer eid,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/getBrandStorys.action", params);
		
		return webContent;
	}

	/** 65.当前城市上线企业水店统计 Json */
	public String getStatisticsShopJson(String location, String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", ""));
		//params.add(new BasicNameValuePair("location", location));
		params.add(new BasicNameValuePair("hslife", "false"));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/query/statisticsShop.action", params);
		
		return webContent;
	}
	
	/** 93.获取平台活动(首桶免费、半价活动)信息 Json */
	public String getActivityInfoJson(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/sysActivity/sysActivityInfo.action", params);
		return webContent;
	}
	
	/** 97.微信关注保存用户信息 */
	public String saveWxUserInfo(List<BasicNameValuePair> wxUserInfo) {
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/wxclient/weixinSubscribe.action", wxUserInfo);
		JSONObject objJson = JSONObject.fromObject(webContent);
		return objJson.optString("code");
	}

	/** 98.通过微信Id获取用户信息 */
	public String getWxLoginStr(String openId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("openid", openId));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/wxclient/weixinLogin.action", params);
		return webContent;
	}
	
	/** 98.通过微信Id获取用户信息 */
	public ClientModel getWxLogin(String openId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("openid", openId));
		params.add(new BasicNameValuePair("authorizer_appid",authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/wxclient/weixinLogin.action", params);
		logger.info(webContent);
		JSONObject objJson = JSONObject.fromObject(webContent);
		if (!StringUtils.isEmpty(webContent) && objJson != null && objJson.has("data")) {
			String dataStr = objJson.optString("data");
			if (StringUtils.isEmpty(dataStr))
				return null;

			ClientModel result = (ClientModel) JSONObject.toBean(JSONObject.fromObject(dataStr), ClientModel.class);
			return result;
		}else{
			if(objJson.has("code")&& !objJson.optString("code").equals("C00039"))
				logger.error("[msg:{}],[url:{}],[webContent:{}]", new Object[] { "用户登录接口失败", WebStyle.getApiUrl() + "/wxclient/weixinLogin.action?openid=" + openId, webContent });
		}
		return null;
	}

	/** 99.获取系统配置参数 */
	public String getSysPram() {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/wxclient/getsysConfig_Params.action", params);
		//logger.info("获取系统参数："+webContent);
		JSONObject objJson = JSONObject.fromObject(webContent);
		//logger.info ("[{},{}]",new Object[]{objJson == null,objJson});
		if (objJson.has("code") && objJson.getString("code").equals("E00000") && objJson.getJSONObject("data") != null) {
			String dataStr = JSONObject.fromObject(objJson.opt("data")).optString("sysconfig");
			if (StringUtils.isEmpty(dataStr))
				return "";
			dataStr = DES3_CBCUtil.des3DecodeCBC(dataStr);

			return dataStr;
		}
		return "";
	}
	
	/** 19.获取全部城市区域信息 */
	public String getAllCityInfoJson(String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/region/queryAllCity.action", params);
		return webContent;
	}

	/** 19.获取全部城市区域信息 */
	public WxCityInfoListModel getAllCityInfo(String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		System.out.println("authorizer_appid = " +authorizer_appid);
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/region/queryAllCity.action", params);
		System.out.println("webContent = " +webContent);
		JSONObject objJson = JSONObject.fromObject(webContent);
		if (objJson.optString("code").equals("E00000") && objJson.get("data") != null) {
			String dataStr = objJson.optString("data");
			if (StringUtils.isEmpty(dataStr))
				return null;
			WxCityInfoListModel result = new Gson().fromJson(dataStr, new TypeToken<WxCityInfoListModel>() {
			}.getType());
			return result;
		}
		return null;
	}

	/** 100.获取城市详细信息 */
	public WxCityInfoModel getCityInfo(Integer cityId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("cityId", cityId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/region/getCityById.action", params);
		JSONObject objJson = JSONObject.fromObject(webContent);
		if (objJson.optString("code").equals("E00000") && objJson.get("data") != null) {
			String dataStr = objJson.optString("data");
			if (StringUtils.isEmpty(dataStr))
				return null;

			WxCityInfoModel result = new Gson().fromJson(dataStr, new TypeToken<WxCityInfoModel>() {
			}.getType());

			return result;
		}
		return null;
	}

	/** 43.快速订水商品 */
	public List<QuickShoppingListModel> getQuickShoppingInfo(String clientId,String authorizer_appid) {
		String webContent = getQuickShoppingJson(clientId,authorizer_appid);
		//logger.info(webContent);
		JSONObject objJson = JSONObject.fromObject(webContent);
		if (objJson.optString("code").equals("E00000") && objJson.get("data") != null) {
			//System.out.println("result -> " + JSONObject.fromObject(objJson.opt("data")).optString("quickWaterList"));
			String dataStr = JSONObject.fromObject(objJson.opt("data")).optString("quickWaterList");
			if (StringUtils.isEmpty(dataStr))
				return null;

			List<QuickShoppingListModel> result = new Gson().fromJson(dataStr, new TypeToken<List<QuickShoppingListModel>>() {
			}.getType());
			for(QuickShoppingListModel cur :result){
				for(java.util.Iterator tor=JSONObject.fromObject(objJson.opt("data")).getJSONArray("quickWaterList").iterator(); tor.hasNext();){
				JSONObject job = (JSONObject)tor.next();
					cur.setSendLocations(job.getString("sendLocation"));
				}
			}
			
			return result;
		}
		return null;
	}
	
	/** 43.快速订水商品Json */
	public String getQuickShoppingJson(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/client/quickWaters.action", params);
		JSONObject objJson = JSONObject.fromObject(webContent);
		if (StringUtils.isEmpty(webContent) || !objJson.optString("code").equals("E00000") || !objJson.has("data")) {
			logger.info("[msg:{}],[url:{}],[webContent:{}]", new Object[] { "快速订水商品Json", WebStyle.getApiUrl() + "/client/quickWaters.action?clientId=" + clientId+"&authorizer_appid="+authorizer_appid, webContent });
		}
		return webContent;
	}
	
	/** 80.获取商品能使用的优惠券 json */
	public String getProductVouchersJson(String clientId, Integer typeId, Integer pid,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("typeId", typeId.toString()));
		params.add(new BasicNameValuePair("pid", pid.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/imacode/getProductVouchers.action", params);
		return webContent;
	}

	/** 80.获取商品能使用的优惠券 */
	public List<WxProductVouchers> getProductVouchers(String clientId, Integer typeId, Integer pid,String authorizer_appid) {
		String webContent = getProductVouchersJson(clientId, typeId, pid,authorizer_appid);
		JSONObject objJson = JSONObject.fromObject(webContent);
		if (objJson.optString("code").equals("E00000") && objJson.get("data") != null) {
			//System.out.println("result -> " + JSONObject.fromObject(objJson.opt("data")).optString("vouchers"));
			List<WxProductVouchers> result = new Gson().fromJson(objJson.getJSONObject("data").getString("vouchers"), new TypeToken<List<WxProductVouchers>>() {
			}.getType());
			return result;
		}
		return null;
	}

	/** 72.注册激活优惠券 */
	public String codeConvert(String clientId, String code_no, Integer cityId, Integer citycode,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("code_no", code_no));
		params.add(new BasicNameValuePair("cityId", cityId.toString()));
		params.add(new BasicNameValuePair("citycode", citycode.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/imacode/codeConvert.action", params);
		return webContent;
	}

	/** 77.获取优惠券能购买的商品信息  Json */
	public String voucherToGoods(String clientId, Integer shopId, Integer code_id,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("shopId", shopId.toString()));
		params.add(new BasicNameValuePair("code_id", code_id.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/imacode/voucherToGoods.action", params);
		return webContent;
	}
	
	/** 78.券赠与他人  Json */
	public String voucherGive(String clientId, String account, String code_no,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("code_no", code_no));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/imacode/voucherGive.action", params);
		return webContent;
	}

	/** 101.保存微信用户对于的城市  */
	public String saveWxUserCity(String openId, Integer cityId, String location,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("openid", openId));
		params.add(new BasicNameValuePair("cityId", cityId.toString()));
		params.add(new BasicNameValuePair("location", StringUtils.isEmpty(location) ? "" : location));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/client/saveClientWXCity.action", params);
		return webContent;
	}
	
	/** 102.获取微信用户对于的城市 */
	public WxCityInfoModel getWxUserCityInfo(String openId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("openid", openId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/client/getClientWXCity.action", params);
		
		JSONObject objJson = JSONObject.fromObject(webContent);
		if (objJson.optString("code").equals("E00000") && objJson.get("data") != null) {
			String dataStr = objJson.optString("data");
			if (StringUtils.isEmpty(dataStr))
				return null;

			WxCityInfoModel result = new Gson().fromJson(dataStr, new TypeToken<WxCityInfoModel>() { }.getType());
			return result;
		}
		return null;
	}



	
	/** 20.获取客户信息 Json */
	public String getClientInfoJson(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/client/getClientInfo.action", params);
		return webContent;
	}
	
	/** 73.获取我的代金券/优惠券明细 Json */
	public String getMyVouchersJson(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("typeId", "2"));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/imacode/getMyVouchers.action", params);
		return webContent;
	}	
	
	/** 92.获取客户积分汇总和优惠券汇总 Json */
	public String getIntegralCodeNumJson(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/integral/getIntegralCodeNum.action", params);
		return webContent;
	}
	
	/** 76.获取优惠券下的水店信息(根据城市区域) Json */
	public String voucherToShopByDistrictJson(String clientId, Integer cityId, Integer code_id, Integer districtId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("cityId", cityId.toString()));
		params.add(new BasicNameValuePair("code_id", code_id.toString()));
		params.add(new BasicNameValuePair("districtId", districtId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/imacode/voucherToShopByDistrict.action", params);
		return webContent;
	}
	
	/** 77.获取优惠券下的水店信息(根据定位) Json */
	public String voucherToShopByLocationJson(String clientId, Integer cityId, Integer code_id, String location,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("cityId", cityId.toString()));
		params.add(new BasicNameValuePair("code_id", code_id.toString()));
		params.add(new BasicNameValuePair("location", location));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/imacode/voucherToShopByLocation.action", params);
		return webContent;
	}
	
	/** 40.客户所有消费习惯《按水店分组显示》  Json */
	public String getClientAllHibitsJson(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/habit/getClientAllHibits.action", params);
		return webContent;
	}
	
	/** 41.批量编辑客户消费习惯  Json */
	public String editClientHabits(String clientId, String habit,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("habit", habit));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/habit/editClientHabits.action", params);
		return webContent;
	}
	
	/** 42.批量删除客户消费习惯  Json */
	public String delClientHabits(String clientId, String habit,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("habit", habit));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/habit/delClientHabits.action", params);
		return webContent;
	}
	
	
	
	
	/** 75.获取商户入驻的信息  Json */
	public String getBlankEnterPrise(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/getBlankEnterPrise.action", params);
		return webContent;
	}

	/** 65.企业的入驻登记  Json */
	public String saveBlankEnterPrise(String clientId, String location, String linkman, String enterprise, String brandName, String tel, String email,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("location", location));
		params.add(new BasicNameValuePair("linkman", linkman));
		params.add(new BasicNameValuePair("enterprise", enterprise));
		params.add(new BasicNameValuePair("brandName", brandName));
		params.add(new BasicNameValuePair("tel", tel));
		params.add(new BasicNameValuePair("email", StringUtils.isEmpty(email) ? "" : email));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/saveBlankEnterPrise.action", params);
		return webContent;
	}
	
	
	

	/** 82.获取退货参数  Json */
	public String orderTuihuoParam(String clientId, Integer orderId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("orderId", orderId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/order/orderTuihuoParam.action", params);
		return webContent;
	}

	/** 83.保存退货单据  Json */
	public String saveOrderTuiHuo(String clientId, String orderJson,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("orderJson", orderJson));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/order/saveOrderTuiHuo.action", params);
		return webContent;
	}
	
	/** 84.退货单据明细  Json */
	public String orderTHDetail(String clientId, Integer orderId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("orderId", orderId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/order/orderTHDetail.action", params);
		return webContent;
	}
	
	/** 88.保存积分兑换单据  Json */
	public String saveIntegralOrder(String clientId, Integer type_id) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("type_id", type_id.toString()));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/integral/saveIntegralOrder.action", params);
		return webContent;
	}
	
	
	
	
	/** 35.我要评价单据  Json */
	public String toOrderEvaluate(String clientId, Integer orderId, Integer shopId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("orderId", orderId.toString()));
		params.add(new BasicNameValuePair("shopId", shopId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/evaluate/toOrderEvaluate.action", params);
		return webContent;
	}
	
	/** 36.保存单据评价  Json */
	public String addOrderEvaluate(String clientId, Integer orderId, Integer shopId, String orderEvaluate, String productEvalutes,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("orderId", orderId.toString()));
		params.add(new BasicNameValuePair("shopId", shopId.toString()));
		params.add(new BasicNameValuePair("orderEvaluate", orderEvaluate));
		params.add(new BasicNameValuePair("productEvalutes", productEvalutes));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/evaluate/addOrderEvaluate.action", params);
		return webContent;
	}
	
	
	
	
	/** 103.微信支付成功,更新单据支付状态  Json */
	public String weixinPayToSuc(String openid, String out_trade_no, String transaction_id, String bank_type, String total_fee, String paytime,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("openid", openid));
		params.add(new BasicNameValuePair("out_trade_no", out_trade_no));
		params.add(new BasicNameValuePair("transaction_id", transaction_id));
		params.add(new BasicNameValuePair("bank_type", bank_type));
		params.add(new BasicNameValuePair("total_fee", total_fee));
		params.add(new BasicNameValuePair("paytime", paytime));		
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/weixin/weixinPayToSuc.action", params);
		logger.info("保存支付回调："+webContent);
		return webContent;
	}
	
	
	
	
	/** 104.水趣推客_我的账户情况  Json */
	public String myEarningInfo(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/myEarningInfo.action", params);
		return webContent;
	}

	/** 105.保存我的提现账号  Json */
	public String saveEarningAccount(String clientId, String alipayNo, String alipayName, String dealPass, String mappingTel, String identityId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("alipayNo", alipayNo));
		params.add(new BasicNameValuePair("alipayName", alipayName));
		params.add(new BasicNameValuePair("dealPass", MD5Util.convertMD5(dealPass)));
		params.add(new BasicNameValuePair("mappingTel", mappingTel));
		params.add(new BasicNameValuePair("identityId", identityId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/saveEarningAccount.action", params);
		return webContent;
	}

	/** 106.修改提现密码获取验证码  Json */
	public String createEarningpassSms(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/createEarningpassSms.action", params);
		return webContent;
	}

	/** 107.修改提现密码  Json */
	public String updateEarningpass(String clientId, String smsCode, String mappingTel, String oldPass, String dealPass,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("smsCode", smsCode));
		params.add(new BasicNameValuePair("mappingTel", mappingTel));
		params.add(new BasicNameValuePair("oldPass", MD5Util.convertMD5(oldPass)));
		params.add(new BasicNameValuePair("dealPass", MD5Util.convertMD5(dealPass)));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/updateEarningpass.action", params);
		return webContent;
	}
	
	/** 108.获取单个模板明细/以及分享产品图片  Json */
	public String getShareTemplateDetail(Integer tid, String clientId, Boolean isWX,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("tid", tid.toString()));
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("isWX", "true"));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/getShareTemplateDetail.action", params);
		//System.out.println(webContent);
		return webContent;
	}
	
	/** 109.微信是否下载关注资料  Json */
	public String existsSubscribe(String openid,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("openid", openid));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/wxclient/existsSubscribe.action", params);
		return webContent;
	}
	
	/** 110.分享注册页面的分享人数据  Json */
	public String getShareRecordToRegedit(Integer shareId, String openid,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shareId", shareId.toString()));
		params.add(new BasicNameValuePair("openid", openid));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/getShareRecordToRegedit.action", params);
		return webContent;
	}

	/** 111.人人分享注册获取验证码，弃用  Json */
	public String getCode_giveup(String cellphone) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("cellphone", cellphone));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/register/getCode.action", params);
		return webContent;
	}
	
	/** 112.人人分享完成用户注册  Json */
	public String shareComplateRegedit(String cellphone, String code, String openid, Integer shareId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("cellphone", cellphone));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("passWord", MD5Util.convertMD5(code)));
		params.add(new BasicNameValuePair("openid", openid));
		params.add(new BasicNameValuePair("sdkType", "3"));
		params.add(new BasicNameValuePair("shareId", shareId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/shareComplateRegedit.action", params);
		return webContent;
	}
	
	/** 113.根据主键获取我的临时模板  Json */
	public String getTemporaryTemplate(String clientId, Integer tid, Integer pictureid,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("tid", tid.toString()));
		params.add(new BasicNameValuePair("pictureId", pictureid.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/getTemporaryTemplate.action", params);
		return webContent;
	}
	
	/** 114.保存我的分享记录(one)  Json */
	public String saveShareRecord(String clientId, Integer tid, Integer pictureId, String nickname, String locCity, String photo,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("tid", tid.toString()));
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("nickname", nickname));
		params.add(new BasicNameValuePair("locCity", locCity));
		params.add(new BasicNameValuePair("photo", photo));
		params.add(new BasicNameValuePair("pictureId", pictureId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/saveShareRecord.action", params);
		return webContent;
	}

	/** 115.更新我的模板海报图片/微信MediaID(three)  Json */
	public String updateShareRecordPicUrl(Integer shareId, String clientId, String posterUrl, Integer tid, Integer pictureId, String mediaId, String dateOvert,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shareId", shareId.toString()));
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("posterUrl", posterUrl));
		params.add(new BasicNameValuePair("tid", tid.toString()));
		params.add(new BasicNameValuePair("pictureId", pictureId.toString()));
		params.add(new BasicNameValuePair("mediaId", mediaId));
		params.add(new BasicNameValuePair("dateOvert", dateOvert));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/updateShareRecordPicUrl.action", params);
		return webContent;
	}

	/** 116.根据shareId获取我的临时模板  Json */
	public String getMyTemplateByShareId(Integer shareId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shareId", shareId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/getMyTemplateByShareId.action", params);
		return webContent;
	}

	/** 117.更新我的模板URL_1(two)  Json */
	public String updateShareRecordUrl1(String clientId, Integer tid, Integer pictureId, Integer shareId, String url_1,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("tid", tid.toString()));
		params.add(new BasicNameValuePair("pictureId", pictureId.toString()));
		params.add(new BasicNameValuePair("shareId", shareId.toString()));
		params.add(new BasicNameValuePair("url_1", url_1));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/updateShareRecordUrl1.action", params);
		return webContent;
	}

	/** 118.找回提现密码获取验证码  Json */
	public String findEarningPassSmsCode(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/findEarningPassSmsCode.action", params);
		return webContent;
	}

	/** 119.找回修改提现密码  Json */
	public String findEarningpass(String clientId, String smsCode, String dealPass,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("smsCode", smsCode));
		params.add(new BasicNameValuePair("dealPass", MD5Util.convertMD5(dealPass)));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/findEarningpass.action", params);
		return webContent;
	}

	/** 120.用户提现金额到支付宝  Json */
	public String userGetMoney(String clientId, String alipayNo, Double money,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("alipayNo", alipayNo));
		params.add(new BasicNameValuePair("money", money.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/userGetMoney.action", params);
		return webContent;
	}

	/** 121.用户提现明细  Json */
	public String getTiXianRecords(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/getTiXianRecords.action", params);
		return webContent;
	}

	/** 122.用户收益明细  Json */
	public String getEarningsRecords(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/getEarningsRecords.action", params);
		return webContent;
	}

	/** 123.水趣推客我的客户  Json */
	public String getDevelopClients(String clientId, Integer type, Integer pageIndex,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("type", type.toString()));
		params.add(new BasicNameValuePair("pageIndex", pageIndex.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/getDevelopClients.action", params);
		return webContent;
	}
	
	/** 124.通过加密clientId获取用户信息  Json */
	public ClientModel getUserInfoById(String clientId,String authorizer_appid) {
		//System.out.println("1111111111111111111111111111111111111111111"+clientId);
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/wxclient/getUserInfoById.action", params);
		//logger.info("接口124：" + webContent);
		JSONObject objJson = JSONObject.fromObject(webContent);
		if (!StringUtils.isEmpty(webContent) && objJson != null && objJson.has("code")&& objJson.optString("code").equals("E00000") && objJson.has("data")) {
			String dataStr = objJson.optString("data");
			if (StringUtils.isEmpty(dataStr))
				return null;

			ClientModel result = (ClientModel) JSONObject.toBean(JSONObject.fromObject(dataStr), ClientModel.class);
			return result;
		}else{
			logger.info("[msg:{}],[url:{}],[webContent:{}]", new Object[] { "用户登录接口失败", WebStyle.getApiUrl() + "/wxclient/getUserInfoById.action?openid=" + clientId, webContent });
		}
		return null;
	}
	
	/** 通过加密clientId获取用户信息  Json */
	public ClientModel getH5UserInfoById(String clientId,String eid) {
		System.out.println("1111111111111111111111111111111111111111111"+clientId+" eid = "+eid);
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("eid", eid));
		
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/wxclient/getH5UserInfoById.action", params);
		logger.info("接口124：" + webContent);
		JSONObject objJson = JSONObject.fromObject(webContent);
		if (!StringUtils.isEmpty(webContent) && objJson != null && objJson.has("code")&& objJson.optString("code").equals("E00000") && objJson.has("data")) {
			String dataStr = objJson.optString("data");
			if (StringUtils.isEmpty(dataStr))
				return null;
			
			ClientModel result = (ClientModel) JSONObject.toBean(JSONObject.fromObject(dataStr), ClientModel.class);
			return result;
		}else{
			logger.info("[msg:{}],[url:{}],[webContent:{}]", new Object[] { "用户登录接口失败", WebStyle.getApiUrl() + "/wxclient/getUserInfoById.action?openid=" + clientId, webContent });
		}
		return null;
	}

	/** 125.通过shareId获取模板微信图文信息  Json */
	public String getTemplateWxText(Integer shareId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shareId", shareId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/getTemplateWxText.action", params);
		return webContent;
	}

	
	

	/** 126.获取客户的增值税发票信息  Json */
	public String getClientVats(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/vat/getClientVats.action", params);
		return webContent;
	}

	/** 127.保存增值税发票信息(新增、修改、默认、删除) Json */
	public String saveClientVAT(String clientId, String dataVat, String tag,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("dataVat", dataVat));
		params.add(new BasicNameValuePair("tag", tag));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/vat/saveClientVAT.action", params);
		return webContent;
	}
	
	/** 128.单据确认界面根据水店ID获取发票信息  Json */
	public String getEidInvoiceGuiGe(String clientId, Integer shopId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("shopId", shopId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/vat/getEidInvoiceGuiGe.action", params);
		return webContent;
	}
	
	
	
	
	
	/** 130.电子票余量列表  Json */
	public String getValidTicketList(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/ticket/getValidTicketList.action", params);
		return webContent;
	}

	/** 131.电子票消费记录列表  Json */
	public String getConsumeTicketList(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/ticket/getConsumeTicketList.action", params);
		return webContent;
	}

	/** 132.电子票消费详情  Json */
	public String getConsumeTicketDetail(String clientId, Integer ticketId, Integer orderId, Integer eid,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("ticketId", ticketId.toString()));
		params.add(new BasicNameValuePair("orderId", orderId.toString()));
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/ticket/getConsumeTicketDetail.action", params);
		return webContent;
	}

	/** 133.立即使用电子票  Json */
	public String useTicketToCart(String clientId, Integer shopId, Integer pid,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("shopId", shopId.toString()));
		params.add(new BasicNameValuePair("pid", pid.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/ticket/useTicketToCart.action", params);
		return webContent;
	}

	

	/** 134.获取企业押桶说明  Json */
	public String getenterpriseyatong(Integer eid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("eid", eid.toString()));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/getenterpriseyatong.action", params);
		return webContent;
	}
	

	/** 135.微信关注日志  Json */
	public String writeWXLog(String openid, String operatType, String remark,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("openid", openid));
		params.add(new BasicNameValuePair("operatType", operatType));
		params.add(new BasicNameValuePair("remark", remark));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/wxclient/writeWXLog.action", params);
		return webContent;
	}


	/** 137.一桶一码扫码验证  Json */
	public String verifyCodeByApp(String ip, String codeUrl,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("ip", ip));
		params.add(new BasicNameValuePair("codeUrl", codeUrl));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi("http://db.h2oqr.com:18098/evian/code/verify/verifyCodeByApp.action", params);
		return webContent;
	}
	


	/** 138.水趣推客 获取申请信息  Json */
	public String getShareApplyInfo(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shareApply/getShareApplyInfo.action", params);
		return webContent;
	}
	
	/** 139.水趣推客 保存开店申请  Json */
	public String saveShareApplyInfo(String clientId, Integer eid, String fullname, String tel, String resource, String ip, String location,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("fullname", fullname));
		params.add(new BasicNameValuePair("tel", tel));
		params.add(new BasicNameValuePair("resource", resource));
		params.add(new BasicNameValuePair("ip", ip));
		params.add(new BasicNameValuePair("location", location));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shareApply/saveShareApplyInfo.action", params);
		return webContent;
	}


	/** 140.水趣推客 保存开店申请上传图片  Json */
	public String saveShareApplyPics(String clientId, String applyId, String pic1, String pic2, String pic3, String pic4, String pic5,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("applyId", applyId));
		params.add(new BasicNameValuePair("pic1", pic1));
		params.add(new BasicNameValuePair("pic2", pic2));
		params.add(new BasicNameValuePair("pic3", pic3));
		params.add(new BasicNameValuePair("pic4", pic4));
		params.add(new BasicNameValuePair("pic5", pic5));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shareApply/saveShareApplyPics.action", params);
		return webContent;
	}
	
	
	
	/** 141.配送员详情  Json */
	public String getStaffDetail(String clientId, Integer eid, Integer staffId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("staffId", staffId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/logistics/getStaffDetail.action", params);
		return webContent;
	}
	

	/** 142.根据坐标、百度ID ,获取所在企业信息  Json */
	public String getEnterPriseByLoc(String location, Integer citycode,String authorizer_appid) {
		citycode = citycode == null ? 0 : citycode;
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("location", location));
		params.add(new BasicNameValuePair("citycode", citycode.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/query/getEnterPriseByLoc.action", params);
		return webContent;
	}
	
	/** 143.单据我要催单  Json */
	public String ordercuishui(String clientId, Integer orderId, String remark,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("orderId", orderId.toString()));
		params.add(new BasicNameValuePair("remark", remark));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/order/ordercuishui.action", params);
		return webContent;
	}
	
	/** 145.水店注册微信  Json */
	public String shopregeditweixin(String ename, String shopNo, String keeperCode, String keeperMobile, String wxOpenId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("ename", ename));
		params.add(new BasicNameValuePair("shopNo", shopNo));
		params.add(new BasicNameValuePair("keeperCode", keeperCode));
		params.add(new BasicNameValuePair("keeperMobile", keeperMobile));
		params.add(new BasicNameValuePair("wxOpenId", wxOpenId));
		params.add(new BasicNameValuePair("wxLiteappOpenId", ""));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shop/shopregeditweixin.action", params);
		return webContent;
	}
	
	
	
	/** 150.获取水店集合的围栏信息  Json */
	public String getShopsWeiLans(String shopStr,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shopStr", shopStr));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shop/getShopsWeiLans.action", params);
		return webContent;
	}

	
	
	/** 151.客户押桶记录查询  Json */
	public String getClientYatongs(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/client/getClientYatongs.action", params);
		return webContent;
	}
	

	
	/** 152.获取推客经理要审核的推客信息  Json */
	public String getManagerTuiKes(String clientId, String openId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("openId", openId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shareApply/getManagerTuiKes.action", params);
		return webContent;
	}
	
	
	
	/** 153.微信端审核推客  Json */
	public String auditTuike(String clientId,Integer applyId, Integer eid, Integer status, String remark,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("applyId", applyId.toString()));
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("status", status.toString()));
		params.add(new BasicNameValuePair("remark", remark));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shareApply/auditTuike.action", params);
		return webContent;
	}
	

	/** 154.推客收益相关客户明细  Json */
	public String tuikeEarningsDetail(Integer xgcid,String clientId, Integer month,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("xgcid", xgcid.toString()));
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("month", month.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/tuikeEarningsDetail.action", params);
		return webContent;
	}
	
	
	
	
	/** 155.保存我的提现账号-银行卡号  Json */
	public String saveEarningBankAccount(String clientId,String bankNo,String bankName ,String openBank ,String openSmallBank,String dealPass, String mappingTel , String identityId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("bankNo", bankNo));
		params.add(new BasicNameValuePair("bankName", bankName));
		params.add(new BasicNameValuePair("openBank", openBank));
		params.add(new BasicNameValuePair("openSmallBank", openSmallBank));
		params.add(new BasicNameValuePair("dealPass", MD5Util.convertMD5(dealPass)));
		params.add(new BasicNameValuePair("mappingTel", mappingTel));
		params.add(new BasicNameValuePair("identityId", identityId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/saveEarningBankAccount.action", params);
		return webContent;
	}
	
	
	
	/** 156.推客获取系统或者企业模板  Json */
	public String getSysEnterPriseTemplate(String clientId,Integer type,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("type", type.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/getSysEnterPriseTemplate.action", params);
		return webContent;
	}
	
	
	
	
	/** 157.根据条码获取码对应信息  Json */
	public String getShopManagerCode(String clientId,Long shopCode,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("shopCode", shopCode.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shareApply/getShopManagerCode.action", params);
		return webContent;
	}
	
	
	
	
	/** 158.根据手机号获取绑定相关信息，验证码  Json */
	public String getShopRegeditGetCode(String clientId,Integer eid,String cellphone,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("cellphone", cellphone));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shareApply/getShopRegeditGetCode.action", params);
		return webContent;
	}
	
	
	
	/** 159.根据手机号验证码保存推客经理绑定  Json */
	public String saveShopManagerCode(String clientId,String openId,String cellphone,Integer existCid,String nickname,String code,Long shopCode,Integer shopId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("openId", openId));
		params.add(new BasicNameValuePair("cellphone", cellphone));
		params.add(new BasicNameValuePair("nickname", nickname));
		params.add(new BasicNameValuePair("existCid", existCid.toString()));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("shopCode", shopCode.toString()));
		params.add(new BasicNameValuePair("shopId", shopId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shareApply/saveShopManagerCode.action", params);
		return webContent;
	}
	
	
	
	/** 160.APP,微信公众号发起支付记录  Json */
	public String paysendrecord(String clientId,String order_no,String platform,String money,Integer source,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("order_no", order_no));
		params.add(new BasicNameValuePair("platform", platform));
		params.add(new BasicNameValuePair("money", money));
		params.add(new BasicNameValuePair("source", source.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/pay/paysendrecord.action", params);
		return webContent;
	}
	
	/** 161.推客经理相关信息  Json */
	public String getClientShareInfo(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shareApply/getClientShareInfo.action", params);
		return webContent;
	}
	
	/** 162.推客经理汇总报表  Json */
	public String getShareTotalReport(String clientId,Integer eid,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shareApply/getShareTotalReport.action", params);
		return webContent;
	}
	
	/** 163.店铺编码明细  Json */
	public String getShopCodeRegeditReport(String clientId,Integer eid,String endDate,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("endDate", endDate));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shareApply/getShopCodeRegeditReport.action", params);
		return webContent;
	}
	
	/** 164.店铺推客明细  Json */
	public String getShopClientRegeditReport(String clientId,Integer eid,String endDate,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("endDate", endDate));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shareApply/getShopClientRegeditReport.action", params);
		return webContent;
	}
	
	/** 165.店铺关联配送员账号  Json */
	public String getShopCourierAccount(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shopcourier/getShopCourierAccount.action", params);
		return webContent;
	}
	
	/** 166.店铺关联配送员发送短信验证码 Json */
	public String sendShopCourierSMS(String clientId,String cellphone,String ip,String shopCode,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("cellphone", cellphone));
		params.add(new BasicNameValuePair("ip", ip));
		params.add(new BasicNameValuePair("shopCode", shopCode));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shopcourier/sendShopCourierSMS.action", params);
		return webContent;
	}
	
	/** 167.保存店铺关联配送员 Json */
	public String saveShopCourier(String clientId,String cellphone,String eName,String shopCode,String userName,String openId,String smsCode,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("cellphone", cellphone));
		params.add(new BasicNameValuePair("eName", eName));
		params.add(new BasicNameValuePair("shopCode", shopCode));
		params.add(new BasicNameValuePair("userName", userName));
		params.add(new BasicNameValuePair("openId", openId));
		params.add(new BasicNameValuePair("smsCode", smsCode));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shopcourier/saveShopCourier.action", params);
		return webContent;
	}
	
	/** 168.微信中配送订单查询 Json */
	public String queryWXsendOrder(String clientId,Integer status,Integer lastId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("status", status.toString()));
		params.add(new BasicNameValuePair("lastId", lastId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/shopcourier/queryWXsendOrder.action", params);
		return webContent;
	}
	
	/** 169.验证公众号支付参数 Json */
	public String validWxPublicPay(String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/validWxPublicPay.action", params);
		return webContent;
	}
	

	/** 170.返回公众号mchId和partnerKey Json */
	public String getWxPublicDetail(String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/getWxPublicDetail.action", params);
		return webContent;
	}
	
	/** 171.用户存在小程序推荐码信息 Json */
	public String existsLiteAppCode(String clientId,String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/existsLiteAppCode.action", params);
		return webContent;
	}
	
	
	/** 172.获取企业相关信息  Json */
	public String getEnterpriseInfo(String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/getEnterpriseInfo.action", params);
		return webContent;
	}
	
	/** 172.获取企业相关信息 Json */
	public WxUserEntInfoModel getWxUserEntInfo(String authorizer_appid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/getEnterpriseInfo.action", params);
		JSONObject objJson = JSONObject.fromObject(webContent);
		if (!StringUtils.isEmpty(webContent) && objJson != null && objJson.has("data")) {
			String dataStr = objJson.optString("data");
			if (StringUtils.isEmpty(dataStr))
				return null;
			WxUserEntInfoModel result = (WxUserEntInfoModel) JSONObject.toBean(JSONObject.fromObject(dataStr), WxUserEntInfoModel.class);
			result.setAppid(authorizer_appid);
			return result;
		}else{
			if(objJson.has("code")&& !objJson.optString("code").equals("E00000"))
				logger.error("[msg:{}],[url:{}],[webContent:{}]", new Object[] { "用户登录接口失败", WebStyle.getApiUrl() + "/enterprise/getEnterpriseInfo.action?openid=" + authorizer_appid, webContent });
		}
		return null;
	}

	/** 173.店铺推广_我的账户情况  Json */
	public String myLiteAppEarningInfo(String authorizer_appid,String clientId,Integer type) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("type", type.toString()));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/myLiteAppEarningInfo.action", params);
		return webContent;
	}
	
	/** 174.微信钱包实名认证  Json */
	public String saveEarningWXAutonym(String authorizer_appid,String clientId,String openId,String autonym,Boolean isAutonym,String dealPass) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("openId", openId));
		params.add(new BasicNameValuePair("autonym", autonym));
		params.add(new BasicNameValuePair("isAutonym", isAutonym.toString()));
		params.add(new BasicNameValuePair("dealPass", MD5Util.convertMD5(dealPass)));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/saveEarningWXAutonym.action", params);
		return webContent;
	}
	
	/** 175.用户提现到微信钱包  Json */
	public String txToWxWallet(String authorizer_appid,String clientId,String openId,Double money,String dealPass) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("openId", openId));
		params.add(new BasicNameValuePair("money", money.toString()));
		params.add(new BasicNameValuePair("dealPass", MD5Util.convertMD5(dealPass)));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/peopleShare/txToWxWallet.action", params);
		return webContent;
	}
	
	/** 176.用户推客所属公众号,是否界面可跳转  Json */
	public String tuikeBelongEnterprise(String authorizer_appid,String clientId) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		params.add(new BasicNameValuePair("clientId", clientId));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/tuikeBelongEnterprise.action", params);
		return webContent;
	}
	
	/** 177.获取所有微信公众号信息  Json */
	public String getAllPublicAccount() {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/getAllPublicAccount.action", params);
		return webContent;
	}
	
	/** 178.企业防伪品牌  Json */
	public String getEnterPriseAntifake(String authorizer_appid,Integer citycode,String location) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
//		params.add(new BasicNameValuePair("citycode", citycode.toString()));
//		params.add(new BasicNameValuePair("location", location));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/enterprise/getEnterPriseAntifake.action", params);
		return webContent;
	}

	/** 179.是否存在注册送券活动  Json */
	public String exitRegeditSendQuan(String authorizer_appid,Integer eid,Integer cityId) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("cityId", cityId.toString()));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/sysActivity/exitRegeditSendQuan.action", params);
		return webContent;
	}
	
	/** 180.判断是否有注册领券活动  Json */
	public String existDiscountsCoupon(String authorizer_appid,Integer cityId,Integer citycode) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		params.add(new BasicNameValuePair("citycode", citycode.toString()));
		params.add(new BasicNameValuePair("cityId", cityId.toString()));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/imacode/existDiscountsCoupon.action", params);
		return webContent;
	}
	
	/** 181.编码送券领取  Json */
	public String imaCouponConvert(String authorizer_appid,Integer activityId,String clientId,String code_no,Integer cityId,Integer citycode) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("authorizer_appid", authorizer_appid));
		params.add(new BasicNameValuePair("activityId", activityId.toString()));
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("code_no", code_no));
		params.add(new BasicNameValuePair("cityId", cityId.toString()));
		params.add(new BasicNameValuePair("citycode", citycode.toString()));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/imacode/imaCouponConvert.action", params);
		return webContent;
	}
	
	/** 182.H5获取积分规则  Json */
	public String getIntegralRule(Integer eid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("eid", eid.toString()));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/integralH5/getIntegralRule.action", params);
		return webContent;
	}
	
	/** 183.H5获取我的积分明细  Json */
	public String getIntegralRecord(String clientId,Integer eid,Integer typeId,Integer pageIndex,Integer pageSize) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("clientId", clientId));
		params.add(new BasicNameValuePair("typeId", typeId.toString()));
		params.add(new BasicNameValuePair("pageIndex", pageIndex.toString()));
		params.add(new BasicNameValuePair("pageSize", pageSize.toString()));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/integralH5/getIntegralRecord.action", params);
		return webContent;
	}
	
	/** 184.H5获取积分商城首页数据(劵详情)  Json */
	public String getHomeIntegralShop(String clientId,Integer eid) {
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("eid", eid.toString()));
		params.add(new BasicNameValuePair("clientId", clientId));
		String webContent = HttpClientUtilOkHttp.postEvianApi(WebStyle.getApiUrl() + "/integralH5/getHomeIntegralShop.action", params);
		return webContent;
	}
	
	
}
