package com.eviano2o.bean.weixin;

import java.util.List;


public class WxOrderDetailModel {
	Integer orderId;
	String orderNo;
	Integer shopId;
	String shopNo;
	String shopName;
	String pictureUrl;
	Integer statusId;
	String status;
	String payMode;
	Double receivableTotal;
	Double cashTotal;
	Double linePayTotal;
	Double freight;
	Integer detailNum;
	Boolean evaluateEnabled;
	Boolean payEnabled;
	Boolean delEnabled;
	Boolean reapEnabled;
	Boolean logisticsEnabled;
	List<WxSaveOrderProductModel> details;
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public String getShopNo() {
		return shopNo;
	}
	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
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
	public Double getFreight() {
		return freight;
	}
	public void setFreight(Double freight) {
		this.freight = freight;
	}
	public Integer getDetailNum() {
		return detailNum;
	}
	public void setDetailNum(Integer detailNum) {
		this.detailNum = detailNum;
	}
	public Boolean getEvaluateEnabled() {
		return evaluateEnabled;
	}
	public void setEvaluateEnabled(Boolean evaluateEnabled) {
		this.evaluateEnabled = evaluateEnabled;
	}
	public Boolean getPayEnabled() {
		return payEnabled;
	}
	public void setPayEnabled(Boolean payEnabled) {
		this.payEnabled = payEnabled;
	}
	public Boolean getDelEnabled() {
		return delEnabled;
	}
	public void setDelEnabled(Boolean delEnabled) {
		this.delEnabled = delEnabled;
	}
	public Boolean getReapEnabled() {
		return reapEnabled;
	}
	public void setReapEnabled(Boolean reapEnabled) {
		this.reapEnabled = reapEnabled;
	}
	public Boolean getLogisticsEnabled() {
		return logisticsEnabled;
	}
	public void setLogisticsEnabled(Boolean logisticsEnabled) {
		this.logisticsEnabled = logisticsEnabled;
	}
	public List<WxSaveOrderProductModel> getDetails() {
		return details;
	}
	public void setDetails(List<WxSaveOrderProductModel> details) {
		this.details = details;
	}
	
}
