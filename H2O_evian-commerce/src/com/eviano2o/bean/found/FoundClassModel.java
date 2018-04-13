package com.eviano2o.bean.found;

import java.util.List;

public class FoundClassModel {

	private Integer classId;

	private String className;

	String cssClass;

	private List<FoundClassTagModel> tagList;

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public List<FoundClassTagModel> getTagList() {
		return tagList;
	}

	public void setTagList(List<FoundClassTagModel> tagList) {
		this.tagList = tagList;
	}

}
