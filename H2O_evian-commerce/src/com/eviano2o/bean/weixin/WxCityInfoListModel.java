package com.eviano2o.bean.weixin;

import java.util.List;

public class WxCityInfoListModel {
	List<WxCityInfoModel> hotCitys;
	List<WxCityInfoModel> allCitys;
	public List<WxCityInfoModel> getHotCitys() {
		return hotCitys;
	}
	public void setHotCitys(List<WxCityInfoModel> hotCitys) {
		this.hotCitys = hotCitys;
	}
	public List<WxCityInfoModel> getAllCitys() {
		return allCitys;
	}
	public void setAllCitys(List<WxCityInfoModel> allCitys) {
		this.allCitys = allCitys;
	}
	@Override
	public String toString() {
		return "WxCityInfoListModel [hotCitys=" + hotCitys + ", allCitys=" + allCitys + ", getHotCitys()=" + getHotCitys() + ", getAllCitys()=" + getAllCitys() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
}
