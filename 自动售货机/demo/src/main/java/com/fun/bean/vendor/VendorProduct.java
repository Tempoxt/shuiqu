package com.fun.bean.vendor;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 商品表
 * @author XHX
 *
 */
public class VendorProduct {

	private Integer id;				// id
	private Integer eid;			// 企业
	private String productName;		// 商品名称
	private Double price;		// 商品价格
	private String picture;			// 商品图片
	private String imageText;		// 商品图文URL
	private Boolean isLine;			// 是否上架
	private Timestamp createTime;	// 录入时间
	private String createUser;		// 录入人
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getEid() {
		return eid;
	}
	public void setEid(Integer eid) {
		this.eid = eid;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getImageText() {
		return imageText;
	}
	public void setImageText(String imageText) {
		this.imageText = imageText;
	}
	public Boolean getIsLine() {
		return isLine;
	}
	public void setIsLine(Boolean isLine) {
		this.isLine = isLine;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	@Override
	public String toString() {
		return "VendorProduct [id=" + id + ", eid=" + eid + ", productName="
				+ productName + ", price=" + price + ", picture=" + picture
				+ ", imageText=" + imageText + ", isLine=" + isLine
				+ ", createTime=" + createTime + ", createUser=" + createUser
				+ "]";
	}
}
