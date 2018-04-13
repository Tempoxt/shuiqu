package com.fun.pay.h5;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.fun.bean.XCXPayModel;
import com.fun.pay.PayResponseReturnModel;
import com.fun.pay.RequestHandler;
import com.fun.util.HttpClientUtil;
import com.fun.util.XmlStringUtil;
@Repository
public class WxH5Pay {

	private static final Logger logger = LoggerFactory.getLogger(WxH5Pay.class);
	public PayResponseReturnModel Pay(XCXPayModel xcxPayModel)
	{
		logger.info("=======================================================================微信H5JS支付开始");
		logger.info("微信H5JS支付入口参数："+xcxPayModel.toString());
		/** 封装参数 */
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", xcxPayModel.getAppId());
		packageParams.put("mch_id", xcxPayModel.getMchId());
		packageParams.put("nonce_str", xcxPayModel.getNonceStr().toLowerCase());
		packageParams.put("body", xcxPayModel.getBody());
		packageParams.put("out_trade_no", xcxPayModel.getOrderSN()); // 商户订单号
		packageParams.put("total_fee", String.valueOf(xcxPayModel.getTotalFee())); // 支付金额，这边需要转成字符串类型，否则后面的签名会失败 
		packageParams.put("spbill_create_ip", xcxPayModel.getCreateIp());
		packageParams.put("notify_url", xcxPayModel.getNotifyUrl()); // 支付成功后的回调地址
		packageParams.put("trade_type", "MWEB"); // 支付方式  
//		packageParams.put("openid", xcxPayModel.getOpenId());
		packageParams.put("scene_info", xcxPayModel.getScene_info());
		
		RequestHandler reqHandler = new RequestHandler();
		reqHandler.init(xcxPayModel.getAppKey());
		String sign = reqHandler.createSign(packageParams);
		
		/** 封装报文 */
		String xml = "<xml>"+
			"<appid><![CDATA[" + xcxPayModel.getAppId() + "]]></appid>"+
			"<trade_type><![CDATA[MWEB]]></trade_type>"+
			"<scene_info><![CDATA[" + xcxPayModel.getScene_info() + "]]></scene_info>"+
			"<sign><![CDATA[" + sign + "]]></sign>"+
			"<spbill_create_ip><![CDATA[" + xcxPayModel.getCreateIp() + "]]></spbill_create_ip>"+
			"<total_fee>" + xcxPayModel.getTotalFee() + "</total_fee>"+
//			"<openid><![CDATA[" + xcxPayModel.getOpenId() + "]]></openid>"+
			"<out_trade_no><![CDATA[" + xcxPayModel.getOrderSN() + "]]></out_trade_no>"+
			"<mch_id><![CDATA[" + xcxPayModel.getMchId() + "]]></mch_id>"+
			"<body><![CDATA[" + xcxPayModel.getBody() + "]]></body>"+
			"<nonce_str><![CDATA[" + xcxPayModel.getNonceStr().toLowerCase() + "]]></nonce_str>"+
			"<notify_url><![CDATA[" + xcxPayModel.getNotifyUrl() + "]]></notify_url>"+
			"</xml>";

		//获取预支付ID
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		logger.info("微信H5支付prepayId请求地址: "+createOrderURL+", 请求数据: "+xml);
		String prepayContent = HttpClientUtil.post(createOrderURL, xml);
		logger.info("微信H5支付prepayId请求返回结果: "+prepayContent);
		
		
		Map map = new HashMap();
		try{
			map = XmlStringUtil.stringToXMLParse(prepayContent);
		}catch (Exception e){}
		String prepayId = map.containsKey("prepay_id")?map.get("prepay_id").toString():"";
		/** 封装签名参数 */
		String packages = "prepay_id="+prepayId;
		Date date = new Date();
		xcxPayModel.setTimeStamp(String.valueOf(date.getTime()));
		SortedMap<String, String> paySignParams = new TreeMap<String, String>();
		paySignParams.put("appId", xcxPayModel.getAppId());
		paySignParams.put("timeStamp", xcxPayModel.getTimeStamp().toString());  
		paySignParams.put("nonceStr", xcxPayModel.getNonceStr());  
		paySignParams.put("package", packages);  
		paySignParams.put("signType", "MD5");
		
		RequestHandler paySingHandler = new RequestHandler();
		paySingHandler.init(xcxPayModel.getAppKey());
		String paySign = paySingHandler.createSign(paySignParams);
		
		PayResponseReturnModel result = new PayResponseReturnModel();
        result.setAppId(xcxPayModel.getAppId());
        result.setTimeStamp(xcxPayModel.getTimeStamp());
        result.setNonceStr(xcxPayModel.getNonceStr());
        result.setPackage(packages);
        result.setPaySign(paySign);
        result.setDanHao(xcxPayModel.getOrderSN());
        result.setTotalFee(xcxPayModel.getTotalFee());
        result.setCode("E00000");
//        result.setJiaMiID(xcxPayModel.getJiaMiID());
		
		/** 返回微信JS支付所需的package */
		logger.info("请求微信JS支付,返回结果：" + result.toString());
		
		logger.info("=======================================================================微信JS支付结束");
		return result;
	}
}
