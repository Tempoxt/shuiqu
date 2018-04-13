package com.eviano2o.controller.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eviano2o.wxDeciphering.AesException;
import com.eviano2o.wxDeciphering.WXBizMsgCrypt;


public class DecipheringHelper {

	private static final Logger logger = LoggerFactory.getLogger(DecipheringHelper.class);
	
	/*String token = "sqtoken";
	String encodingAesKey = "isUjro7KQIxOWbbvEtJQ6pVz1AlVh1iZb7yDRAKqQ5K";
	String appId = "wx9c8cd3284651135f";
	*//**
	 * 第三方解密
	 * @return 
	 *//*
	public String getComponent_verify_ticket(){
		try {
			WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
		} catch (AesException e) {
			// TODO Auto-generated catch block
			logger.error("第三方解密失败 "+e);
		}
		
		String result2 = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
		return "";
	}*/
}
