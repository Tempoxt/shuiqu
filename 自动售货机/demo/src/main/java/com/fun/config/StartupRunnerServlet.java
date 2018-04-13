package com.fun.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fun.bean.PayConfig;
import com.fun.bean.QiNiuConfigModel;
import com.fun.bean.VendingMachineConfig;
/**
 * 加载七牛配置    SpringBoot初始化加载
 * @author XHX
 *
 */
@Component
public class StartupRunnerServlet implements CommandLineRunner {

	@Autowired  
    private Environment env;
	
	@Override
	public void run(String... args) throws Exception {
		// 七牛配置初始化
		QiNiuConfigModel.setAccessKey(env.getProperty("qiniu.accessKey"));
		QiNiuConfigModel.setBucket(env.getProperty("qiniu.bucket"));
		QiNiuConfigModel.setNamespace(env.getProperty("qiniu.namespace"));
		QiNiuConfigModel.setSecretKey(env.getProperty("qiniu.secretKey"));
		
		// 支付配置初始化
		PayConfig.setWeixin_appid(env.getProperty("weixin.gzh.AppId"));
		PayConfig.setWeixin_mchId(env.getProperty("weixin.gzh.MchId"));
		PayConfig.setWeixin_appKey(env.getProperty("weixin.gzh.AppKey"));
		PayConfig.setWeixin_certificatePath(env.getProperty("weixin.gzh.certificatePath"));
		PayConfig.setXcx_appid(env.getProperty("xcx.AppId"));
		PayConfig.setXcx_mchId(env.getProperty("xcx.MchId"));
		PayConfig.setXcx_appKey(env.getProperty("xcx.AppKey"));
		PayConfig.setXcx_domainName(env.getProperty("xcx.DomainName"));
		
		// 售货机配置初始化
		VendingMachineConfig.setWxPayAppId(env.getProperty("wx.gzhPay.AppId"));
		VendingMachineConfig.setWxPaySecret(env.getProperty("wx.gzhPay.Secret"));
		VendingMachineConfig.setWxPayMchId(env.getProperty("wx.gzhPay.MchId"));
		VendingMachineConfig.setWxPayAppKey(env.getProperty("wx.gzhPay.AppKey"));
		VendingMachineConfig.setAilPrivateKey(env.getProperty("alipay.private.key"));
		VendingMachineConfig.setAilPublicKey(env.getProperty("alipay.public.key"));
		VendingMachineConfig.setAilPayAppId(env.getProperty("alipay.h5.appId"));
	}

}
