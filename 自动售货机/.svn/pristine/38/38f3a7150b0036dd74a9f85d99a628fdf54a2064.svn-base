package com.fun.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fun.dao.IVendorContainerDao;
import com.fun.bean.vendor.VendorContainer;

@Repository
public class VendorContainerImpl implements IVendorContainerDao {

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public com.fun.bean.vendor.VendorContainer selectVendorContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VendorContainer> selectVendorContainerIdByMainboard(Integer mainboardId) {
		String sql = "select vmId,bmId,doorNumber,vmIndex,dateCreated,createdUser from vendor_container where bmId=?";
		
		List<VendorContainer> query = jdbcTemplate.query(sql,new Object[]{mainboardId}, new BeanPropertyRowMapper<>(VendorContainer.class));
		return query;
	}

}
