package com.eviano2o.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class userInputDataUtil {
	
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^1\\d{10}$");
		Matcher m = p.matcher(mobiles);
		System.out.println(m.matches() + "---");
		return m.matches();
	}
}
