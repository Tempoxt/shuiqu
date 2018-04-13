package com.fun.dao;

import java.util.List;
import java.util.Map;

import com.fun.bean.vendor.VendorOrder;

public interface IVendorOrderDao {

	public Map<String,Object> insertOrder(String openId,Integer orderSource,String mainboardNoMD5,Integer doorIndex,Integer productId,Double realityPrice,Double discountPrice,String discountName);
	
	public Map<String, Object> selectOrderByOrderNo(String orderNo);
	
	public VendorOrder selectOrderByOrderId(Integer orderId);
	
	public List<VendorOrder> selectOrderByUserId(String userId,Integer action,Integer out);
	
	public List<Map<String, Object>> selectTest(String userId,Integer action,Integer out);
}
