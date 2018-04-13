package com.fun.dao;

import java.util.List;
import java.util.Map;

import com.fun.bean.vendor.VendorDoor;
import com.fun.bean.vendor.VendorProductModel;

public interface IVendorDoorDao {

	public List<VendorDoor> selectVendorDoorByContainer(Integer bmId,Integer vmId);
	
	public VendorDoor selectDoorByContainerOrDoor(Integer bmId,Integer mcId,Integer doorIndex);
	
	/**
	 * 根据主板和货柜查询柜门商品
	 * @param bmId
	 * @param mcId
	 * @return
	 */
	public List<VendorProductModel> selectDoorProductByContainer(Integer bmId,Integer mcId);
	
	public List<VendorProductModel> selectMcIdByBmId(Integer bmId);
	
}
