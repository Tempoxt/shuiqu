package com.eviano2o.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eviano2o.util.HttpClientUtil;
import com.eviano2o.util.SysParamNames;

@Controller
@RequestMapping("/wechatliteapp")
public class WechatLiteappController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(WechatLiteappController.class);
	
	private void postContent(HttpServletRequest request){
		try {
			StringBuffer sb = new StringBuffer();
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			String strXml = sb.toString();

			logger.info("小程序发送事件111:      "+strXml);
		} catch (Exception e) {
			logger.info("小程序发送事件 错误111：    "+e.toString());
		}
		
		String requestString ="";
		Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();  
			requestString += (paraName+": "+request.getParameter(paraName));  
		} 
		logger.info("小程序发送事件2222:      "+requestString);
	}
	
	

	/** 11.获取水店产品类别、产品信息 ajax(json)*/
	@RequestMapping(value = "getShopProductJson", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getShopProductJson(HttpServletRequest request, String shopId) {
		postContent(request);
		Integer curShopId = Integer.valueOf(shopId.replace("SQ", "").replace("MD", ""));
		return JSONObject.fromObject(weiXinService.getShopProductJson(curShopId,getSessionAuthorizerAppid(request)));
	}
	
	
	@RequestMapping(value = "cities", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject cities(HttpServletRequest request) {
		String result = weiXinService.getAllCityInfoJson(getSessionAuthorizerAppid(request));
		return JSONObject.fromObject(result);
	}
}
