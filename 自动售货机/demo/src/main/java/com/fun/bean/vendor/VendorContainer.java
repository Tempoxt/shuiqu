package com.fun.bean.vendor;

import java.sql.Timestamp;
/**
 * 货柜表
 * @author XHX
 *
 */
public class VendorContainer {
	private Integer vmId;			// 货柜ID
	private Integer bmId;			// 主板ID
	private Integer doorNumber;		// 柜门数量
	private Integer vmIndex;		// 货柜在主板中的序号 (1-3)
	private Timestamp dateCreated;  // 录入时间
	private String createdUser;		// 录入人
	public Integer getVmId() {
		return vmId;
	}
	public void setVmId(Integer vmId) {
		this.vmId = vmId;
	}
	public Integer getBmId() {
		return bmId;
	}
	public void setBmId(Integer bmId) {
		this.bmId = bmId;
	}
	public Integer getDoorNumber() {
		return doorNumber;
	}
	public void setDoorNumber(Integer doorNumber) {
		this.doorNumber = doorNumber;
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
	public Integer getVmIndex() {
		return vmIndex;
	}
	public void setVmIndex(Integer vmIndex) {
		this.vmIndex = vmIndex;
	}
	@Override
	public String toString() {
		return "VendorContainer [vmId=" + vmId + ", bmId=" + bmId
				+ ", doorNumber=" + doorNumber + ", vmIndex=" + vmIndex
				+ ", dateCreated=" + dateCreated + ", createdUser="
				+ createdUser + "]";
	}
}
