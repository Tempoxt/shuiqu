package com.fun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fun.bean.vendor.VendorProduct;
import com.fun.dao.IVendorProductDao;
import com.fun.service.IVendorProductService;
@Service
public class VendorProductServiceImpl implements IVendorProductService {

	@Autowired
	IVendorProductDao productDao;
	
	@Override
	public VendorProduct findProductById(Integer id) {
		VendorProduct vendorProduct = productDao.selectVendorProductById(id);
		return vendorProduct; 
	}

}
