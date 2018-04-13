package com.fun.service;

import java.util.List;
import java.util.Map;

import com.fun.bean.vendor.VendorDoor;
import com.fun.bean.vendor.VendorProductModel;

public interface IVendorDoorService {

	public List<VendorDoor> findDoorByContainer(Integer bmId,Integer vmId);
	
	public VendorDoor findDoorByContainerOrDoor(Integer bmId,Integer mcId,Integer doorIndex);
	
	/**
	 * 根据主板和货柜查询柜门商品
	 * @param bmId
	 * @param mcId
	 * @return
	 */
	public Map<String, Object> findDoorProductByContainer(Integer bmId,Integer mcId);
}
