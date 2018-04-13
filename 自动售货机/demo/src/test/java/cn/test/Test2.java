package cn.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Test2 {

	public static void main(String[] args) {
		Map<String, Object> sss = sss();
		System.out.println(sss);
	}

	public static <T> T sss(){
		
		Map<String, Object> a = new HashMap<String, Object>();
		a.put("1", 1);
		return (T) a;
	}

}
