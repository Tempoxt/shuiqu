package com.fun.bean;

public class WxPayRefundModel {

	private String AppId;			// 微信id
	private String mchId;			// 商户号
	private String nonceStr;		// 随机字符串 
	private String outTradeNo;		// 商户订单号
	private String outRefundNo;		// 商户退款单号
	private String totalFee;		// 支付金额
	private String refundFee;		// 退款金额
	private String AppKey;			// 微信密钥
	public String getAppId() {
		return AppId;
	}
	public void setAppId(String appId) {
		AppId = appId;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getOutRefundNo() {
		return outRefundNo;
	}
	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	public String getRefundFee() {
		return refundFee;
	}
	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}
	public String getAppKey() {
		return AppKey;
	}
	public void setAppKey(String appKey) {
		AppKey = appKey;
	}
	@Override
	public String toString() {
		return "WxPayRefundModel [AppId=" + AppId + ", mchId=" + mchId
				+ ", nonceStr=" + nonceStr + ", outTradeNo=" + outTradeNo
				+ ", outRefundNo=" + outRefundNo + ", totalFee=" + totalFee
				+ ", refundFee=" + refundFee + ", AppKey=" + AppKey + "]";
	}
	
}
