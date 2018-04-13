package com.eviano2o.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eviano2o.util.HttpClientUtilOkHttp;


@Controller
@RequestMapping("/test")
public class WeiXinControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(WeiXinControllerTest.class);
	
	@RequestMapping(value = "verifyTicket", produces = "text/plain; charset=utf-8")
	public @ResponseBody String verifyTicket(Model model, HttpServletRequest request,HttpServletResponse response) {
		String nonce = request.getParameter("nonce");  
        String timestamp = request.getParameter("timestamp");  
        String signature = request.getParameter("signature");  
        String msgSignature = request.getParameter("msg_signature");
        logger.info("component_verify_ticket:————>nonce="+nonce+" timestamp="+timestamp+" signature="+signature+" msgSignature="+msgSignature);
        
        /*Map map=request.getParameterMap();  
        Set keSet=map.entrySet();  
        StringBuffer sb0 = new StringBuffer();
        for(Iterator itr=keSet.iterator();itr.hasNext();){  
            Map.Entry me=(Map.Entry)itr.next();  
            Object ok=me.getKey();  
            Object ov=me.getValue();  
            String[] value=new String[1];  
            if(ov instanceof String[]){  
                value=(String[])ov;  
            }else{  
                value[0]=ov.toString();  
            }  
            
            for(int k=0;k<value.length;k++){  
                sb0.append(ok+"="+value[k]); 
                sb0.append(" ");
            }  
            
          }  
        logger.info("所有request请求————>"+sb0.toString());*/
        
        
        try {
			logger.info("所有getInputStream请求————>"+request.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			logger.info("所有getInputStream请求————>"+"null");
		}
        try {
			StringBuffer sb = new StringBuffer();
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			int i = 0;
			while ((s = br.readLine()) != null) {
				sb.append(s);
				i++;
			}
			String strXml = sb.toString();

			
			logger.info("微信发送事件      "+strXml);
			logger.info("微信发送事件--i————>"+i);
			isr.close();
			is.close();
			br.close();
		} catch (Exception e) {
		}
        
        
        
		try {
			StringBuffer sb = new StringBuffer();
			BufferedReader in = request.getReader();
			String s = "";
			int i = 0;
			while ((s = in.readLine()) != null) {
				
				sb.append(s);
				i++;
			}
			String strXml = sb.toString();
			logger.info("微信推送————>"+strXml);
			logger.info("length————>"+i);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	@RequestMapping("showShouquan")
	public void showShouquan(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException{
		request.getRequestDispatcher("/demo.vm").forward(request, response);
	}
	
	@RequestMapping(value = "setAuthorizerAppid", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject setAuthorizerAppid(HttpServletRequest request,String authorizer_appid){
		String re = "";
		if(StringUtils.isEmpty(authorizer_appid)){
			re = "{\"code\":\"E10000\",\"message\":\"请从公众号内访问\",\"data\":{}}";
		}else{
			request.getSession().setAttribute("authorizer_appid", authorizer_appid);
			re = "{\"code\":\"E00000\",\"message\":\"成功\",\"data\":{\"authorizer_appid\":\""+authorizer_appid+"\"}}";
		}
		
		return JSONObject.fromObject(re);
	}
	
	@RequestMapping("showCode")
	public void showCode(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException{
//		request.getRequestDispatcher("/demo.vm").forward(request, response);
		/*String component_appid = "wx98d2ea7671c59607";
		String component_appsecret = "ab48726a19c862d2b01917225d860abd";
		String component_verify_ticket = "ticket@@@JfvROsNWkVIGDlBPL1TM_9u8NmRs-9EVQHpkVr80-pacEAinYcU2snV0IJMa8CI0NRUJpxFrkqoxPdPjlrVhrA";
		String code = weiXinService.getCode(component_appid, component_appsecret, component_verify_ticket);
		System.out.println(code);*/
		// 获取token
		/*HttpClientUtilOkHttp c  = new HttpClientUtilOkHttp();
		
		String param ="{\"component_appid\":\"wx9c8cd3284651135f\",\"component_appsecret\":\"e7d4c3518b4bbffa7467ed90e2c15512\",\"component_verify_ticket\":\"ticket@@@LloOhHiSa0R6UQnzu0nZCD59if7EnA1jEcm_IAdZTlAPwKAKBGKjdMMUBbQ03QZP1WQmsQXuzN3g-XmG9F_Fcw\"}";
		String sendPost = c.sendPost("https://api.weixin.qq.com/cgi-bin/component/api_component_token", param);
		System.out.println(sendPost);*/
		// 获取code
		/*HttpClientUtilOkHttp c  = new HttpClientUtilOkHttp();
		String component_access_token="5_IpMHLyxOALd00vouCDSpHngFagp1K6wG8Ft5mwBYqZkdfMsB_RhkQY2ea1wMczptR2aMvhuUuZ6KLW-G7Ow8Qw13T5-yGwoSw56UGfAoKnvQri5ZWKQXcbTlBbZV6IHnnZLnPxiYbx_7hgcDGOSdAGAXFJ";
		String param ="{\"component_appid\":\"wx9c8cd3284651135f\"}";
		String sendPost = c.sendPost("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="+component_access_token, param);
		System.out.println(sendPost);*/
		// 获取授权信息
		// https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=xxxx
		HttpClientUtilOkHttp c  = new HttpClientUtilOkHttp();
		String component_access_token="5_IpMHLyxOALd00vouCDSpHngFagp1K6wG8Ft5mwBYqZkdfMsB_RhkQY2ea1wMczptR2aMvhuUuZ6KLW-G7Ow8Qw13T5-yGwoSw56UGfAoKnvQri5ZWKQXcbTlBbZV6IHnnZLnPxiYbx_7hgcDGOSdAGAXFJ";
		//String param ="{\"component_appid\":\"wx9c8cd3284651135f\",\"authorization_code\":\"queryauthcode@@@M9s1XVharrVVZqnZFpuIMqKZfvQKY9qjqTbQHPlTn8iCsQrJ2mi0J30ZFLpMjIre2C3_CldEjb5LyZzQfOeEVw\"}"; // 妙趣风声
		String param ="{\"component_appid\":\"wx9c8cd3284651135f\",\"authorization_code\":\"queryauthcode@@@1R715663Ml8DdjNu27-SBgac2PDurfPC06XUsJJ2zofbRK7p_BmNw5GPhU9MJ6T19Gz-Q0pr8gH8JZUhYDGn8w\"}";  //好水生活
		String sendPost = c.sendPost("https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="+component_access_token, param);
		System.out.println(sendPost);
		/*Map map=request.getParameterMap();  
        Set keSet=map.entrySet();  
        StringBuffer sb0 = new StringBuffer();
        for(Iterator itr=keSet.iterator();itr.hasNext();){  
            Map.Entry me=(Map.Entry)itr.next();  
            Object ok=me.getKey();  
            Object ov=me.getValue();  
            String[] value=new String[1];  
            if(ov instanceof String[]){  
                value=(String[])ov;  
            }else{  
                value[0]=ov.toString();  
            }  
            
            for(int k=0;k<value.length;k++){  
                sb0.append(ok+"="+value[k]); 
                sb0.append(" ");
            }  
            
          }  
        logger.info("showCode用户所有信息————>"+sb0.toString());
        return "screen/weixin/home";*/
	}
	
	
	
	@RequestMapping("showLogs")
	public void showLogs(Model model, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String day=df.format(new Date());// new Date()为获取当前系统时间
		File f = new File("d:"+File.separator+"data"+File.separator+"logs"+File.separator+"evian-commerce"+File.separator+"info."+day+".log");   
		BufferedReader reader = null;
		StringBuffer sb=new StringBuffer();
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(f));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString+"<br/>");
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        String b = sb.toString();
        request.setAttribute("dayLogs", b);
		request.getRequestDispatcher("/Log.vm").forward(request, response);
	}
}
