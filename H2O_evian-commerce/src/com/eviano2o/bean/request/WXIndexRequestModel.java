package com.eviano2o.bean.request;

public class WXIndexRequestModel {
	String signature;
	String timestamp;
	String nonce;
	String echoStr;
	
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getEchoStr() {
		return echoStr;
	}
	public void setEchoStr(String echoStr) {
		this.echoStr = echoStr;
	}
}
