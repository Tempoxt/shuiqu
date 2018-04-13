package com.eviano2o.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class EvianHelpDESUtil {
	/**
     * Java与Ios DES加密解密密钥
     */
    static final String SIGNKEY = "Pwd8y43K";
    
    private static String evian_decrypt(String message, String key) {
        try {
            byte[] bytesrc = convertHexString(message);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] retByte = cipher.doFinal(bytesrc);
            return new String(retByte);
        } catch (Exception e) {
            //EvianLog.fatal(e.toString());
        }
        return null;
    }

    private static byte[] evian_encrypt(String message, String key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            return cipher.doFinal(message.getBytes("UTF-8"));
        } catch (Exception e) {
            //EvianLog.fatal(e.toString());
        }
        return null;
    }

    private static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
        return digest;
    }

    private static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString();
    }

    /**
     * 解密数据(不区分大小写)
     *
     * @param message 密文
     * @return 明文(失败返回:null)
     */
    public static String decrypt(String message) {
        try {
            String d = evian_decrypt(message, SIGNKEY);
            if (d != null)
                return java.net.URLDecoder.decode(d, "utf-8");
        } catch (Exception e) {
            //EvianLog.fatal(e.toString());
        }
        return null;
    }

    /**
     * 加密数据
     *
     * @param message 明文
     * @return 密文(失败返回:null)
     */
    public static String encrypt(String message) {
        try {
            byte[] e = evian_encrypt(
                    java.net.URLEncoder.encode(message, "utf-8"), SIGNKEY);
            if (e != null)
                return toHexString(e).toUpperCase();
        } catch (Exception e) {
            //EvianLog.fatal(e.toString());
        }
        return null;
    }
}
