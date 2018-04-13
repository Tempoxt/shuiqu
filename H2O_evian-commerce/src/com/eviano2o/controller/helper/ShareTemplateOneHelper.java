package com.eviano2o.controller.helper;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.eviano2o.bean.weixin.ShareTemplatePicsModel;
import com.eviano2o.bean.weixin.ShareTemplateTextModel;
import com.eviano2o.util.DateUtil;
import com.eviano2o.util.DownloadFileUtil;
import com.eviano2o.util.HttpClientUtil;
import com.eviano2o.util.ImageWaterMakeUtil;
import com.eviano2o.util.SysParamNames;
import com.eviano2o.util.WxTokenAndJsticketCache;

/**
 * 人人开店模板1生成
 */
public class ShareTemplateOneHelper extends BaseControllerHelper {
	Integer shareId;// 分享id
	Integer tid;// 模板id
	Integer pictureid;// 图片id
	String productPicUrl;// 商品图片url
	Integer shareNum;//分享人数统计
	Integer clientType;//来源（1：微信, 2：APP）
	String userNickname;//APP取nickname， 微信取wxnickName
	
	//lideguang-pc:8080/weixin/viewPic?tid=1&pictureid=41&productPicUrl=http://7xlmry.com1.z0.glb.clouddn.com/Upload/Product/images/20151123/151123161959343195.gif
	String rootPath = _request.getSession().getServletContext().getRealPath("/");// 根目录
	
	String headImgUrl = "http://sheqoo.com/files/images/default.png";// 头像URL
	
	public ShareTemplateOneHelper(Model model, HttpServletRequest request) {
		super(model, request);
		
		shareId = Integer.valueOf(StringUtils.isEmpty(_request.getParameter("shareId")) ? "0" : _request.getParameter("shareId"));
		tid = Integer.valueOf(StringUtils.isEmpty(_request.getParameter("tid")) ? "0" : _request.getParameter("tid"));
		pictureid = Integer.valueOf(StringUtils.isEmpty(_request.getParameter("pictureid")) ? "0" : _request.getParameter("pictureid"));
		productPicUrl = _request.getParameter("productPicUrl");
		clientType = Integer.valueOf(StringUtils.isEmpty(_request.getParameter("clientType")) ? "0" : _request.getParameter("clientType"));
		logger.info("生成模板参数-> tid:" +tid + "   pictureid:" + pictureid + "   productPicUrl:" + productPicUrl + "   shareId:" + shareId + "   clientType:" + clientType);
		userNickname = getSessionClient().getNickname();
	}

	public void Init() {
		if ((tid.intValue() == 0 || pictureid.intValue() == 0 || StringUtils.isEmpty(productPicUrl)) && shareId.intValue() == 0 && clientType.intValue() == 0) {
			_result = formatJsonResult("E90000", "参数错误！");
			return;
		}

		if(!StringUtils.isEmpty(getSessionClient().getPhoto()))
			headImgUrl = getSessionClient().getPhoto();
		
		
		String template = checkTemplate_new();
		if (StringUtils.isEmpty(template)){
			//_result = formatJsonResult("E90000", "获取预览失败！");
			return;
		}
		
		JSONObject templateJson = JSONObject.fromObject(template);
		if(1==1){//为了不保存生成的图片，不做历史查询，分享完毕马上删除生成的图片
			mackPic();
			return;
		}
		
		if (templateJson.getJSONObject("data") == null 
			|| !templateJson.getJSONObject("data").has("url_1") 
			|| StringUtils.isEmpty(templateJson.getJSONObject("data").getString("url_1"))) {//如果没有生成过
				
			mackPic();
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
			//如果是微信，则发送
			if(clientType.intValue()== 1)
				sendSharePic(templateJson.getJSONObject("data").getString("mediaId"));
			
			_result = resultJson.toString();
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
			if(clientType.intValue()== 1)
				sendSharePic(templateJson.getJSONObject("data").getString("mediaId"));	
			
			_result = resultJson.toString();
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
			resultJsonData.put("posterUrl", "");
			resultJson.put("data", resultJsonData);
			_result = resultJson.toString();
			return;
		}
	}
	
	
	/** 记录判断主键，改为不保存图片，每次新生成 */
	public String checkTemplate_new(){
		String result = weiXinService.saveShareRecord(getSessionClientIdentityCode(), tid, pictureid, userNickname, "", headImgUrl,getSessionWXAppId());
		if(JSONObject.fromObject(result).has("code") && JSONObject.fromObject(result).getString("code").equals("E00000")){
			shareId = JSONObject.fromObject(result).getJSONObject("data").getInt("shareId");
		}
		else{
			_result = result;
			result = "";
		}
		return result;
	}
	

	/** 记录判断主键 */
	public String checkTemplate(){
		String result = "";
		if(shareId >0){
			result = weiXinService.getMyTemplateByShareId(shareId,getSessionWXAppId());
			if(StringUtils.isEmpty(result)){
				_result = formatJsonResult("E90000", "获取已有预览错误！");
				return "";
			}
		}else{
			result = weiXinService.getTemporaryTemplate(getSessionClientIdentityCode(), tid, pictureid,getSessionWXAppId());
			if(StringUtils.isEmpty(result) || !JSONObject.fromObject(result).getString("code").equals("E00000")){
				logger.info("获取已有预览错误1，clientId:" + getSessionClientIdentityCode()+ "  tid:" +tid + "  pictureid:" +pictureid + "   result:"+result);
				_result = formatJsonResult("E90000", "获取已有预览错误！");
				return "";
			}else{
				
				//System.out.println(result);
				JSONObject getRecodeJson = JSONObject.fromObject(result);
				//System.out.println("获取已有预览错误2，clientId:" + getSessionClientIdentityCode()+ "  tid:" +tid + "  pictureid:" +pictureid + "   result:"+result);
				if(!getRecodeJson.has("code") || !getRecodeJson.getString("code").equals("E00000")){
					logger.info("获取已有预览错误2，clientId:" + getSessionClientIdentityCode()+ "  tid:" +tid + "  pictureid:" +pictureid + "   result:"+result);
					_result = formatJsonResult("E90000", "获取已有预览错误！");
					return "";
				}else if(getRecodeJson.getString("code").equals("E00000") && getRecodeJson.getJSONObject("data") == null || !getRecodeJson.getJSONObject("data").has("shareId")){
					result = weiXinService.saveShareRecord(getSessionClientIdentityCode(), tid, pictureid, userNickname, "", headImgUrl,getSessionWXAppId());
					System.out.println("获取已有预览错误3，clientId:" + getSessionClientIdentityCode()+ "  tid:" +tid + "  pictureid:" +pictureid + "   result:"+result);
				}else{
					
				}
				if(JSONObject.fromObject(result).has("code") && JSONObject.fromObject(result).getString("code").equals("E00000")){
					shareId = JSONObject.fromObject(result).getJSONObject("data").getInt("shareId");
				}
				else{
					_result = result;
					result = "";
				}
			}
		}
		return result;
	}
	
	private Boolean mackPic() {
		// 模板路径
		String srcImgPath = templateBgPath();
		
		// 生成头像水印路径
		String scaleHeadImgPath = headImg();
		
		// 生成商品水印路径
		String productImgPath = productPicPath();

		if (StringUtils.isEmpty(srcImgPath) || StringUtils.isEmpty(scaleHeadImgPath) || StringUtils.isEmpty(productImgPath)) {
			return false;
		}

		String folder = "ShareTemplate/viewTemplate";
		File filePath = new File(rootPath + folder);
		if (!filePath.exists() && !filePath.isDirectory()) {
			filePath.mkdirs();
		}
		//logger.info("headImgUrl:"+headImgUrl);
		String saveFilePath = rootPath + folder + "/" + shareId + ".jpg";
		
		List<ShareTemplatePicsModel> pictrues = new ArrayList();
		ShareTemplatePicsModel headWaterPic = new ShareTemplatePicsModel();
		headWaterPic.setAlpha(1.0f);
		headWaterPic.setPicPath(scaleHeadImgPath);
		headWaterPic.setPicX(48);
		headWaterPic.setPicY(80);
		pictrues.add(headWaterPic);
		ShareTemplatePicsModel productWaterPic = new ShareTemplatePicsModel();
		productWaterPic.setAlpha(1.0f);
		productWaterPic.setPicPath(productImgPath);
		productWaterPic.setPicX(435);
		productWaterPic.setPicY(1020);
		pictrues.add(productWaterPic);
		
		List<ShareTemplateTextModel> texts = new ArrayList();
		ShareTemplateTextModel nickName = new ShareTemplateTextModel();
		nickName.setAlpha(1.0f);
		nickName.setFontColor(Color.white);
		nickName.setFontName("黑体");
		nickName.setFontSize(40);
		nickName.setFontStyle(40);
		nickName.setFontX(180);
		nickName.setFontY(120);
		nickName.setTextContent(StringUtils.isEmpty(userNickname) ? "水趣用户" : userNickname);
		texts.add(nickName);
		ShareTemplateTextModel shareCount = new ShareTemplateTextModel();
		shareCount.setAlpha(1.0f);
		shareCount.setFontColor(Color.white);
		shareCount.setFontName("黑体");
		shareCount.setFontSize(25);
		shareCount.setFontStyle(25);
		shareCount.setFontX(180);
		shareCount.setFontY(165);
		shareCount.setTextContent("第" + shareNum + "个为健康好水代言");
		texts.add(shareCount);

		//Boolean isSuccess = ImageWaterMakeUtil.markImageByIcon(scaleHeadImgPath, srcImgPath, saveFilePath, 0, userNickname);
		Boolean isSuccess = ImageWaterMakeUtil.evianShareTemplate(srcImgPath, saveFilePath, pictrues, texts);
		
		String localPicPath = "http://" + getSysParamMapValue(SysParamNames.ParamWXWebSit) + "/" + folder + saveFilePath.substring(saveFilePath.lastIndexOf("/"));

		
		if (isSuccess) {
			String saveRecord = weiXinService.updateShareRecordUrl1(getSessionClientIdentityCode(), tid, pictureid, shareId, localPicPath,getSessionWXAppId());
			//logger.info("保存分享记录结果：" + saveRecord);
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", "E00000");
			resultJson.put("message", "生成成功！");
			JSONObject resultJsonData = new JSONObject();
			resultJsonData.put("shareId", shareId);
			resultJsonData.put("url1", localPicPath);
			resultJsonData.put("posterUrl", "");
			resultJson.put("data", resultJsonData);

			_result = resultJson.toString();
			return true;
		}

		_result = formatJsonResult("E90000", "生成错误！");
		return false;
	}

	/** 头像处理 */
	private String headImg() {
		Integer imgWidth = 120, imgHeight = 120;//缩略图宽高
		String waterHeadImg = "";//头像缩略图
		String headPicServerPath = "";//服务器原图路径

		if (StringUtils.isEmpty(headImgUrl)) {// 如果头像url为空则直接取默认头像
			headPicServerPath = rootPath + "files\\images\\default.jpg";
			waterHeadImg = headPicServerPath;
			//logger.info("头像为空，取默认头像:" + rootPath + "files\\images\\default.jpg");
		} else {
			if (headImgUrl.indexOf(getSysParamMapValue(SysParamNames.ParamWXWebSit)) > 0) {// 如果头像url中有微信前端域名关键字，则是微信头像，否则为APP头像
			
				headPicServerPath = headImgUrl.replace("http://" + getSysParamMapValue(SysParamNames.ParamWXWebSit) + "/", rootPath);
				waterHeadImg = new StringBuilder(headPicServerPath).insert(headPicServerPath.lastIndexOf("."), "_x").toString();
				//logger.info("微信头像：" + headImgUrl + "  原图路径："+headPicServerPath+"  缩略图路径：" + waterHeadImg);
			} else {// 如果是APP头像，则需要下载到本地处理，才可打印
				String folder = "ShareTemplate/headImg";
				File filePath = new File(rootPath + folder);
				if (!filePath.exists() && !filePath.isDirectory()) {
					filePath.mkdirs();
				}
				
				//存在无后缀图片url判断，如http://wx.qlogo.cn/mmopen/3x1JLYelTwczxbYssg4dLzHicJs2vKXvQ5ic0Xvic6KFIMBLvjfNRLd0BISSDibM99IB9znEkYVPHaQsnadibwjO5UwA3fwMsOnKs/0
				String houZhui = headImgUrl.substring(headImgUrl.length() - 3);
				if(!houZhui.toLowerCase().equals("jpg") && !houZhui.toLowerCase().equals("png") && !houZhui.toLowerCase().equals("gif"))
					headPicServerPath = rootPath + folder + "\\" + getSessionWeiXinId() + ".jpg";
				else
					headPicServerPath = rootPath + folder + headImgUrl.substring(headImgUrl.lastIndexOf("/"));//下载的APP头像
				
				waterHeadImg = new StringBuilder(headPicServerPath).insert(headPicServerPath.lastIndexOf("."), "_x").toString();
				//logger.info("APP头像：" + headImgUrl + "  原图路径："+headPicServerPath+"  缩略图路径：" + waterHeadImg);
			}
			
			// 如果服务器不存在头像,则重新下载
			if (!new File(headPicServerPath).exists() || !new File(waterHeadImg).exists()) {
				if (!DownloadFileUtil.downloadWxPicture(headImgUrl, headPicServerPath)){
					_result = formatJsonResult("E90000", "下载头像失败！");
					logger.info("下载头像失败：" + headImgUrl + "  保存路径："+headPicServerPath);
					return "";
				}else{
					//logger.info("111111111 原图路径："+headPicServerPath+"  缩略图路径：" + waterHeadImg);
				}
				//logger.info(headImgUrl + "  |  "+headPicServerPath+"    |     "+waterHeadImg);
				
				try{
					File f = new File(headPicServerPath);
					BufferedImage bi = ImageIO.read(f);
					if(bi.getHeight()<=imgHeight && bi.getWidth() <= imgWidth)
						waterHeadImg = headPicServerPath;
					else{
						ImageWaterMakeUtil.scale2(headPicServerPath, waterHeadImg, imgWidth, imgHeight, false);
					}
				}catch(Exception ex){}
				
				if (!new File(headPicServerPath).exists() || !new File(waterHeadImg).exists()) {
					_result = formatJsonResult("E90000", "生成APP头像失败！");
					//logger.info("生成缩略图头像失败 -> 原图路径："+headPicServerPath+"  缩略图路径：" + waterHeadImg);
					return ""; 
				}else{
					//logger.info("22222222 原图路径："+headPicServerPath+"  缩略图路径：" + waterHeadImg);
				}
			}else{
				//logger.info("33333333 原图路径："+headPicServerPath+"  缩略图路径：" + waterHeadImg);
			}
			
		}
		
		return waterHeadImg;
	}

	/** 获取模板物理路径 */
	private String templateBgPath() {
		String templateJson = weiXinService.getShareTemplateDetail(tid, getSessionClientIdentityCode(), true,getSessionWXAppId());
		if (StringUtils.isEmpty(templateJson)) {
			_result = formatJsonResult("E90000", "获取模板失败！");
			return "";
		}

		JSONObject template = JSONObject.fromObject(templateJson);
		if (template.has("code") && template.getString("code").equals("E00000") && template.getJSONObject("data") != null 
				&& template.getJSONObject("data").getJSONObject("templateData") != null 
				&& !StringUtils.isEmpty(template.getJSONObject("data").getJSONObject("templateData").getString("backImage"))) {
			shareNum = template.getJSONObject("data").getJSONObject("templateData").getInt("shareNum");
			String folder = "ShareTemplate/templateBg";
			File templatePath = new File(rootPath + folder);
			if (!templatePath.exists() && !templatePath.isDirectory()) {
				templatePath.mkdirs();
			}
			String backImageUrl = template.getJSONObject("data").getJSONObject("templateData").getString("backImage");
			String saveTemplatePath = rootPath + folder + backImageUrl.substring(backImageUrl.lastIndexOf("/"));
			File saveTemplatePathFile = new File(saveTemplatePath);
			//System.out.println("-----------"+templateJson);
			if (!saveTemplatePathFile.exists() || !template.getJSONObject("data").getJSONObject("templateData").getBoolean("ifdown")) {// 如果已经存在头像
				if (!DownloadFileUtil.downloadWxPicture(template.getJSONObject("data").getJSONObject("templateData").getString("backImage"), saveTemplatePath)) {
					_result = formatJsonResult("E90000", "下载模板失败！");
					return "";
				}
			}
			return saveTemplatePath;
		} else {
			_result = formatJsonResult("E90000", "获取模板失败。");
			logger.info("获取模板失败:" + templateJson);
			return "";
		}
	}

	/** 获取商品图片物理路径 */
	private String productPicPath() {
		//Integer imgWidth = 300, imgHeight = 300;

		String folder = "ShareTemplate/productImg";
		File templatePath = new File(rootPath + folder);
		if (!templatePath.exists() && !templatePath.isDirectory()) {
			templatePath.mkdirs();
		}
		String saveProductPicPath = rootPath + folder + productPicUrl.substring(productPicUrl.lastIndexOf("/"));
		//String waterPic = new StringBuilder(saveProductPicPath).insert(saveProductPicPath.lastIndexOf("."), "_x").toString();

		File saveTemplatePathFile = new File(saveProductPicPath);
		if (!saveTemplatePathFile.exists()) {// 如果没有该商品图片
			if (!DownloadFileUtil.downloadWxPicture(productPicUrl, saveProductPicPath)) {
				_result = formatJsonResult("E90000", "下载商品图片失败！");
				return "";
			}
			//ImageWaterMakeUtil.scale2(saveProductPicPath, waterPic, imgWidth, imgHeight, false);
		}

		return saveProductPicPath;
	}

	/** 发送海报 */
	private void sendSharePic(String media_id){
		String sendUserMsg = "{\"touser\":\""+getSessionWeiXinId()+"\",\"msgtype\":\"image\",\"image\":{\"media_id\":\""+media_id+"\"}}";
		String wxSendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+WxTokenAndJsticketCache.getAccess_token(getSessionWXAppId());
		String sendResult = HttpClientUtil.post(wxSendMsgUrl, sendUserMsg);
		logger.info("发送人人开店模板："+sendResult+"   wxSendMsgUrl:"+wxSendMsgUrl+"   sendUserMsg:"+sendUserMsg);
	}
	
	
	private static final Logger logger = LoggerFactory.getLogger(ShareTemplateOneHelper.class);

	@Override
	public String getResult() {
		return _result;
	}
}
