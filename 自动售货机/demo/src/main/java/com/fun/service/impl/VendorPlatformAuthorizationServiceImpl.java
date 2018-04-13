package com.fun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fun.bean.vendor.VendorPlatformAuthorization;
import com.fun.dao.IVendorPlatformAuthorizationDao;
import com.fun.service.IVendorPlatformAuthorizationService;
@Service
public class VendorPlatformAuthorizationServiceImpl implements
		IVendorPlatformAuthorizationService {
	
	@Autowired
	IVendorPlatformAuthorizationDao vendorPlatformAuthorizationDao;
	
	@Override
	public Integer addAuthorization(VendorPlatformAuthorization va) {
		// TODO Auto-generated method stub
		return vendorPlatformAuthorizationDao.insertAuthorization(va);
	}

	@Override
	public Integer upAuthorization(VendorPlatformAuthorization va) {
		// TODO Auto-generated method stub
		return vendorPlatformAuthorizationDao.updateAuthorization(va);
	}

	@Override
	public VendorPlatformAuthorization findAuthorizationByOpenId(String openId) {
		// TODO Auto-generated method stub
		return vendorPlatformAuthorizationDao.selectAuthorization(openId);
	}

}
