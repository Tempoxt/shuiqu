package com.eviano2o.bean.weixin;

public class WxUserEntInfoModel {
	private String appid;
	private Integer eid;
	private String ename;
	private String eCode;
	private String logoUrl;
	private String address;
	private String tel;
	private String qq;
	private String email;
	private String legalMan;
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public Integer getEid() {
		return eid;
	}
	public void setEid(Integer eid) {
		this.eid = eid;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLegalMan() {
		return legalMan;
	}
	public void setLegalMan(String legalMan) {
		this.legalMan = legalMan;
	}
	@Override
	public String toString() {
		return "WxUserEntInfoModel [appid=" + appid + ", eid=" + eid
				+ ", ename=" + ename + ", eCode=" + eCode + ", logoUrl="
				+ logoUrl + ", address=" + address + ", tel=" + tel + ", qq="
				+ qq + ", email=" + email + ", legalMan=" + legalMan + "]";
	}

}
