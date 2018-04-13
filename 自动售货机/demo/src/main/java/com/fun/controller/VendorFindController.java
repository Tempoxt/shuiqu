package com.fun.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fun.bean.vendor.VendorOrder;
import com.fun.bean.vendor.VendorPlatformAuthorization;
import com.fun.service.IVendorContainerService;
import com.fun.service.IVendorDoorService;
import com.fun.service.IVendorOrderService;
import com.fun.service.IVendorPlatformAuthorizationService;
import com.fun.util.CallBackPar;

@RestController
@RequestMapping("/find")
public class VendorFindController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IVendorContainerService containerService;
	
	@Autowired
	IVendorDoorService doorService;
	
	@Autowired
	IVendorOrderService orderService;
	
	@Autowired
	IVendorPlatformAuthorizationService vendorPlatformAuthorizationService;
	
	@RequestMapping("/findContainers")
	public Map<String, Object> findContainers(HttpServletRequest request){
		Map<String, Object> parMap = CallBackPar.getParMap();
		try {
//			Map<String, Object> findDoorProductByContainer = doorService.findDoorProductByContainer(getVendorBmId(request), null);
			Map<String, Object> findDoorProductByContainer = doorService.findDoorProductByContainer(1, null);
			parMap.put("data", findDoorProductByContainer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("e : "+e);
			parMap.put("code", "E00001");
			parMap.put("message", "系统异常");
		}
		return parMap;
	}
	
	@RequestMapping("/findMyOrders")
	public Map<String, Object> findMyOrders(HttpServletRequest request,Integer page ,Integer count,Integer test){
		Map<String, Object> parMap = CallBackPar.getParMap();
		List<VendorOrder> findOrderByUserId = orderService.findOrderByUserId(getUserId(request), page, count);
		if(test!=null&&test==666){
			findOrderByUserId=orderService.findOrderByUserId("oGLfUwE5ZHCB_d5tWdXaxZ-MOq_k", page, count);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orders", findOrderByUserId);
		map.put("page", page);
		parMap.put("data", map);
		return parMap;
	}
	
	@RequestMapping("/findUserInfo")
	public Map<String, Object> findUserInfo(HttpServletRequest request,Integer test){
		Map<String, Object> parMap = CallBackPar.getParMap();
		VendorPlatformAuthorization va=null;
		if(test!=null&&test==666){
			va= vendorPlatformAuthorizationService.findAuthorizationByOpenId("oGLfUwJGKEJgqCQmcX4JxZMqhqFg");
		}else{
			va = vendorPlatformAuthorizationService.findAuthorizationByOpenId(getUserId(request));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Long day = null;
		try {
			Date date = sdf.parse(va.getDateCreated());
			day=(new Date().getTime()-date.getTime())/(24*60*60*1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("[时间转换错误:{}] [时间:{}]:{}",new Object[]{"yyyy-MM-dd",va.getDateCreated()});
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userInfo", va);
		map.put("day", day);
		parMap.put("data", map);
		return parMap;
	}
}
