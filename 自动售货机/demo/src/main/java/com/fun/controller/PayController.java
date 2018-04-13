package com.fun.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.fun.bean.VendingMachineConfig;
import com.fun.bean.WxPayRefundModel;
import com.fun.bean.XCXPayModel;
import com.fun.bean.vendor.VendorDoor;
import com.fun.bean.vendor.VendorMainboard;
import com.fun.bean.vendor.VendorOrder;
import com.fun.bean.vendor.VendorProduct;
import com.fun.pay.PayResponseReturnModel;
import com.fun.pay.WxPay;
import com.fun.pay.WxPayRefund;
import com.fun.pay.h5.WxH5Pay;
import com.fun.pay.h5.ZFBPay;
import com.fun.service.IVendorDoorService;
import com.fun.service.IVendorOrderService;
import com.fun.service.IVendorProductService;
import com.fun.util.CallBackPar;
import com.fun.util.HttpClientUtil;
import com.fun.util.OrderSave;
import com.fun.util.StrUtils;

@RestController
@RequestMapping("/pay")
public class PayController extends BaseController{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	WxH5Pay wxH5Pay;
	
	@Autowired
	WxPay wxPay;
	
	@Autowired
	IVendorProductService productService;
	
	@Autowired
	IVendorDoorService doorService;
	
	@Autowired
	IVendorOrderService orderService;
	
	@RequestMapping("/zfbNotifyurl")
	public String zfbNotify_url(HttpServletRequest request){
		Map<Object, Object> notifyMap = new HashMap<Object, Object>();
		Map map=request.getParameterMap();  
	    Set keSet=map.entrySet();  
	    for(Iterator itr=keSet.iterator();itr.hasNext();){  
	        Map.Entry me=(Map.Entry)itr.next();  
	        Object ok=me.getKey();  
	        Object ov=me.getValue();  
	        String[] value=new String[1];  
	        if(ov instanceof String[]){  
	            value=(String[])ov;  
	        }else{  
	            value[0]=ov.toString();  
	        }  
	  
	        for(int k=0;k<value.length;k++){  
	            System.out.println(ok+"="+value[k]);
	            notifyMap.put(ok, value[k]);
	        }  
	      }  
	    logger.info("[支付回调:{}] [notifyMap:{}]",new Object[]{"支付宝",notifyMap});
	    return "success";
	}
	
	@RequestMapping("/zfbzf")
	public Map<String, Object> zfbzf(HttpServletRequest request,Integer mcId,Integer boxNum,Integer productId,Double price) throws IOException{
		logger.info("[bmId:{}] [vmId:{}] [boxNum:{}] [productId:{}] [price:{}]",
				new Object[] { request.getSession().getAttribute("bmId"), mcId, boxNum, productId ,price});
		VendorDoor vendorDoor = doorService.findDoorByContainerOrDoor((Integer)request.getSession().getAttribute("bmId"), mcId, boxNum);
		logger.info("[vendorDoor:{}] [request.getSession().getAttribute(\"bmId\"):{}] [mcId:{}] [boxNum:{}] [productId:{}] ",
				new Object[] {vendorDoor, request.getSession().getAttribute("bmId"), mcId, boxNum,productId});
		VendorProduct vendorProduct = null;
		Map<String, Object> parMap = CallBackPar.getParMap();
		if(vendorDoor.getProductId()==productId){
			vendorProduct = productService.findProductById(productId);
			if(!vendorProduct.getPrice().toString().equals(price.toString())){
				logger.error("vendorProduct.getPrice()="+vendorProduct.getPrice()+" price="+price);
				parMap.put("code","E00001");
				parMap.put("message", "请确认箱柜号是否正确");
				return parMap;
			}
		}else{
			logger.error("vendorDoor.getProductId()="+vendorDoor.getProductId()+" productId="+productId);
			parMap.put("code","E00001");
			parMap.put("message", "请确认箱柜号是否正确");
			return parMap;
		}
		ZFBPay pay = new ZFBPay(); 
//		PrintWriter out = response.getWriter();
//		out.print(pay.pay());
		Map<String, Object> addOrderMap = orderService.addOrder(getAlipayUserId(request),1,(String)request.getSession().getAttribute("mainboardNoMD5"), boxNum, productId, vendorProduct.getPrice(), 0.0, "");
		String addOrder = (String)addOrderMap.get("orderNo");
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		if(addOrder==null){
			parMap.put("code","E00001");
			parMap.put("message", addOrderMap.get("TAG"));
			return parMap;
			
		}else{
			if(!pattern.matcher(addOrder).matches()){
				parMap.put("code","E00001");
				parMap.put("message", addOrder);
				return parMap;
			}else{
				Map<String, String> param = new HashMap<String, String>();
				param.put("orderId", ((Integer)addOrderMap.get("orderId")).toString());
				param.put("taskType", "1");
				// 订单生成 开启任务调度
				String doPost = HttpClientUtil.doPost("http://box.shuiqoo.cn:7080/Container_Task/evian/task/saveOrderTask.action", param );
				logger.info("[project:开启任务调度] [orderId:{}] [taskType:1] [return:{}]",new Object[] { addOrderMap.get("orderId"), doPost});
			}
			
		}
        
		parMap.put("data", pay.pay(mcId,boxNum,price,addOrder,vendorProduct.getProductName()));
		return parMap;
	}
	
	@RequestMapping("/wxh5zf")
	public Map<String, Object> wxh5zf(HttpServletRequest request){
		Map<String, Object> parMap = CallBackPar.getParMap();
		XCXPayModel xcxPayModel = new XCXPayModel();
		xcxPayModel.setAppId("wx5e24d332ca079ac1");
		xcxPayModel.setMchId("1301591101");
		UUID uuid=UUID.randomUUID();
	    String str = uuid.toString(); 
	    String uuidStr=str.replace("-", "");
		xcxPayModel.setNonceStr(uuidStr);
		xcxPayModel.setBody("售货机");
		xcxPayModel.setOrderSN(OrderSave.getOrderNew());
		xcxPayModel.setTotalFee("1");
		xcxPayModel.setCreateIp(HttpClientUtil.getRequestIP(request));
		xcxPayModel.setNotifyUrl("http://box.shuiqoo.cn:7080/Container_Market/evian/paynotify/weixinNotify.action");
		xcxPayModel.setScene_info("{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://x10.shuiqoo.cn\",\"wap_name\": \"售货机\"}} ");
		xcxPayModel.setAppKey("ShQooappcomjkjehbnvzioejbazkmbao");
		PayResponseReturnModel pay = wxH5Pay.Pay(xcxPayModel);
		parMap.put("data", pay);
		return parMap;
	}
	
	@RequestMapping("/wxPay")
	public Map<String, Object> wxPay(HttpServletRequest request,Integer mcId,Integer boxNum,Integer productId,Double price){
		logger.info("[bmId:{}] [vmId:{}] [boxNum:{}] [productId:{}] [price:{}]",
				new Object[] { request.getSession().getAttribute("bmId"), mcId, boxNum, productId ,price});
		VendorDoor vendorDoor = doorService.findDoorByContainerOrDoor((Integer)request.getSession().getAttribute("bmId"), mcId, boxNum);
		logger.info("[vendorDoor:{}]",new Object[]{vendorDoor});
		VendorProduct vendorProduct = null;
		Map<String, Object> parMap = CallBackPar.getParMap();
		if(vendorDoor.getProductId()==productId){
			vendorProduct = productService.findProductById(productId);
			if(!vendorProduct.getPrice().toString().equals(price.toString())){
				logger.error("vendorProduct.getPrice()="+vendorProduct.getPrice()+" price="+price);
				parMap.put("code","E00001");
				parMap.put("message", "请确认箱柜号是否正确");
				return parMap;
			}
		}else{
			logger.error("vendorDoor.getProductId()="+vendorDoor.getProductId()+" productId="+productId);
			parMap.put("code","E00001");
			parMap.put("message", "请确认箱柜号是否正确");
			return parMap;
		}
		XCXPayModel xcxPayModel = new XCXPayModel();
//		xcxPayModel.setAppId("wx5288d5e0e47b4f76");
//		xcxPayModel.setMchId("1488773192");
		xcxPayModel.setAppId(VendingMachineConfig.getWxPayAppId());
		xcxPayModel.setMchId(VendingMachineConfig.getWxPayMchId());
		UUID uuid=UUID.randomUUID();
		String str = uuid.toString();
		String uuidStr=str.replace("-", "");
		xcxPayModel.setNonceStr(uuidStr);
		xcxPayModel.setBody(vendorProduct.getProductName());
		String openId = getWxOpenId(request);
		if(StrUtils.isEmOrUn(openId)){
			logger.error("[message:{}] [openId:{}]",new Object[]{"系统异常",openId});
			parMap.put("code","E00001");
			parMap.put("message", "系统异常");
			return parMap;
		}
		
		xcxPayModel.setOpenId(openId);
		Map<String, Object> addOrderMap = orderService.addOrder(openId,2,(String)request.getSession().getAttribute("mainboardNoMD5"), boxNum, productId, vendorProduct.getPrice(), 0.0, "");
		String addOrder = (String)addOrderMap.get("orderNo");
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
		if(addOrder==null){
			parMap.put("code","E00001");
			parMap.put("message", addOrderMap.get("TAG"));
			return parMap;
			
		}else{
			if(!pattern.matcher(addOrder).matches()){
				parMap.put("code","E00001");
				parMap.put("message", addOrder);
				return parMap;
			}else{
				Map<String, String> param = new HashMap<String, String>();
				param.put("orderId", ((Integer)addOrderMap.get("orderId")).toString());
				param.put("taskType", "1");
				// 订单生成 开启任务调度
				String doPost = HttpClientUtil.doPost("http://box.shuiqoo.cn:7080/Container_Task/evian/task/saveOrderTask.action", param );
				logger.info("[project:开启任务调度] [orderId:{}] [taskType:1] [return:{}]",new Object[] { addOrderMap.get("orderId"), doPost});
			}
			
		}
		xcxPayModel.setOrderSN(addOrder);
		BigDecimal bigDecimal = new BigDecimal(vendorProduct.getPrice().toString());
		BigDecimal big100 = new BigDecimal("100");
		xcxPayModel.setTotalFee((big100.multiply(bigDecimal).intValue())+"");
		logger.info("TotalFee : "+xcxPayModel.getTotalFee());
		xcxPayModel.setCreateIp(HttpClientUtil.getRequestIP(request));
		xcxPayModel.setNotifyUrl("http://box.shuiqoo.cn:7080/Container_Market/evian/paynotify/weixinNotify.action");
//		xcxPayModel.setOpenId("o2k5MwqDeB8fXc_rR-J-vLeCIZDM");
//		xcxPayModel.setAppKey("shuiqoosfgweefsqoo99j6ehgfh5o511");
		xcxPayModel.setAppKey(VendingMachineConfig.getWxPayAppKey());
		PayResponseReturnModel pay = wxPay.Pay(xcxPayModel);
		parMap.put("data", pay);
		return parMap;
	}
	
	@RequestMapping("/refund")
	public Map<String, Object> refund(String type,Integer orderId,String total_fee){
		logger.info("[project:开始退款] [type:{}] [orderId:{}] [total_fee:{}]",new Object[] { type, orderId,total_fee});
		Map<String, Object> parMap = CallBackPar.getParMap();
		if("wechat".equals(type)){
			WxPayRefundModel wx = new WxPayRefundModel();
			wx.setAppId(VendingMachineConfig.getWxPayAppId());
			wx.setAppKey(VendingMachineConfig.getWxPayAppKey());
			wx.setMchId(VendingMachineConfig.getWxPayMchId());
			UUID uuid=UUID.randomUUID();
		    String str = uuid.toString(); 
		    String uuidStr=str.replace("-", "");
			wx.setNonceStr(uuidStr);
			String orderNew = OrderSave.getOrderNew();
			System.out.println("setOutRefundNo ="+orderNew);
			wx.setOutRefundNo(orderNew);
			VendorOrder order = orderService.findOrderByOrderId(orderId);
			wx.setOutTradeNo(order.getOrderNo());
			BigDecimal totalFeeBigDecimal = new BigDecimal(order.getRealityPrice().toString());
			BigDecimal refundFeeCigDecimal = new BigDecimal(order.getRealityPrice().toString());
			BigDecimal big100 = new BigDecimal("100");
			wx.setRefundFee((big100.multiply(refundFeeCigDecimal).intValue())+"");
			wx.setTotalFee((big100.multiply(totalFeeBigDecimal).intValue())+"");
			WxPayRefund a = new WxPayRefund();
			String pay = a.Pay(wx);
			JSONObject jsonObject = XML.toJSONObject(pay);
			// 类型转换 将json通过alibabajson 转为map
			Map<String, Object> parse = (Map<String, Object>) JSON.parse(jsonObject.get("xml").toString());
			parMap.put("data", parse);
			return parMap;
		}else if("alipay".equals(type)){
			ZFBPay pay = new ZFBPay();
			VendorOrder order = orderService.findOrderByOrderId(orderId);
			JSONObject jsonObject = pay.refund(Double.valueOf(total_fee), order.getOrderNo());
			// 类型转换 将json通过alibabajson 转为map
			Map<String, Object> parse = (Map<String, Object>) JSON.parse(jsonObject.toString());
			parMap.put("data", parse);
			return parMap;
		}
		parMap.put("code", "E00001");
		parMap.put("message", "退款类型不正确");
		return parMap;
	}
	
}
