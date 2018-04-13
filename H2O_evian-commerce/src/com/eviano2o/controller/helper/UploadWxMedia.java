package com.eviano2o.controller.helper;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadWxMedia {
	private static final Logger logger = LoggerFactory.getLogger(UploadWxMedia.class);
	
	/**
	 * 向微信服务器上传文件
	 * @param accessToken进入的接口
	 * @param fileType 文件类型(语音或者是图片)(image/jpeg、audio/mp3、video/mpeg4)
	 * @param filePath 文件的存储路径
	 * @return json的格式{"media_id":"nrSKG2eY1E_svLs0Iv2Vvh46PleKk55a47cNO1ZS5_pdiNiSXuijbCmWWc8unzBQ","created_at":1408436207,"type":"image"}
	 */
	public static String uploadFile(String accessToken, String fileType, String filePath) throws Exception {

		
		// 上传文件请求路径
		String action = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=" + accessToken + "&type=" + fileType;
		URL url = new URL(action);
		String result = "";
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			logger.info("上传到微信的文件不存在："+filePath);
			throw new IOException("上传的文件不存在");
		}

		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); // post方式不能使用缓存

		// 设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		// 设置边界

		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);

		// 请求正文信息
		// 第一部分：

		StringBuilder sb = new StringBuilder();
		sb.append("--"); // 必须多两道线
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		byte[] head = sb.toString().getBytes("utf-8");

		// 获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());

		// 输出表头
		out.write(head);

		// 文件正文部分
		// 把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		// 结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
		out.write(foot);
		out.flush();
		out.close();
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			//if (result == null) {
				result = buffer.toString();
			//}
				logger.info("上传到微信文件返回："+result+"");
		} catch (IOException e) {
			//logger.info("上传到微信文件错误："+e.toString()+"");
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
			throw new IOException("数据读取异常");
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		return result;

	}
}
