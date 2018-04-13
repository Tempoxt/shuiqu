package com.fun.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fun.bean.vendor.VendorOrder;
import com.fun.dao.IVendorOrderDao;
import com.fun.service.IVendorOrderService;
@Service
public class VendorOrderServiceImpl implements IVendorOrderService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IVendorOrderDao orderDao;
	
	@Override
	public Map<String, Object> addOrder(String openId,Integer orderSource,String mainboardNoMD5, Integer doorIndex,
			Integer productId, Double realityPrice, Double discountPrice,
			String discountName) {
		logger.info("[openId:{}] [orderSource:{}] [mainboardNoMD5:{}] [doorIndex:{}] [productId:{}] [realityPrice:{}] [discountPrice:{}] [discountName:{}]",
				new Object[] {openId,orderSource, mainboardNoMD5, doorIndex, productId, realityPrice ,discountPrice,discountName});
		return orderDao.insertOrder(openId,orderSource,mainboardNoMD5, doorIndex, productId, realityPrice, discountPrice, discountName);
	}

	@Override
	public VendorOrder findOrderByOrderId(Integer orderId) {
		// TODO Auto-generated method stub
		return orderDao.selectOrderByOrderId(orderId);
	}

	@Override
	public List<VendorOrder> findOrderByUserId(String userId, Integer page,
			Integer count) {
		
		Integer action = (page-1)*count+1;
		Integer out = page*count;
		return orderDao.selectOrderByUserId(userId, action, out);
	}

}
