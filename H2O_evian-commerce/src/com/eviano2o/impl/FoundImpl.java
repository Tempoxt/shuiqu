package com.eviano2o.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.eviano2o.bean.found.FoundArticleCommentModel;
import com.eviano2o.bean.found.FoundArticleModel;
import com.eviano2o.bean.found.FoundArticlePicModel;
import com.eviano2o.bean.found.FoundClassModel;
import com.eviano2o.bean.found.FoundClassTagModel;
import com.eviano2o.bean.request.FoundArticleCommentListRequestModel;
import com.eviano2o.bean.request.FoundArticleListRequestModel;
import com.eviano2o.bean.weixin.EnterpriseModel;
import com.eviano2o.util.ResultSetToBeanHelper;
import com.eviano2o.util.response.JsonModel;
import com.eviano2o.util.response.JsonResponseHelper;
import com.eviano2o.util.response.JsonResponseResultCodeDefine;

@Component
public class FoundImpl {
	private static final Logger logger = LoggerFactory.getLogger(FoundImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/** 文章分类 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<FoundClassModel> getFoundClassList() {
		String sql = "select classId, className, cssClass from found_article_class where del = 0 order by sortId";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(FoundClassModel.class));
	}

	/** 文章标签 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<FoundClassTagModel> getFoundClassTagList() {
		String sql = "select tagId, classId, tagName from found_article_class_tag where del = 0 order by sortId";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(FoundClassTagModel.class));
	}

	/** 文章列表 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonModel getArticleList(final FoundArticleListRequestModel requestModel) {
		String sql = "{call Proc_found_article_select_list_wx(?,?,?,?,?,?)}";
		JsonModel jsonModel = new JsonModel();
		try {
			jsonModel = jdbcTemplate.execute(sql, new CallableStatementCallback() {
				@Override
				public Object doInCallableStatement(CallableStatement cs) throws SQLException {
					cs.setObject("classId", requestModel.getClassId());
					cs.setObject("tagId", requestModel.getTagId());
					cs.setString("articleTitle", requestModel.getArticleTitle());
					cs.setObject("PageIndex", requestModel.getPageIndex());
					cs.setObject("PageSize", requestModel.getPageSize());
					cs.setObject("clientId", requestModel.getClientId());

					ResultSet rs = cs.executeQuery();

					try {
						List<FoundArticleModel> result = ResultSetToBeanHelper.resultSetToList(rs, FoundArticleModel.class);
						if (result != null) {
							for (FoundArticleModel article : result) {
								article.setPicList(getFoundArticlePicList(article.getArticleId()));
							}
						}
						return new JsonResponseHelper(JsonResponseResultCodeDefine.SUCCESS, result).getJsonModel();
					} catch (Exception e) {
						logger.error("[ex:{}]", new Object[] { e });
						return JsonResponseHelper.getDBErrorJsonModel();
					}finally{
						if(rs!=null){
							rs.close();
						}
					}

				}
			});

		} catch (Exception e) {
			logger.error("[ex:{}],[parem:{}]", new Object[] { e, requestModel.toString() });
			return JsonResponseHelper.getDBErrorJsonModel();
		}
		return jsonModel;
	}

	/** 文章详情 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FoundArticleModel getFoundArticleDetail(int articleId, Integer clientId) {
		String sql = "select A.articleId, A.eid, A.classId, A.articleTitle,A.auchor, A.articleContent, A.readCount, A.createTime ";
		sql += ",B.eShortName, B.logoUrl, C.className, A.likeCount ";
		sql += ", (select count(1) from found_article_comment where articleId=A.articleId and parentId = 0) as commentCount ";
		if (clientId != null)
			sql += ", convert(bit, case when exists (select * from found_article_like where clientId=" + clientId + " and articleId=A.articleId ) then 1 else 0 end) as isLike ";
		else
			sql += ", convert(bit, 0) as isLike ";
		sql += "from found_article A ";
		sql += "inner join e_enterprise B on A.eid = B.eid ";
		sql += "inner join found_article_class C on A.classId = C.classId ";
		sql += "left join found_article_like D on A.articleId = D.articleId ";
		sql += "where A.isCheck = 1 and A.articleId = " + articleId + " ";
		List<FoundArticleModel> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper(FoundArticleModel.class));
		return result == null || result.size() == 0 ? null : result.get(0);
	}

	/** 文章图片 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<FoundArticlePicModel> getFoundArticlePicList(int articleId) {
		String sql = "select articleId, picUrl from found_article_pic where articleId = " + articleId + " ";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(FoundArticlePicModel.class));
	}

	/** 文章点赞 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonModel ArticleLikeOperat(final Integer articleId, final Integer clientId) {
		String sql = "{call Proc_found_article_like_operat(?,?,?)}";
		JsonModel jsonModel = new JsonModel();
		try {
			jsonModel = jdbcTemplate.execute(sql, new CallableStatementCallback() {
				@Override
				public Object doInCallableStatement(CallableStatement cs) throws SQLException {
					cs.setObject("articleId", articleId);
					cs.setObject("clientId", clientId);
					cs.registerOutParameter("TAG", Types.NVARCHAR);// 注册输出参数的类型
					cs.execute();

					return JsonResponseHelper.getProcTagValueJsonModel(cs.getString("TAG"));
				}
			});

		} catch (Exception e) {
			logger.error("[ex:{}]", new Object[] { e });
			return JsonResponseHelper.getDBErrorJsonModel();
		}
		return jsonModel;
	}

	/** 文章评论列表 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonModel getArticleCommentList(final FoundArticleCommentListRequestModel requestModel) {
		String sql = "{call Proc_found_article_comment_select(?,?,?,?)}";
		JsonModel jsonModel = new JsonModel();
		try {
			jsonModel = jdbcTemplate.execute(sql, new CallableStatementCallback() {
				@Override
				public Object doInCallableStatement(CallableStatement cs) throws SQLException {
					cs.setObject("articleId", requestModel.getArticleId());
					cs.setObject("PageIndex", requestModel.getPageIndex());
					cs.setObject("PageSize", requestModel.getPageSize());
					cs.setObject("clientId", requestModel.getClientId());

					ResultSet rs = cs.executeQuery();

					try {
						List<FoundArticleCommentModel> result = ResultSetToBeanHelper.resultSetToList(rs, FoundArticleCommentModel.class);
						if (result != null) {
							for (FoundArticleCommentModel comment : result) {
								comment.setCommentList(getFoundArticleSonCommentList(comment.getCommentId()));
							}
						}
						return new JsonResponseHelper(JsonResponseResultCodeDefine.SUCCESS, result).getJsonModel();
					} catch (Exception e) {
						logger.error("[ex:{}]", new Object[] { e });
						return JsonResponseHelper.getDBErrorJsonModel();
					}finally{
						if(rs!=null){
							rs.close();
						}
					}

				}
			});

		} catch (Exception e) {
			logger.error("[ex:{}],[param:]", new Object[] { e, requestModel.toString() });
			return JsonResponseHelper.getDBErrorJsonModel();
			// return new
			// JsonResponseHelper(JsonResponseResultCodeDefine.DB_ERROR,
			// null).getJsonModel();
		}
		return jsonModel;
	}

	/** 查询对文章评论发起的评论 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<FoundArticleCommentModel> getFoundArticleSonCommentList(Integer commentId) {
		String sql = "SELECT A.commentId, A.parentId, A.articleId, A.clientId,A.commentContent, A.replyClientId, A.isCheck, A.createTime, likeCount ";
		sql += ", (case when B.nickName = '' then left(B.account, 3) + '****' + right(B.account, 4) else B.nickName end) as commentName ";
		sql += ", (case when C.nickName = '' then left(C.account, 3) + '****' + right(C.account, 4) else C.nickName end) as replyName ";
		sql += "FROM found_article_comment A ";
		sql += "inner join e_client B on A.clientId = B.clientId ";
		sql += "inner join e_client C on A.replyClientId = C.clientId ";
		sql += "WHERE A.isCheck = 1 and parentId=" + commentId + " order by A.createTime DESC";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(FoundArticleCommentModel.class));
	}

	/** 发起文章评论 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonModel ArticleCommentOperat(final FoundArticleCommentModel articleCommentModel) {
		String sql = "{call Proc_found_article_comment_operat(?,?,?,?,?,?)}";
		JsonModel jsonModel = new JsonModel();
		try {
			jsonModel = jdbcTemplate.execute(sql, new CallableStatementCallback() {
				@Override
				public Object doInCallableStatement(CallableStatement cs) throws SQLException {
					cs.setObject("parentId", articleCommentModel.getParentId());
					cs.setObject("articleId", articleCommentModel.getArticleId());
					cs.setObject("clientId", articleCommentModel.getClientId());
					cs.setObject("commentContent", articleCommentModel.getCommentContent());
					cs.setObject("replyClientId", articleCommentModel.getReplyClientId());
					cs.registerOutParameter("TAG", Types.NVARCHAR);// 注册输出参数的类型
					cs.execute();

					return JsonResponseHelper.getProcTagValueJsonModel(cs.getString("TAG"));
				}
			});

		} catch (Exception e) {
			logger.error("[ex:{}],[param:{}]", new Object[] { e, articleCommentModel.toString() });
			return JsonResponseHelper.getDBErrorJsonModel();
		}
		return jsonModel;
	}

	/** 客户identityCode查询对应clientId */
	public Integer getIdentityCodeToClientId(final String identityCode) {
		/*String sql=String.format("select clientId from e_client where identityCode='%s'", identityCode);
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);*/
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select clientId from e_client where identityCode=?", new Object[]{identityCode});
		if(list.size()>0)
			return (Integer)list.get(0).get("clientId");
		return null;
	}

	/** 文章评论点赞 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonModel ArticleCommentLikeOperat(final Integer commentId, final Integer clientId) {
		String sql = "{call Proc_found_article_comment_like_operat(?,?,?)}";
		JsonModel jsonModel = new JsonModel();
		try {
			jsonModel = jdbcTemplate.execute(sql, new CallableStatementCallback() {
				@Override
				public Object doInCallableStatement(CallableStatement cs) throws SQLException {
					cs.setObject("commentId", commentId);
					cs.setObject("clientId", clientId);
					cs.registerOutParameter("TAG", Types.NVARCHAR);// 注册输出参数的类型
					cs.execute();

					return JsonResponseHelper.getProcTagValueJsonModel(cs.getString("TAG"));
				}
			});

		} catch (Exception e) {
			logger.error("[ex:{}]", new Object[] { e });
			return JsonResponseHelper.getDBErrorJsonModel();
		}
		return jsonModel;
	}

	/** 企业名片 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EnterpriseModel getEnterpriseCard(Integer eid) {
		try {
			String sql = "select eid, eName, logoUrl, tel from e_enterprise where eid=" + eid;
			List<EnterpriseModel> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper(EnterpriseModel.class));
			return result == null || result.size() == 0 ? null : result.get(0);
		} catch (Exception e) {
			logger.error("[ex:{}]", new Object[] { e });
			return null;
		}
	}

}
