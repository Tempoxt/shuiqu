package com.fun.bean.vendor;

import java.sql.Timestamp;
/**
 * 货柜门表
 * @author XHX
 *
 */
public class VendorDoor {

	private Integer bmId;			// 主板ID
	private Integer doorIndex;		// 柜门序号(1-28)
	private Integer mcId;			// 货柜ID
	private Integer productId;		// 销售商品ID
	private Timestamp dateCreated;	// 录入日期
	private String createdUser;		// 录入人
	private Integer eid;			// 企业ID
	private Integer productState;	// 柜门内商品状态 0:无货品  1:有货品
	private Integer doorState;		// 柜门状态 0:关闭  1:打开  -1:故障
	private String alias;			// 别名（用作开门提示）
	private Boolean isValid;		// 是否验证柜门，需要安装员操作该项
	public Integer getMcId() {
		return mcId;
	}
	public void setMcId(Integer mcId) {
		this.mcId = mcId;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Boolean getIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
	public Integer getBmId() {
		return bmId;
	}
	public void setBmId(Integer bmId) {
		this.bmId = bmId;
	}
	public Integer getDoorIndex() {
		return doorIndex;
	}
	public void setDoorIndex(Integer doorIndex) {
		this.doorIndex = doorIndex;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
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
	public Integer getEid() {
		return eid;
	}
	public void setEid(Integer eid) {
		this.eid = eid;
	}
	public Integer getProductState() {
		return productState;
	}
	public void setProductState(Integer productState) {
		this.productState = productState;
	}
	public Integer getDoorState() {
		return doorState;
	}
	public void setDoorState(Integer doorState) {
		this.doorState = doorState;
	}
	@Override
	public String toString() {
		return "VendorDoor [bmId=" + bmId + ", doorIndex=" + doorIndex
				+ ", mcId=" + mcId + ", productId=" + productId
				+ ", dateCreated=" + dateCreated + ", createdUser="
				+ createdUser + ", eid=" + eid + ", productState="
				+ productState + ", doorState=" + doorState + ", alias="
				+ alias + ", isValid=" + isValid + "]";
	}
	
}
