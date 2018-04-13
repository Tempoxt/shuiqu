package com.eviano2o.bean.weixin;


public class WxCityInfoModel {
	Integer cityId;
	String cityName;
	Integer citycode;
	Boolean ifHot;
	Boolean ifLine;
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Integer getCitycode() {
		return citycode;
	}
	public void setCitycode(Integer citycode) {
		this.citycode = citycode;
	}
	public Boolean getIfHot() {
		return ifHot;
	}
	public void setIfHot(Boolean ifHot) {
		this.ifHot = ifHot;
	}
	public Boolean getIfLine() {
		return ifLine;
	}
	public void setIfLine(Boolean ifLine) {
		this.ifLine = ifLine;
	}
	
	
	
	String zipCode;
	String location;
	Integer provinceId;
	Integer baiduCode;
	Integer zoom;
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	public Integer getBaiduCode() {
		return baiduCode;
	}
	public void setBaiduCode(Integer baiduCode) {
		this.baiduCode = baiduCode;
	}
	public Integer getZoom() {
		return zoom;
	}
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	
	
	
	String firstLetter;
	public String getFirstLetter() {
		return firstLetter;
	}
	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}
	@Override
	public String toString() {
		return "WxCityInfoModel [cityId=" + cityId + ", cityName=" + cityName
				+ ", citycode=" + citycode + ", ifHot=" + ifHot + ", ifLine="
				+ ifLine + ", zipCode=" + zipCode + ", location=" + location
				+ ", provinceId=" + provinceId + ", baiduCode=" + baiduCode
				+ ", zoom=" + zoom + ", firstLetter=" + firstLetter + "]";
	}
	
	
	
}
