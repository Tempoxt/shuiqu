package com.fun.dao.impl;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fun.bean.vendor.VendorTheme;
import com.fun.dao.IVendorThemeDao;

@Repository
public class VendorThemeDaoImpl implements IVendorThemeDao {

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public VendorTheme selectThemeByEid(Integer eid) {
		String sql = "select * from vendor_style_enterprise where eid=?";
		
		VendorTheme query = jdbcTemplate.queryForObject(sql,new Object[]{eid}, new BeanPropertyRowMapper<>(VendorTheme.class));
		return query;
	}

}
