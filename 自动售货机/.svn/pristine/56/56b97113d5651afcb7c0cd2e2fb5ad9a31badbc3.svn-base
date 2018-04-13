package com.fun.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.fun.bean.WxPayRefundModel;
import com.fun.bean.XCXPayModel;
import com.fun.util.HttpClientUtil;
import com.fun.util.XmlStringUtil;

/** 一定要注意支付的参数名和值的大小写，一定一定要注意 */
@Repository
public class WxPayRefund {
	private static final Logger logger = LoggerFactory.getLogger(WxPayRefund.class);
	public String Pay(WxPayRefundModel wxPayRefundModel)
	{
		logger.info("=======================================================================微信JS退款开始");
		logger.info("微信JS退款入口参数："+wxPayRefundModel.toString());
		/** 封装参数 */
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", wxPayRefundModel.getAppId());
		packageParams.put("mch_id", wxPayRefundModel.getMchId());
		packageParams.put("nonce_str", wxPayRefundModel.getNonceStr().toLowerCase());
		packageParams.put("out_trade_no", wxPayRefundModel.getOutTradeNo()); // 商户订单号
		packageParams.put("out_refund_no", wxPayRefundModel.getOutRefundNo()); // 商户退款单号
		packageParams.put("total_fee", String.valueOf(wxPayRefundModel.getTotalFee())); // 支付金额，这边需要转成字符串类型，否则后面的签名会失败 
		packageParams.put("refund_fee", String.valueOf(wxPayRefundModel.getRefundFee())); // 退款金额，这边需要转成字符串类型，否则后面的签名会失败 
		RequestHandler reqHandler = new RequestHandler();
		reqHandler.init(wxPayRefundModel.getAppKey());
		String sign = reqHandler.createSign(packageParams);
		
		/** 封装报文 */
		String xml = "<xml>"+
			"<appid><![CDATA[" + wxPayRefundModel.getAppId() + "]]></appid>"+
			"<out_refund_no><![CDATA["+ wxPayRefundModel.getOutRefundNo() +"]]></out_refund_no>"+
			"<sign><![CDATA[" + sign + "]]></sign>"+
			"<refund_fee>" + wxPayRefundModel.getRefundFee() + "</refund_fee>"+
			"<total_fee>" + wxPayRefundModel.getTotalFee() + "</total_fee>"+
			"<out_trade_no><![CDATA[" + wxPayRefundModel.getOutTradeNo() + "]]></out_trade_no>"+
			"<mch_id><![CDATA[" + wxPayRefundModel.getMchId() + "]]></mch_id>"+
			"<nonce_str><![CDATA[" + wxPayRefundModel.getNonceStr().toLowerCase() + "]]></nonce_str>"+
			"</xml>";

		//获取预支付ID
		String createOrderURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
		logger.info("微信退款请求地址: "+createOrderURL+", 请求数据: "+xml);
		String result = HttpClientUtil.sslPost(createOrderURL, xml,wxPayRefundModel.getMchId());
		logger.info("微信退款请求返回结果: "+result);
		
		
		logger.info("=======================================================================微信JS退款结束");
		return result;
	}
}
