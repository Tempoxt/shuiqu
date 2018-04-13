package com.eviano2o.util.response;

import java.util.HashMap;
import java.util.Map;

/**返回结果定义*/
public class JsonResponseResultCodeDefine {
	public static final int WEIXIN_NO_CITY = -4;// 微信未选城市
	public static final int APP_NO_LOGIN = -3;// APP未登录
	public static final int WEIXIN_NO_OPENID = -2;// 微信OpenId超时
	public static final int WEIXIN_NO_LOGIN = -1;// 微信未绑定或者登录超时
	public static final int NO_DARA = 0;// 没有相关信息
	public static final int SUCCESS = 1; // 成功
	public static final int DB_ERROR = 2; // 数据库执行错误
	public static final int PRAMA_ERROR = 3; // 参数错误
	public static final int API_ERROR = 4; // 接口调用错误
	public static final int API_RESULT_ERROR = 5; // 接口调用返回结果错误

	private static Map<Integer, String> map = new HashMap<Integer, String>();
	static {
		map.put(WEIXIN_NO_CITY, "微信未选城市");
		map.put(APP_NO_LOGIN, "APP未登录");
		map.put(WEIXIN_NO_OPENID, "微信OpenId超时");
		map.put(WEIXIN_NO_LOGIN, "未登录或者登录超时");
		map.put(NO_DARA, "没有相关信息");
		map.put(SUCCESS, "成功");
		map.put(DB_ERROR, "数据库执行错误");
		map.put(PRAMA_ERROR, "参数错误");
		map.put(API_ERROR, "接口调用错误");
		map.put(API_RESULT_ERROR, "接口调用返回结果错误");
	}

	/**
	 * 返回文字结果
	 * 
	 * @return
	 */
	public static String getCodeValue(int code) {
		if (map.containsKey(code))
			return map.get(code);
		return map.get(NO_DARA) + "  " + code;
	}

	/**
	 * 字符串转换guid
	 * 
	 * @param uuid
	 * @return
	 */
	public static String StringToUUID(String raw) {
		StringBuffer sb = new StringBuffer(36);
		sb.append(raw.substring(0, 8));
		sb.append("-");
		sb.append(raw.substring(8, 12));
		sb.append("-");
		sb.append(raw.substring(12, 16));
		sb.append("-");
		sb.append(raw.substring(16, 20));
		sb.append("-");
		sb.append(raw.substring(20));
		return sb.toString();
	}
}
