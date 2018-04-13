package com.fun.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fun.bean.vendor.VendorMainboard;
import com.fun.dao.IVendorMainboardDao;
import com.fun.util.ResultSetToBeanHelper;
@Repository
public class VendorMainboardImpl implements IVendorMainboardDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings({ "unchecked", "rawtypes" }) 
	@Override
	public List<VendorMainboard> selectMainboardBymainboardNoMD5(final String mainboardNoMD5) {
		List<VendorMainboard> result = jdbcTemplate.execute(
				"{call Proc_Backstage_vendor_mainboard_select(?,?,?,?,?,?,?,?,?,?,?)}",
				new CallableStatementCallback() {
					public Object doInCallableStatement(CallableStatement cs)
							throws SQLException {
						cs.setObject("beginTime", null);
						cs.setObject("endTime", null);
						cs.setObject("eName", null);
						cs.setObject("shopName", null);
						cs.setObject("eid", null);
						cs.setObject("mainboardNo", null);
						cs.setObject("mainboardNoMD5", mainboardNoMD5);
						cs.setObject("PageIndex", null);
						cs.setObject("PageSize", null);
						cs.setObject("IsSelectAll", null);
						cs.registerOutParameter("Count", Types.NVARCHAR);// 注册输出参数的类型
						cs.execute();
						ResultSet rs = cs.executeQuery();
						try {
							List<VendorMainboard> result = ResultSetToBeanHelper.resultSetToList(rs, VendorMainboard.class);
							return result;
						} catch (Exception e) {
							return null;
						}
					}
				});
		return result;
		}
	
	

}
