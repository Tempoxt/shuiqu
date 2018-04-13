package com.fun.inteceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fun.bean.VendingMachineConfig;
import com.fun.util.StrUtils;

public class AuthorizationInteceptor implements HandlerInterceptor{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * controller 执行之前调用
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Boolean attribute = (Boolean) session.getAttribute("authorization");
		logger.info("[attribute:{}]",new Object[]{attribute});
			String header = request.getHeader("user-agent");
			if(header.contains("AlipayClient")){
				String userid = (String) session.getAttribute("userid");
				if(!StrUtils.isEmOrUn(userid)){
					return true;
				}else{
					response.sendRedirect("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id="+VendingMachineConfig.getAilPayAppId()+"&scope=auth_userinfo&redirect_uri=https%3a%2f%2fsvm.shuiqoo.cn/funVm/authorize");
				}
			}else if(header.contains("MicroMessenger")){
				String openid = (String) session.getAttribute("openid");
				if(!StrUtils.isEmOrUn(openid)){
					return true;
				}else{
					response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+VendingMachineConfig.getWxPayAppId()+"&redirect_uri=https%3a%2f%2fsvm.shuiqoo.cn%2ffunVm%2fauthorize&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect");
				}
//				response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5288d5e0e47b4f76&redirect_uri=https%3a%2f%2fx9.shuiqoo.cn%2ffunVm%2fauthorize&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect");
			}
			return false;
	}

	/**
     * 页面渲染之后调用，一般用于资源清理操作
     */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
     * controller 执行之后，且页面渲染之前调用
     */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
