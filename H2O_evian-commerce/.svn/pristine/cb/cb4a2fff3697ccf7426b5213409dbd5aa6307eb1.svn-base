package com.eviano2o.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eviano2o.util.HttpClientUtil;
import com.eviano2o.util.SysParamCache;
import com.eviano2o.util.SysParamNames;

/** 访问人人开店文件管理项目接口 */
public class FileManageSpiService {
	private static final Logger logger = LoggerFactory.getLogger(FileManageSpiService.class);
	String apiPath = "http://" + SysParamCache.getCache(SysParamNames.ParamFileManageDomain) + "/";

	/** 人人分享生成模板第一步，返回预览图片相对路径 */
	public String ShearTemplateOne(Integer shareId, String templateBgUrl, String productPicUrl, String headPicUrl
			, String qrcodePicUrl, Integer shareNum, Integer clientType, String userNickname) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shareId", shareId.toString()));
		params.add(new BasicNameValuePair("templateBgUrl", templateBgUrl));
		params.add(new BasicNameValuePair("productPicUrl", productPicUrl));
		params.add(new BasicNameValuePair("headPicUrl", headPicUrl));
		params.add(new BasicNameValuePair("qrcodePicUrl", qrcodePicUrl));
		params.add(new BasicNameValuePair("shareNum", shareNum.toString()));
		params.add(new BasicNameValuePair("clientType", clientType.toString()));
		params.add(new BasicNameValuePair("userNickname", userNickname));
		String webContent = HttpClientUtil.postBackstageApi(apiPath + "Ssqoo/ShearTemplateOne", params);
		return webContent;
	}
	
	/** 人人分享生成模板第二步，返回预览图片相对路径 */
	public String ShearTemplateTwo(Integer shareId, String templateViewUrl, String accessToken, Integer clientType, String openId) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shareId", shareId.toString()));
		params.add(new BasicNameValuePair("templateViewUrl", templateViewUrl));
		params.add(new BasicNameValuePair("accessToken", accessToken));
		params.add(new BasicNameValuePair("clientType", clientType.toString()));
		params.add(new BasicNameValuePair("openId", openId));
		String webContent = HttpClientUtil.postBackstageApi(apiPath + "Ssqoo/ShearTemplateTwo", params);
		return webContent;
	}
}
