package com.eviano2o.bean.request;

import org.apache.commons.lang.StringUtils;

public class FoundArticleListRequestModel {
	private Integer pageIndex = 1;
	private Integer pageSize = 4;
	private Integer classId;
	private Integer tagId;
	private String articleTitle;
	Integer clientId;

	public int getPageIndex() {
		return pageIndex <= 0 ? 1 : pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize <= 0 ? 4 : pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getArticleTitle() {
		return  StringUtils.isEmpty(articleTitle) ? null : articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return "FoundArticleListRequestModel [pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", classId=" + classId + ", tagId=" + tagId + ", articleTitle=" + articleTitle + ", clientId="
				+ clientId + "]";
	}

}
