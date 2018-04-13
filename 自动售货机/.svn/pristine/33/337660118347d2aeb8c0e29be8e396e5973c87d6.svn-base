package com.fun.dao.impl;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.fun.bean.vendor.VendorDoor;
import com.fun.bean.vendor.VendorMainboard;
import com.fun.bean.vendor.VendorProductModel;
import com.fun.dao.IVendorDoorDao;
import com.fun.util.ResultSetToBeanHelper;
@Repository
public class VendorDoorDaoImpl implements IVendorDoorDao {

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<VendorDoor> selectVendorDoorByContainer(Integer bmId,Integer vmId) {
		String sql = "select bmId,doorIndex,vmId,productId,dateCreated,createdUser,eid,productState,doorState from vendor_door where bmId=? and vmId=?";
		
		List<VendorDoor> query = jdbcTemplate.query(sql,new Object[]{bmId,vmId}, new BeanPropertyRowMapper<>(VendorDoor.class));
		return query;
	}

	@Override
	public VendorDoor selectDoorByContainerOrDoor(Integer bmId, Integer mcId,Integer doorIndex) {
		String sql = "select bmId,doorIndex,mcId,productId,dateCreated,createdUser,eid,productState,doorState,alias,isValid from vendor_door where bmId=? and mcId=? and doorIndex=?";
		
		try {
			VendorDoor query = jdbcTemplate.queryForObject(sql,new Object[]{bmId,mcId,doorIndex}, new BeanPropertyRowMapper<>(VendorDoor.class));
			return query;
		} catch (EmptyResultDataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<VendorProductModel> selectDoorProductByContainer(final Integer bmId, final Integer mcId) {
		List<VendorProductModel> result = jdbcTemplate.execute(
				"{call Proc_Backstage_vendor_door_select(?,?)}",
				new CallableStatementCallback() {
					public Object doInCallableStatement(CallableStatement cs)
							throws SQLException {
						cs.setObject("bmId", bmId);
						cs.setObject("mcId", mcId);
						cs.execute();
						ResultSet rs = cs.executeQuery();
						try {
							List<VendorProductModel> result = ResultSetToBeanHelper.resultSetToList(rs, VendorProductModel.class);
							return result;
						} catch (Exception e) {
							return null;
						}
					}
				});
		return result;
	}

	@Override 
	public List<VendorProductModel> selectMcIdByBmId(final Integer bmId) {
		String sql = "select mcId from vendor_door where bmId=? group by mcId";
		
		List<VendorProductModel> query = jdbcTemplate.query(sql,new Object[]{bmId}, new BeanPropertyRowMapper<>(VendorProductModel.class));
		return query;
	}
	
}
