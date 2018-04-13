package com.eviano2o.wxpay;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import static com.eviano2o.wxpay.HttpsClientUtil.getPostMethod;

public class SwapWechatPay {
	public static DefaultHttpClient httpclient;
	private static Logger logger = Logger.getLogger("payment");



	static
	{
		logger.info("微信支付prepayId构造方法");
		httpclient = new DefaultHttpClient();
		httpclient = (DefaultHttpClient) HttpsClientUtil.getSSLInstance(httpclient);
	}

	/**
	 * 微信支付交互
	 * @param url
	 * @param xmlParam
	 * @return
	 */
	public static Map swapWechat(String url,String xmlParam){
		logger.info("微信支付prepayId请求地址: "+url+", 请求数据: "+xmlParam);
		//System.out.println("微信支付请求地址: "+url+", 请求数据: "+xmlParam);
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		try {
			HttpPost httpost= getPostMethod(url);
			httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
			HttpResponse response = httpclient.execute(httpost);
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.info("微信支付prepayId请求返回结果: "+jsonStr);
			//System.out.println("微信支付请求返回结果: "+jsonStr);
			Map map = doXMLParse(jsonStr);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("微信支付prepayId请求返回结果: "+e);
			return null;
		}
	}

	/**
	 * 解析xml,返回 MAP。
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map doXMLParse(String strxml) throws Exception {
		if(null == strxml || "".equals(strxml)) {
			return null;
		}
		Map m = new HashMap();
		InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if(children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}
			m.put(k, v);
		}
		//关闭流
		in.close();
		return m;
	}

	/**
	 * 获取子结点的xml
	 * @param children
	 * @return String
	 */
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if(!children.isEmpty()) {
			Iterator it = children.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if(!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		return sb.toString();
	}

}