package com.fun.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
// 自己添加的，指定配置文件
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class RemoteInterfaceAddress {

	@Value("${xcx.GETSESSIONKEYOROPENID}")
	private String GETSESSIONKEYOROPENID;
	
	@Value("${xcx.AppId}")
	private String AppletAPPID;
	
	@Value("${xcx.AppSecret}")
	private String AppletAppSecret;
	
	/*private String GETSESSIONKEYOROPENID = "https://api.weixin.qq.com/sns/jscode2session";
	
	private String AppletAPPID = "wx2e6c5ab609666413";
	
	private String AppletAppSecret = "0bd9f48f72f1e67d588b667db28ead09";*/
	
	public String getAppletAPPID() {
		return AppletAPPID;
	}
	public void setAppletAPPID(String appletAPPID) {
		AppletAPPID = appletAPPID;
	}
	public String getAppletAppSecret() {
		return AppletAppSecret;
	}
	public void setAppletAppSecret(String appletAppSecret) {
		AppletAppSecret = appletAppSecret;
	}
	public String getGETSESSIONKEYOROPENID() {
		return GETSESSIONKEYOROPENID;
	}
	public void setGETSESSIONKEYOROPENID(String gETSESSIONKEYOROPENID) {
		GETSESSIONKEYOROPENID = gETSESSIONKEYOROPENID;
	}
	
}
