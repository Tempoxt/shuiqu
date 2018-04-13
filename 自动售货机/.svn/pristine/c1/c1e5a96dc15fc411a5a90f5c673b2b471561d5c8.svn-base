package com.fun.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XCX3rdSession {

	private final static Logger logger = LoggerFactory.getLogger(XCX3rdSession.class);
	
	/**
	 * 获取3rd_session里的session_key
	 * @param request
	 * @param cacheKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getSession_Key(HttpServletRequest request,String cacheKey){
		Map<String, String> cacheKeyMap = (Map<String, String>) request.getSession().getAttribute(cacheKey);
		logger.info("getSession_Key->cacheKeyMap = "+cacheKeyMap+" cacheKey = "+cacheKey+" session = "+request.getSession());
		return cacheKeyMap.get("session_key");
	}
	
	/**
	 * 获取3rd_session里的openId
	 * @param request
	 * @param cacheKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getOpenId(HttpServletRequest request,String cacheKey){
		Map<String, String> cacheKeyMap = (Map<String, String>) request.getSession().getAttribute(cacheKey);
		return cacheKeyMap.get("openId");
	}
}
