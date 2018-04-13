package com.eviano2o.wxpay;


import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eviano2o.util.HttpClientUtil;
import com.eviano2o.util.XmlStringUtil;

/** 支付参数验证，后台商户添加了公众号mchid等信息后需要验证是否匹配AppId */
public class WxPayParamCheck {
	private static final Logger logger = LoggerFactory.getLogger(WxPayParamCheck.class);
	public JSONObject CheckPay(WxPayModel wxPayRequest)
	{
		logger.info("=======================================================================公众号支付测试开始");
		logger.info("支付参数测试："+wxPayRequest.toString());
		/** 封装参数 */
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", wxPayRequest.getAppId());
		packageParams.put("mch_id", wxPayRequest.getMchId());
		packageParams.put("nonce_str", wxPayRequest.getNonceStr().toLowerCase());
		packageParams.put("body", wxPayRequest.getBody());
		packageParams.put("out_trade_no", wxPayRequest.getOrderSN());
		packageParams.put("total_fee", String.valueOf(wxPayRequest.getTotalFee()));
		packageParams.put("spbill_create_ip", wxPayRequest.getCreateIp());
		packageParams.put("notify_url", wxPayRequest.getNotifyUrl());
		//packageParams.put("sub_mch_id", wxPayRequest.getSubMchId());
		packageParams.put("trade_type", "JSAPI");
		packageParams.put("openid", wxPayRequest.getOpenId());
		
		RequestHandler reqHandler = new RequestHandler();
		reqHandler.init(wxPayRequest.getAppKey());
		String sign = reqHandler.createSign(packageParams);
		
		/** 封装报文 */
		String xml = "<xml>"+
			"<appid><![CDATA[" + wxPayRequest.getAppId() + "]]></appid>"+
			"<trade_type><![CDATA[JSAPI]]></trade_type>"+
			"<sign><![CDATA[" + sign + "]]></sign>"+
			"<spbill_create_ip><![CDATA[" + wxPayRequest.getCreateIp() + "]]></spbill_create_ip>"+
			"<total_fee>" + wxPayRequest.getTotalFee() + "</total_fee>"+
			"<openid><![CDATA[" + wxPayRequest.getOpenId() + "]]></openid>"+
			"<out_trade_no><![CDATA[" + wxPayRequest.getOrderSN() + "]]></out_trade_no>"+
			"<mch_id><![CDATA[" + wxPayRequest.getMchId() + "]]></mch_id>"+
			"<body><![CDATA[" + wxPayRequest.getBody() + "]]></body>"+
			"<nonce_str><![CDATA[" + wxPayRequest.getNonceStr().toLowerCase() + "]]></nonce_str>"+
			"<notify_url><![CDATA[" + wxPayRequest.getNotifyUrl() + "]]></notify_url>"+
			"</xml>";

		//获取预支付ID
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		logger.info("微信支付prepayId请求地址: "+createOrderURL+", 请求数据: "+xml);
		String prepayContent = HttpClientUtil.post(createOrderURL, xml);
		logger.info("微信支付prepayId请求返回结果: "+prepayContent);
		
		Map map = new HashMap();
		try{
			map = XmlStringUtil.stringToXMLParse(prepayContent);
		}catch (Exception e){}
		logger.info("=======================================================================公众号支付测试结束");
		if(map.containsKey("return_code") && map.containsKey("prepay_id") && map.get("return_code").toString().equals("SUCCESS"))
			return JSONObject.fromObject("{\"code\":\"E00000\",\"message\":\"验证成功，刷新后台可以看到最新结果\",\"data\":\"\"}");
		else {
			return JSONObject.fromObject("{\"code\":\"E00000\",\"message\":\"验证失败:" + (map.containsKey("return_code") ? map.get("return_code").toString():"")+ (map.containsKey("return_msg") ? map.get("return_msg").toString():"") + "\",\"data\":\"\"}");
		}
		
	}
	
}