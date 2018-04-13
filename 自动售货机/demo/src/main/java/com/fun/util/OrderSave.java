package com.fun.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderSave {

	
	public static String getOrderNew(){
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());  
        String seconds = new SimpleDateFormat("HHmmss").format(new Date());
        return date+"00001000"+getTwo()+"00"+seconds+getTwo();
	}
	
	public static String getTwo(){  
        Random rad=new Random();  
  
        String result  = rad.nextInt(100) +"";  
  
        if(result.length()==1){  
            result = "0" + result;  
        }  
        return result; 
    } 
}
