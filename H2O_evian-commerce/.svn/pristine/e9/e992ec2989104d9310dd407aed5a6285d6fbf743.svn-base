package com.eviano2o.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.eviano2o.bean.weixin.QiNiuConfigModel;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;


/**
 * Created by IntelliJ IDEA
 * PackName: com.evian.whp.api.util
 * User: pengan
 * Date: 2015/8/29
 * Time: 10:26
 * Remark:七牛上传图片Util
 */
public class QiniuFileSystemUtil {

    /**
     * 控件域名
     */
    public final static String namespaceUrl = QiNiuConfigModel.getNamespace();

    /**
     * 上传客户头像
     *
     * @param imageTxt
     * @return
     */
    public static String uploadClientPhotos(String imageTxt) throws QiniuException {
        
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String time = String.format("%s%s", date, System.currentTimeMillis());
        String filePath = String.format("Upload/Client/photos/images/%s/%s.jpg", date, time);
        UploadManager uploadManager = new UploadManager();
        Auth auth = Auth.create(QiNiuConfigModel.getAccessKey(), QiNiuConfigModel.getSecretKey());
        String token = auth.uploadToken(QiNiuConfigModel.getBucket());
        Response r = uploadManager.put(Base64.decode(imageTxt), filePath, token);
        return r.bodyString();
    }

    /**
     * 上传客户头像
     *
     * @param imageTxt
     * @return
     */
    public static String uploadClientPhotos(byte[] datas) throws QiniuException {
        
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String time = String.format("%s%s", date, System.currentTimeMillis());
        String filePath = String.format("Upload/Client/photos/images/%s/%s.jpg", date, time);
        
        UploadManager uploadManager = new UploadManager();
        Auth auth = Auth.create(QiNiuConfigModel.getAccessKey(), QiNiuConfigModel.getSecretKey());
        String token = auth.uploadToken(QiNiuConfigModel.getBucket());
        Response r = uploadManager.put(datas, filePath, token);
        return r.bodyString();
    }
    /**
     * 上传用户反馈图片
     *
     * @param imageTxt
     * @return
     */
    public static String uploadFeedBackPic(String imageTxt) throws QiniuException {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String time = String.format("%s%s", date, System.currentTimeMillis());
        String filePath = String.format("Upload/Client/feedback/images/%s/%s.jpg", date, time);
        UploadManager uploadManager = new UploadManager();
        Auth auth = Auth.create(QiNiuConfigModel.getAccessKey(), QiNiuConfigModel.getSecretKey());
        String token = auth.uploadToken(QiNiuConfigModel.getBucket());
        Response r = uploadManager.put(Base64.decode(imageTxt), filePath, token);
        return r.bodyString();
    }
    
    

    /**
     * 上传用户分享图片
     *
     * @param imageTxt
     * @return {"hash":"Fpntdbb7yTG1yWMhikxPjgwBfHFw","key":"Upload/Client/Shear/images/20170330/201703301490870434207.png"}
     */
    public static String uploadShearPic(String file) throws QiniuException {
    	
    	String prefix=file.substring(file.lastIndexOf(".")+1);
    	
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String time = String.format("%s%s", date, System.currentTimeMillis());
        String filePath = String.format("Upload/Client/Shear/images/%s/%s."+prefix, date, time);
        UploadManager uploadManager = new UploadManager();
        Auth auth = Auth.create(QiNiuConfigModel.getAccessKey(), QiNiuConfigModel.getSecretKey());
        String token = auth.uploadToken(QiNiuConfigModel.getBucket());
        Response r = uploadManager.put(file, filePath, token);
        return r.bodyString();
    }
    
}
