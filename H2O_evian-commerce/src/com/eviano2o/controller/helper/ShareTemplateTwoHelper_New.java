package com.eviano2o.controller.helper;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.eviano2o.util.DateUtil;
import com.eviano2o.util.SysParamCache;
import com.eviano2o.util.SysParamNames;

public class ShareTemplateTwoHelper_New extends BaseControllerHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(ShareTemplateTwoHelper_New.class);
	
	Integer shareId;//分享记录主键ID
	Integer clientType;//来源（1：微信, 2：APP）
	String rootPath = _request.getSession().getServletContext().getRealPath("/");// 根目录
	
	public ShareTemplateTwoHelper_New(Model model, HttpServletRequest request){
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
		}else if (templateJson.getJSONObject("data") != null 
			&& templateJson.getJSONObject("data").has("url_1") 
			&& !StringUtils.isEmpty(templateJson.getJSONObject("data").getString("url_1")) 
			&& StringUtils.isEmpty(templateJson.getJSONObject("data").getString("posterUrl"))) {//只生成过预览图片，没有打印二维码并发送给客户
				
			String makeResult = fileService.ShearTemplateTwoB(shareId, clientType, getSessionWeiXinId(), getSessionWXAppId());
			//logger.info("11                    "+makeResult);
			if (StringUtils.isEmpty(makeResult)) {
				_result = formatJsonResult("E90000", "生成模板失败！");
				return;
			}
			
			JSONObject json = JSONObject.fromObject(makeResult);
			if (json.has("code") && json.getString("code").equals("E00000")){
				
			}else{
				_result = makeResult;
				return;
			}
			
			//记录
			weiXinService.updateShareRecordPicUrl(shareId, getSessionClientIdentityCode()
					, "https://" + SysParamCache.getCache(SysParamNames.ParamFileManageDomain) + "/" + json.getString("data")
					, 1
    				, 1, "", DateUtil.addHours(DateUtil.getStringDate(), 47),getSessionWXAppId());
			
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", "E00000");
			resultJson.put("message", "生成成功！");
			JSONObject resultJsonData = new JSONObject();
			resultJsonData.put("shareId", shareId);
			resultJsonData.put("url1", templateJson.getJSONObject("data").getString("url_1"));
			resultJsonData.put("posterUrl", "https://" + SysParamCache.getCache(SysParamNames.ParamFileManageDomain) + "/" + json.getString("data"));
			resultJson.put("data", resultJsonData);
			_result = resultJson.toString();
			//logger.info("444444444444444444444444444444444："+ _result);
			return;
		}
	}

	

	@Override
	public String getResult() {
		return _result;
	}
}
