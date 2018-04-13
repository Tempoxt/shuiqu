package com.fun.service;

import java.util.List;
import java.util.Map;

import com.fun.bean.vendor.VendorOrder;

public interface IVendorOrderService {

	public Map<String, Object> addOrder(String openId,Integer orderSource,String mainboardNoMD5,Integer doorIndex,Integer productId,Double realityPrice,Double discountPrice,String discountName);
	
	public VendorOrder findOrderByOrderId(Integer orderId);
	
	public List<VendorOrder> findOrderByUserId(String userId,Integer page,Integer count);
}
