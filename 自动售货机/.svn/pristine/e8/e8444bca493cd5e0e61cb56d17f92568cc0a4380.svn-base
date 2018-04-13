package com.fun.pay.h5;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.fun.bean.VendingMachineConfig;

public class ZFBPay { 

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String pay(Integer cabinetNum,Integer number,Double price,String order,String productName){
		String private_key = VendingMachineConfig.getAilPrivateKey();
		String alipay_public_key = VendingMachineConfig.getAilPublicKey();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",VendingMachineConfig.getAilPayAppId(),private_key ,"json","utf-8",alipay_public_key ,"RSA2");
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
		alipayRequest.setReturnUrl("https://svm.shuiqoo.cn/funVm/paySuccess?cabinetNum="+cabinetNum+"&boxNum="+number);
		alipayRequest.setNotifyUrl("http://box.shuiqoo.cn:7080/Container_Market/evian/paynotify/alipayNotify.action");//在公共参数中设置回跳和通知地址
//		alipayRequest.setNotifyUrl("https://x9.shuiqoo.cn/pay/zfbNotifyurl");//在公共参数中设置回跳和通知地址
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date afterDate = new Date(new Date().getTime() + 61000);
		alipayRequest.setBizContent("{" +
				" \"subject\":\"测试支付\"," +
				" \"out_trade_no\":\""+order+"\"," +
				" \"total_amount\":\""+price+"\"," +
				" \"subject\":\""+productName+"\"," +
				" \"time_expire\":\""+sdf.format(afterDate)+"\"," + // 结束时间
				" \"product_code\":\"QUICK_WAP_PAY\"" +
				" }");//填充业务参数
				String form="";
				try {
					form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
				} catch (AlipayApiException e) {
//					e.printStackTrace();
					logger.error("AlipayApiException : "+e);
				}
				logger.info("orderNo : "+order+" form : "+form);
				
			return form;
	}
	
	public JSONObject refund(Double price,String orderNo){
		String private_key = VendingMachineConfig.getAilPrivateKey();
		String alipay_public_key = VendingMachineConfig.getAilPublicKey();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",VendingMachineConfig.getAilPayAppId(),private_key ,"json","utf-8",alipay_public_key ,"RSA2");
		AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();//创建API对应的request
		alipayRequest.setBizContent("{" +
				" \"out_trade_no\":\""+orderNo+"\"," +
				" \"refund_amount\":"+price+
				" }");//填充业务参数
		JSONObject json = new JSONObject();
		
		try {
			AlipayTradeRefundResponse response = alipayClient.execute(alipayRequest);
			if(response.isSuccess()){
				if("10000".equals(response.getCode())){
					json.put("return_code", "SUCCESS");
					json.put("return_msg", "OK");
				}else{
					json.put("return_code", "FAIL");
					json.put("return_msg", response.getSubMsg());
				}
			} else {
				json.put("return_code", "FAIL");
				json.put("return_msg", response.getSubMsg());
			}
			logger.info("orderNo : "+orderNo+" json : "+json +"body : "+response.getBody());
		} catch (AlipayApiException e1) {
			logger.error(e1.getErrMsg());
		}
		
		return json;
	}
}
