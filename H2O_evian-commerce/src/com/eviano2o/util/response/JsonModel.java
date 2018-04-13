package com.eviano2o.util.response;

import com.google.gson.Gson;


/**非页面返回路径统一返回json响应信息*/
public class JsonModel {
	int code = 0;

	String message = "";

	Object data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	//转换为json
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
}
