package com.fun.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fun.bean.vendor.VendorMainboard;
import com.fun.service.IVendorMainboardService;

@Controller
public class GainInfoController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IVendorMainboardService mainboardService;
	
	
	@RequestMapping("/{param}")
	public String getInfo(@PathVariable(value = "param") String param,HttpServletRequest request){
		List<VendorMainboard> list = mainboardService.selectMainboardBymainboardNoMD5(param);
		if(list.size()>0){
			request.getSession().setAttribute("vendorMainboard", list.get(0));
			request.getSession().setAttribute("bmId", list.get(0).getBmId());
			request.getSession().setAttribute("mainboardNoMD5", param);
			return "redirect:/funVm/index";
		}else{
			logger.error("param :"+param);
			return null;
		}
	}
}
