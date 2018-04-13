package com.eviano2o.bean.weixin;

import java.util.List;

public class WxSaveOrderModel {
	Integer shopId;
	Integer did;
	String sendAddress;
	String phone;
	String contacts;
	String appointmentTime;
	String sendRemark;
	String sdkType;
	String mobileType;
	String mobileIMEL;
	String appVer;
	String orderRemark;
	Integer ticketCount;
	Double receivableTotal;
	Double cashTotal;
	Double linePayTotal;
	Integer payMode;
	Integer privilegeId;
	Integer activityType;
	String code_no;
	Double code_money;
	Double discountMoney;
	String discountDescribe;
	Double freight;
	Integer integral;
	List<WxSaveOrderProductModel> goodsJson;
	WxSaveOrderInvoiceModel invoice;
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public Integer getDid() {
		return did;
	}
	public void setDid(Integer did) {
		this.did = did;
	}
	public String getSendAddress() {
		return sendAddress;
	}
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	public String getSendRemark() {
		return sendRemark;
	}
	public void setSendRemark(String sendRemark) {
		this.sendRemark = sendRemark;
	}
	public String getSdkType() {
		return sdkType;
	}
	public void setSdkType(String sdkType) {
		this.sdkType = sdkType;
	}
	public String getMobileType() {
		return mobileType;
	}
	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}
	public String getMobileIMEL() {
		return mobileIMEL;
	}
	public void setMobileIMEL(String mobileIMEL) {
		this.mobileIMEL = mobileIMEL;
	}
	public String getAppVer() {
		return appVer;
	}
	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}
	public String getOrderRemark() {
		return orderRemark;
	}
	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}
	public Integer getTicketCount() {
		return ticketCount;
	}
	public void setTicketCount(Integer ticketCount) {
		this.ticketCount = ticketCount;
	}
	public Double getReceivableTotal() {
		return receivableTotal;
	}
	public void setReceivableTotal(Double receivableTotal) {
		this.receivableTotal = receivableTotal;
	}
	public Double getCashTotal() {
		return cashTotal;
	}
	public void setCashTotal(Double cashTotal) {
		this.cashTotal = cashTotal;
	}
	public Double getLinePayTotal() {
		return linePayTotal;
	}
	public void setLinePayTotal(Double linePayTotal) {
		this.linePayTotal = linePayTotal;
	}
	public Integer getPayMode() {
		return payMode;
	}
	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}
	public Integer getPrivilegeId() {
		return privilegeId;
	}
	public void setPrivilegeId(Integer privilegeId) {
		this.privilegeId = privilegeId;
	}
	public Integer getActivityType() {
		return activityType;
	}
	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}
	public String getCode_no() {
		return code_no;
	}
	public void setCode_no(String code_no) {
		this.code_no = code_no;
	}
	public Double getCode_money() {
		return code_money;
	}
	public void setCode_money(Double code_money) {
		this.code_money = code_money;
	}
	public Double getDiscountMoney() {
		return discountMoney;
	}
	public void setDiscountMoney(Double discountMoney) {
		this.discountMoney = discountMoney;
	}
	public String getDiscountDescribe() {
		return discountDescribe;
	}
	public void setDiscountDescribe(String discountDescribe) {
		this.discountDescribe = discountDescribe;
	}
	public Double getFreight() {
		return freight;
	}
	public void setFreight(Double freight) {
		this.freight = freight;
	}
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	public List<WxSaveOrderProductModel> getGoodsJson() {
		return goodsJson;
	}
	public void setGoodsJson(List<WxSaveOrderProductModel> goodsJson) {
		this.goodsJson = goodsJson;
	}
	public WxSaveOrderInvoiceModel getInvoice() {
		return invoice;
	}
	public void setInvoice(WxSaveOrderInvoiceModel invoice) {
		this.invoice = invoice;
	}
	
}
