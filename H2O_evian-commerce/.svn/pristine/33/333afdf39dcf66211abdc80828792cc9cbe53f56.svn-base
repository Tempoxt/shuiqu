package com.eviano2o.bean.found;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**文章评论*/
public class FoundArticleCommentModel {
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
	List<FoundArticleCommentModel> commentList;
	String photo;
	String createTimeStr;
	Boolean isLike;
	
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
		this.createTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(createTime.getTime()));
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
	public List<FoundArticleCommentModel> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<FoundArticleCommentModel> commentList) {
		this.commentList = commentList;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public Boolean getIsLike() {
		return isLike;
	}
	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}
	@Override
	public String toString() {
		return "FoundArticleCommentModel [commentId=" + commentId + ", parentId=" + parentId + ", articleId=" + articleId + ", clientId=" + clientId + ", commentContent=" + commentContent
				+ ", replyClientId=" + replyClientId + ", isCheck=" + isCheck + ", createTime=" + createTime + ", likeCount=" + likeCount + ", commentName=" + commentName + ", replyName=" + replyName
				+ ", commentList=" + commentList + "]";
	}
	
	
}
