package com.fun.pay;

public class PayResponseReturnModel {
    String AppId;
    String TimeStamp;
    String NonceStr;
    String Package;
    String PaySign;
    String DanHao;
    String TotalFee;
    String code;
    String message;
    String JiaMiID;
	public String getAppId() {
		return AppId;
	}
	public void setAppId(String appId) {
		AppId = appId;
	}
	public String getTimeStamp() {
		return TimeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		TimeStamp = timeStamp;
	}
	public String getNonceStr() {
		return NonceStr;
	}
	public void setNonceStr(String nonceStr) {
		NonceStr = nonceStr;
	}
	public String getPackage() {
		return Package;
	}
	public void setPackage(String package1) {
		Package = package1;
	}
	public String getPaySign() {
		return PaySign;
	}
	public void setPaySign(String paySign) {
		PaySign = paySign;
	}
	public String getDanHao() {
		return DanHao;
	}
	public void setDanHao(String danHao) {
		DanHao = danHao;
	}
	public String getTotalFee() {
		return TotalFee;
	}
	public void setTotalFee(String totalFee) {
		TotalFee = totalFee;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getJiaMiID() {
		return JiaMiID;
	}
	public void setJiaMiID(String jiaMiID) {
		JiaMiID = jiaMiID;
	}
	@Override
	public String toString() {
		return "PayResponseReturnModel [AppId=" + AppId + ", TimeStamp=" + TimeStamp + ", NonceStr=" + NonceStr + ", Package=" + Package + ", PaySign=" + PaySign + ", DanHao=" + DanHao + ", TotalFee=" + TotalFee + ", errorCode=" + code + ", JiaMiID=" + JiaMiID + "]";
	}
    
}
