package com.eviano2o.util;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

//��ݿ����ʵ�巴����
public class DBToBeanHelper {

	// @SuppressWarnings
	@SuppressWarnings("unchecked")
	public static List resultSetToList(ResultSet rs, Class cls) throws Exception {

		// ȡ��Method
		Method[] methods = cls.getDeclaredMethods();
		System.out.println(methods[0].getName());
		List lst = new ArrayList();
		// ���ڻ�ȡ�������������
		ResultSetMetaData meta = rs.getMetaData();
		Object obj = null;
		while (rs.next()) {
			// ��ȡformbeanʵ�����
			obj = cls.newInstance(); // ��Class.forName����ʵ������new����ʵ��������кܴ����ģ���Ҫ��JVM���ȴ���������в����࣬Ȼ����ʵ�������ִ�����еľ�̬��������new�������½�һ������ʵ��
			// ѭ����ȡָ���е�ÿһ�е���Ϣ
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				// ��ǰ����
				String colName = meta.getColumnName(i);

				// ���÷�����
				String setMethodName = "set" + colName;

				// ����Method
				for (int j = 0; j < methods.length; j++) {
					if (methods[j].getName().equalsIgnoreCase(setMethodName)) {
						setMethodName = methods[j].getName();

						// System.out.println(setMethodName);
						// ��ȡ��ǰλ�õ�ֵ������Object����
						Object value = rs.getObject(colName);
						if (value == null) {
							continue;
						}

						// ʵ��Set����
						try {
							// // ���÷����ȡ����
							// JavaBean�ڲ����Ժ�ResultSet��һ��ʱ��
							Method setMethod = obj.getClass().getMethod(setMethodName, value.getClass());
							setMethod.invoke(obj, value);
						} catch (Exception e) {
							// JavaBean�ڲ����Ժ�ResultSet�в�һ��ʱ��ʹ��String������ֵ��
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
