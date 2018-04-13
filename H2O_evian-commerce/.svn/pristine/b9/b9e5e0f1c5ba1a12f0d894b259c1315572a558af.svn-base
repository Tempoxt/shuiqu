package com.eviano2o.servlet;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eviano2o.bean.weixin.QiNiuConfigModel;
import com.eviano2o.util.WebStyle;

public class FristServlet implements Servlet {
	private static final Logger logger = LoggerFactory.getLogger(FristServlet.class);
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(ServletConfig arg0) throws ServletException {
		// 读取七牛配置
		Properties ppt = new Properties();
		try {
			ppt.load(this.getClass().getResourceAsStream("/qiniu.properties"));
			QiNiuConfigModel.setAccessKey(ppt.getProperty("qiniu.accessKey"));
			QiNiuConfigModel.setSecretKey(ppt.getProperty("qiniu.secretKey"));
			QiNiuConfigModel.setBucket(ppt.getProperty("qiniu.bucket"));
			QiNiuConfigModel.setNamespace(ppt.getProperty("qiniu.namespace"));
		} catch (Exception e) {
		}
		
		// 网站模板
		Properties pStyle = new Properties();
		try {
			pStyle.load(this.getClass().getResourceAsStream("/style.properties"));
			WebStyle.setStyle(pStyle.getProperty("style"));
			WebStyle.setApiUrl(pStyle.getProperty("apiUrl"));
		} catch (Exception e) {
			logger.info(e.toString());
		}
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		

	}

}
