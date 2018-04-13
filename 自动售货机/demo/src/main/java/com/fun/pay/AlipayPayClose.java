package com.fun.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.fun.bean.VendingMachineConfig;

public class AlipayPayClose implements Runnable{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String order;
	
	public AlipayPayClose(String order) {
		this.order=order;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(30000);
			payClose();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error("支付订单交易关系延时错误！ :"+e.getMessage());
		}
		
	}

	
	
	public void payClose(){
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",VendingMachineConfig.getAilPayAppId(),VendingMachineConfig.getAilPrivateKey() ,"json","utf-8",VendingMachineConfig.getAilPublicKey() ,"RSA2");
		AlipayTradeCloseRequest tradeCloseRequest = new AlipayTradeCloseRequest();
		tradeCloseRequest.setNotifyUrl("https://svm.shuiqoo.cn/pay/zfbNotifyurl");
		tradeCloseRequest.setBizContent("{" +
				" \"out_trade_no\":\""+order+"\"," +
				" }");//填充业务参数
		try {
			AlipayTradeCloseResponse response = alipayClient.execute(tradeCloseRequest);
			if(response.isSuccess()){
				logger.info(order+"单号交易关闭成功");
			} else {
				logger.error(order+"单号交易关闭失败: "+response.getSubMsg());
			}
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			logger.error(order+"单号交易关闭失败: "+e.getErrMsg());
		}
	}
}
