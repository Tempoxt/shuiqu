package com.eviano2o.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.eviano2o.bean.weixin.ClientModel;
import com.eviano2o.bean.weixin.WxCityInfoModel;
import com.eviano2o.service.BackstageApiService;
import com.eviano2o.service.WeiXinService;
import com.eviano2o.util.SessionConstantDefine;

/**
 *如果拦截器不起效，请检查项目XML中是否有相关配置 
 */
public class CheckCityFilter extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(CheckCityFilter.class);
	
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
			
			CheckCity checkCity = ((HandlerMethod) handler).getMethodAnnotation(CheckCity.class);
			if (checkCity == null || checkCity.validate() == false)
				return true;
			WxCityInfoModel attribute = (WxCityInfoModel) request.getSession().getAttribute(SessionConstantDefine.WX_CITY);
			logger.error("+++++++++++++++++++++++++++curCity = "+attribute);
			//logger.error("[{}],[s:{}],[v:{}]", new Object[] { "session:" + currentUrl(request), request.getSession().getAttribute(SessionConstantDefine.WX_CITY), request.getSession().getAttribute(SessionConstantDefine.WX_CITY) == null });
			if (attribute == null || attribute.getCityId()==null) {// 验证失败
				
				//从数据库查询是否有对应的sessionId信息
				/*String sessionId=request.getSession().getId();
				String getOpenIdBySessionIdJson = new BackstageApiService().GetOpenIdBySessionId(sessionId);
				if(!StringUtils.isEmpty(getOpenIdBySessionIdJson)){
					JSONObject json = JSONObject.fromObject(getOpenIdBySessionIdJson);
					if(json != null && json.has("code") && json.getString("code").equals("E00000")){
						request.getSession().setAttribute(SessionConstantDefine.WX_OPENID, json.getJSONObject("data").getString("openId"));
						
						WxCityInfoModel curCity = new WeiXinService().getWxUserCityInfo(json.getJSONObject("data").getString("openId"),(String)request.getSession().getAttribute("authorizer_appid"));
						if(curCity != null){
							request.getSession().setAttribute(SessionConstantDefine.WX_CITY, curCity);
							return true;
						}
					}
				}*/
				
				//logger.error("[城市session丢失:{}]", new Object[] { currentUrl(request) });
				response.sendRedirect("/weixin/city");
				// response.getWriter().write(JsonResponseHelper.getAPIErrorJsonModel().toString());
				response.getWriter().flush();
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
