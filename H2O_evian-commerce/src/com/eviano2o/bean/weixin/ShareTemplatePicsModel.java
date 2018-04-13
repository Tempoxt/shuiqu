package com.eviano2o.bean.weixin;

/**生成模板水印图片*/
public class ShareTemplatePicsModel {
	/**水印图片物理路径*/
	String picPath;
	/**不透明度,1为不透明*/
	Float alpha;
	/**水印起始X像素*/
	Integer picX;
	/**水印起始Y像素*/
	Integer picY;
	
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public Float getAlpha() {
		return alpha;
	}
	public void setAlpha(Float alpha) {
		this.alpha = alpha;
	}
	public Integer getPicX() {
		return picX;
	}
	public void setPicX(Integer picX) {
		this.picX = picX;
	}
	public Integer getPicY() {
		return picY;
	}
	public void setPicY(Integer picY) {
		this.picY = picY;
	}
	@Override
	public String toString() {
		return "ShareTemplatePicsModel [picPath=" + picPath + ", alpha=" + alpha + ", picX=" + picX + ", picY=" + picY + ", getPicPath()=" + getPicPath() + ", getAlpha()=" + getAlpha() + ", getPicX()=" + getPicX() + ", getPicY()=" + getPicY() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
}
