package com.eviano2o.bean.request;

public class FoundArticleCommentListRequestModel {
	private Integer pageIndex = 1;
	private Integer pageSize = 4;
	private Integer articleId;
	Integer clientId;
	public Integer getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	@Override
	public String toString() {
		return "FoundArticleCommentListRequestModel [pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", articleId=" + articleId + ", clientId=" + clientId + "]";
	}
	
}
