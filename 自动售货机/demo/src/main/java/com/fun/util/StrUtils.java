package com.fun.util;

import org.apache.commons.lang3.StringUtils;

public class StrUtils {

	public static boolean isEmOrUn(CharSequence cs){
		if(StringUtils.isEmpty(cs)||"undefined".equals(cs)){
			return true;
		}
		return false;
	}
}
