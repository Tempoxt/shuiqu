package com.eviano2o.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eviano2o.bean.weixin.QuickShoppingListModel;
import com.eviano2o.bean.weixin.ShopTimeframeModel;

/** 水店时间段选择 */
public class ShopBuyTimeUtil {

	QuickShoppingListModel _shop;
	
	/** 水店时间段选择 */
	public ShopBuyTimeUtil(QuickShoppingListModel shop){
		_shop = shop;
		if(_shop.getSendOnTime().intValue() <=0 || shop.getTimeframes() == null || shop.getTimeframes().size() == 0)
			return;
		
		intSysTimeStamps();
		//intTimeStamps();
	}
	
	/** 后台返回时间段选择 */
	private void intSysTimeStamps(){
		_shop.setTimeStampSuffix(0);//从第几个时间段下标读取,0代表第二天
		//当前时间 + 平均送达分钟（预约时间）
		Date curBuyTime = DateUtil.strToDateLong(DateUtil.addMinutes(DateUtil.getStringDate(), _shop.getSendOnTime()));
		int i = 1;
		List<String> timeStamps = new ArrayList<String>();
		for(ShopTimeframeModel stamp : _shop.getTimeframes()){
			timeStamps.add(stamp.getBeginHour() + "-" + stamp.getEndHour());
			//System.out.println(curBuyTime + "     " + DateUtil.getStringDateShort() + " " + stamp.getEndHour() + ":00");
			if(_shop.getTimeStampSuffix().intValue() == 0 && curBuyTime.before(DateUtil.strToDateLong(DateUtil.getStringDateShort() + " " + stamp.getEndHour() + ":00")))
				_shop.setTimeStampSuffix(i);
			i++;
		}
		_shop.setTimeStamps(timeStamps);
	}
	
	/** 时间段计算,原来根据上下班时间和送达时间计算，暂停使用 */
	private void intTimeStamps(){
		_shop.setTimeStampSuffix(0);
		//时间段结束时间，比如：10:00 - 11:00 为 2011-01-01 11:00 初始化为上班时间
		Date timeStampEnd = DateUtil.strToDateLong(DateUtil.getStringDateShort() + " " + _shop.getStartTime() + ":00");
		//今天下班时间
		Date workOffTime = DateUtil.strToDateLong(DateUtil.getStringDateShort() + " " + _shop.getEndTime() + ":00");
		//时间段
		List<String> timeStamps = new ArrayList<String>();
		Integer i = 1;
		while (timeStampEnd.before(workOffTime)){
			String endStamp = DateUtil.addMinutes(DateUtil.dateToStrLong(timeStampEnd), _shop.getSendOnTime());
			
			timeStamps.add(DateUtil.getTimeHourAndMinute(timeStampEnd) + "-" + DateUtil.getTimeHourAndMinute(DateUtil.strToDateLong(endStamp).before(workOffTime) ? DateUtil.strToDateLong(endStamp) : workOffTime));
			//System.out.println(DateUtil.getTimeHourAndMinute(timeStampEnd) + "-" + DateUtil.getTimeHourAndMinute(DateUtil.strToDateLong(endStamp)) + " | " + _shop.getTimeStampSuffix() +"  |  "+ endStamp);
			
			timeStampEnd = DateUtil.strToDateLong(endStamp);
			
			if(_shop.getTimeStampSuffix().intValue() == 0 && DateUtil.getNow().before(timeStampEnd))
				_shop.setTimeStampSuffix(i);
			i++;
		}
		_shop.setTimeStamps(timeStamps);
	}
	
}
