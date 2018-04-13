package com.eviano2o.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.eviano2o.bean.weixin.ClientModel;
import com.eviano2o.util.SessionConstantDefine;
import com.eviano2o.util.response.JsonResponseHelper;

/**
 *如果拦截器不起效，请检查项目XML中是否有相关配置 
 */
public class CheckAppLoginFilter extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(CheckAppLoginFilter.class);
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

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
			
			CheckAppLogin authPassport = ((HandlerMethod) handler).getMethodAnnotation(CheckAppLogin.class);
			if (authPassport == null || authPassport.validate() == false)
				return true;

			ClientModel clientUser = (ClientModel) request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO);
			// 在这里实现自己的权限验证逻辑
			logger.info("111111111111111111111111111111111111111111111");
			if (clientUser == null) {// 如果验证成功返回true（这里直接写false来模拟验证失败的处理）
				logger.info("22222222222222222222222222222222222222222222");
				// response.sendRedirect("account/login");
				//这句话的意思，是让浏览器用utf8来解析返回的数据  
				response.setHeader("Content-type", "text/html;charset=UTF-8");  
				//这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859  
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(JsonResponseHelper.getAppNoLoginJsonModel().toString());
				response.getWriter().flush();
				return false;
			} else// 如果验证失败
			{
				logger.info("333333333333333333333333333333333333333333333");
				// 返回到登录界面
				return true;
			}

		} else
			return true;
	}

}
