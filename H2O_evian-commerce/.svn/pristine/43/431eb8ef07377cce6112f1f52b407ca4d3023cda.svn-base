package com.eviano2o.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;

public class EvianSignatureUtil {
	public static final byte[] signKey = {66, 52, 49, 55, 52, 53, 56, 68, 57, 53, 67, 55, 57, 65, 49, 50, 57, 68, 48, 69, 53, 67, 68, 70, 53, 54, 55, 69, 70, 55, 54, 65, 54, 50, 48, 70, 68, 70, 53, 56, 69, 53, 69, 57, 54, 53, 55, 50, 48, 67, 67, 68, 70, 48, 66, 49, 52, 52, 56, 48, 66, 66, 56, 55, 67, 65, 57, 49, 67, 55, 52, 67, 48, 68, 67, 48, 52, 52, 54, 51, 56, 70, 54, 55, 56, 51, 69, 53, 67, 70, 56, 54, 70, 53, 52, 55, 69, 55, 56, 48, 55, 70, 56, 52, 67, 52, 67, 54, 53, 50, 65, 55, 67, 57, 48, 54, 70, 51, 70, 53, 69, 65, 50, 48, 65, 70, 69, 49};

	/**
	 * 签名生成算法
	 * 
	 * @param params
	 *            <String,String> params请求参数集，所有参数必须已转换为字符串类型
	 * @param secret
	 *            签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignature(Map<String, String> params, String secret) throws IOException {
		// 先将参数以其参数名的字典序升序进行排序
		Map<String, String> sortedParams = new TreeMap<String, String>(params);
		Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder basestring = new StringBuilder();
		for (Entry<String, String> param : entrys) {
			// 过滤掉没有参数值的键值对
			if (!param.getKey().equalsIgnoreCase("sign") && StringUtils.isEmpty(param.getKey()) == false && StringUtils.isEmpty(param.getValue()) == false) {
				basestring.append(param.getKey()).append("=").append(param.getValue());
			}
		}
		basestring.append(secret);

		// 使用MD5对待签名串求签
		byte[] bytes = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
		} catch (GeneralSecurityException ex) {
			throw new IOException(ex);
		}

		// 将MD5输出的二进制结果转换为小写的十六进制
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex);
		}
		return sign.toString().toUpperCase();
	}

	/**
	 * 签名生成算法
	 * 
	 * @param params
	 *            <String,String> params请求参数集，所有参数必须已转换为字符串类型
	 * @param secret
	 *            签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignature(List<BasicNameValuePair> nameValues) {
		try {
			Map<String, String> params = new HashMap<String, String>();

			for (BasicNameValuePair v : nameValues) {
				params.put(v.getName(), v.getValue());
			}

			// 先将参数以其参数名的字典序升序进行排序
			Map<String, String> sortedParams = new TreeMap<String, String>(params);
			Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

			// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
			StringBuilder basestring = new StringBuilder();
			for (Entry<String, String> param : entrys) {
				// 过滤掉没有参数值的键值对
				if (!param.getKey().equalsIgnoreCase("sign") && StringUtils.isEmpty(param.getKey()) == false && StringUtils.isEmpty(param.getValue()) == false) {
					basestring.append(param.getKey()).append("=").append(param.getValue());
				}
			}
			basestring.append(new String(signKey));
			//System.out.println("----------------------------------------------------------  "+basestring);
			// 使用MD5对待签名串求签
			byte[] bytes = null;
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
			} catch (GeneralSecurityException ex) {
				throw new IOException(ex);
			}

			// 将MD5输出的二进制结果转换为小写的十六进制
			StringBuilder sign = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(bytes[i] & 0xFF);
				if (hex.length() == 1) {
					sign.append("0");
				}
				sign.append(hex);
			}
			return sign.toString().toUpperCase();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 返回参数链接，方便记录日志
	*/
	public static String getParamStr(List<BasicNameValuePair> nameValues){
		Map<String, String> params = new HashMap<String, String>();

		for (BasicNameValuePair v : nameValues) {
			params.put(v.getName(), v.getValue());
		}

		// 先将参数以其参数名的字典序升序进行排序
		Map<String, String> sortedParams = new TreeMap<String, String>(params);
		Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder basestring = new StringBuilder();
		for (Entry<String, String> param : entrys) {
			// 过滤掉没有参数值的键值对
			if (!param.getKey().equalsIgnoreCase("sign") && StringUtils.isEmpty(param.getKey()) == false && StringUtils.isEmpty(param.getValue()) == false) {
				basestring.append(param.getKey()+"="+param.getValue()+", ");
			}
		}
		return basestring.toString();
	}
}
