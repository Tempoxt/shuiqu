package com.fun.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.fun.bean.VendingMachineConfig;
import com.fun.bean.vendor.VendorPlatformAuthorization;
import com.fun.bean.vendor.VendorTheme;
import com.fun.service.IVendorPlatformAuthorizationService;
import com.fun.service.IVendorThemeService;
import com.fun.util.HttpClientUtil;
import com.fun.util.UserInfoUtil;
import com.fun.util.WxJsTicketUtil;

@Controller
@RequestMapping("/funVm")
public class VmController extends BaseController{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IVendorThemeService ThemeService; 
	
	@Autowired
	IVendorPlatformAuthorizationService vendorPlatformAuthorizationService;
	
	@RequestMapping("/testVm")
	public String testVm(Map<String, Object> model,String auth_code,String app_id,String source,String userOutputs,String scope,String alipay_token){
		model.put("__projects","页面跳转成功！");  
		model.put("auth_code",auth_code);
		model.put("app_id",app_id);
		model.put("source",source);
		model.put("userOutputs",userOutputs);
		model.put("scope",scope);
		model.put("alipay_token",alipay_token);
		return "testVm";
	}
	// https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5288d5e0e47b4f76&redirect_uri=https://x9.shuiqoo.cn/funVm/authorize&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect
	@RequestMapping("/testVm2")
	public String testVm2(){
		
		return "testVm2";
	}
	
	@RequestMapping("/notifyUrl")
	public String notifyUrl(HttpServletRequest request, Model model){
		HttpSession session = request.getSession();
		session.setAttribute("authorization", true);
		Map<Object, Object> authorizeMap = new HashMap<Object, Object>();
		Map map=request.getParameterMap();  
	    Set keSet=map.entrySet();  
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
//	            System.out.println(ok+"="+value[k]);
	            authorizeMap.put(ok, value[k]);
	        }  
	      }  
	    logger.info("[支付回调:{}] [notifyMap:{}]",new Object[]{"支付宝",authorizeMap});
	    return "success";
	}
	
	/**
	 * 登录回调
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/authorize")
	public String authorize(HttpServletRequest request, Model model){
		HttpSession session = request.getSession();
		session.setAttribute("authorization", true);
		Map<Object, Object> authorizeMap = new HashMap<Object, Object>();
		Map map=request.getParameterMap();  
	    Set keSet=map.entrySet();  
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
//	            System.out.println(ok+"="+value[k]);
	            authorizeMap.put(ok, value[k]);
	        }  
	      }  
	    
	    String header = request.getHeader("user-agent");
	    request.getSession().setAttribute("cabinetNum", 1);
		if(header.contains("AlipayClient")){
			logger.info("[登录方式:{}] [notifyMap:{}]",new Object[]{"支付宝",authorizeMap});
			String alipay_auth_code = (String) authorizeMap.get("auth_code");
			AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",VendingMachineConfig.getAilPayAppId(),VendingMachineConfig.getAilPrivateKey() ,"json","utf-8",VendingMachineConfig.getAilPublicKey() ,"RSA2");
			AlipaySystemOauthTokenRequest requestLogin1 = new AlipaySystemOauthTokenRequest();
			requestLogin1.setCode(alipay_auth_code);
			requestLogin1.setGrantType("authorization_code");
			try {
				AlipaySystemOauthTokenResponse responseToken = alipayClient.execute(requestLogin1);
				String userId=null;
				String access_token=null;
				logger.info("responseToken : "+responseToken.getBody());
				if(responseToken.isSuccess()){
				    
				    access_token=responseToken.getAccessToken();
				    userId=responseToken.getAlipayUserId();
				    
				    if(userId!=null){
				    	JSONObject gainUserInfo = UserInfoUtil.gainUserInfo(access_token, userId,1);
				    	VendorPlatformAuthorization va = vendorPlatformAuthorizationService.findAuthorizationByOpenId(userId);
						if(va==null){
							vendorPlatformAuthorizationService.addAuthorization(new VendorPlatformAuthorization(userId,gainUserInfo.getString("nick_name"),gainUserInfo.getString("avatar"),1));
						}else{
							vendorPlatformAuthorizationService.upAuthorization(new VendorPlatformAuthorization(userId,gainUserInfo.getString("nick_name"),gainUserInfo.getString("avatar"),1));
						}
				    	
				    }
				    request.getSession().setAttribute("alipay_access_token", access_token);
					request.getSession().setAttribute("userid", userId);
				    
				} else {
					if("40002".equals(responseToken.getCode())){
						logger.info("code已经被使用过，跳过刷新session");
						return "vendor/index";
					}
					logger.error("没有获取到支付宝userId : " +responseToken.getSubMsg());
				    return null;
				}
			} catch (AlipayApiException e) {
				// TODO Auto-generated catch block
				logger.error("AlipayApiException : "+e.getErrMsg());
				return null;
			}
			
		}else if(header.contains("MicroMessenger")){
			logger.info("[登录方式:{}] [notifyMap:{}]",new Object[]{"微信",authorizeMap});
			
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
			Map<String, String> param = new HashMap<String, String>();
//	    	param.put("appid", "wx5288d5e0e47b4f76");
//	    	param.put("secret", "f2601ba78aca4888877b81f07540b892");
			param.put("appid", VendingMachineConfig.getWxPayAppId());
			param.put("secret", VendingMachineConfig.getWxPaySecret());
			param.put("code", request.getParameter("code"));
			param.put("grant_type", "authorization_code");
			String access_tokenJson = HttpClientUtil.doGet(url, param);
			logger.info("微信 access_tokenJson : "+access_tokenJson);
			JSONObject parseObject = JSON.parseObject(access_tokenJson);
			if(parseObject.getInteger("errcode")!=null&&parseObject.getInteger("errcode")==40163){
				logger.info("code已经被使用过，跳过刷新session");
			}else{
				String access_token = parseObject.getString("access_token");
				String openid = parseObject.getString("openid");
				if(openid!=null){
					JSONObject gainUserInfo = UserInfoUtil.gainUserInfo(access_token, openid, 2);
					VendorPlatformAuthorization va = vendorPlatformAuthorizationService.findAuthorizationByOpenId(openid);
					if(va==null){
						vendorPlatformAuthorizationService.addAuthorization(new VendorPlatformAuthorization(openid,gainUserInfo.getString("nickname"),gainUserInfo.getString("headimgurl"),2));
					}else{
						vendorPlatformAuthorizationService.upAuthorization(new VendorPlatformAuthorization(openid,gainUserInfo.getString("nickname"),gainUserInfo.getString("headimgurl"),2));
					}
				}
				request.getSession().setAttribute("access_token", access_token);
				request.getSession().setAttribute("openid", openid);
			}
			
			// 往model里传输js-api的参数
			WxJsTicketUtil.sign(model, currentUrl(request), VendingMachineConfig.getWxPayAppId());
	    
		}
		return "vendor/index";
	}
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request, Model model){
		/*ModelAndView ma = new ModelAndView();
		String themeColor =null;
		if(getEid(request)!=null){
			VendorTheme vendorTheme = ThemeService.findThemeByEid(getEid(request));
			if(vendorTheme!=null){
				themeColor =vendorTheme.getStyleCode();
				request.getSession().setAttribute("themeColor", themeColor);
			}
		}
		ma.addObject("themeColor", getTheme(request));
		ma.setViewName("vendor/index");*/
		// 往model里传输js-api的参数
		WxJsTicketUtil.sign(model, currentUrl(request), VendingMachineConfig.getWxPayAppId());
		return "vendor/index";
	}
	
	@RequestMapping("/home")
	public ModelAndView home(HttpServletRequest request){
		ModelAndView ma = new ModelAndView();
		ma.addObject("themeColor", getTheme(request));
		ma.setViewName("vendor/home");
		return ma;
	}
	
	@RequestMapping("/space")
	public ModelAndView space(HttpServletRequest request){
		ModelAndView ma = new ModelAndView();
		ma.addObject("themeColor", getTheme(request));
		ma.setViewName("vendor/space");
		return ma;
	}
	
	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request){
		ModelAndView ma = new ModelAndView();
		ma.addObject("themeColor", getTheme(request));
		ma.setViewName("vendor/main");
		return ma;
	}
	
	@RequestMapping("/my")
	public ModelAndView my(HttpServletRequest request){
		ModelAndView ma = new ModelAndView();
		ma.addObject("themeColor", getTheme(request));
		ma.setViewName("vendor/my");
		return ma;
	}
	
	@RequestMapping("/myCoupon")
	public ModelAndView myCoupon(HttpServletRequest request){
		ModelAndView ma = new ModelAndView();
		ma.addObject("themeColor", getTheme(request));
		ma.setViewName("vendor/myCoupon");
		return ma;
	}
	
	@RequestMapping("/order")
	public ModelAndView order(HttpServletRequest request){
		ModelAndView ma = new ModelAndView();
		ma.addObject("themeColor", getTheme(request));
		ma.setViewName("vendor/order");
		return ma;
	}
	
	@RequestMapping("/orderExplain")
	public ModelAndView orderExplain(HttpServletRequest request){
		ModelAndView ma = new ModelAndView();
		ma.addObject("themeColor", getTheme(request));
		ma.setViewName("vendor/orderExplain");
		return ma;
	}
	
	@RequestMapping("/paySuccess")
	public ModelAndView paySuccess(Model model,HttpServletRequest request ){
//		model.addAttribute("cabinetNum", request.getSession().getAttribute("cabinetNum"));
		ModelAndView ma = new ModelAndView();
		ma.addObject("themeColor", getTheme(request));
		ma.addObject("cabinetNum", request.getSession().getAttribute("cabinetNum"));
		ma.setViewName("vendor/paySuccess");
		return ma;
	}
	
	@RequestMapping("/browser")
	public ModelAndView browser(HttpServletRequest request){
		ModelAndView ma = new ModelAndView();
		ma.addObject("themeColor", getTheme(request));
		ma.setViewName("vendor/browser");
		return ma;
	}
	
	protected String currentUrl(HttpServletRequest request){
		String url = request.getRequestURL().toString();
		if(request.getQueryString()!=null) //判断请求参数是否为空  
			url+="?"+request.getQueryString();   // 参数
		return url;
	}
}
