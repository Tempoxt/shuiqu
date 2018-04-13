package com.eviano2o.wxpay;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SSLSocketFactory extends org.apache.http.conn.ssl.SSLSocketFactory {
	
	static{
		sSLSocketFactory = new SSLSocketFactory(createSContext());
	}
	
	private static SSLSocketFactory sSLSocketFactory = null;

	private static SSLContext createSContext(){
		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContext.getInstance("SSL");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			sslcontext.init(null, null, null);
		} catch (KeyManagementException e) {
			e.printStackTrace();
			return null;
		}
		return sslcontext;
	}
	
	private SSLSocketFactory(SSLContext sslContext) {
		super(sslContext);
		this.setHostnameVerifier(ALLOW_ALL_HOSTNAME_VERIFIER);
	}
	
	public static SSLSocketFactory getInstance(){
		if(sSLSocketFactory != null){
			return sSLSocketFactory;
		}else{
			return sSLSocketFactory = new SSLSocketFactory(createSContext());
		}
	}
	
}