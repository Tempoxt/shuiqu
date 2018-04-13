package com.fun.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class RequestUtils {

	/**
	 * 获取客户端IP地址，支持proxy
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @return IP地址
	 */
	public static String getRemoteAddr(HttpServletRequest req) {
		String ip = req.getHeader("X-Forwarded-For");
		if (StringUtils.isBlank(ip)) {
			String[] ips = StringUtils.split(ip, ',');
			if (ips != null) {
				for (String tmpip : ips) {
					if (StringUtils.isBlank(tmpip))
						continue;
					tmpip = tmpip.trim();
					if (isIPAddr(tmpip) && !tmpip.startsWith("10.")
							&& !tmpip.startsWith("192.168.")
							&& !"127.0.0.1".equals(tmpip)) {
						return tmpip.trim();
					}
				}
			}
		}
		ip = req.getHeader("x-real-ip");
		if (isIPAddr(ip))
			return ip;
		ip = req.getRemoteAddr();
		if (ip.indexOf('.') == -1)
			ip = "127.0.0.1";
		return ip;
	}
	
	/**
	 * 判断字符串是否是一个IP地址
	 * 
	 * @param addr
	 *            字符串
	 * @return true:IP地址，false：非IP地址
	 */
	public static boolean isIPAddr(String addr) {
		if (StringUtils.isEmpty(addr))
			return false;
		String[] ips = StringUtils.split(addr, '.');
		if (ips.length != 4)
			return false;
		try {
			int ipa = Integer.parseInt(ips[0]);
			int ipb = Integer.parseInt(ips[1]);
			int ipc = Integer.parseInt(ips[2]);
			int ipd = Integer.parseInt(ips[3]);
			return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
					&& ipc <= 255 && ipd >= 0 && ipd <= 255;
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 获得POST 过来参数设置到新的params中
	 * 
	 * @param requestParams
	 *            POST 过来参数Map
	 * @return 新的Map
	 */
	public static Map<String, String> genMapByRequestParas(Map requestParams) {
		Map<String, String> params = new HashMap<String, String>();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}

		return params;
	}
	
}