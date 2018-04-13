package com.fun.bean.vendor;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 订单表
 * @author XHX
 *
 */
public class VendorOrder {
	private Integer orderId;			// 订单ID
	private String orderNo;				// 订单编号
	private Integer eid;				// 所属企业ID
	private Integer bmId;				// 主板ID
	private Integer doorIndex;			// 柜门序号(1-28)
	private Integer mcId;				// 货柜序号
	private Integer productId;			// 商品ID
	private String productName;			// 商品名称
	private String picture;				// 商品图片
	private BigDecimal sellPrice;		// 商品售价
	private BigDecimal realityPrice;	// 实际收款金额
	private BigDecimal discountPrice;	// 优惠金额  (规格:优惠金额+实际金额=商品售价)
	private String discountName;		// 优惠活动描述  
	private Timestamp dateCreated;		// 下单日期
	private Integer orderState;			// 订单状态 0:下单成功  1:成功提货 -1:退款
	private Boolean paySuc;				// 是否支付成功
	private Timestamp lockDate;			// 开锁时间
	private String openId;				// 在支付宝或者微信上的用户身份ID
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getMcId() {
		return mcId;
	}
	public void setMcId(Integer mcId) {
		this.mcId = mcId;
	}
	public String getDiscountName() {
		return discountName;
	}
	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}
	public Boolean getPaySuc() {
		return paySuc;
	}
	public void setPaySuc(Boolean paySuc) {
		this.paySuc = paySuc;
	}
	public Timestamp getLockDate() {
		return lockDate;
	}
	public void setLockDate(Timestamp lockDate) {
		this.lockDate = lockDate;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getEid() {
		return eid;
	}
	public void setEid(Integer eid) {
		this.eid = eid;
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public BigDecimal getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}
	public BigDecimal getRealityPrice() {
		return realityPrice;
	}
	public void setRealityPrice(BigDecimal realityPrice) {
		this.realityPrice = realityPrice;
	}
	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}
	public Timestamp getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Integer getOrderState() {
		return orderState;
	}
	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}
	@Override
	public String toString() {
		return "VendorOrder [orderId=" + orderId + ", orderNo=" + orderNo
				+ ", eid=" + eid + ", bmId=" + bmId + ", doorIndex="
				+ doorIndex + ", mcId=" + mcId + ", productId=" + productId
				+ ", productName=" + productName + ", picture=" + picture
				+ ", sellPrice=" + sellPrice + ", realityPrice=" + realityPrice
				+ ", discountPrice=" + discountPrice + ", discountName="
				+ discountName + ", dateCreated=" + dateCreated
				+ ", orderState=" + orderState + ", paySuc=" + paySuc
				+ ", lockDate=" + lockDate + ", openId=" + openId + "]";
	}
	
}
