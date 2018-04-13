package com.eviano2o.bean.weixin;

import java.sql.Timestamp;

public class ClientModel {
	String clientId;
	String account;
	String passWord;
	String email;
	String boundPhone;
	String nickname;
	String sex;
	String birthday;
	String photo;
	Short status;
	String sdkType = "weixin";
	String mobileIMEL;
	String mobileType;
	String sdkVer;
	Integer loginNumber = 0;
	String identityCode;
	Timestamp dateLastLogin;
	Timestamp dateCreated;
	Integer userId;
	String weixinId;
	
	/** 表中的identityCode */
	public String getClientId() {
		return clientId;
	}
	/** 表中的identityCode */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBoundPhone() {
		return boundPhone;
	}

	public void setBoundPhone(String boundPhone) {
		this.boundPhone = boundPhone;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickName) {
		this.nickname = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getSdkType() {
		return sdkType;
	}

	public void setSdkType(String sdkType) {
		this.sdkType = sdkType;
	}

	public String getMobileIMEL() {
		return mobileIMEL;
	}

	public void setMobileIMEL(String mobileIMEL) {
		this.mobileIMEL = mobileIMEL;
	}

	public String getMobileType() {
		return mobileType;
	}

	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}

	public String getSdkVer() {
		return sdkVer;
	}

	public void setSdkVer(String sdkVer) {
		this.sdkVer = sdkVer;
	}

	public Integer getLoginNumber() {
		return loginNumber;
	}

	public void setLoginNumber(Integer loginNumber) {
		this.loginNumber = loginNumber;
	}

	public String getIdentityCode() {
		return identityCode;
	}

	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}

	public Timestamp getDateLastLogin() {
		return dateLastLogin;
	}

	public void setDateLastLogin(Timestamp dateLastLogin) {
		this.dateLastLogin = dateLastLogin;
	}

	public Timestamp getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	/** 表中的clientId */
	public Integer getUserId() {
		return userId;
	}

	/** 表中的clientId */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getWeixinId() {
		return weixinId;
	}
	public void setWeixinId(String weixinId) {
		this.weixinId = weixinId;
	}
	
	/** 是否已经下载关注用户, 微信登录时返回 */
	Boolean exists;
	public Boolean getExists() {
		return exists;
	}
	public void setExists(Boolean exists) {
		this.exists = exists;
	}
	/** 用户选择城市, 微信登录时返回 */
	WxCityInfoModel citys;
	public WxCityInfoModel getCitys() {
		return citys;
	}
	public void setCitys(WxCityInfoModel citys) {
		this.citys = citys;
	}
	
	@Override
	public String toString() {
		return "ClientModel [clientId=" + clientId + ", account=" + account + ", passWord=" + passWord + ", email=" + email + ", boundPhone=" + boundPhone + ", nickname=" + nickname + ", sex=" + sex + ", birthday=" + birthday + ", photo=" + photo + ", status=" + status + ", sdkType=" + sdkType + ", mobileIMEL=" + mobileIMEL + ", mobileType=" + mobileType + ", sdkVer=" + sdkVer + ", loginNumber=" + loginNumber + ", identityCode=" + identityCode + ", dateLastLogin=" + dateLastLogin + ", dateCreated=" + dateCreated + ", userId=" + userId + ", weixinId=" + weixinId + ", getClientId()=" + getClientId() + ", getAccount()=" + getAccount() + ", getPassWord()=" + getPassWord() + ", getEmail()=" + getEmail() + ", getBoundPhone()=" + getBoundPhone() + ", getNickname()=" + getNickname()
				+ ", getSex()=" + getSex() + ", getBirthday()=" + getBirthday() + ", getPhoto()=" + getPhoto() + ", getStatus()=" + getStatus() + ", getSdkType()=" + getSdkType() + ", getMobileIMEL()=" + getMobileIMEL() + ", getMobileType()=" + getMobileType() + ", getSdkVer()=" + getSdkVer() + ", getLoginNumber()=" + getLoginNumber() + ", getIdentityCode()=" + getIdentityCode() + ", getDateLastLogin()=" + getDateLastLogin() + ", getDateCreated()=" + getDateCreated() + ", getUserId()=" + getUserId() + ", getWeixinId()=" + getWeixinId() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	
	
	

	
}
