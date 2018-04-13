package com.fun.dao;

import com.fun.bean.vendor.VendorPlatformAuthorization;

public interface IVendorPlatformAuthorizationDao {

	
	public Integer insertAuthorization(VendorPlatformAuthorization va);
	
	public Integer updateAuthorization(VendorPlatformAuthorization va);
	
	public VendorPlatformAuthorization selectAuthorization(String openId);
	
}
