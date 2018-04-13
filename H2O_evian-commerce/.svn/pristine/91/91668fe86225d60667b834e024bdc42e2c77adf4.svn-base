package com.eviano2o.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eviano2o.bean.found.FoundArticleCommentModel;
import com.eviano2o.bean.found.FoundArticleModel;
import com.eviano2o.bean.found.FoundClassModel;
import com.eviano2o.bean.found.FoundClassTagModel;
import com.eviano2o.bean.request.FoundArticleCommentListRequestModel;
import com.eviano2o.bean.request.FoundArticleListRequestModel;
import com.eviano2o.bean.weixin.EnterpriseModel;
import com.eviano2o.impl.FoundImpl;
import com.eviano2o.util.response.JsonModel;

@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class FoundService {

	@Autowired
	private FoundImpl foundImpl;
	
	//@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	//@Cacheable(value = "findCache", key = "#kid")   //���key=kid������Ϣ
	/** 文章分类 */
	public List<FoundClassModel> getFoundClassList() {
		return foundImpl.getFoundClassList();
	}
	
	/** 文章标签 */
	public List<FoundClassTagModel> getFoundClassTagList() {
		return foundImpl.getFoundClassTagList();
	}
	
	/** 文章列表 */
	public JsonModel getArticleList(FoundArticleListRequestModel requestModel){
		return foundImpl.getArticleList(requestModel);
	}
	
	/** 文章详情 */
	public FoundArticleModel getFoundArticleDetail(int articleId, Integer clientId) {
		return foundImpl.getFoundArticleDetail(articleId, clientId);
	}
	
	/** 文章点赞 */
	public JsonModel ArticleLikeOperat(Integer articleId, Integer clientId){
		return foundImpl.ArticleLikeOperat(articleId, clientId);
	}
	
	/** 文章评论列表 */
	public JsonModel getArticleCommentList(FoundArticleCommentListRequestModel requestModel){
		return foundImpl.getArticleCommentList(requestModel);
	}

	/** 发起文章评论 */
	public JsonModel ArticleCommentOperat(final FoundArticleCommentModel articleCommentModel){
		return foundImpl.ArticleCommentOperat(articleCommentModel);
	}
	
	/** 文章评论点赞 */
	public JsonModel ArticleCommentLikeOperat(Integer commentId, Integer clientId)
	{
		return foundImpl.ArticleCommentLikeOperat(commentId, clientId);
	}
	
	/** 企业名片 */
	public EnterpriseModel getEnterpriseCard(Integer eid) {
		return foundImpl.getEnterpriseCard(eid);
	}
}
