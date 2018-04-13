package com.fun.inteceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WxOrAlipayConfiguration extends WebMvcConfigurerAdapter{

	private String InteceptorSwitch;
	
	@Autowired  
    private Environment env; 
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		InteceptorSwitch=env.getProperty("tiaozhuan.InteceptorSwitch");
		
		// 参数日志拦截器
		registry.addInterceptor(new LoggerParamsInteceptor()).addPathPatterns("/**");
		if("true".equals(InteceptorSwitch)){
			registry.addInterceptor(new BrowserInteceptor()).addPathPatterns("/funVm/*").excludePathPatterns("/funVm/browser");
			registry.addInterceptor(new AuthorizationInteceptor()).addPathPatterns("/funVm/*").excludePathPatterns("/funVm/browser","/funVm/authorize");
			registry.addInterceptor(new Browser2Inteceptor()).addPathPatterns("/funVm/browser");
		}
		
	}
}
