package com.eviano2o.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eviano2o.bean.found.FoundArticleCommentModel;
import com.eviano2o.bean.request.FoundArticleCommentListRequestModel;
import com.eviano2o.bean.request.FoundArticleListRequestModel;
import com.eviano2o.filter.CheckAppLogin;
import com.eviano2o.service.FoundService;
import com.eviano2o.util.response.JsonModel;
import com.eviano2o.util.response.JsonResponseHelper;

/**
 * @Title: FoundController
 * @Description: 发现板块类
 * @author lideguang
 * @createTime 2016年9月19日 下午12:14:28
 * @lastEdit
 */
@Controller
@RequestMapping("/found")
public class FoundController extends BaseController {

	@Resource
	private FoundService foundService;
	private static final Logger logger = LoggerFactory.getLogger(FoundController.class);
	// 分类信息
	@RequestMapping(value = "class", method = RequestMethod.GET)
	public String classList(Model model) {
		model.addAttribute("classList", foundService.getFoundClassList());
		model.addAttribute("classTagList", foundService.getFoundClassTagList());
		return "screen/found/class";
	}
	
	// 分类信息
	@RequestMapping(value = "wxClass", method = RequestMethod.GET)
	public String wxClass(Model model) {
		model.addAttribute("classList", foundService.getFoundClassList());
		model.addAttribute("classTagList", foundService.getFoundClassTagList());
		return "screen/found/wxClass";
	}
	
	// 文章列表json 注意在返回json格式时需要把produces 由text/plain;charset=UTF-8;改为application/json; charset=utf-8， 否则报406错误
	@RequestMapping(value = "ajax/articleList", produces = "application/json; charset=utf-8")
	public @ResponseBody JsonModel articleList(@ModelAttribute("requestModel") FoundArticleListRequestModel requestModel, HttpServletRequest request) {
		if (requestModel.getClassId() == null)
			return JsonResponseHelper.getParamErrorJsonModel();
		requestModel.setClientId(getSessionClientId(request));
		return foundService.getArticleList(requestModel);
	}

	// 文章详情
	@RequestMapping(value = "articleDetail", method = RequestMethod.GET)
	public String articleDetail(int articleId, Model model, HttpServletRequest request) {
		model.addAttribute("detail", foundService.getFoundArticleDetail(articleId, getSessionClientId(request)));
		return "screen/found/articleDetail";
	}
	
	// 文章详情
	@RequestMapping(value = "appArticleDetail", method = RequestMethod.GET)
	public String appArticleDetail(int articleId, Model model, HttpServletRequest request) {
		model.addAttribute("detail", foundService.getFoundArticleDetail(articleId, getSessionClientId(request)));
		return "screen/found/appArticleDetail";
	}
	
	// 文章点赞,json
	@CheckAppLogin
	@RequestMapping(value = "ajax/articleLikeOperat", produces = "application/json; charset=utf-8")
	public @ResponseBody JsonModel articleLikeOperat(Integer articleId, HttpServletRequest request) {
		if (articleId == null)
			return JsonResponseHelper.getParamErrorJsonModel();

		return foundService.ArticleLikeOperat(articleId, getSessionClientId(request));
	}

	// 文章评价列表,json
	@RequestMapping(value = "ajax/articleCommentList", produces = "application/json; charset=utf-8")
	public @ResponseBody JsonModel articleCommentList(@ModelAttribute("requestModel") FoundArticleCommentListRequestModel requestModel, HttpServletRequest request) {
		if (requestModel.getArticleId() == null)
			return JsonResponseHelper.getParamErrorJsonModel();
		
		requestModel.setClientId(getSessionClientId(request));
		return foundService.getArticleCommentList(requestModel);
	}

	// 提交文章评价
	@CheckAppLogin
	@RequestMapping(value = "ajax/articleCommentOperat", method = {RequestMethod.POST})
	public @ResponseBody JsonModel articleCommentOperat(@ModelAttribute("requestModel") FoundArticleCommentModel requestModel, HttpServletRequest request) {
		if (requestModel.getArticleId() == null)
			return JsonResponseHelper.getParamErrorJsonModel();

		requestModel.setReplyClientId(getSessionClientId(request));
		return foundService.ArticleCommentOperat(requestModel);
	}

	// 评论点赞,json
	@CheckAppLogin
	@RequestMapping(value = "ajax/articleCommentLikeOperat", produces = "application/json; charset=utf-8")
	public @ResponseBody JsonModel articleCommentLikeOperat(Integer commentId, HttpServletRequest request) {
		if (commentId == null)
			return JsonResponseHelper.getParamErrorJsonModel();

		return foundService.ArticleCommentLikeOperat(commentId, getSessionClientId(request));
	}
	
	// 分类信息
	@RequestMapping(value = "brand", method = RequestMethod.GET)
	public String enterpriseCard(int eid, Model model) {
		model.addAttribute("enterpriseCard", foundService.getEnterpriseCard(eid));
		return "screen/found/brand";
	}

	
	// 搜索页面
	@RequestMapping(value = "search", method = RequestMethod.GET)
	public String search() {
		return "screen/found/search";
	}
	
	/** APP 用户登录判断 JSON */
	@RequestMapping(value = "ajax/checkAppLogin", produces = "application/json; charset=utf-8")
	public @ResponseBody JsonModel checkAppLogin(HttpServletRequest request) {
		//logger.info(Boolean.toString(getSessionClient(request) == null));
		if (getSessionClient(request) == null)
			return JsonResponseHelper.getAppNoLoginJsonModel();
		return JsonResponseHelper.getSuccessJsonModel();
	}
}


