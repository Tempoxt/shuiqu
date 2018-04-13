package com.eviano2o.bean.weixin;

public class WxSaveOrderInvoiceModel {
	Integer type;
	String invoicoName;
	String invoiceDetail;
	Integer vatId;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	
	
}
