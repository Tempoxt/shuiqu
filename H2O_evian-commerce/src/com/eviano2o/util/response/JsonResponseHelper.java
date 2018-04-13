package com.eviano2o.util.response;

import com.google.gson.Gson;

/** 返回json结果类 */
public class JsonResponseHelper {
	JsonModel jsonModel;

	public JsonResponseHelper(int code, Object data) {
		jsonModel = new JsonModel();
		jsonModel.setCode(code);
		jsonModel.setMessage(JsonResponseResultCodeDefine.getCodeValue(code));
		jsonModel.setData(data);
	}

	/** 返回json字符结果 */
	public String GetResponseJsonResult() {
		String result = new Gson().toJson(jsonModel);
		return result;
	}

	public JsonModel getJsonModel() {
		return jsonModel;
	}

	/** 成功 */
	public static JsonModel getSuccessJsonModel() {
		JsonModel reuslt = new JsonModel();
		reuslt.setCode(1);
		reuslt.setMessage(JsonResponseResultCodeDefine.getCodeValue(1));
		return reuslt;
	}

	

	/** 数据库执行错误 */
	public static JsonModel getDBErrorJsonModel() {
		JsonModel reuslt = new JsonModel();
		reuslt.setCode(2);
		reuslt.setMessage(JsonResponseResultCodeDefine.getCodeValue(2));
		return reuslt;
	}

	
	/** 参数错误 */
	public static JsonModel getParamErrorJsonModel() {
		JsonModel reuslt = new JsonModel();
		reuslt.setCode(3);
		reuslt.setMessage(JsonResponseResultCodeDefine.getCodeValue(3));
		return reuslt;
	}

	/** App未登录 */
	public static JsonModel getAppNoLoginJsonModel() {
		JsonModel reuslt = new JsonModel();
		reuslt.setCode(-3);
		reuslt.setMessage(JsonResponseResultCodeDefine.getCodeValue(-3));
		return reuslt;
	}
	

	/** 数据库无数据 */
	public static JsonModel getNoDataModel() {
		JsonModel reuslt = new JsonModel();
		reuslt.setCode(0);
		reuslt.setMessage(JsonResponseResultCodeDefine.getCodeValue(0));
		return reuslt;
	}
	
	/** 微信未绑定或者登录超时 */
	public static JsonModel getWeiXinNoJsonModel() {
		JsonModel reuslt = new JsonModel();
		reuslt.setCode(-1);
		reuslt.setMessage(JsonResponseResultCodeDefine.getCodeValue(-1));
		return reuslt;
	}
	
	/** 微信OpenId超时 */
	public static JsonModel getWeiXinTimeOutJsonModel() {
		JsonModel reuslt = new JsonModel();
		reuslt.setCode(-2);
		reuslt.setMessage(JsonResponseResultCodeDefine.getCodeValue(-2));
		return reuslt;
	}
	
	/** 存储过程执行返回TAG值判断 */
	public static JsonModel getProcTagValueJsonModel(String tagValue) {
		JsonModel reuslt = new JsonModel();
		if (tagValue.equals("1")) {
			reuslt.setCode(1);
			reuslt.setMessage(JsonResponseResultCodeDefine.getCodeValue(1));
		} else {
			reuslt.setCode(2);
			reuslt.setMessage(tagValue);
		}
		return reuslt;
	}
	
	/** 调用接口错误 */
	public static JsonModel getAPIErrorJsonModel() {
		JsonModel reuslt = new JsonModel();
		reuslt.setCode(4);
		reuslt.setMessage(JsonResponseResultCodeDefine.getCodeValue(4));
		return reuslt;
	}
	
	
	/** 微信未选城市 */
	public static JsonModel getWeiXinNoCityJsonModel() {
		JsonModel reuslt = new JsonModel();
		reuslt.setCode(-4);
		reuslt.setMessage(JsonResponseResultCodeDefine.getCodeValue(-4));
		return reuslt;
	}
	
	/** 调用接口结果错误 */
	public static JsonModel getAPIResultErrorJsonModel(String msg) {
		JsonModel reuslt = new JsonModel();
		reuslt.setCode(5);
		reuslt.setMessage(msg);
		return reuslt;
	}
	
	
	
}
