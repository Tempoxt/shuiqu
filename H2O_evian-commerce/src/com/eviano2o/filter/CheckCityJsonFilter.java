package com.eviano2o.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.eviano2o.util.SessionConstantDefine;
import com.eviano2o.util.response.JsonResponseHelper;

/**
 *如果拦截器不起效，请检查项目XML中是否有相关配置 
 */
public class CheckCityJsonFilter extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(CheckCityJsonFilter.class);
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// request.getSession().setAttribute("username", "upxiaofeng");
		// request.getSession().setAttribute("password", "upxiaofeng");
		// request.getSession().setAttribute("userid", "10086");

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		// super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("gbk");
		response.setHeader("contentType", "application/json;charset=gbk");
		
		// TODO Auto-generated method stub
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			
			CheckCityJson checkCity = ((HandlerMethod) handler).getMethodAnnotation(CheckCityJson.class);
			if (checkCity == null || checkCity.validate() == false)
				return true;

			if (request.getSession().getAttribute(SessionConstantDefine.WX_CITY) == null) {// 验证失败
				logger.error("[url:{}]", new Object[] { currentUrl(request) });
				//这句话的意思，是让浏览器用utf8来解析返回的数据  
				response.setHeader("Content-type", "text/html;charset=UTF-8");  
				//这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859  
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(JsonResponseHelper.getWeiXinNoCityJsonModel().toString());
				return false;
			} else {
				// 返回到登录界面
				return true;
			}

		} else
			return true;
	}

	
	/** 当前网址 */
	private String currentUrl(HttpServletRequest request){
		String url = request.getRequestURL().toString();
		if(request.getQueryString()!=null) //判断请求参数是否为空  
			url+="?"+request.getQueryString();   // 参数
		return url;
	}
}
