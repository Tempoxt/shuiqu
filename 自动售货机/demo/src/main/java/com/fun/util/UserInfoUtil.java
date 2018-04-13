package com.fun.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.fun.bean.VendingMachineConfig;

public class UserInfoUtil {
	
	
	public static JSONObject gainUserInfo(String access_token,String openId,int platform){
		Logger logger = LoggerFactory.getLogger(UserInfoUtil.class);
		if(1==platform){
			String private_key = VendingMachineConfig.getAilPrivateKey();
			String alipay_public_key = VendingMachineConfig.getAilPublicKey();
			AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",VendingMachineConfig.getAilPayAppId(),private_key ,"json","utf-8",alipay_public_key ,"RSA2");
			AlipayUserUserinfoShareRequest request = new AlipayUserUserinfoShareRequest();
			try {
				AlipayUserUserinfoShareResponse response = alipayClient.execute(request,access_token);
				if(response.isSuccess()){
					JSONObject jsonObject = JSON.parseObject(response.getBody()).getJSONObject("alipay_user_userinfo_share_response");
					logger.info("支付宝 用户信息 : "+jsonObject);
					return jsonObject;
				} else {
					logger.info("[保存用户信息失败 :{}]",new Object[]{response.getBody()});
				}
			} catch (AlipayApiException e) {
				logger.info("[保存用户信息错误:{}]",new Object[]{e.getErrMsg()});
			}
		}else if(2==platform){
			String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openId+"&lang=zh_CN";
			String doGet = HttpClientUtil.doGet(url);
			logger.info("微信 用户信息 : "+doGet);
			JSONObject userInfoJson = JSON.parseObject(doGet);
			return userInfoJson;
		}
		return null;
	}
}
