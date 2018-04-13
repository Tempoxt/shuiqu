package com.eviano2o.controller.helper;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.eviano2o.util.SessionConstantDefine;

public class AppLogoutHelper extends BaseControllerHelper {
	
	public AppLogoutHelper(Model model, HttpServletRequest request, HttpServletResponse response) {
		super(model, request, response);
	}
	
	@Override
	public void Init() {
		_request.getSession().removeAttribute(SessionConstantDefine.CLIENT_INFO);
		Enumeration em = _request.getSession().getAttributeNames();
		  while(em.hasMoreElements()){
		   _request.getSession().removeAttribute(em.nextElement().toString());
		}
	}
	
	@Override
	public String getResult() {
		return _result;
	}
}
