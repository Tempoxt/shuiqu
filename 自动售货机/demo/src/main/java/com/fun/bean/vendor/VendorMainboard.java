package com.fun.bean.vendor;

import java.sql.Timestamp;
/**
 * 主板表
 * @author XHX
 *
 */
public class VendorMainboard {

	private Integer bmId;				// 主板ID
	private String mainboardNo;			// 主板序号
	private Integer portNumber;			// 主板端口数量
	private Timestamp dateCreated;		// 录入日期
	private String createdUser;			// 录入人
	private String housesName;			// 小区名称
	private String address;				// 详细地址
	private String location;			// 坐标
	private Integer eid;				// 企业
	private Integer shopId;				// 水店Id，水店内的职员可以管理该主板对应的货柜
	public Integer getBmId() {
		return bmId;
	}
	public void setBmId(Integer bmId) {
		this.bmId = bmId;
	}
	public String getMainboardNo() {
		return mainboardNo;
	}
	public void setMainboardNo(String mainboardNo) {
		this.mainboardNo = mainboardNo;
	}
	public Integer getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(Integer portNumber) {
		this.portNumber = portNumber;
	}
	public Timestamp getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String getHousesName() {
		return housesName;
	}
	public void setHousesName(String housesName) {
		this.housesName = housesName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getEid() {
		return eid;
	}
	public void setEid(Integer eid) {
		this.eid = eid;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	@Override
	public String toString() {
		return "VendorMainboard [bmId=" + bmId + ", mainboardNo=" + mainboardNo
				+ ", portNumber=" + portNumber + ", dateCreated=" + dateCreated
				+ ", createdUser=" + createdUser + ", housesName=" + housesName
				+ ", address=" + address + ", location=" + location + ", eid="
				+ eid + ", shopId=" + shopId + "]";
	}
}
