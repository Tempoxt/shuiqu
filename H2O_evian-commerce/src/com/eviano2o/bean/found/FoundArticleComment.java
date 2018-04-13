package com.eviano2o.bean.found;

import java.sql.Timestamp;
import java.util.List;

/**文章评论*/
public class FoundArticleComment {
	Integer commentId;
	Integer parentId;
	Integer articleId;
	Integer clientId;
	String commentContent;
	Integer replyClientId;
	Boolean isCheck;
	Timestamp createTime;
	Integer likeCount;
	String commentName;
	String replyName;
	List<FoundArticleComment> commentList;
	
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public Integer getReplyClientId() {
		return replyClientId;
	}
	public void setReplyClientId(Integer replyClientId) {
		this.replyClientId = replyClientId;
	}
	public Boolean getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Integer getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	public String getCommentName() {
		return commentName;
	}
	public void setCommentName(String commentName) {
		this.commentName = commentName;
	}
	public String getReplyName() {
		return replyName;
	}
	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}
	public List<FoundArticleComment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<FoundArticleComment> commentList) {
		this.commentList = commentList;
	}
	
	
}
