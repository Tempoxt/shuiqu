package com.eviano2o.util;

import java.util.Arrays;

public class WXCheckUtil {
	private static final String token = "token";
    public static boolean checkSignature(String signature,String timestamp,String nonce){
         
        String[] arr = new String[] { token, timestamp, nonce };
         
        // 排序
        Arrays.sort(arr);
        // 生成字符串
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
         
        // sha1加密
        String temp = Sha1Util.getSha1(content.toString());
         
        return temp.equals(signature); // 与微信传递过来的签名进行比较
    }
     
}
