package com.fun.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlStringUtil {
	private static final Logger logger = LoggerFactory.getLogger(XmlStringUtil.class);

	/**
	 * 解析xml,返回 MAP。
	 * 
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static Map stringToXMLParse(String strxml) throws Exception {
		try {
			if (null == strxml || "".equals(strxml)) {
				return null;
			}
			Map m = new HashMap();
			InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(in);
			Element root = doc.getRootElement();
			List list = root.getChildren();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String k = e.getName();
				String v = "";
				List children = e.getChildren();
				if (children.isEmpty()) {
					v = e.getTextNormalize();
				} else {
					v = getChildrenText(children);
				}
				m.put(k, v);
			}
			// 关闭流
			in.close();
			return m;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("解析prepayId请求返回XML: " + e);
			return null;
		}
	}

	/**
	 * 获取子结点的xml
	 * 
	 * @param children
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		return sb.toString();
	}
}
