package com.fun.inteceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class Browser2Inteceptor implements HandlerInterceptor{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * controller 执行之前调用
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String header = request.getHeader("user-agent");
		logger.info("[user-agent:{}]",new Object[]{header});
		
		if(header.contains("AlipayClient")||header.contains("MicroMessenger")){
			response.sendRedirect("/funVm/index?stamptime="+System.currentTimeMillis());
			return false;
		}else{
			return true;
		}
		
		
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
