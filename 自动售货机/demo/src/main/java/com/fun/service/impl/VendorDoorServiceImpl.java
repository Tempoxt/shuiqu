package com.fun.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fun.bean.vendor.VendorDoor;
import com.fun.bean.vendor.VendorProductModel;
import com.fun.dao.IVendorDoorDao;
import com.fun.service.IVendorDoorService;
@Service
public class VendorDoorServiceImpl implements IVendorDoorService {

	@Autowired
	IVendorDoorDao doorDao;
	
	@Override
	public List<VendorDoor> findDoorByContainer(Integer bmId,Integer vmId) {
		List<VendorDoor> vendorDoors = doorDao.selectVendorDoorByContainer(bmId,vmId);
		return vendorDoors;
	}

	@Override
	public VendorDoor findDoorByContainerOrDoor(Integer bmId, Integer mcId,
			Integer doorIndex) {
		return doorDao.selectDoorByContainerOrDoor(bmId, mcId, doorIndex);
	}

	@Override
	public Map<String, Object> findDoorProductByContainer(Integer bmId,Integer mcId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<VendorProductModel> selectMcIdByBmId = doorDao.selectMcIdByBmId(bmId);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (VendorProductModel vendorProductModel : selectMcIdByBmId) {
			Map<String , Object> map2 =new HashMap<String, Object>();
			map2.put("mcId", vendorProductModel.getMcId());
			map2.put("vendorDoorProucts", doorDao.selectDoorProductByContainer(bmId, vendorProductModel.getMcId()));
			list.add(map2);
		}
		map.put("vendorMainboardContainers", list);
		
		return map;
	}

}
