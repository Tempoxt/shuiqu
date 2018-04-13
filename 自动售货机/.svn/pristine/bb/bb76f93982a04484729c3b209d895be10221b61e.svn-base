package com.fun.dao.impl;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fun.bean.vendor.VendorPlatformAuthorization;
import com.fun.dao.IVendorPlatformAuthorizationDao;
@Repository
public class VendorPlatformAuthorizationDaoImpl implements
		IVendorPlatformAuthorizationDao {

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Integer insertAuthorization(
			VendorPlatformAuthorization va) {
		// TODO Auto-generated method stub
		String sql = "insert into vendor_platform_authorization(openId,nickName,photo,[platform]) values(?,?,?,?)";
		int update = jdbcTemplate.update(sql, new Object[]{va.getOpenId(),va.getNickName(),va.getPhoto(),va.getPlatform()});
		return update;
	}

	@Override
	public Integer updateAuthorization(
			VendorPlatformAuthorization va) {
		// TODO Auto-generated method stub
		String sql = "update vendor_platform_authorization set nickName=?,photo=?,[platform]=? where openId=?";
		int update = jdbcTemplate.update(sql, new Object[]{va.getNickName(),va.getPhoto(),va.getPlatform(),va.getOpenId()});
		return update;
	}

	@Override
	public VendorPlatformAuthorization selectAuthorization(
			String openId) {
		// TODO Auto-generated method stub
		String sql="select * from vendor_platform_authorization where openId=?";
		try {
			VendorPlatformAuthorization queryForObject = jdbcTemplate.queryForObject(sql,new Object[]{openId}, new BeanPropertyRowMapper<>(VendorPlatformAuthorization.class));
			return queryForObject;
		} catch (DataAccessException e) {
		}
		return null;
	}

}
