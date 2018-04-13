package com.fun.bean.vendor;

public class VendorTheme {

	private Integer eid;
	private String styleCode;
	public Integer getEid() {
		return eid;
	}
	public void setEid(Integer eid) {
		this.eid = eid;
	}
	public String getStyleCode() {
		return styleCode;
	}
	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}
	@Override
	public String toString() {
		return "VendorTheme [eid=" + eid + ", styleCode=" + styleCode + "]";
	}
	
}
