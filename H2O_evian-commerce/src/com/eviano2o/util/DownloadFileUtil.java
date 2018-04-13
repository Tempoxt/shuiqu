package com.eviano2o.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadFileUtil {
	private static final Logger logger = LoggerFactory.getLogger(DownloadFileUtil.class);
	/**
	 * 传入要下载的图片的url，将url所对应的图片下载到本地
	 * 
	 * @param urlList
	 */
	public static Boolean downloadWxPicture(String urlString, String savePath) {
		try {
			URL url = new URL(urlString);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());
			String imageName = savePath;
			FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));

			byte[] buffer = new byte[1024];
			int length;
			while ((length = dataInputStream.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, length);
			}

			dataInputStream.close();
			fileOutputStream.close();
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.info("下载文件错误1，urlString："+urlString+"      savePath："+savePath+"      e："+e.toString());
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("下载文件错误2，urlString："+urlString+"      savePath："+savePath+"      e："+e.toString());
			return false;
		}
	}
	
	
	/**
	 *下载微信客户发送的文件 
	 * mediaId
	 * voiceType: 语音文件的后缀（语音格式，如amr，speex等）
	 * messageType：信息类型（voice/video/image）
	 */
	public static String downloadWxMessageFile(String mediaId, String voiceType, String messageType, String rootPath, String appId){
		String folder = "WeiXinFile/" + messageType + "/" + DateUtil.getNowDateShort();
		File templatePath = new File(rootPath + folder);
		if (!templatePath.exists() && !templatePath.isDirectory()) {
			templatePath.mkdirs();
		}
		
		String downLoadUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + WxTokenAndJsticketCache.getAccess_token(appId) + "&media_id=" + mediaId;
		String fileName = mediaId + ".";
		if(messageType.equals("voice")){
			fileName += voiceType;
		}else if(messageType.equals("video")){
			fileName += "mp4";
		}else if(messageType.equals("image")){
			fileName += "jpg";
		}else{return "";}
		String savePath = templatePath.toString() + "/" + fileName;
		if(downloadWxPicture(downLoadUrl, savePath))
			return savePath;
		else
			return "";
	}
	
	
	
}
