package com.fun.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fun.bean.vendor.VendorContainer;
import com.fun.bean.vendor.VendorDoor;
import com.fun.bean.vendor.VendorProduct;
import com.fun.dao.IVendorContainerDao;
import com.fun.dao.IVendorDoorDao;
import com.fun.dao.IVendorProductDao;
import com.fun.service.IVendorContainerService;

@Service
public class VendorContainerServiceImpl implements IVendorContainerService{

	@Autowired
	IVendorContainerDao containerDao;
	
	@Autowired
	IVendorDoorDao doorDao;
	
	@Autowired
	IVendorProductDao productDao;

	@Override
	public List<VendorContainer> findContainerIdByMainboard(Integer bmId) {
		List<VendorContainer> VendorContainers = containerDao.selectVendorContainerIdByMainboard(bmId);
		return VendorContainers;
	}
	
	public Map<String, Object> findContainersInfo(Integer bmId){
		
		// 获取货柜信息
		List<VendorContainer> c = containerDao.selectVendorContainerIdByMainboard(bmId);
		
		Map<String, Object> containerMap = new HashMap<String, Object>();
		if(c!=null){
			containerMap.put("containerCount", c.size());
			if(c.size()>0){
				List<Map<String, Object>> containerList = new ArrayList<Map<String,Object>>();
				for (VendorContainer container : c) {
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("vmId", container.getVmId());
					map2.put("doorNumber", container.getDoorNumber());
					// 获取柜门信息
					if(container!=null&&container.getVmId()!=null){
						List<VendorDoor> d = doorDao.selectVendorDoorByContainer(bmId,container.getVmId());
						if(d!=null){
							if(d.size()>0){
								List<Map<String, Object>> doorList = new ArrayList<Map<String,Object>>();
								for (VendorDoor door : d) {
									Map<String, Object> map3 = new HashMap<String, Object>();
									map3.put("doorIndex", door.getDoorIndex()); 
									map3.put("productId", door.getProductId());
									map3.put("eid", door.getProductId());
									map3.put("productState", door.getProductState());
									map3.put("doorState", door.getDoorState());
									if(door.getProductState()==1){
										
										
										Map<String, Object> map4 = new HashMap<String, Object>();
										// 获取商品信息详情
										VendorProduct product = productDao.selectVendorProductById(door.getProductId());
										map4.put("pName", product.getProductName());
										map4.put("price", product.getPrice());
										map4.put("picture", product.getPicture());
										map4.put("imageText", product.getImageText());
										map4.put("isLine", product.getIsLine());
										map3.put("productInfo", map4);
									}
									doorList.add(map3);
								}
								map2.put("doorInfo", doorList);
								
							}
						}
					}
					
					
					containerList.add(map2);
				}
				containerMap.put("containerIds", containerList);
			}
			
		}else{
			
			containerMap.put("containerCount", 0);
		}
		
		return containerMap;
	}
}
