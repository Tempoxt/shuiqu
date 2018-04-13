package com.fun.controller;

import javax.servlet.http.HttpServletRequest;

import com.fun.bean.vendor.VendorMainboard;

public class BaseController {

	
	public String getWxOpenId(HttpServletRequest request){
		return (String)request.getSession().getAttribute("openid");
	}
	
	public String getAlipayUserId(HttpServletRequest request){
		return (String)request.getSession().getAttribute("userid");
	}
	
	public Integer getVendorBmId(HttpServletRequest request){
		return (Integer)request.getSession().getAttribute("bmId");
	}
	
	public String getUserId(HttpServletRequest request){
		String header = request.getHeader("user-agent");
		if(header.contains("AlipayClient")){
			return getAlipayUserId(request);
		}else if(header.contains("MicroMessenger")){
			return getWxOpenId(request);
		}else{
			return null;
		}
	}
	
	public Integer getEid(HttpServletRequest request){
		if(request.getSession().getAttribute("vendorMainboard")!=null){
			VendorMainboard vm =(VendorMainboard) request.getSession().getAttribute("vendorMainboard");
			return vm.getEid();
		}else{
			return null;
		}
	}
	
	public String getTheme(HttpServletRequest request){
		if(request.getSession().getAttribute("themeColor")!=null){
			String themeColor = (String) request.getSession().getAttribute("themeColor");
			return themeColor;
		}else{
			return null;
		}
	}
}
