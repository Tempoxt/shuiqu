package com.eviano2o.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * Created by IntelliJ IDEA
 * PackName: com.evian.whp.core.util
 * User: pengan
 * Date: 2015/8/28
 * Time: 20:38
 * Remark:DES3加密解密工具类
 */
public class DES3_CBCUtil {
    private static final byte[] key = Base64.decode("a035eH83199L51DB8b6ceRe2A521e2Pf");
    private static final byte[] keyiv = {85, 12, 30, 87, 11, 6, 5, 8};
    private static final Log logger = LogFactory.getLog(DES3_CBCUtil.class.getName());

    /**
     * ECB加密,不要IV
     *
     * @param key  密钥
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    private static byte[] des3EncodeECB(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * ECB解密,不要IV
     *
     * @param key  密钥
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    private static byte[] ees3DecodeECB(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * CBC加密
     *
     * @param key   密钥
     * @param keyiv IV
     * @param data  明文
     * @return Base64编码的密文
     * @throws Exception
     */
    private static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * CBC解密
     *
     * @param key   密钥
     * @param keyiv IV
     * @param data  Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    private static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * CBC加密
     *
     * @param data
     * @return 密文
     */
    public static String des3EncodeCBC(String data) {
        try {
            byte[] dataByte = data.getBytes("UTF-8");
            byte encodeByte[] = des3EncodeCBC(key, keyiv, dataByte);
            return Base64.encode(encodeByte);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return null;
    }

    /**
     * CBC解密
     *
     * @param data
     * @return 明文
     */
    public static String des3DecodeCBC(String data) {
        try {
            byte decodeByte[] = des3DecodeCBC(key, keyiv, Base64.decode(data));
            return new String(decodeByte, "UTF-8");
        } catch (Exception e) {
            logger.fatal(e);
        }
        return null;
    }
}

