package com.fun.util;

import java.util.UUID;

public class Create3rd_Session {

	
	public static String get3rdSession(){
		UUID uuid=UUID.randomUUID();
	    String str = uuid.toString(); 
	    String uuidStr=str.replace("-", "");
	    return uuidStr;
	}
}
