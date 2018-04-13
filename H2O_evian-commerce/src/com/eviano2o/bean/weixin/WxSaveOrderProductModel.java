package com.eviano2o.bean.weixin;

public class WxSaveOrderProductModel {
	Integer pid;
	Integer number;
	Double price;
	Double total;
	String settlementType;
	String voucherCode;
	Integer activityId;
	/** 代金券金额/活动优惠金额 , 实际抵扣金额*/
	Double voucherMoney;
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getSettlementType() {
		return settlementType;
	}
	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}
	public String getVoucherCode() {
		return voucherCode;
	}
	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}
	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	public Double getVoucherMoney() {
		return voucherMoney;
	}
	public void setVoucherMoney(Double voucherMoney) {
		this.voucherMoney = voucherMoney;
	}
	
	int fpid;
	int maxnum;
	public int getFpid() {
		return fpid;
	}
	public void setFpid(int fpid) {
		this.fpid = fpid;
	}
	public int getMaxnum() {
		return maxnum;
	}
	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}
	
}
