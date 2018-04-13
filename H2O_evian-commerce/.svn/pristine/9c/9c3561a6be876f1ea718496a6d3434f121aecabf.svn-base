package com.eviano2o.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.eviano2o.bean.weixin.WxProductVouchers;
import com.eviano2o.bean.weixin.e_enterprise_wechatliteapp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SysParamCache {
	static Map<String, String> cache = new HashMap<String, String>();

	public static void setCache(String key, String obj) {
		cache.put(key, obj);
	}

	public static String getCache(String key) {
		return cache.get(key);
	}

	public static void removeCache(String key) {
		cache.remove(key);
	}

	public static void clearCache() {
		cache.clear();
	}

	public static Boolean isNullObj() {
		return cache == null || cache.size() == 0 || cache.isEmpty();
	}

	public static Map<String, String> getAllParam(){
		return cache;
	}
	
	
	
	
	static Map<String, e_enterprise_wechatliteapp> wechatLiteapp;

	public static Map<String, e_enterprise_wechatliteapp> getWechatLiteapp() {
		return wechatLiteapp;
	}

	public static void setWechatLiteapp(Map<String, e_enterprise_wechatliteapp> wechatLiteapp) {
		SysParamCache.wechatLiteapp = wechatLiteapp;
	}
	
	
	
	
}
