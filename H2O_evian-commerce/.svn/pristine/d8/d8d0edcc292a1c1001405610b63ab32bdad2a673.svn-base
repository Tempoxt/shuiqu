package com.eviano2o.bean.weixin;

import java.io.Serializable;
import java.util.List;

import com.eviano2o.util.NumberUtil;

public class QuickShoppingListModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//开始营业时间
	String startTime;
	
	String pictureUrl;
	String location;
	String shopName;
	Integer shopId;
	Double freight;
	Integer grade;
	
	//平均送达时间(单位：分钟)
	Integer sendOnTime;
	
	//结束营业时间
	String endTime;
	
	Double minSendPrice;
	String shopNo;
	Integer eid;
	
	Integer did;
	//用户时间段
	String appointmentTime;
	//地址联系电话 + 时间段
	String sendRemark;
	//用户填写
	String orderRemark;
	Integer payMode;
	Integer invoiceType;
	String invoicoName;
	String invoiceDetail;
	Integer vatId;
	
	Double shopProductMoney;
	Integer shopProductQuantity;
	Integer shopProductTicket;
	Double shopTotalMoney;
	Integer shopProductETicket;
	
	//时间段下标，0为今天已经结束
	Integer timeStampSuffix;
	//选择时间段(计算返回客户端)
	List<String> timeStamps;
	//接口返回时间段
	List<ShopTimeframeModel> timeframes;
	
	//快速订水商品列表
	List<QuickShoppingGoods> habits;
	
	//购物车商品列表
	List<QuickShoppingGoods> products;
	
	//11:首桶免费,12:首桶半价
	Integer privilegeId;
	
	//折扣（活动或者是代金券，改为在确认订单页面操作）
	List<QuickShoppingListDiscountModel> discounts;
	
	//百度围栏坐标
	String sendLocations;
	
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public Double getFreight() {
		return freight;
	}
	public void setFreight(Double freight) {
		this.freight = freight;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public Integer getSendOnTime() {
		return sendOnTime;
	}
	public void setSendOnTime(Integer sendOnTime) {
		this.sendOnTime = sendOnTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Double getMinSendPrice() {
		return minSendPrice;
	}
	public void setMinSendPrice(Double minSendPrice) {
		this.minSendPrice = minSendPrice;
	}
	public String getShopNo() {
		return shopNo;
	}
	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}
	public Integer getEid() {
		return eid;
	}
	public void setEid(Integer eid) {
		this.eid = eid;
	}
	public Integer getDid() {
		return did;
	}
	public void setDid(Integer did) {
		this.did = did;
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
	public Integer getPayMode() {
		return payMode;
	}
	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}
	public Integer getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getInvoicoName() {
		return invoicoName;
	}
	public void setInvoicoName(String invoicoName) {
		this.invoicoName = invoicoName;
	}
	public String getInvoiceDetail() {
		return invoiceDetail;
	}
	public void setInvoiceDetail(String invoiceDetail) {
		this.invoiceDetail = invoiceDetail;
	}


	public Integer getVatId() {
		return vatId;
	}
	public void setVatId(Integer vatId) {
		this.vatId = vatId;
	}
	public List<QuickShoppingGoods> getHabits() {
		return habits;
	}
	public void setHabits(List<QuickShoppingGoods> habits) {
		this.habits = habits;
	}
	public Double getShopProductMoney() {
		return shopProductMoney;
	}
	public void setShopProductMoney(Double shopProductMoney) {
		this.shopProductMoney = shopProductMoney;
	}

	public String getShopProductMoneyStr() {
		return NumberUtil.toFixedNumber(this.shopProductMoney);
	}
	
	public Integer getShopProductQuantity() {
		return shopProductQuantity;
	}
	public void setShopProductQuantity(Integer shopProductQuantity) {
		this.shopProductQuantity = shopProductQuantity;
	}
	public Integer getShopProductTicket() {
		return shopProductTicket;
	}
	public void setShopProductTicket(Integer shopProductTicket) {
		this.shopProductTicket = shopProductTicket;
	}
	public Double getShopTotalMoney() {
		return shopTotalMoney;
	}
	public void setShopTotalMoney(Double shopTotalMoney) {
		this.shopTotalMoney = shopTotalMoney;
	}
	public Integer getShopProductETicket() {
		return shopProductETicket;
	}
	public void setShopProductETicket(Integer shopProductETicket) {
		this.shopProductETicket = shopProductETicket;
	}
	public Integer getTimeStampSuffix() {
		return timeStampSuffix;
	}
	public void setTimeStampSuffix(Integer timeStampSuffix) {
		this.timeStampSuffix = timeStampSuffix;
	}
	public List<String> getTimeStamps() {
		return timeStamps;
	}
	public void setTimeStamps(List<String> timeStamps) {
		this.timeStamps = timeStamps;
	}
	
	public List<ShopTimeframeModel> getTimeframes() {
		return timeframes;
	}
	public void setTimeframes(List<ShopTimeframeModel> timeframes) {
		this.timeframes = timeframes;
	}
	
	public List<QuickShoppingGoods> getProducts() {
		return products;
	}
	public void setProducts(List<QuickShoppingGoods> products) {
		this.products = products;
	}
	public Integer getPrivilegeId() {
		return privilegeId;
	}
	public void setPrivilegeId(Integer privilegeId) {
		this.privilegeId = privilegeId;
	}
	public List<QuickShoppingListDiscountModel> getDiscounts() {
		return discounts;
	}
	public void setDiscounts(List<QuickShoppingListDiscountModel> discounts) {
		this.discounts = discounts;
	}
	public String getSendLocations() {
		return sendLocations;
	}
	public void setSendLocations(String sendLocations) {
		this.sendLocations = sendLocations;
	}
	
	public String getOrderRemark() {
		return orderRemark;
	}
	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}
	
	Boolean linepay;
	public Boolean getLinepay() {
		return linepay;
	}
	public void setLinepay(Boolean linepay) {
		this.linepay = linepay;
	}
	

	//是否提供开票功能
	Boolean openInvoice;


	public Boolean getOpenInvoice() {
		return openInvoice;
	}
	public void setOpenInvoice(Boolean openInvoice) {
		this.openInvoice = openInvoice;
	}
	
}

