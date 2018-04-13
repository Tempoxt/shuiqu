package com.eviano2o.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eviano2o.bean.backstageApi.e_weixin_user_message;
import com.eviano2o.util.HttpClientUtil;
import com.eviano2o.util.SysParamCache;
import com.eviano2o.util.SysParamNames;

/** 访问水趣后台接口 */
public class BackstageApiService {
	private static final Logger logger = LoggerFactory.getLogger(BackstageApiService.class);
	String apiPath = "https://" + SysParamCache.getCache(SysParamNames.ParamAdminWebSit) + "/";

	/** 保存用户发送信息 */
	public String SaveUserMsg(e_weixin_user_message cur) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("message", cur.getMessage()));
		params.add(new BasicNameValuePair("messageType", cur.getMessageType()));
		params.add(new BasicNameValuePair("openId", cur.getOpenId()));
		params.add(new BasicNameValuePair("recongnition", cur.getRecongnition()));
		params.add(new BasicNameValuePair("eid", cur.getEid().toString()));
		String webContent = HttpClientUtil.postBackstageApi(apiPath + "SheqooApi/SaveUserMsg", params);
		return webContent;
	}
	
	/** 用户关注回复 */
	public String SubscripbeReply(Integer eid) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("eid", eid.toString()));
		String webContent = HttpClientUtil.postBackstageApi(apiPath + "SheqooApi/SubscripbeReply", params);
		//logger.info(apiPath + "SheqooApi/SubscripbeReply" + " |  " + webContent);
		return webContent;
	}
	
	
	/** 用户输入文本回复 */
	public String WxKeywordReply(String keyword, Integer eid) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("keyword", keyword));
		params.add(new BasicNameValuePair("eid", eid.toString()));
		String webContent = HttpClientUtil.postBackstageApi(apiPath + "SheqooApi/WxKeywordReply", params);
		//logger.info(apiPath + "SheqooApi/WxKeywordReply" + " |  " + webContent);
		return webContent;
	}


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
		String webContent = HttpClientUtil.postBackstageApi(apiPath + "ShareTemplateAPI/ShearTemplateOne", params);
		return webContent;
	}
	
	/** 人人分享生成模板第二步，返回预览图片相对路径 */
	public String ShearTemplateTwo(Integer shareId, String templateViewUrl, String accessToken, Integer clientType, String openId, String appId) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shareId", shareId.toString()));
		params.add(new BasicNameValuePair("templateViewUrl", templateViewUrl));
		params.add(new BasicNameValuePair("accessToken", accessToken));
		params.add(new BasicNameValuePair("clientType", clientType.toString()));
		params.add(new BasicNameValuePair("openId", openId));
		params.add(new BasicNameValuePair("appId", appId));
		String webContent = HttpClientUtil.postBackstageApi(apiPath + "ShareTemplateAPI/ShearTemplateTwo", params);
		return webContent;
	}

	/** 人人分享生成模板第二步，返回预览图片相对路径 */
	public String ShearTemplateTwoB(Integer shareId, Integer clientType, String openId, String appId) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shareId", shareId.toString()));
		params.add(new BasicNameValuePair("clientType", clientType.toString()));
		params.add(new BasicNameValuePair("openId", openId));
		params.add(new BasicNameValuePair("appId", appId));
		String webContent = HttpClientUtil.postBackstageApi(apiPath + "ShareTemplateAPI/ShearTemplateTwoB", params);
		return webContent;
	}

	/** 生成小程序店铺码模板接口 */
	public String ShearTemplateLiteappShopCode(String shopCode, Integer clientType, String openId, Integer tid, String appId) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shopCode", shopCode));
		params.add(new BasicNameValuePair("clientType", clientType.toString()));
		params.add(new BasicNameValuePair("tid", tid.toString()));
		params.add(new BasicNameValuePair("openId", openId));
		params.add(new BasicNameValuePair("appId", appId));
		String webContent = HttpClientUtil.postBackstageApi(apiPath + "ShareTemplateAPI/ShearTemplateLiteappShopCode", params);
		return webContent;
	}
	
	

	/** 将sessionId及其对应微信openId记录到数据表中 */
	public String SaveSessionId(String openId, String sessionId) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("openId", openId));
		params.add(new BasicNameValuePair("sessionId", sessionId));
		String webContent = HttpClientUtil.postBackstageApi(apiPath + "SheqooApi/SaveSessionId", params);
		return webContent;
	}
	
	/** 查询sessionId对应的微信openId */
	public String GetOpenIdBySessionId(String sessionId) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("sessionId", sessionId));
		String webContent = HttpClientUtil.postBackstageApi(apiPath + "SheqooApi/GetOpenIdBySessionId", params);
		return webContent;
	}
}
