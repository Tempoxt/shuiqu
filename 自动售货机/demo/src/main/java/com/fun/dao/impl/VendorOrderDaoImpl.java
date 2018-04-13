package com.fun.dao.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fun.bean.vendor.VendorOrder;
import com.fun.bean.vendor.VendorProduct;
import com.fun.dao.IVendorOrderDao;
import com.fun.util.ResultSetToBeanHelper;
@Repository
public class VendorOrderDaoImpl implements IVendorOrderDao{
	
	@Resource
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings({ "unchecked", "rawtypes" }) 
	@Override
	public Map<String,Object> insertOrder(final String openId,final Integer orderSource,final String mainboardNoMD5,final Integer doorIndex,final Integer productId,final Double realityPrice,final Double discountPrice,
			final String discountName) {
		Object result = jdbcTemplate.execute(
				"{call Proc_DisPark_Vendor_Save_Order(?,?,?,?,?,?,?,?,?,?,?)}",
				new CallableStatementCallback() {
					public Object doInCallableStatement(CallableStatement cs)
							throws SQLException {
						cs.setObject("openId", openId);
						cs.setObject("orderSource", orderSource);
						cs.setObject("mainboardNoMD5", mainboardNoMD5);
						cs.setObject("doorIndex", doorIndex);
						cs.setObject("productId", productId);
						cs.setObject("realityPrice", realityPrice);
						cs.setObject("discountPrice", discountPrice);
						cs.setObject("discountName", discountName);
						cs.registerOutParameter("orderId", Types.INTEGER);// 注册输出参数的类型
						cs.registerOutParameter("orderNo", Types.NVARCHAR);// 注册输出参数的类型
						cs.registerOutParameter("TAG", Types.NVARCHAR);// 注册输出参数的类型
						cs.execute();
						
						Map<String,Object> resultMap=new HashMap<String,Object>();
						resultMap.put("TAG", cs.getString("TAG"));
						resultMap.put("orderId", cs.getInt("orderId"));
						resultMap.put("orderNo", cs.getString("orderNo"));
						return  resultMap;
					}
				});
		return (Map<String,Object>)result;
	}

	@Override
	public Map<String, Object> selectOrderByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VendorOrder selectOrderByOrderId(Integer orderId) {
		String sql = "select * from vendor_order where orderId=?";
		
		try {
			VendorOrder query = jdbcTemplate.queryForObject(sql,new Object[]{orderId}, new BeanPropertyRowMapper<>(VendorOrder.class));
			
			return query;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	@Override
	public List<VendorOrder> selectOrderByUserId(String userId,Integer action,Integer out) {
		String sql = "";
		System.out.println("action = "+action +" out = "+out);
		if(action==null||out==null){
			sql = "select * from vendor_order where openId=?";
		}else{
			sql ="select * from ( select *, ROW_NUMBER() OVER(Order by dateCreated desc) AS RowId from vendor_order where openId=?) as b where RowId between ? and ? ";
		}
		try {
			List<VendorOrder> query = jdbcTemplate.query(sql,new Object[]{userId,action,out}, new BeanPropertyRowMapper<>(VendorOrder.class));
			
			return query;
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> selectTest(String userId, Integer action,
			Integer out) {
		String sql = "select * from vendor_order where openId=?";
		List<Map<String, Object>> query = jdbcTemplate.query(sql, new Object[]{userId},ResultSetToBeanHelper.resultSetToListMap());
		return query;
	}

}
