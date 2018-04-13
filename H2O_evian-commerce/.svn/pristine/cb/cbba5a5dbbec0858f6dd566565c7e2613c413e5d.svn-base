package com.eviano2o.controller.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.eviano2o.bean.weixin.ShareTemplatePicsModel;
import com.eviano2o.util.DateUtil;
import com.eviano2o.util.DownloadFileUtil;
import com.eviano2o.util.HttpClientUtil;
import com.eviano2o.util.ImageWaterMakeUtil;
import com.eviano2o.util.SysParamNames;
import com.eviano2o.util.WxTokenAndJsticketCache;

public class ShareTemplateTwoHelper extends BaseControllerHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(ShareTemplateTwoHelper.class);
	
	Integer shareId;//分享记录主键ID
	Integer clientType;//来源（1：微信, 2：APP）
	String rootPath = _request.getSession().getServletContext().getRealPath("/");// 根目录
	
	public ShareTemplateTwoHelper(Model model, HttpServletRequest request){
		super(model, request);
		shareId = Integer.valueOf(_request.getParameter("shareId"));
		clientType = Integer.valueOf(StringUtils.isEmpty(_request.getParameter("clientType")) ? "0" : _request.getParameter("clientType"));
	}
	
	public void Init() {
		if (shareId.intValue() == 0 || clientType.intValue() == 0) {
			_result = formatJsonResult("E90000", "参数错误！");
			return;
		}
		
		String viewTemplate = weiXinService.getMyTemplateByShareId(shareId,getSessionWXAppId());
		if(StringUtils.isEmpty(viewTemplate)){
			_result = formatJsonResult("E90000", "没有该信息！");
			return;
		}
		
		JSONObject templateJson = JSONObject.fromObject(viewTemplate);
		logger.info("获取生成图片信息："+viewTemplate);
		if (templateJson.getJSONObject("data") == null 
				|| !templateJson.getJSONObject("data").has("url_1") 
				|| StringUtils.isEmpty(templateJson.getJSONObject("data").getString("url_1"))) {//如果没有生成过
					
			_result = formatJsonResult("E90000", "没有该信息！");
			logger.info("获取生成预览海报错误：" + viewTemplate);
			return;
		} else if (templateJson.getJSONObject("data") != null 
			&& templateJson.getJSONObject("data").has("url_1") 
			&& !StringUtils.isEmpty(templateJson.getJSONObject("data").getString("url_1")) 
			&& !StringUtils.isEmpty(templateJson.getJSONObject("data").getString("posterUrl")) 
			&& DateUtil.compare_date(templateJson.getJSONObject("data").getString("dateOvert") + ":00", DateUtil.getStringDate()) > 0) {//如果已经完整生成并且media_id不超时

			JSONObject resultJson = new JSONObject();
			resultJson.put("code", "E00000");
			resultJson.put("message", "生成成功！");
			JSONObject resultJsonData = new JSONObject();
			resultJsonData.put("shareId", shareId);
			resultJsonData.put("url1", templateJson.getJSONObject("data").getString("url_1"));
			resultJsonData.put("posterUrl", templateJson.getJSONObject("data").getString("posterUrl"));
			resultJson.put("data", resultJsonData);
			//如果是微信，则重新提交换取media_id，并发送
			if(clientType.intValue()== 1)
				sendSharePic(templateJson.getJSONObject("data").getString("mediaId"));	
			
			_result = resultJson.toString();
			//logger.info("222222222222222222222222222222："+ _result);
			return;
		} else if (templateJson.getJSONObject("data") != null 
			&& templateJson.getJSONObject("data").has("url_1") 
			&& !StringUtils.isEmpty(templateJson.getJSONObject("data").getString("url_1")) 
			&& !StringUtils.isEmpty(templateJson.getJSONObject("data").getString("posterUrl")) 
			&& DateUtil.compare_date(templateJson.getJSONObject("data").getString("dateOvert") + ":00", DateUtil.getStringDate()) <= 0) {//完整生成过但media_id已经超时
				
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", "E00000");
			resultJson.put("message", "生成成功！");
			JSONObject resultJsonData = new JSONObject();
			resultJsonData.put("shareId", shareId);
			resultJsonData.put("url1", templateJson.getJSONObject("data").getString("url_1"));
			resultJsonData.put("posterUrl", templateJson.getJSONObject("data").getString("posterUrl"));
			resultJson.put("data", resultJsonData);
			//如果是微信，则重新提交换取media_id，并发送
			if(clientType.intValue()== 1){
				String posterUrl = templateJson.getJSONObject("data").getString("posterUrl");
				String filePath = posterUrl.replace("http://" + getSysParamMapValue(SysParamNames.ParamWXWebSit) + "/", rootPath);
				String media_id = uploadWXImg(filePath);
	    		if(StringUtils.isEmpty(media_id)){
	            	return;
	    		}
	    		String result = weiXinService.updateShareRecordPicUrl(shareId, getSessionClientIdentityCode(), posterUrl, resultJsonData.getJSONObject("data").getInt("tid")
	    				, resultJsonData.getJSONObject("data").getInt("pictureId"), media_id, DateUtil.addHours(DateUtil.getStringDate(), 47),getSessionWXAppId());
	    		logger.info("提交海报第三步返回："+ result);
				sendSharePic(media_id);
			}
			
			_result = resultJson.toString();
			//logger.info("33333333333333333333333333333333333333："+ _result);
			return;
		}else if (templateJson.getJSONObject("data") != null 
			&& templateJson.getJSONObject("data").has("url_1") 
			&& !StringUtils.isEmpty(templateJson.getJSONObject("data").getString("url_1")) 
			&& StringUtils.isEmpty(templateJson.getJSONObject("data").getString("posterUrl"))) {//只生成过预览图片，没有打印二维码并发送给客户
				
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", "E00000");
			resultJson.put("message", "生成成功！");
			JSONObject resultJsonData = new JSONObject();
			resultJsonData.put("shareId", shareId);
			resultJsonData.put("url1", templateJson.getJSONObject("data").getString("url_1"));
			resultJsonData.put("posterUrl", makePic(templateJson));
			resultJson.put("data", resultJsonData);
			_result = resultJson.toString();
			//logger.info("444444444444444444444444444444444："+ _result);
			return;
		}
	}

	
	/**生成最终图片
	 * url1 : 预览图片url
	 * */
	private String makePic(JSONObject json){
		//预览图片物理路径
		String viewPath = json.getJSONObject("data").getString("url_1").replace("http://" + getSysParamMapValue(SysParamNames.ParamWXWebSit) + "/", rootPath);
		//水印图片物理路径
		String waterPath = getWXQrcode();
		if(StringUtils.isEmpty(waterPath)){
        	return "";
		}
		
		List<ShareTemplatePicsModel> pictrues = new ArrayList();
		ShareTemplatePicsModel headWaterPic = new ShareTemplatePicsModel();
		headWaterPic.setAlpha(1.0f);
		headWaterPic.setPicPath(waterPath);
		headWaterPic.setPicX(280);
		headWaterPic.setPicY(400);
		pictrues.add(headWaterPic);
		
		String folder = "ShareTemplate/success";
    	File filePath = new File(rootPath + folder);
    	if  (!filePath.exists()  && !filePath.isDirectory())
    	{
    		filePath.mkdirs();
    	}
    	String saveFilePath = rootPath + folder + "/" + shareId + ".jpg";//最终图片物理路径
    	String localPicPath = "http://" + getSysParamMapValue(SysParamNames.ParamWXWebSit) + "/" + folder + "/" + shareId + ".jpg";//最终图片url
    	
    	//打印二维码
    	Boolean isSuccess = ImageWaterMakeUtil.evianShareTemplate(viewPath, saveFilePath, pictrues, null);
    	
    	if(isSuccess){
    		String media_id = "APP";
    		if(clientType.intValue() == 1){
    			media_id = uploadWXImg(saveFilePath);
    			if(StringUtils.isEmpty(media_id)){
    				return "";
    			}
    		}
    		String result = weiXinService.updateShareRecordPicUrl(shareId, getSessionClientIdentityCode(), localPicPath, json.getJSONObject("data").getInt("tid")
    				, json.getJSONObject("data").getInt("pictureId"), media_id, DateUtil.addHours(DateUtil.getStringDate(), 47),getSessionWXAppId());
    		logger.info("提交海报第三步返回："+ result);
    		return localPicPath;
    	}
    	
		return "";
	}

	/**发送海报*/
	private void sendSharePic(String mediaId){
		String sendUserMsg = "{\"touser\":\"" + getSessionWeiXinId() + "\",\"msgtype\":\"image\",\"image\":{\"media_id\":\"" + mediaId + "\"}}";
		// https://mp.weixin.qq.com/wiki/7/12a5a320ae96fecdf0e15cb06123de9f.html
		String wxSendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + WxTokenAndJsticketCache.getAccess_token(getSessionWXAppId());
		String sendResult = HttpClientUtil.post(wxSendMsgUrl, sendUserMsg);
		logger.info("发送人人开店模板："+sendResult+"   wxSendMsgUrl:"+wxSendMsgUrl+"   sendUserMsg:"+sendUserMsg+"   clientType:"+clientType);
	}
	
	/**上传微信图片，换取media_id*/
	private String uploadWXImg(String filePath){
		try {
			String mediaInfoStr = UploadWxMedia.uploadFile(WxTokenAndJsticketCache.getAccess_token(getSessionWXAppId()), "image/jpeg", filePath);
			if (StringUtils.isEmpty(mediaInfoStr)) {
				logger.info("上传到weixin:" + mediaInfoStr);
				_result = formatJsonResult("E90000", "换取media_id失败！");
			}
			JSONObject mediaInfo = JSONObject.fromObject(mediaInfoStr);
			if (mediaInfo.has("media_id") && !StringUtils.isEmpty(mediaInfo.getString("media_id"))) {
				sendSharePic(mediaInfo.getString("media_id"));
				//为了不保存生成的图片，不做历史查询，分享完毕马上删除生成的图片
				new File(filePath).delete();
				return mediaInfo.getString("media_id");
			} else {
				logger.info("换取微信mediaId失败:" + mediaInfo.toString());
				_result = formatJsonResult("E90000", "换取微信media_id失败！");
			}
		} catch (Exception ex) {
			logger.info("海报生成第二步:" + ex.toString());
		}
		return "";
	}
	
	/**获取永久二维码，并返回缩略图物理地址 */
	private String getWXQrcode(){
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + WxTokenAndJsticketCache.getAccess_token(getSessionWXAppId());
        String content = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"share"+ shareId +"\"}}}";
        String ticketStr = HttpClientUtil.post(url, content);
        if(StringUtils.isEmpty(ticketStr)){
        	_result = formatJsonResult("E90000", "生成永久二维码换取ticket失败！");
        	logger.info("生成永久二维码换取ticket失败。url:" + url + "   content:"+content);
        	return "";
        }
        JSONObject ticketJson = JSONObject.fromObject(ticketStr);
        if(!ticketJson.has("ticket") || StringUtils.isEmpty(ticketJson.getString("ticket"))){
        	_result = formatJsonResult("E90000", "换取永久二维码ticket失败！");
        	logger.info("换取永久二维码ticket失败。url:" + url + "   content:"+content + "   tikectStr:"+ticketStr);
        	return "";
        }
        url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticketJson.getString("ticket");
        
		String folder = "ShareTemplate/qrcode";
    	File filePath = new File(rootPath + folder);
    	if  (!filePath.exists()  && !filePath.isDirectory())
    	{
    		filePath.mkdirs();
    	}
    	String savePicPath = rootPath + folder + "/" + shareId + ".jpg";
    	if(!DownloadFileUtil.downloadWxPicture(url, savePicPath)){
    		_result = formatJsonResult("E90000", "下载永久二维码失败！");
    		logger.info("下载永久二维码失败。url:" + url + "   savePicPath:"+savePicPath);
    		return "";
    	}
    	String makeWaterPic = new StringBuilder(savePicPath).insert(savePicPath.lastIndexOf("."), "_x").toString();;
    	ImageWaterMakeUtil.scale2(savePicPath, makeWaterPic, 205, 205, false);
    	return makeWaterPic;
	}
	

	@Override
	public String getResult() {
		return _result;
	}
}
