package com.fun.util;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.fun.bean.WxConfig;

/** js api 所需要的参数 */
public class WxJsTicketUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(WxJsTicketUtil.class);
	
	public static void sign(Model model, String url, String appId) {
		//因为测试服务器用了nginx代理，所以url参数里面的域名要替换成微信可访问的域名
		if(WxConfig.Gzh_Jsapi_Ticket==null){
			WxConfig wx = new WxConfig();
			wx.setJsapiTicket();
		}
		String nonceStr = create_nonce_str();
		String timeStamp = create_timestamp();
		String string1;
		String signature = "";
		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + WxConfig.Gzh_Jsapi_Ticket + "&noncestr=" + nonceStr + "&timestamp=" + timeStamp + "&url=" + url;
		//logger.info("-----------------------------------------signatureStr11111: " + string1);
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//logger.info("-----------------------------------------signatureStr: " + string1 + "            signature:" + signature);
		model.addAttribute("url", url);
		model.addAttribute("appId", appId);
		model.addAttribute("jsapi_ticket", WxConfig.Gzh_Jsapi_Ticket);
		model.addAttribute("nonceStr", nonceStr);
		model.addAttribute("timestamp", timeStamp);
		model.addAttribute("signature", signature);
		
		logger.info("[url:{}] [appId:{}] [jsapi_ticket:{}] [nonceStr:{}] [timestamp:{}] [signature:{}]",
//				new Object[] { url,"wx5288d5e0e47b4f76",WxConfig.Gzh_Jsapi_Ticket,nonceStr,timeStamp,signature });
		new Object[] { url,"wx57c89676f397cfd1",WxConfig.Gzh_Jsapi_Ticket,nonceStr,timeStamp,signature });
		
		
		
		//定位所需要的sign
        String addressSign = "accesstoken="+WxConfig.Gzh_Jsapi_Ticket+"&appid="+appId+"&noncestr="+nonceStr+"&timestamp="+timeStamp+"&url="+url;
        addressSign = Sha1Util.getSha1(addressSign);
        model.addAttribute("addressSign", addressSign);
	}

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    
    public static String create_nonce_str() {
        String guid = UUID.randomUUID().toString();
        return MD5Util.md5(guid).toUpperCase();
    }
    
    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    
}
