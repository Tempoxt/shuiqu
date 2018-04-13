package com.eviano2o.util;

import java.security.MessageDigest;

public class MD5Util {
	/**
	 * Md5加密32位
	 * 
	 * @param plainText
	 * @return
	 */
	public static String md5(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("UTF-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			re_md5 = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!re_md5.equals("") && re_md5 != null) {
			re_md5 = re_md5.toUpperCase();
			//System.out.print(re_md5);
			return re_md5;
		}
		return null;
	}
	
	public static String md516(String encryptStr) {  
        return md5(encryptStr).substring(8, 24);  
    } 

	/**
	 * 加密解密算法 执行一次加密，两次加密 转字符
	 */
	public static String convertMD5(String inStr) {
		String s = md5(md5(inStr));
		return s;
	}

	/**
	 * 加密解密算法 执行一次加密，两次解密 用于网路传输 转字符
	 */
	public static String convertMD52(String inStr) {

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 'y');
		}
		String s = new String(a);
		return s;

	}
}
