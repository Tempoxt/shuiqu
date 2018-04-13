package com.eviano2o.controller.helper;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public class PublicHelper extends BaseControllerHelper {
	
	public PublicHelper(Model model, HttpServletRequest request) {
		super(model, request);
	}
	
	@Override
	public void Init() {
		
	}
	
	@Override
	public String getResult() {
		return _result;
	}
}
