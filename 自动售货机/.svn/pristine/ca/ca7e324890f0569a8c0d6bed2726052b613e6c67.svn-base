package com.fun.pay;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fun.util.MD5Util;


public class RequestHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
	//logger.info("[msg:{}]", new Object[] { "用户登录！"});
	
	/** 商户key */
	private String key;
	/** 编码格式 */
	private String charset = "UTF-8";

	/**
	 * 初始化函数。
	 */
	public void init(String partnerKey) {
		this.key = partnerKey;
	}

	/**
	 * 微信签名算法,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * @param packageParams
	 * @return
	 */
	public String createSign(SortedMap<String, String> packageParams) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		logger.info("微信支付sign: "+sb.toString());
		String sign = MD5Encode(sb.toString(), this.charset).toUpperCase();
		sign = MD5Util.md5(sb.toString());
		logger.info("sign: "+sign);
		return sign;
	}

	/**
	 * MD5
	 * @param origin
	 * @param charsetname
	 * @return
	 */
	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");

			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	/**
	 *
	 * @param b
	 * @return
	 */
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	/**
	 *
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 *
	 */
	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

}
