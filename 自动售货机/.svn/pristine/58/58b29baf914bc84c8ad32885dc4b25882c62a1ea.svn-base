package com.fun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fun.bean.vendor.VendorTheme;
import com.fun.dao.IVendorThemeDao;
import com.fun.service.IVendorThemeService;
@Service
public class VendorThemeServiceImpl implements IVendorThemeService {

	@Autowired
	IVendorThemeDao themeDao;
	
	@Override
	public VendorTheme findThemeByEid(Integer eid) {
		// TODO Auto-generated method stub
		return themeDao.selectThemeByEid(eid);
	}

}
