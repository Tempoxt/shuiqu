package com.eviano2o.bean.weixin;

public class WxUserInfoModel {
	String clientId;
	String account;
	String nickname;
	String photo;
	String weixinId;
	Integer cityId;
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getWeixinId() {
		return weixinId;
	}
	public void setWeixinId(String weixinId) {
		this.weixinId = weixinId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	
	@Override
	public String toString() {
		return "WxUserInfoModel [clientId=" + clientId + ", account=" + account + ", nickname=" + nickname + ", photo=" + photo + ", weixinId=" + weixinId + "]";
	}
}
