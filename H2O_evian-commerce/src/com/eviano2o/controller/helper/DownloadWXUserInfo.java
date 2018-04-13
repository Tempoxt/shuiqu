package com.eviano2o.controller.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.eviano2o.bean.weixin.ClientModel;
import com.eviano2o.bean.weixin.QiNiuConfigModel;
import com.eviano2o.service.WeiXinService;
import com.eviano2o.util.DateUtil;
import com.eviano2o.util.HttpClientUtil;
import com.eviano2o.util.QiniuFileSystemUtil;
import com.eviano2o.util.SessionConstantDefine;
import com.eviano2o.util.SysParamCache;
import com.eviano2o.util.SysParamNames;
import com.eviano2o.util.WxTokenAndJsticketCache;

/** 下载关注用户资料 */
public class DownloadWXUserInfo {
	private static final Logger logger = LoggerFactory.getLogger(DownloadWXUserInfo.class);
	String accessToken;
	String openId;
	String qrScene;
	Integer shareId;
	String rootPath;
	String appId;
	HttpServletRequest _request;

	public DownloadWXUserInfo(HttpServletRequest request, String access_token, String OpenID, String qrscene, Integer shareId, String rootpath, String appId) {
		this.accessToken = access_token;
		this.openId = OpenID;
		this.qrScene = qrscene;
		this.shareId = shareId;
		this.rootPath = rootpath;
		this._request = request;
		this.appId = appId;
	}

	public void doDownload(int times) {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
		url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + WxTokenAndJsticketCache.getAccess_token(appId) + "&openid=" + openId + "&lang=zh_CN";
		String webContent = HttpClientUtil.get(url);
		// logger.info("[下载关注用户信息:{}]，[url:{}]", new Object[] { webContent, url
		// });
		if (StringUtils.isEmpty(webContent))
			return;
		JSONObject objJson = JSONObject.fromObject(webContent);
		if(objJson.containsKey("errcode") && objJson.getInt("errcode") == 42001 && times == 0){
			doDownload(1);
		}

		String headimgurl = objJson.optString("headimgurl");
		if (!StringUtils.isEmpty(objJson.optString("headimgurl"))) {
			try{
				headimgurl = downLoadFromUrl(objJson.optString("headimgurl"), objJson.optString("openid"));
			}catch(Exception ex){
				logger.info("[ex:{}], [webContent:{}]", ex, webContent);
			}
		}

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		// 添加参数
		params.add(new BasicNameValuePair("openid", objJson.optString("openid")));
		params.add(new BasicNameValuePair("nickname", objJson.optString("nickname")));
		params.add(new BasicNameValuePair("sex", objJson.optString("sex")));
		params.add(new BasicNameValuePair("language", objJson.optString("language")));
		params.add(new BasicNameValuePair("city", objJson.optString("city")));
		params.add(new BasicNameValuePair("province", objJson.optString("province")));
		params.add(new BasicNameValuePair("country", objJson.optString("country")));
		params.add(new BasicNameValuePair("headimgurl", headimgurl));
		params.add(new BasicNameValuePair("subscribe_time", objJson.optString("subscribe_time")));
		params.add(new BasicNameValuePair("unionid", objJson.optString("unionid")));
		params.add(new BasicNameValuePair("remark", objJson.optString("remark")));
		params.add(new BasicNameValuePair("groupid", objJson.optString("groupid")));
		params.add(new BasicNameValuePair("tagid_list", objJson.optString("tagid_list")));
		params.add(new BasicNameValuePair("qrscene", qrScene));
		params.add(new BasicNameValuePair("qrsceneRemark", ""));
		params.add(new BasicNameValuePair("shareId", shareId.toString()));
		params.add(new BasicNameValuePair("authorizer_appid", appId));
		

		String saveResult = new WeiXinService().saveWxUserInfo(params);
		if (!saveResult.equals("E00000"))
			logger.error("[保存关注用户信息:{}],[获取用户信息:{}],[url:{}]", new Object[] { saveResult, webContent ,url});
		/*else{
			ClientModel userInfo = new WeiXinService().getWxLogin(objJson.optString("openid"));
			if(userInfo==null){
				//new DownloadWXUserInfo(clientModel.getAccess_token(), clientModel.getOpenid(), null, "", _request.getSession().getServletContext().getRealPath("/")).doDownload();
				_request.getSession().removeAttribute(SessionConstantDefine.CLIENT_INFO);
			}else{
				_request.getSession().setAttribute(SessionConstantDefine.CLIENT_INFO, userInfo);
			}
		}*/
			
	}

	/**
	 * 从网络Url中下载文件
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	String downLoadFromUrl(String urlStr, String openid) throws IOException {
		
		String result = "/WeiXinFile/" + DateUtil.getStringDateShort() + "/" + openid + ".jpg";
    	rootPath = rootPath + "WeiXinFile/" + DateUtil.getStringDateShort();
    	File filePath = new File(rootPath);
    	if  (!filePath.exists()  && !filePath.isDirectory())
    	{
    		filePath.mkdirs();
    	}
    	String fileFullName = rootPath + "\\" + openid + ".jpg";
    	
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为10秒
		conn.setConnectTimeout(10 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);
		File file = new File(fileFullName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
		
		String upResult = QiniuFileSystemUtil.uploadClientPhotos(getFileBytes(fileFullName));
		if(!StringUtils.isEmpty(upResult)){
			JSONObject upResultJson = JSONObject.fromObject(upResult);
			if(upResultJson.has("hash") && upResultJson.has("key")){
				file.delete();
				return QiNiuConfigModel.getNamespace() + upResultJson.getString("key");
			}
		}
		
		return "http://" + SysParamCache.getCache(SysParamNames.ParamWXWebSit) + result;
	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	
	
    private byte[] getFileBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }
}
