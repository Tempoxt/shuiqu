package com.fun.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fun.bean.vendor.VendorMainboard;
import com.fun.dao.IVendorMainboardDao;

public interface IVendorMainboardService {

	public List<VendorMainboard> selectMainboardBymainboardNoMD5(String mainboardNoMD5);
}
