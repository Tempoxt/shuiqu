package com.fun.dao.impl;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fun.bean.vendor.VendorProduct;
import com.fun.dao.IVendorProductDao;
@Repository
public class VendorProductDaoImpl implements IVendorProductDao {

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public VendorProduct selectVendorProductById(Integer id) {
		String sql = "select id,eid,productName,price,picture,imageText,isLine,createTime,createUser from vendor_product where id=?";
		
		VendorProduct query = jdbcTemplate.queryForObject(sql,new Object[]{id}, new BeanPropertyRowMapper<>(VendorProduct.class));
		return query;
	}

}
