package cn.test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.json.XML;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fun.util.HttpClientUtil;

public class Test {

	public static void main(String[] args) throws DocumentException {
		/*// {"access_token":"7_MInCc1EhMRs7pPtXbP7-KLt_9TBX0cjf3YP8tY1-ldObmVS678JHEnLcraE3MzLXjREQKbrGfiP5RrBKNPKpygEKWzuH4bNAqOq-YJPL42QAgs2pUIih_rdSPMS6DPTtb6walglJtAC5GGcESUZiAFAMOT","expires_in":7200}
		//String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx5288d5e0e47b4f76&secret=f2601ba78aca4888877b81f07540b892";
		String token = "7_MInCc1EhMRs7pPtXbP7-KLt_9TBX0cjf3YP8tY1-ldObmVS678JHEnLcraE3MzLXjREQKbrGfiP5RrBKNPKpygEKWzuH4bNAqOq-YJPL42QAgs2pUIih_rdSPMS6DPTtb6walglJtAC5GGcESUZiAFAMOT";
		String caidan = "{\"button\":[{\"type\":\"click\",\"name\":\"今日歌曲\",\"key\":\"V1001_TODAY_MUSIC\"}]}";
		String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token="+token;
//		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+token;
		Map<String, String> param = new HashMap<String, String>();
		
		param.put("button", "{\"button\":[{\"type\":\"click\",\"name\":\"今日歌曲\",\"key\":\"V1001_TODAY_MUSIC\"}]}");
				String doGet = HttpClientUtil.doGet(url);
		
//		String doGet = HttpClientUtil.doPostJson(url, caidan);
		
		System.out.println(doGet);*/
		/*Long valueOf = Long.valueOf("18030716420590711863");
		System.out.println(valueOf);*/
		/*boolean integer = isInteger("18030阿萨德716420590711863");
		System.out.println(integer);*/
		/*Object a = null;
		String b =(String)a;
		System.out.println(b);*/
/*		long currentTimeMillis = System.currentTimeMillis();
		System.out.println(currentTimeMillis);*/
		
		String aaaxml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wx57c89676f397cfd1]]></appid><mch_id><![CDATA[1316186301]]></mch_id><nonce_str><![CDATA[ZSkTsgUMiRvwtpkG]]></nonce_str><sign><![CDATA[F435D6977D754293A9F47A2C67829560]]></sign><result_code><![CDATA[FAIL]]></result_code><err_code><![CDATA[ERROR]]></err_code><err_code_des><![CDATA[订单已全额退款]]></err_code_des></xml>";
		Document parseText = DocumentHelper.parseText(aaaxml);
		JSONObject json = new JSONObject();
		org.json.JSONObject jsonObject = XML.toJSONObject(aaaxml);
//		Map<String, Object> a =(Map)jsonObject.get("xml");
		Map<String, Object> a =(Map)JSON.parse(jsonObject.get("xml").toString());
		System.out.println(a);
//		System.out.println(jsonObject.get("xml"));
	}
	
	public static boolean isInteger(String str) {  
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
        return pattern.matcher(str).matches();  
  }
}
