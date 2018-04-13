package com.fun.bean;

public class WXHB {
	private String nonce_str;
	private String AppId;
	private String AppSecret;
	private String AppKey;
	private String mchBillno;
	private String mchId;
	private String wxappid;
	private String msgappid;
	private String sendName;
	private String reOpenid;
	private String totalAmount;
	private String totalNum;
	private String amt_type;
	private String wishing;
	private String clientIp;
	private String actName;
	private String remark;
	public String getAmt_type() {
		return amt_type;
	}
	public void setAmt_type(String amt_type) {
		this.amt_type = amt_type;
	}
	public String getMsgappid() {
		return msgappid;
	}
	public void setMsgappid(String msgappid) {
		this.msgappid = msgappid;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getAppId() {
		return AppId;
	}
	public void setAppId(String appId) {
		AppId = appId;
	}
	public String getAppSecret() {
		return AppSecret;
	}
	public void setAppSecret(String appSecret) {
		AppSecret = appSecret;
	}
	public String getAppKey() {
		return AppKey;
	}
	public void setAppKey(String appKey) {
		AppKey = appKey;
	}
	public String getMchBillno() {
		return mchBillno;
	}
	public void setMchBillno(String mchBillno) {
		this.mchBillno = mchBillno;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getWxappid() {
		return wxappid;
	}
	public void setWxappid(String wxappid) {
		this.wxappid = wxappid;
	}
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public String getReOpenid() {
		return reOpenid;
	}
	public void setReOpenid(String reOpenid) {
		this.reOpenid = reOpenid;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}
	public String getWishing() {
		return wishing;
	}
	public void setWishing(String wishing) {
		this.wishing = wishing;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getActName() {
		return actName;
	}
	public void setActName(String actName) {
		this.actName = actName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "WXHB [nonce_str=" + nonce_str + ", AppId=" + AppId
				+ ", AppSecret=" + AppSecret + ", AppKey=" + AppKey
				+ ", mchBillno=" + mchBillno + ", mchId=" + mchId
				+ ", wxappid=" + wxappid + ", msgappid=" + msgappid
				+ ", sendName=" + sendName + ", reOpenid=" + reOpenid
				+ ", totalAmount=" + totalAmount + ", totalNum=" + totalNum
				+ ", amt_type=" + amt_type + ", wishing=" + wishing
				+ ", clientIp=" + clientIp + ", actName=" + actName
				+ ", remark=" + remark + "]";
	}
	
	
	
}
