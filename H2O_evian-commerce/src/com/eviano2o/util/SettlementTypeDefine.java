package com.eviano2o.util;

import java.util.HashMap;
import java.util.Map;

public class SettlementTypeDefine {
	static Map<Integer, String> settlementTypeDefine = new HashMap<Integer, String>();
	
	static{
		settlementTypeDefine.put(1, "现金");
		settlementTypeDefine.put(2, "电子票");
		settlementTypeDefine.put(3, "水票");
//		settlementTypeDefine.put(4, "押桶");
	}
	
	public static Map<Integer, String> getDefine(){
		return settlementTypeDefine;
	}
}
