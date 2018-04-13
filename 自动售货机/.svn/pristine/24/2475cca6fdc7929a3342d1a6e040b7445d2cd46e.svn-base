package com.fun.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fun.util.HttpClientUtil;

/**
 * 获取公众号token
 * 获取公众号js-api Ticket
 * @author XHX
 *
 */
public class WxConfig {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static String Gzh_Access_Token;
	
	public static String Gzh_Jsapi_Ticket;
	
	
	public boolean setAccessToken(){
//		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx5288d5e0e47b4f76&secret=f2601ba78aca4888877b81f07540b892";
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+VendingMachineConfig.getWxPayAppId()+"&secret="+VendingMachineConfig.getWxPaySecret();
		String access_tokenJson = HttpClientUtil.doGet(url);
		logger.info("Gzh_Access_TokenJson : "+access_tokenJson);
		JSONObject parseObject = JSON.parseObject(access_tokenJson);
		Object access_token = parseObject.get("access_token");
		if(access_token!=null){
			Gzh_Access_Token = (String) access_token;
			return true;
		}else{
			logger.error("Gzh_Access_Token获取失败 = "+parseObject);
			return false;
		}
	}
	
	public void setJsapiTicket(){
		if(Gzh_Access_Token!=null){
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+Gzh_Access_Token+"&type=jsapi";
			String jsapi_ticketJson = HttpClientUtil.doGet(url);
			logger.info("Gzh_Jsapi_Ticket : "+jsapi_ticketJson);
			JSONObject parseObject = JSON.parseObject(jsapi_ticketJson);
			Object ticket = parseObject.get("ticket");
			if(ticket!=null){
				Gzh_Jsapi_Ticket = (String) ticket;
			}else{
				logger.error("Gzh_Jsapi_Ticket获取失败 = {errcode:"+parseObject.getByteValue("errcode")+",errmsg:"+parseObject.getByteValue("errmsg"));
			}
		}else{
			if(setAccessToken()){
				setJsapiTicket();
			}else{
				logger.error("Gzh_Access_Token获取失败 所以获取不到Gzh_Jsapi_Ticket");
			};
		}
	}
}
