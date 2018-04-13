package com.fun.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fun.bean.vendor.VendorMainboard;
import com.fun.dao.IVendorMainboardDao;
import com.fun.service.IVendorMainboardService;
@Service
public class VendorMainboardServiceImpl implements IVendorMainboardService {

	@Autowired
	IVendorMainboardDao mainboardDao;
	
	@Override
	public List<VendorMainboard> selectMainboardBymainboardNoMD5(
			String mainboardNoMD5) {
		List<VendorMainboard> VendorMainboardlist = mainboardDao.selectMainboardBymainboardNoMD5(mainboardNoMD5);
		return VendorMainboardlist;
	}

}
