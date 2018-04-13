package com.eviano2o.util;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**数据库ResultSet反射List类*/
public class ResultSetToBeanHelper {

	
	@SuppressWarnings("unchecked")
	public static List resultSetToList(ResultSet rs, Class cls) throws Exception {

		//
		Method[] methods = cls.getDeclaredMethods();
		System.out.println(methods[0].getName());
		List lst = new ArrayList();
		// 
		ResultSetMetaData meta = rs.getMetaData();
		Object obj = null;
		while (rs.next()) {
			obj = cls.newInstance(); 
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				String colName = meta.getColumnName(i);
				String setMethodName = "set" + colName;

				for (int j = 0; j < methods.length; j++) {
					if (methods[j].getName().equalsIgnoreCase(setMethodName)) {
						setMethodName = methods[j].getName();
						Object value = rs.getObject(colName);
						if (value == null) {
							continue;
						}
						try {
							Method setMethod = obj.getClass().getMethod(setMethodName, value.getClass());
							setMethod.invoke(obj, value);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			lst.add(obj);
		}

		return lst;

	}
}
