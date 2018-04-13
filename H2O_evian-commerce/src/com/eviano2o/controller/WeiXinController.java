package com.eviano2o.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eviano2o.bean.weixin.ClientModel;
import com.eviano2o.bean.weixin.WxCityInfoListModel;
import com.eviano2o.bean.weixin.WxCityInfoModel;
import com.eviano2o.bean.weixin.WxUserAddressModel;
import com.eviano2o.bean.weixin.WxUserEntInfoModel;
import com.eviano2o.controller.helper.AppLoginHelper;
import com.eviano2o.controller.helper.AppLogoutHelper;
import com.eviano2o.controller.helper.BaseControllerHelper;
import com.eviano2o.controller.helper.DownloadWXUserInfo;
import com.eviano2o.controller.helper.IndexHelper;
import com.eviano2o.controller.helper.LiteappTemplateShopCodeHelper;
import com.eviano2o.controller.helper.Oauth2Helper;
import com.eviano2o.controller.helper.PublicHelper;
import com.eviano2o.controller.helper.QuickShoppingConfirmHelper;
import com.eviano2o.controller.helper.QuickShoppingSaveHelper;
import com.eviano2o.controller.helper.ShareTemplateOneHelper_New;
import com.eviano2o.controller.helper.ShareTemplateTwoHelper_New;
import com.eviano2o.controller.helper.ShoppingCarConfirmHelper;
import com.eviano2o.controller.helper.ShoppingCarSaveHelper;
import com.eviano2o.controller.helper.notifyUrlHelper;
import com.eviano2o.filter.CheckCity;
import com.eviano2o.filter.CheckCityJson;
import com.eviano2o.filter.CheckWeiXinId;
import com.eviano2o.filter.CheckWeiXinLogin;
import com.eviano2o.util.BaiDuRailUtil;
import com.eviano2o.util.DES3_CBCUtil;
import com.eviano2o.util.DateUtil;
import com.eviano2o.util.GetFirstLetterUtil;
import com.eviano2o.util.HttpClientUtil;
import com.eviano2o.util.SessionConstantDefine;
import com.eviano2o.util.SysParamCache;
import com.eviano2o.util.SysParamNames;
import com.eviano2o.util.WebStyle;
import com.eviano2o.util.WxJsTicketUtil;
import com.eviano2o.util.WxTokenAndJsticketCache;
import com.eviano2o.util.userInputDataUtil;
import com.eviano2o.util.response.JsonResponseHelper;
import com.eviano2o.wxpay.PayResponseReturnModel;
import com.eviano2o.wxpay.WxPay;
import com.eviano2o.wxpay.WxPayModel;

@Controller
@RequestMapping("/weixin")
public class WeiXinController extends BaseController {

	
	private static final Logger logger = LoggerFactory.getLogger(WeiXinController.class);
	//logger.error("[remark:{}]", new Object[] { "获取微信OpenId" });
	
	/** 微信事件接口 */
	@RequestMapping(value = "index", produces = "text/plain; charset=utf-8")
	public @ResponseBody String index(Model model, HttpServletRequest request) {
		BaseControllerHelper helper = new IndexHelper(model, request);
		helper.Init();
		return helper.getResult();
	}
	
	/** 微信跳转接口 */
	@RequestMapping(value = "oauth2", method = RequestMethod.GET)
	public String oauth2(Model model, HttpServletRequest request, HttpServletResponse response) {
		logger.info("oauth2--------------------------------------------------------curUrl:"+currentUrl(request));
		long starttime = new Date().getTime();
		
		BaseControllerHelper helper = new Oauth2Helper(model, request, response);
		helper.Init();
		long endtime =new Date().getTime();

		long timediff=endtime-starttime;
		logger.info("[oauth2跳转Url:{}, 所需时间：{} 秒]", new Object[] { helper.getResult(), timediff });
		return helper.getResult();
		// new
		// ModelAndView("redirect:/toList?param1="+value1+"&m2="+value2);这样有个弊端，就是传中文可能会有乱码问题。
		// return "redirect:/namespace/toController";
	}
	
	private ClientModel getAppUserInfo(HttpServletRequest request){
		String uinfo = request.getParameter("uinfo");
		if (StringUtils.isEmpty(uinfo)){
			//_result = formatJsonResult("E90000", "参数为空啦");
			return null;
		}
		
		String clientId = uinfo.split("\\|")[0];
		String appType = uinfo.split("\\|")[1];
		String eid = uinfo.split("\\|")[2];
		System.out.println("clientId = "+clientId+" appType = "+appType+" eid = "+eid);
		if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(appType) ||StringUtils.isEmpty(eid)){
			//_result = formatJsonResult("E90000", "参数错误啦");
			return null;
		}
		
		ClientModel userInfo = weiXinService.getH5UserInfoById(clientId,eid);
		return userInfo;
	}
	
	// app跳转登录到我的积分
	@RequestMapping(value = "appSkipLogin", method = RequestMethod.GET)
	public ModelAndView appSkip(HttpServletRequest request, HttpServletResponse response,String uinfo){
		ClientModel appUserInfo = getAppUserInfo(request);
		if(appUserInfo==null){
			return null;
		}
		JSONObject a = new JSONObject();
		String clientId = uinfo.split("\\|")[0];
		String appType = uinfo.split("\\|")[1];
		String eid = uinfo.split("\\|")[2];
		a.put("clientId", clientId);
		a.put("appType", appType);
		a.put("eid", eid);
		Cookie userCookie=new Cookie("appUserInfo",a.toString());
//        userCookie.setPath("/");
        response.addCookie(userCookie);
        logger.info("212121212121212");
        return new ModelAndView(new RedirectView("myIntegral"));
	}

	/** 清除系统参数缓存 */
	@RequestMapping(value = "clearCache", method = RequestMethod.GET)
	public @ResponseBody String clearCache() {
		SysParamCache.clearCache();
		SysParamCache.setWechatLiteapp(null);
		WxTokenAndJsticketCache.clearTokenAndTickent();
		WxTokenAndJsticketCache.setIsChangeComponent_access_token();
		return "1";
	}

	/** 微信分享跳转接口（因为微信分享出去的link域名必须和js授权接口一致，这里统一做跳转） */
	@RequestMapping(value = "searchRedirect", method = RequestMethod.GET)
	public ModelAndView searchRedirect(Model model, HttpServletRequest request, HttpServletResponse response, String state) {
		//String urlString = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+getSysParamMapValue(SysParamNames.wxParaWeiXinAppID)+"&redirect_uri=https%3A%2F%2F"+getSysParamMapValue(SysParamNames.ParamWXWebSit)+"%2Fweixin%2Foauth2&response_type=code&scope=snsapi_base&state="+state;
		String appId = "";
		if(state.startsWith("productdetail")){
			appId = state.replace("EVIAN", ",").split(",")[3];
			state = state.replace("EVIAN"+appId, "");
		}
		
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=https%3A%2F%2F" + getSysParamMapValue(SysParamNames.ParamWXWebSit) + "%2Fweixin%2Foauth2&response_type=code&scope=snsapi_base&state=" + state + "&component_appid=" + getSysParamMapValue(SysParamNames.wxParaComponentAppID) + "#wechat_redirect";

		return new ModelAndView(new RedirectView(url));
		//return urlString;
	}
	
	

	/** APP 用户登录 */
	@RequestMapping(value = "appLogin", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject appLogin(Model model, HttpServletRequest request, HttpServletResponse response)
	{
		BaseControllerHelper helper = new AppLoginHelper(model, request, response);
		helper.Init();
		return JSONObject.fromObject(helper.getResult());
	}
	
	/** APP 用户退出*/
	@RequestMapping(value = "appLogout", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject appLogout(Model model, HttpServletRequest request, HttpServletResponse response)
	{
		new AppLogoutHelper(model, request, response).Init();
		request.getSession().removeAttribute(SessionConstantDefine.CLIENT_INFO);
		Enumeration em = request.getSession().getAttributeNames();
		  while(em.hasMoreElements()){
		   request.getSession().removeAttribute(em.nextElement().toString());
		}
		return formatJsonResult("E00000", "操作成功");
	}
	
	/** 微信坐标转换百度坐标,返回对应位置信息  傻B腾讯这里的经纬度命名是反的，需要注意  */
	@CheckWeiXinId
	@RequestMapping(value = "changeWxLocationToBaidu", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject changeWxLocationToBaidu(HttpServletRequest request,String lat, String lon) {
		String url = "http://api.map.baidu.com/geoconv/v1/?coords=" + lon + "," + lat + "&from=1&to=5&ak=Xfqil7j1OG6yBLhAoqQKQev4";
		String locationContent = HttpClientUtil.get(url);
		
		if(locationContent == null || StringUtils.isEmpty(locationContent)){
			logger.error("[msg:{}],[url:{}],[webContent:{}]", new Object[] { "转换坐标失败！", url, locationContent });
			return formatJsonResult("E90000", "转换坐标失败！");
		}
		
		JSONObject locationObject = JSONObject.fromObject(locationContent);
		JSONObject resultObject = new JSONObject();
		if(locationObject.has("status") && locationObject.getInt("status") == 0 && locationObject.has("result")){
			resultObject.put("code", "E00000");
			JSONArray locationArray = locationObject.getJSONArray("result");
			if(locationArray.size()>0)
			{
			resultObject.put("lat", locationArray.optJSONObject(0).optDouble("x"));
			resultObject.put("lon", locationArray.optJSONObject(0).optDouble("y"));
			}
			// System.out.println("lat : "+resultObject.getString("lat")+" lon : "+resultObject.getString("lon"));
		}else{
			logger.error("[msg:{}],[url:{}],[webContent:{}]", new Object[] { "转换坐标失败！！", url, locationContent });
			return formatJsonResult("E90000", "转换坐标失败！！");
		}
		

		url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=Xfqil7j1OG6yBLhAoqQKQev4&location="+resultObject.getString("lon")+","+resultObject.getString("lat");
		String cityContent = HttpClientUtil.get(url);
		if(cityContent == null || StringUtils.isEmpty(cityContent)){
			resultObject.put("cityId", 0);
			logger.error("[msg:{}],[url:{}],[webContent:{}]", new Object[] { "获取城市失败！", url, cityContent });
			return resultObject;
		}
		
		JSONObject cityObject = JSONObject.fromObject(cityContent);
		if(cityObject.has("status") && cityObject.getInt("status") == 0){

			resultObject.put("formatted_address", cityObject.getJSONObject("result").getString("formatted_address"));
			resultObject.put("city", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("city"));
			resultObject.put("direction", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("direction"));
			resultObject.put("distance", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("distance"));
			resultObject.put("district", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("district"));
			resultObject.put("province", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("province"));
			resultObject.put("street", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("street"));
			resultObject.put("street_number", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("street_number"));
			resultObject.put("cityCode", cityObject.getJSONObject("result").getInt("cityCode"));
			
			String dbCity = weiXinService.getLocationBrandJson(resultObject.getString("lat"), resultObject.getString("lon"), cityObject.getJSONObject("result").getString("cityCode"),getSessionWXAppId(request));
			if(StringUtils.isEmpty(dbCity)){
				resultObject.put("cityId", 0);
			}else{
				JSONObject dbCityObject = JSONObject.fromObject(dbCity);
				if(dbCityObject.has("code") && dbCityObject.getString("code").equals("E00000")){
					resultObject.put("cityId", dbCityObject.getJSONObject("data").getInt("cityId"));
				}else{
					resultObject.put("cityId", 0);
				}
			}
        }else{
        	logger.error("[msg:{}],[url:{}],[webContent:{}]", new Object[] { "获取城市失败！！", url, cityContent });
        }
		logger.info("baiduResultObject = "+resultObject);
        return resultObject;
	}
	
	/** 百度坐标返回对应位置信息  */
	@CheckWeiXinId
	@RequestMapping(value = "changeBaiduLocationInfo", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject changeBaiduLocationInfo(HttpServletRequest request,String lat, String lon) {
		String url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=Xfqil7j1OG6yBLhAoqQKQev4&location="+lat+","+lon;
		String cityContent = HttpClientUtil.get(url);
		//System.out.println(cityContent);
		JSONObject resultObject = new JSONObject();
		if(cityContent == null || StringUtils.isEmpty(cityContent)){
			logger.error("[msg:{}],[url:{}],[webContent:{}]", new Object[] { "获取城市失败！", url, cityContent });
			return resultObject;
		}
		
		JSONObject cityObject = JSONObject.fromObject(cityContent);
		if(cityObject.has("status") && cityObject.getInt("status")==0){

			resultObject.put("formatted_address", cityObject.getJSONObject("result").getString("formatted_address"));
			resultObject.put("city", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("city"));
			resultObject.put("direction", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("direction"));
			resultObject.put("distance", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("distance"));
			resultObject.put("district", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("district"));
			resultObject.put("province", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("province"));
			resultObject.put("street", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("street"));
			resultObject.put("street_number", cityObject.getJSONObject("result").getJSONObject("addressComponent").getString("street_number"));
			resultObject.put("cityCode", cityObject.getJSONObject("result").getInt("cityCode"));
			
			String dbCity = weiXinService.getLocationBrandJson(lon, lat, cityObject.getJSONObject("result").getString("cityCode"),getSessionWXAppId(request));
			if(StringUtils.isEmpty(dbCity)){
				resultObject.put("cityId", 0);
			}else{
				JSONObject dbCityObject = JSONObject.fromObject(dbCity);
				if(dbCityObject.has("code") && dbCityObject.getString("code").equals("E00000")){
					resultObject.put("cityId", dbCityObject.getJSONObject("data").getInt("cityId"));
				}else{
					resultObject.put("cityId", 0);
				}
			}
        }else{
        	logger.error("[msg:{}],[url:{}],[webContent:{}]", new Object[] { "获取城市失败！！", url, cityContent });
        }

		//System.out.println(resultObject);
        return resultObject;
	}
	
	/** 设置缓冲中的微信access_token超时 */
	@RequestMapping(value = "setTokenTimeOut", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject setTokenTimeOut(String appId) {
		WxTokenAndJsticketCache.setTokenTimeout(appId);
		return formatJsonResult("E00000", "成功");
	}

	/** 设置缓冲中的微信JsTicket超时 */
	@RequestMapping(value = "setJsTicketTimeOut", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject setJsTicketTimeOut(String appId) {
		WxTokenAndJsticketCache.setTicketTimeout(appId);
		return formatJsonResult("E00000", "成功");
	}
	

	/** 是否有微信openid */
	@CheckWeiXinId
	@RequestMapping(value = "checkWeiXinID", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject checkWeiXinID() {
		return formatJsonResult("E00000", "成功");
	}

	/** 是否有登录 */
	@CheckWeiXinLogin
	@RequestMapping(value = "checkWeiXinLogin", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject checkWeiXinLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
		//System.out.println("1."+(request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO)==null) + " | " +(request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO)==null));
		if(getSessionWXAppId(request)==null){
			return formatJsonResult("E00000", "成功");
		}else{
			return formatJsonResult("E00000", "成功","{\"appid\":\""+getSessionWXAppId(request)+"\"}");
		}
	}	

	/** 是否选择定位城市 */
	@CheckCityJson
	@RequestMapping(value = "checkCity", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject checkCity(HttpServletRequest request) {
		return formatJsonResult("E00000", "成功!", getSessionCity(request));
	}	
	
	/** 返回用户手机号 */
	@CheckWeiXinLogin
	@RequestMapping(value = "getUserCellphone", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getUserCellphone(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "E00000"); 
        jsonObject.put("message", "成功");
        jsonObject.put("data", getSessionClient(request).getAccount());
        return jsonObject;
	}
	
	
	
	
	/** 首页 vm */
	@CheckWeiXinId
	@RequestMapping(value = "home", method = RequestMethod.GET)
	public String home(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		//model.addAttribute("ads", weiXinService.getIndexAds());
		WxCityInfoListModel cities = weiXinService.getAllCityInfo(getSessionWXAppId(request));
		System.out.println("fsdfwefsdfsdf22222222222222 = "+cities);
		for(WxCityInfoModel c :cities.getAllCitys()){
			c.setFirstLetter(GetFirstLetterUtil.getFirstLetter(c.getCityName()));
		}
		model.addAttribute("allCity", JSONArray.fromObject(cities.getAllCitys()));
		return "screen/weixin/home";
	}

	/** 46.首页广告栏 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getSysAds", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getSysAds(HttpServletRequest request,String location) {
		return JSONObject.fromObject(weiXinService.getSysAds(location,getSessionWXAppId(request)));
	}

	/** 3.定位获取范围内的品牌信息 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getLocationBrandJson", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getLocationBrandJson(HttpServletRequest request,String lat, String lon) {
		return JSONObject.fromObject(weiXinService.getLocationBrandJson(lat, lon, "", getSessionWXAppId(request)));
	}
	
	/** 21.根据订水电话检索水店 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getTelSearchShopsJson", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getTelSearchShopsJson(HttpServletRequest request,String lat, String lon, String brand) {
		return JSONObject.fromObject(weiXinService.getTelSearchShopsJson(lat, lon, brand,getSessionWXAppId(request)));
	}
	
	/** 22.根据产品品牌检索水店 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getBrandSearchShopsJson", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getBrandSearchShopsJson(HttpServletRequest request,String lat, String lon, String brand) {
		return JSONObject.fromObject(weiXinService.getBrandSearchShopsJson(lat, lon, brand,getSessionWXAppId(request)));
	}
	
	/** 60.获取(同步)购物车商品 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getShopCartProductJson", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getShopCartProductJson(HttpServletRequest request, Integer eid) {
		try{
			if(eid == null)
				eid = 0;
			return JSONObject.fromObject(weiXinService.getShopCartProductJson(getSessionClientIdentityCode(request), eid,getSessionWXAppId(request)));
		}catch(Exception ex){
			logger.info("[错误：{}],[eid:{}]", ex,eid);
			return formatJsonResult("E90000", "系统发生错误");
		}
	}	
	
	/** 61.编辑购物车商品 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "editShopCart", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject editShopCart(String products, String tag, HttpServletRequest request) {
		//model.addAttribute("quickList", weiXinService.getQuickShoppingInfo(getSessionClientIdentityCode(request.getSession())));
		return JSONObject.fromObject(weiXinService.editShopCart(getSessionClientIdentityCode(request), products, tag,getSessionWXAppId(request)));
	}
	
	/** 64.企业的品牌故事 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getBrandStoryJson", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getBrandStoryJson(HttpServletRequest request,Integer eid) {
		//model.addAttribute("quickList", weiXinService.getQuickShoppingInfo(getSessionClientIdentityCode(request.getSession())));
		return JSONObject.fromObject(weiXinService.getBrandStoryJson(eid,getSessionWXAppId(request)));
	}

	/** 66.当前城市上线企业水店统计 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getStatisticsShopJson", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getStatisticsShopJson(String location, HttpServletRequest request) {
		//model.addAttribute("quickList", weiXinService.getQuickShoppingInfo(getSessionClientIdentityCode(request.getSession())));
		return JSONObject.fromObject(weiXinService.getStatisticsShopJson(location, getSessionClientIdentityCode(request),getSessionWXAppId(request)));
	}
	
	/** 93.获取平台活动(首桶免费、半价活动)信息 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getActivityInfoJson", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getActivityInfoJson(HttpServletRequest request) {
		return JSONObject.fromObject(weiXinService.getActivityInfoJson(getSessionClientIdentityCode(request),getSessionWXAppId(request)));
	}
	

	
	
	
	/** 购物车 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "shoppingCar", method = RequestMethod.GET)
	public String shoppingCar(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/shoppingCar";
	}
	
	/** 购物车确认 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "confirmCar")
	public String confirmCar(Model model, HttpServletRequest request) {
		BaseControllerHelper helper = new ShoppingCarConfirmHelper(model, request);
		helper.Init();
		return "screen/weixin/confirmCar";
	}
	
	/** 94.单据确认结算  */
	@CheckWeiXinLogin
	@RequestMapping(value = "shopCartDiscount", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject shopCartDiscount(HttpServletRequest request, String shopCarts) {
		return JSONObject.fromObject(weiXinService.shopCartDiscount(getSessionClientIdentityCode(request), shopCarts, getSessionWeiXinId(request),getSessionWXAppId(request)));
	}
	
	/** 购物车确认 ajax(json) */
	@CheckCityJson
	@CheckWeiXinLogin
	@RequestMapping(value = "shoppingCarConfirmJson", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject shoppingCarConfirmJson(String quickSpJson, Model model, HttpServletRequest request) {
		BaseControllerHelper helper = new ShoppingCarConfirmHelper(model, request);
		helper.Init();
		return JSONObject.fromObject(helper.getResult());
	}
	
	/** 保存购物车  json */
	@CheckWeiXinLogin
	@RequestMapping(value = "shoppingCarSave", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject shoppingCarSave(Model model, HttpServletRequest request){
		BaseControllerHelper helper = new ShoppingCarSaveHelper(model, request);
		helper.Init();
		//System.out.println(helper.getResult());
		return JSONObject.fromObject(helper.getResult());
	}
	
	/** 判断地址坐标是否在百度围栏内(1：是，0：否) */
	@RequestMapping(value = "baiduRailLocation", produces = "application/json; charset=utf-8")
	public @ResponseBody String baiduRailLocation(String addressLocation, String baiduRail){
		if(StringUtils.isEmpty(addressLocation) || StringUtils.isEmpty(baiduRail))
			return "-1";
		return (BaiDuRailUtil.checkWithJdkGeneralPathStrParam(addressLocation, baiduRail) ? "1" : "0");
	}
	
	
	
	
	
	/** 搜索页面 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "searchShopOrProduct", method = RequestMethod.GET)
	public String searchShopOrProduct(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/searchShopOrProduct";
	}

	
	
	/** 水店列表 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "shops", method = RequestMethod.GET)
	public String shops(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/shops";
	}
	
	/** 4.获取品牌下的水店列表 ajax(json) */
	@CheckWeiXinLogin
	@CheckCityJson
	@RequestMapping(value = "getBrandShopList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getBrandShopList(String shopMap, String location, HttpServletRequest request) {
		return JSONObject.fromObject(weiXinService.getBrandShopListJson(shopMap, getSessionCity(request).getCityId(), StringUtils.isEmpty(location) ? "" : location,getSessionWXAppId(request)));
	}
	
	
    /*double DEF_PI = 3.14159265359; // PI
    double DEF_2PI = 6.28318530712; // 2*PI
    double DEF_PI180 = 0.01745329252; // PI/180.0
    double DEF_R = 6370693.5; // radius of earth
    /// <summary>
    /// 百度地图两点直线距离
    /// </summary>
    /// <param name="lon1"></param>
    /// <param name="lat1"></param>
    /// <param name="lon2"></param>
    /// <param name="lat2"></param>
    /// <returns></returns>
    private double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
            dew = DEF_2PI + dew;
        dx = DEF_R * Math.Cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.Sqrt(dx * dx + dy * dy);
        return distance;
    }*/

	
	
	/** 商品列表 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "products", method = RequestMethod.GET)
	public String products(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/products";
	}
	
	/** 店铺注册 vm */
	@CheckWeiXinId
	@RequestMapping(value = "shopRegister", method = RequestMethod.GET)
	public String shopRegister(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/shopRegister";
	}
	
	/** 11.获取水店产品类别、产品信息 ajax(json) */
	@CheckWeiXinLogin
	@CheckCityJson
	@RequestMapping(value = "getShopProduct", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getShopProduct(HttpServletRequest request,Integer shopId) {
		return JSONObject.fromObject(weiXinService.getShopProductJson(shopId,getSessionWXAppId(request)));
	}
	@RequestMapping(value = "getShopProductJson", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getShopProductJson(HttpServletRequest request,Integer shopId) {
		return JSONObject.fromObject(weiXinService.getShopProductJson(shopId,getSessionWXAppId(request)));
	}
	
	/** 26.获取商户信息 ajax(json) */
	@CheckWeiXinLogin
	@CheckCityJson
	@RequestMapping(value = "getShopById", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getShopById(HttpServletRequest request,Integer shopId) {
		return JSONObject.fromObject(weiXinService.getShopByIdJson(shopId,getSessionWXAppId(request)));
	}	
	
	/** 67.企业的客服信息 Json ajax(json) */
	@CheckWeiXinLogin
	@CheckCityJson
	@RequestMapping(value = "getdEnterpriseService", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getdEnterpriseService(HttpServletRequest request,Integer eid) {
		return JSONObject.fromObject(weiXinService.getdEnterpriseServiceJson(eid,getSessionWXAppId(request)));
	}	
	
	/** 67.我的评价<分页> Json ajax(json) */
	@CheckWeiXinLogin
	@CheckCityJson
	@RequestMapping(value = "getMyEvaluates", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getMyEvaluates(Integer pageIndex, HttpServletRequest request) {
		return JSONObject.fromObject(weiXinService.getMyEvaluates(getSessionClientIdentityCode(request), pageIndex,getSessionWXAppId(request)));
	}
	
	/** 31.获取客户收藏的水店信息 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getCollectShops", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getCollectShops(HttpServletRequest request) {
		return JSONObject.fromObject(weiXinService.getCollectShopsJson(getSessionClientIdentityCode(request),getSessionWXAppId(request)));
	}
	
	/** 32.添加收藏产品/水店 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "addCollect", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject addCollect(HttpServletRequest request, Integer collectId, Integer type) {
		return JSONObject.fromObject(weiXinService.addCollect(getSessionClientIdentityCode(request), collectId, type,getSessionWXAppId(request)));
	}
	
	/** 33.删除收藏产品/水店 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "delCollect", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject delCollect(HttpServletRequest request, Integer collectId, Integer type) {
		return JSONObject.fromObject(weiXinService.delCollect(getSessionClientIdentityCode(request), collectId, type,getSessionWXAppId(request)));
	}
	
	/** 30.获取客户收藏的产品信息 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getCollectProducts", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getCollectProducts(HttpServletRequest request) {
		return JSONObject.fromObject(weiXinService.getCollectProductsJson(getSessionClientIdentityCode(request),getSessionWXAppId(request)));
	}
	
	
	/** 27.获取商户评价 Json ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getShopEvaluate", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getShopEvaluate(HttpServletRequest request,Integer shopId,Integer evaluateId) {
		return JSONObject.fromObject(weiXinService.getShopEvaluate(shopId,evaluateId,getSessionWXAppId(request)));
	}	
	
	
	/** 商品详情 vm */
	@CheckWeiXinId
//	@CheckCity
	@RequestMapping(value = "productDetail", method = RequestMethod.GET)
	public String productDetail(Model model, HttpServletRequest request, Integer shopId, Integer pid) {
		new PublicHelper(model, request);
		model.addAttribute("shareLink", "https://"+getSysParamMapValue(SysParamNames.ParamWXWebSit)+"/weixin/searchRedirect?state=productdetailEVIAN"+shopId+"EVIAN"+pid+"EVIAN"+getSessionWXAppId(request));
		return "screen/weixin/productDetail";
	}
	
	/** 28.获取产品信息 Json ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getProductDetail", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getProductDetail(HttpServletRequest request,Integer pid, Integer shopId) {
		JSONObject fromObject = JSONObject.fromObject(weiXinService.getProductDetailJson(pid, shopId,getSessionWXAppId(request)));
		JSONObject jsonObject = fromObject.getJSONObject("data");
		if(jsonObject!=null){
			JSONObject jsonObject2 = jsonObject.getJSONObject("product");
			if(jsonObject2!=null){
				String jsonObject3 = jsonObject2.getString("imageTextUrl");
				if(jsonObject3!=null){
					String string = HttpClientUtil.get(jsonObject3);
					jsonObject2.put("imageTextUrl", string);
					jsonObject.put("product", jsonObject2);
					fromObject.put("data", jsonObject);
				}
			}
		}
		return fromObject;
	}	
	
	/** 38.获取客户对产品单个消费习惯信息 Json ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getProductHabit", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getProductHabit(Integer shopId, Integer pid, HttpServletRequest request) {
		return JSONObject.fromObject(weiXinService.getProductHabitJson(getSessionClientIdentityCode(request), shopId, pid,getSessionWXAppId(request)));
	}	
	
	/** 39.新增/修改单个产品的消费习惯 Json ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "addClientHabit", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject addClientHabit(String habit, HttpServletRequest request) {
		return JSONObject.fromObject(weiXinService.addClientHabit(getSessionClientIdentityCode(request), habit,getSessionWXAppId(request)));
	}
	
	/** 29.获取产品评价集合(分页) Json ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getProductEvaluates", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getProductEvaluates(HttpServletRequest request,Integer pid, Boolean isPic, Integer grade, Integer pageIndex) {
		return JSONObject.fromObject(weiXinService.getProductEvaluatesJson(pid, isPic, grade, pageIndex,getSessionWXAppId(request)));
	}	
	
	/** 商品评价 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "productEvaluate")
	public String productEvaluate(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/productEvaluate";
	}	
	
	/** 我的评价 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "myEvaluate")
	public String myEvaluate(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/myEvaluate";
	}
	
	/** 兑换优惠码 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "imaCode", method = RequestMethod.GET)
	public String imaCode(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/imaCode";
	}
	
	/** 72.注册激活优惠券 ajax(json) */
	@CheckWeiXinId
	@CheckCityJson
	@RequestMapping(value = "codeConvert", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject codeConvert(String code_no, HttpServletRequest request) {
		if (StringUtils.isEmpty(code_no))
			return formatJsonResult("E90000", "参数不能为空");

		String result = weiXinService.codeConvert(getSessionClientIdentityCode(request), code_no, getSessionCity(request).getCityId(), getSessionCity(request).getBaiduCode(),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 77.获取优惠券能购买的商品信息 ajax(json) */
	@CheckWeiXinLogin
	@CheckCityJson
	@RequestMapping(value = "voucherToGoods", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject voucherToGoods(Integer shopId, Integer code_id, HttpServletRequest request) {
		if (shopId == null || code_id == null)
			return formatJsonResult("E90000", "参数不能为空");
		
		String result = weiXinService.voucherToGoods(getSessionClientIdentityCode(request), shopId, code_id,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 78.券赠与他人 ajax(json) */
	@CheckWeiXinLogin
	@CheckCityJson
	@RequestMapping(value = "voucherGive", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject voucherGive(String account, String code_no, HttpServletRequest request) {
		if (StringUtils.isEmpty(code_no))
			return formatJsonResult("E90000", "参数不能为空");
		
		if (StringUtils.isEmpty(account))
			return formatJsonResult("E90000", "参数不能为空");
		
		String result = weiXinService.voucherGive(getSessionClientIdentityCode(request), account, code_no,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}	
	
	/** 优惠券可用商品 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "codeProducts", method = RequestMethod.GET)
	public String codeProducts(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/codeProducts";
	}	
	
	
	
	/** 定位选择城市 vm */
	@CheckWeiXinId
	@RequestMapping(value = "city", method = RequestMethod.GET)
	public String city(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		WxCityInfoListModel cities = weiXinService.getAllCityInfo(getSessionWXAppId(request));
		System.out.println("fsdfwefsdfsdf = "+cities);
		for(WxCityInfoModel c :cities.getAllCitys()){
			c.setFirstLetter(GetFirstLetterUtil.getFirstLetter(c.getCityName()));
		}
		model.addAttribute("allCity", cities);
		return "screen/weixin/city";
	}
	
	/** 101.保存用户定位城市到request.getSession() ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "saveCity", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject saveCity(Integer cityId, String location, HttpServletResponse response, HttpServletRequest request) {
		WxCityInfoModel curCity = weiXinService.getCityInfo(cityId,getSessionWXAppId(request));
		if(curCity == null){
			return formatJsonResult("E90001", "该城市不存在");
		}else{
			logger.error("---------------------------curCity = "+curCity);
			request.getSession().setAttribute(SessionConstantDefine.WX_CITY, curCity);
			request.getSession().setMaxInactiveInterval(30*60);
			return JSONObject.fromObject(weiXinService.saveWxUserCity(getSessionWeiXinId(request), cityId, location,getSessionWXAppId(request)));
			//return formatJsonResult("E00000", "成功");
		}
	}
	
	
	
	
	/** 快速订水 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "quickShopping", method = RequestMethod.GET)
	public String quickShopping(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/quickShopping";
	}
	
	/** 43.快速订水商品 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "quickShoppingJson", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject quickShoppingJson(Model model, HttpServletRequest request) {
		//model.addAttribute("quickList", weiXinService.getQuickShoppingInfo(getSessionClientIdentityCode(request.getSession())));
		return JSONObject.fromObject(weiXinService.getQuickShoppingJson(getSessionClientIdentityCode(request),getSessionWXAppId(request)));
	}
	
	/** 80.获取商品能使用的优惠券 ajax(json) */
	@CheckCityJson
	@CheckWeiXinLogin
	@RequestMapping(value = "productVouchers", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject productVouchers(Integer pid, HttpServletRequest request) {
		return JSONObject.fromObject(weiXinService.getProductVouchersJson(getSessionClientIdentityCode(request), 2, pid,getSessionWXAppId(request)));
	}

	
	/** 确认快速订水商品  vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "quickShoppingConfirm")
	public String quickShoppingConfirm(String quickSpJson, Model model, HttpServletRequest request) {
		BaseControllerHelper helper = new QuickShoppingConfirmHelper(model, request);
		helper.Init();
		return "screen/weixin/quickShoppingConfirm";
	}
	
	/** 确认快速订水商品 ajax(json) */
	@CheckCityJson
	@CheckWeiXinLogin
	@RequestMapping(value = "quickShoppingConfirmJson", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject quickShoppingConfirmJson(String quickSpJson, Model model, HttpServletRequest request) {
		BaseControllerHelper helper = new QuickShoppingConfirmHelper(model, request);
		helper.Init();
		return JSONObject.fromObject(helper.getResult());
	}
	
	/** 保存快速订水  json */
	@CheckWeiXinLogin
	@RequestMapping(value = "quickShoppingSave", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject quickShoppingSave(Model model, HttpServletRequest request){
		BaseControllerHelper helper = new QuickShoppingSaveHelper(model, request);
		helper.Init();
		return JSONObject.fromObject(helper.getResult());
	}
	
	
	
	/** 订单组支付  json */
	@CheckWeiXinLogin
	@RequestMapping(value = "payGroup", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject payGroup(String group, Model model, HttpServletRequest request){
		/*为了避免微信支付不启用但商品必须在线支付的尴尬情况发生，所以无论任何情况，视微信支付为启用。*/
		
		if (StringUtils.isEmpty(group))
			return formatJsonResult("E90000", "参数不能为空");
		String webContent = weiXinService.wxToPayMoney(getSessionClientIdentityCode(request), group,getSessionWXAppId(request));
		if(StringUtils.isEmpty(webContent)){
			return formatJsonResult("E90000", "订单不存在");
		}
		
		
		JSONObject moneyJson = JSONObject.fromObject(webContent);
		if(moneyJson == null || !moneyJson.has("code") || !moneyJson.getString("code").equals("E00000")){
			return formatJsonResult("E90000", "获取支付金额失败");
		}
		
		if(StringUtils.isEmpty(moneyJson.getJSONObject("data").getString("mchid")) || StringUtils.isEmpty(moneyJson.getJSONObject("data").getString("partnerKey"))){
			return formatJsonResult("E90000", "支付信息不完整");
		}
		
		double totalMoney = moneyJson.getJSONObject("data").getDouble("linePayTotal");
		if(totalMoney <= 0){
			return formatJsonResult("E90000", "无订单金额，无需在线支付！");
		}
		
		// 调用160接口发起支付记录 记录成功后才能往下执行
		String result1 = weiXinService.paysendrecord(getSessionClientIdentityCode(request),group,"微信",Double.toString(totalMoney),2,getSessionWXAppId(request));
		JSONObject fo = JSONObject.fromObject(result1);
		if("E00000".equals(fo.getString("code"))){
			//String sub_mch_id = moneyJson.getJSONObject("data").getString("mchid");
			
			WxPayModel config = new WxPayModel();
			//config.setMchId(getSysParamMapValue(SysParamNames.wxParaWeiXinMCHID));
			//config.setAppId(getSysParamMapValue(SysParamNames.wxParaWeiXinAppID));
			//config.setAppSecret(getSysParamMapValue(SysParamNames.wxParaWeiXinSecret));
			//config.setAppKey(getSysParamMapValue(SysParamNames.wxParaWeiXinPartnerKey));
			config.setAppId(getSessionWXAppId(request));
			config.setMchId(moneyJson.getJSONObject("data").getString("mchid"));
			config.setAppKey(DES3_CBCUtil.des3DecodeCBC(moneyJson.getJSONObject("data").getString("partnerKey")));
			//config.setNotifyUrl(notifyPayUrl);
			config.setNotifyUrl("https://" + getSysParamMapValue(SysParamNames.ParamWXWebSit) + "/weixin/notifyUrl");
			config.setOrderSN(group.toUpperCase());
			config.setBody("shuiqu");
			config.setOpenId(getSessionWeiXinId(request));
			config.setTotalFee(String.valueOf((int)(totalMoney * 100)));
			config.setNonceStr(WxJsTicketUtil.create_nonce_str());
			config.setTimeStamp(WxJsTicketUtil.create_timestamp());
			config.setCreateIp(getIp(request));
			//config.setSubMchId(sub_mch_id);
			
			PayResponseReturnModel result = new WxPay().Pay(config);
			result.setDanHao(group);
			return JSONObject.fromObject(result);
		}else{
			// 发起错误 返回错误信息
			return fo;
		}
	}

	/** 单个订单支付  json */
	@CheckWeiXinLogin
	@RequestMapping(value = "payOrder", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject payOrder(Integer orderId, String orderGroup, Model model, HttpServletRequest request){
		/*为了避免微信支付不启用但商品必须在线支付的尴尬情况发生，所以无论任何情况，视微信支付为启用。*/
	
		if (orderId == null)
			return formatJsonResult("E90000", "参数不能为空");
		if (StringUtils.isEmpty(orderGroup))
			return formatJsonResult("E90000", "参数不能为空");
		String webContent = weiXinService.orderAgainPay(getSessionClientIdentityCode(request), orderId, orderGroup,getSessionWXAppId(request));
		logger.info("继续支付---------------------------------------------------------------------------- " + webContent);
		if(StringUtils.isEmpty(webContent)){
			return formatJsonResult("E90000", "订单不存在");
		}
		JSONObject order = JSONObject.fromObject(webContent);
		if(order == null || !order.has("code") || !order.getString("code").equals("E00000")){
			logger.info("获取单个订单再次支付失败:" + webContent + "       orderId:" + orderId);
			return formatJsonResult("E90000", "订单不存在!");
		}

		
		if(StringUtils.isEmpty(order.getJSONObject("data").getString("mchid")) || StringUtils.isEmpty(order.getJSONObject("data").getString("partnerKey"))){
			return formatJsonResult("E90000", "支付信息不完整");
		}
		
		double totalMoney = order.getJSONObject("data").getDouble("linePayTotal");
		if(totalMoney <= 0){
			return formatJsonResult("E90000", "无订单金额，无需在线支付！");
		}
		//String sub_mch_id = order.getJSONObject("data").getString("mchid");
		String group = order.getJSONObject("data").getString("orderGroup");
		//String notifyPayUrl = EvianHelpDESUtil.decrypt(order.getJSONObject("data").getJSONObject("payParams").getString("WxPayNotifyUrl"));
		
		WxPayModel config = new WxPayModel();
		
		config.setAppId(getSessionWXAppId(request));
		//config.setAppSecret(getSysParamMapValue(SysParamNames.wxParaWeiXinSecret));
		config.setMchId(order.getJSONObject("data").getString("mchid"));
		config.setAppKey(DES3_CBCUtil.des3DecodeCBC(order.getJSONObject("data").getString("partnerKey")));
		//config.setNotifyUrl(notifyPayUrl);
		config.setNotifyUrl("https://" + getSysParamMapValue(SysParamNames.ParamWXWebSit) + "/weixin/notifyUrl");
		config.setOrderSN(group);
		config.setBody("shuiqu");
		config.setOpenId(getSessionWeiXinId(request));
		config.setTotalFee(String.valueOf((int)(totalMoney * 100)));
		config.setNonceStr(WxJsTicketUtil.create_nonce_str());
		config.setTimeStamp(WxJsTicketUtil.create_timestamp());
		config.setCreateIp(getIp(request));
		//config.setSubMchId(sub_mch_id);
		
		PayResponseReturnModel result = new WxPay().Pay(config);
		result.setDanHao(group);
		return JSONObject.fromObject(result);
	}
	
	/** 微信支付回调  json */
	@RequestMapping(value = "notifyUrl")
	public @ResponseBody String notifyUrl(Model model, HttpServletRequest request, HttpServletResponse response){
		logger.info("微信支付回调！");
		BaseControllerHelper helper = new notifyUrlHelper(model, request, response);
		helper.Init();
		return helper.getResult();
	}
	
	
	
	
	/** 订单列表 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "orders", method = RequestMethod.GET)
	public String orders(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/orders";
	}
	
	/** 37.根据状态获取单据列表 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getStatusOrders", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getStatusOrders(Integer statusId, Integer pageIndex, HttpServletRequest request) {
		String result = weiXinService.getStatusOrdersJson(getSessionClientIdentityCode(request), statusId, pageIndex,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 61.取消订货单据 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "cancelOrder", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject cancelOrder(Integer orderId, String reason, HttpServletRequest request) {
		String result = weiXinService.cancelOrder(getSessionClientIdentityCode(request), orderId, reason,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 62.后台设置的订单取消原因 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "orderCancelReason", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject orderCancelReason(HttpServletRequest request) {
		String result = weiXinService.orderCancelReason(getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 47.删除完成的单据 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "delOrder", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject delOrder(Integer orderId, HttpServletRequest request) {
		String result = weiXinService.delOrder(getSessionClientIdentityCode(request), orderId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 48.单据确认收货 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "reapOrder", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject reapOrder(Integer orderId, HttpServletRequest request) {
		String result = weiXinService.reapOrder(getSessionClientIdentityCode(request), orderId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	/** 退货/取消订单 vm */
	@CheckWeiXinId
	@CheckCityJson
	@RequestMapping(value = "canceled", method = RequestMethod.GET)
	public String canceled(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/canceled";
	}
	
	
	
	
	/** 订单详情 vm */
	@CheckWeiXinLogin
	@CheckCity
	@RequestMapping(value = "orderDetail", method = RequestMethod.GET)
	public String orderDetail(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/orderDetail";
	}
	
	/** 49.获取订单明细 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getOrderDetail", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getOrderDetail(Integer orderId, HttpServletRequest request) {
		String result = weiXinService.getOrderDetailJson(getSessionClientIdentityCode(request), orderId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 25.获取单据物流信息 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getOrderLog", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getOrderLog(Integer orderId, HttpServletRequest request) {
		String result = weiXinService.getOrderLogJson(getSessionClientIdentityCode(request), orderId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	
	
	
	/** 用户地址 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "address", method = RequestMethod.GET)
 	public String address(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		/*if(!StringUtils.isEmpty(shopStr))
		{
			String shopLocationString = weiXinService.getShopsWeiLans(shopStr);
			JSONObject shopLocationJsonObject = JSONObject.fromObject(shopLocationString);
			if(shopLocationJsonObject.containsKey("code") && shopLocationJsonObject.getString("code").equals("E00000") 
					&& shopLocationJsonObject.getJSONObject("data").getInt("count") > 0){
				//初始化地图中心取第一门店的第一个围栏点
				String iniPointString = JSONObject.fromObject(shopLocationJsonObject.getJSONObject("data").getJSONArray("shopLocations").getString(0)).getString("location");
			}
		}*/
		return "screen/weixin/address";
	}
	
	/** 150.获取水店集合的围栏信息 ajax(json) */
	@RequestMapping(value = "getShopsWeiLans", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getShopsWeiLans(HttpServletRequest request, String shopStr) {
		if(StringUtils.isEmpty(shopStr)){
			return JSONObject.fromObject("{\"code\":\"E00009\"}");
		}else{
			shopStr = shopStr.substring(0, shopStr.length() - 1);
		}
		String result = weiXinService.getShopsWeiLans(shopStr,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 14. 用户地址信息 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "userAddress", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject userAddress(HttpServletRequest request) {
		String result = weiXinService.getUserAddressListJsonStr(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 15. 添加用户地址 ajax(json) 需要讨论如何获取 标识物 和 标识物具体地址 */
	@CheckCityJson
	@CheckWeiXinLogin
	@RequestMapping(value = "addUserAddress", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject addUserAddress(@ModelAttribute("requestModel") WxUserAddressModel requestModel, HttpServletRequest request) {
		
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", getSessionClientIdentityCode(request)));
		params.add(new BasicNameValuePair("streetName", requestModel.getStreetName()));
		params.add(new BasicNameValuePair("streetDescribe", requestModel.getStreetDescribe()));
		params.add(new BasicNameValuePair("doorplate", requestModel.getDoorplate()));
		params.add(new BasicNameValuePair("phone", requestModel.getPhone()));
		params.add(new BasicNameValuePair("contacts", requestModel.getContacts()));
		params.add(new BasicNameValuePair("location", requestModel.getLocation()));
		params.add(new BasicNameValuePair("tag", requestModel.getTag()));	
		params.add(new BasicNameValuePair("sdkType", "3"));	
		params.add(new BasicNameValuePair("authorizer_appid",getSessionWXAppId(request)));	
		return JSONObject.fromObject(weiXinService.addAddress(params));
	}
	
	/** 149.根据坐标点新增地址V2 ajax(json) */
	@CheckCityJson
	@CheckWeiXinLogin
	@RequestMapping(value = "addAddress_v2", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject addAddress_v2(@ModelAttribute("requestModel") WxUserAddressModel requestModel, HttpServletRequest request) {
		
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", getSessionClientIdentityCode(request)));
		params.add(new BasicNameValuePair("streetName", requestModel.getStreetName()));
		params.add(new BasicNameValuePair("address", requestModel.getAddress()));
		params.add(new BasicNameValuePair("phone", requestModel.getPhone()));
		params.add(new BasicNameValuePair("contacts", requestModel.getContacts()));
		params.add(new BasicNameValuePair("location", requestModel.getLocation()));
		params.add(new BasicNameValuePair("tag", requestModel.getTag()));	
		params.add(new BasicNameValuePair("sdkType", "3"));
		params.add(new BasicNameValuePair("authorizer_appid",getSessionWXAppId(request)));
		return JSONObject.fromObject(weiXinService.addAddress_v2(params));
	}

	/** 16.编辑客户地址 */
	@CheckCityJson
	@CheckWeiXinLogin
	@RequestMapping(value = "editAddress", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject editAddress(@ModelAttribute("requestModel") WxUserAddressModel requestModel, HttpServletRequest request) {
		
		List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("clientId", getSessionClientIdentityCode(request)));
		params.add(new BasicNameValuePair("did", requestModel.getDid().toString()));
		params.add(new BasicNameValuePair("streetName", requestModel.getStreetName()));
		params.add(new BasicNameValuePair("streetDescribe", requestModel.getStreetDescribe()));
		params.add(new BasicNameValuePair("doorplate", requestModel.getDoorplate()));
		params.add(new BasicNameValuePair("phone", requestModel.getPhone()));
		params.add(new BasicNameValuePair("contacts", requestModel.getContacts()));
		params.add(new BasicNameValuePair("location", requestModel.getLocation()));
		params.add(new BasicNameValuePair("tag", requestModel.getTag()));	
		params.add(new BasicNameValuePair("sdkType", "3"));
		params.add(new BasicNameValuePair("authorizer_appid",getSessionWXAppId(request)));
		return JSONObject.fromObject(weiXinService.editAddress(params));
	}
	
	/** 17. 设置默认地址 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "setDefaultAddress", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject setDefaultAddress(Integer did, HttpServletRequest request) {
		if (did == null)
			return formatJsonResult("E90000", "参数不能为空");

		List<WxUserAddressModel> addressList = weiXinService.getUserAddressList(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		if(addressList == null)
			return formatJsonResult("E90000", "没有该信息");
		else{
			//System.out.println(did);
			Boolean isBelongUser = false;
			for(WxUserAddressModel address : addressList)
			{
				//System.out.println(address.getDid() +" | " + did + " | " + (address.getDid() == did));
				if(address.getDid().intValue() == did.intValue()){
					//System.out.println(isBelongUser);
					isBelongUser = true;
				}
			}
			//System.out.println(":"+isBelongUser);
			if(!isBelongUser)
				return formatJsonResult("E90000", "您不能修改该信息");
		}
		
		return JSONObject.fromObject(weiXinService.defaultAddress(getSessionClientIdentityCode(request), did,getSessionWXAppId(request)));
	}
																																																																																																																		
	/** 18. 删除地址 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "delAddress", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject delAddress(Integer did, HttpServletRequest request) {
		if (did == null)
			return formatJsonResult("E90000", "参数不能为空");

		List<WxUserAddressModel> addressList = weiXinService.getUserAddressList(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		if(addressList == null)
			return formatJsonResult("E90000", "没有该信息");
		else{
			Boolean isBelongUser = false;
			for(WxUserAddressModel address : addressList)
			{
				if(address.getDid().intValue() == did.intValue())
					isBelongUser = true;
			}
			if(!isBelongUser)
				return formatJsonResult("E90000", "您不能修改该信息");
		}
		
		return JSONObject.fromObject(weiXinService.delAddress(getSessionClientIdentityCode(request), did,getSessionWXAppId(request)));
	}	
	
	/** 90.获取客户的地址标签集合 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getAddressTags", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getAddressTags(HttpServletRequest request) {
		String result = weiXinService.getAddressTags(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 91.新增客户的地址标签 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "addAddressTags", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject addAddressTags(String tag, HttpServletRequest request) {
		if (StringUtils.isEmpty(tag))
			return formatJsonResult("E90000", "参数不能为空");
		
		String result = weiXinService.addAddressTags(getSessionClientIdentityCode(request), tag,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	
	/** 我的 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "account", method = RequestMethod.GET)
 	public String account(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
//		if(WebStyle.getStyle().equals("dalishuishou"))
//			return "screen/weixin/tianjinAccount"; 
		return "screen/weixin/account";
	}
	
	/** 20.获取客户信息 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getClientInfo", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getClientInfo(HttpServletRequest request) {
		String result = weiXinService.getClientInfoJson(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 重新下载微信资料(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "freshWXInfo", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject freshWXInfo(HttpServletRequest request) {
		new DownloadWXUserInfo(request, WxTokenAndJsticketCache.getAccess_token(getSessionWXAppId(request)), getSessionWeiXinId(request), "", 0, request.getSession().getServletContext().getRealPath("/"), getSessionWXAppId(request)).doDownload(0);
		return formatJsonResult("E00000", "");
	}
	
	
	
	
	/** 消费习惯 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "hibits", method = RequestMethod.GET)
 	public String hibits(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/hibits";
	}
	
	/** 40.客户所有消费习惯《按水店分组显示》 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getClientAllHibits", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getClientAllHibits(HttpServletRequest request) {
		String result = weiXinService.getClientAllHibitsJson(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 41.批量编辑客户消费习惯 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "editClientHabits", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject editClientHabits(String habit, HttpServletRequest request) {
		String result = weiXinService.editClientHabits(getSessionClientIdentityCode(request), habit,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 42.批量删除客户消费习惯 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "delClientHabits", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject delClientHabits(String habit, HttpServletRequest request) {
		String result = weiXinService.delClientHabits(getSessionClientIdentityCode(request), habit,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	
	
	/** 73.获取我的代金券/优惠券明细 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getMyVouchers", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getMyVouchers(HttpServletRequest request) {
		String result = weiXinService.getMyVouchersJson(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		result = result.replace("null", "\"\"");
		return JSONObject.fromObject(result);
	}
	
	/** 92.获取客户积分汇总和优惠券汇总 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getIntegralCodeNum", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getIntegralCodeNum(HttpServletRequest request) {
		String result = weiXinService.getIntegralCodeNumJson(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 76.获取优惠券下的水店信息(根据城市区域) ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "voucherToShopByDistrict", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject voucherToShopByDistrict(HttpServletRequest request, Integer code_id, Integer districtId) {
		String result = weiXinService.voucherToShopByDistrictJson(getSessionClientIdentityCode(request), getSessionCity(request).getCityId(), code_id, districtId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 77.获取优惠券下的水店信息(根据定位) ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "voucherToShopByLocation", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject voucherToShopByLocation(HttpServletRequest request, Integer code_id, Integer cityId, String location) {
		if(cityId==null || cityId.intValue()==0)
			cityId = getSessionCity(request).getCityId();
		String result = weiXinService.voucherToShopByLocationJson(getSessionClientIdentityCode(request), cityId, code_id, location,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 优惠券可用门店 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "codeShops", method = RequestMethod.GET)
	public String codeShops(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/codeShops";
	}	
	
	
	
	/** 用户绑定 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "bind", method = RequestMethod.GET)
	public String bind(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/bind";
	}
	
	/** 用户协议 vm */
	@CheckWeiXinId
	@RequestMapping(value = "agreement", method = RequestMethod.GET)
	public String agreement(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/agreement";
	}
	
	/** 2.保存用户手机绑定 ajax(json) */
	@CheckWeiXinId
	@CheckCityJson
	@RequestMapping(value = "saveBind", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject saveBind(String cellphone, String code, String location, HttpServletRequest request) {
		if (StringUtils.isEmpty(cellphone) || StringUtils.isEmpty(code))
			return formatJsonResult("E90000", "参数不能为空");

		if(!userInputDataUtil.isMobileNO(cellphone))
			return formatJsonResult("E90000", "手机号错误");
		
		String result = weiXinService.saveBind(cellphone, code, getSessionCity(request).getCityId(), getSessionWeiXinId(request), location, getSessionWXAppId(request));
		if(result.indexOf("E00000") > 0){
			ClientModel userInfo = weiXinService.getWxLogin(getSessionWeiXinId(request),getSessionWXAppId(request));
			request.getSession().setAttribute(SessionConstantDefine.CLIENT_INFO, userInfo);
		}
		return JSONObject.fromObject(result);
	}

	/** 手机验证码 返回错误码 E90000、E90001 (E90001时message为剩余秒数) ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "moblieMsg", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject moblieMsg(String cellphone, HttpServletRequest request) {
		if (StringUtils.isEmpty(cellphone))
			return formatJsonResult("E90000", "参数不能为空");
		
		if(!userInputDataUtil.isMobileNO(cellphone))
			return formatJsonResult("E90000", "手机号错误");
		
		if (request.getSession().getAttribute("bindMsgTime") != null) {
			Long nextSendMsgTimeOut = Long.parseLong(request.getSession().getAttribute("bindMsgTime").toString());
			Long currentTimeMilli = System.currentTimeMillis() / 1000;
			if (currentTimeMilli > nextSendMsgTimeOut){
				nextSendMsgTimeOut = System.currentTimeMillis() / 1000 + Integer.parseInt(getSysParamMapValue(SysParamNames.ParamCustomerMobileMsgStep));
				request.getSession().setAttribute("bindMsgTime", nextSendMsgTimeOut);
			}else{
				return formatJsonResult("E90001",String.valueOf((nextSendMsgTimeOut - currentTimeMilli) < 0 ? 0 : (nextSendMsgTimeOut - currentTimeMilli)));
			}
		}
		
		String result = weiXinService.getCode(cellphone, getSessionWeiXinId(request), getSessionWXAppId(request));
		if(result.contains("E00000")){
			Long nextSendMsgTimeOut = System.currentTimeMillis() / 1000 + Integer.parseInt(getSysParamMapValue(SysParamNames.ParamCustomerMobileMsgStep));
			request.getSession().setAttribute("bindMsgTime", nextSendMsgTimeOut);
			return formatJsonResult("E00000", getSysParamMapValue(SysParamNames.ParamCustomerMobileMsgStep));
		}
		
		return JSONObject.fromObject(result);
	}

	/** 注册协议 */
	@RequestMapping(value = "registerAgreement", produces = "text/html; charset=utf-8")
	public @ResponseBody String registerAgreement(Model model) {
		return HttpClientUtil.get(getSysParamMapValue(SysParamNames.ParamRegisterAgreement));
	}

	
	
	
	
	/** 我要评价 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "evaluate", method = RequestMethod.GET)
	public String evaluate(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/evaluate";
	}
	
	/** 35.我要评价单据 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "toOrderEvaluate", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject toOrderEvaluate(HttpServletRequest request, Integer orderId, Integer shopId) {
		String result = weiXinService.toOrderEvaluate(getSessionClientIdentityCode(request), orderId, shopId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 36.保存单据评价 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "addOrderEvaluate", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject addOrderEvaluate(HttpServletRequest request, Integer orderId, Integer shopId, String orderEvaluate, String productEvalutes) {
		String result = weiXinService.addOrderEvaluate(getSessionClientIdentityCode(request), orderId, shopId, orderEvaluate, productEvalutes,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	
	
	/** 优惠券 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "coupon", method = RequestMethod.GET)
	public String coupon(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/coupon";
	}	
	
	/** 优惠券详情 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "couponDetail", method = RequestMethod.GET)
	public String couponDetail(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/couponDetail";
	}
	
	
	
	
	/** 商户入驻 vm */
	@CheckWeiXinId
	@CheckCity
	@RequestMapping(value = "seller", method = RequestMethod.GET)
	public String seller(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/seller";
	}	
	
	/** 75.获取商户入驻的信息 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getBlankEnterPrise", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getBlankEnterPrise(HttpServletRequest request) {
		String result = weiXinService.getBlankEnterPrise(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 75.获取商户入驻的信息 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "saveBlankEnterPrise", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject saveBlankEnterPrise(HttpServletRequest request, String location, String linkman, String enterprise, String brandName, String tel, String email) {
		if(StringUtils.isEmpty(location) || StringUtils.isEmpty(linkman)|| StringUtils.isEmpty(enterprise)|| StringUtils.isEmpty(brandName) || StringUtils.isEmpty(tel))
			return formatJsonResult("E90000", "参数不能为空");
		
		String result = weiXinService.saveBlankEnterPrise(getSessionClientIdentityCode(request), location, linkman, enterprise, brandName, tel, email,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	
	/** 82.获取退货参数 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "orderTuihuoParam", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject orderTuihuoParam(HttpServletRequest request, Integer orderId) {
		if(orderId == null)
			return formatJsonResult("E90000", "参数不能为空");
		String result = weiXinService.orderTuihuoParam(getSessionClientIdentityCode(request), orderId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 83.保存退货单据 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "saveOrderTuiHuo", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject saveOrderTuiHuo(HttpServletRequest request, String orderJson) {
		if(StringUtils.isEmpty(orderJson))
			return formatJsonResult("E90000", "参数不能为空");
		String result = weiXinService.saveOrderTuiHuo(getSessionClientIdentityCode(request), orderJson,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 84.退货单据明细 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "orderTHDetail", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject orderTHDetail(HttpServletRequest request, Integer orderId) {
		if(orderId == null)
			return formatJsonResult("E90000", "参数不能为空");
		String result = weiXinService.orderTHDetail(getSessionClientIdentityCode(request), orderId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 88.保存积分兑换单据 ajax(json) */
//	@CheckWeiXinLogin
	@RequestMapping(value = "saveIntegralOrder", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject saveIntegralOrder(HttpServletRequest request,String identityCode, Integer type_id) {
		if(StringUtils.isEmpty(identityCode)){
			if (request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO) == null 
					|| StringUtils.isEmpty(((ClientModel)request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO)).getClientId())) {// 如果验证成功返回true（这里直接写false来模拟验证失败的处理）
				return JSONObject.fromObject(JsonResponseHelper.getWeiXinNoJsonModel().toString());
			}
		}
		String result = null;
		if(identityCode==null||identityCode==""){
			logger.info("null identityCode="+getSessionClientIdentityCode(request)+" type_id = "+type_id);
			result = weiXinService.saveIntegralOrder(getSessionClientIdentityCode(request),type_id);
		}else{
			logger.info("!null identityCode="+getSessionClientIdentityCode(request)+" type_id = "+type_id);
			result = weiXinService.saveIntegralOrder(identityCode,type_id);
		}
		return JSONObject.fromObject(result);
	}
	
	
	

	/** 已绑定微信 vm */
	@RequestMapping(value = "getGift", method = RequestMethod.GET)
	public String getGift(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
//		if(WebStyle.getStyle().equals("dalishuishou"))
//			return "screen/weixin/tianjinGetGift";
		
		return "screen/weixin/getGift";
	}

	/** 未绑定微信 vm */
	@RequestMapping(value = "unboundWeixin", method = RequestMethod.GET)
	public String unboundWeixin(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
//		if(WebStyle.getStyle().equals("dalishuishou"))
//			return "screen/weixin/tianjinUnboundWeixin";
		return "screen/weixin/unboundWeixin";
	}
	
	/** 水趣推客 vm */
	@CheckWeiXinId
	@RequestMapping(value = "everyShop", method = RequestMethod.GET)
	public String everyShop(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
//		if(WebStyle.getStyle().equals("dalishuishou"))
//			return "screen/weixin/tianjinEveryShop";
		return "screen/weixin/everyShop";
	}

	/** 水趣推客 vm */
	@CheckWeiXinId
	@RequestMapping(value = "everyShopNew", method = RequestMethod.GET)
	public String everyShopNew(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
//		if(WebStyle.getStyle().equals("dalishuishou"))
//			return "screen/weixin/tianjinEveryShop";
		return "screen/weixin/everyShopNew";
	}
	
	/** 添加银行卡提现账号 vm */
	@CheckWeiXinId
	@CheckWeiXinLogin
	@RequestMapping(value = "addCash1", method = RequestMethod.GET)
	public String addCash(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/addCash1";
	}
	
	
	/** 添加支付宝提现账号 vm */
	@CheckWeiXinId
	@CheckWeiXinLogin
	@RequestMapping(value = "addCash", method = RequestMethod.GET)
	public String addCash1(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/addCash";
	}
	
	
	
	/** 收益账单 vm */
	@CheckWeiXinId
	@CheckWeiXinLogin
	@RequestMapping(value = "incomeBill", method = RequestMethod.GET)
	public String incomeBill(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/incomeBill";
	}

	/** 我的客户 vm */
	@CheckWeiXinId
	@CheckWeiXinLogin
	@RequestMapping(value = "myClient", method = RequestMethod.GET)
	public String myClient(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/myClient";
	}
	
	/** 我的团队vm */
	@CheckWeiXinId
	@CheckWeiXinLogin
	@RequestMapping(value = "myTeam", method = RequestMethod.GET)
	public String myTeam(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/myTeam";
	}

	/** 分享海报 vm */
	@CheckWeiXinId
	@CheckWeiXinLogin
	@RequestMapping(value = "poster", method = RequestMethod.GET)
	public String poster(Model model, HttpServletRequest request, Integer tid) {
		new PublicHelper(model, request);
		model.addAttribute("tid", tid);
		return "screen/weixin/poster";
	}
	
	/** 修改提现密码 vm */
	@CheckWeiXinId
	@CheckWeiXinLogin
	@RequestMapping(value = "editCash", method = RequestMethod.GET)
	public String editCash(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/editCash";
	}
	
	/** 忘记提现密码 vm */
	@CheckWeiXinId
	@CheckWeiXinLogin
	@RequestMapping(value = "forgetCash", method = RequestMethod.GET)
	public String forgetCash(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/forgetCash";
	}
	
	/** 登录APP提现余额 vm */
	@RequestMapping(value = "loginApp", method = RequestMethod.GET)
	public String loginApp(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/loginApp";
	}
	
	/** 绑定微信账号 vm */
	@CheckWeiXinId
	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String register(Model model, HttpServletRequest request, Integer shareId) {
		new PublicHelper(model, request);
		model.addAttribute("shareId", shareId);
//		if(WebStyle.getStyle().equals("dalishuishou"))
//			return "screen/weixin/tianjinRegister";
		return "screen/weixin/register";
	}
	
	/** 修改提现账号 vm */
	@CheckWeiXinId
	@CheckWeiXinLogin
	@RequestMapping(value = "editCashAccount", method = RequestMethod.GET)
	public String editCashAccount(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/editCashAccount";
	}
	
	/** 活动过期 vm */
	@RequestMapping(value = "overtime", method = RequestMethod.GET)
	public String overtime(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
//		if(WebStyle.getStyle().equals("dalishuishou"))
//			return "screen/weixin/tianjinOvertime";
		return "screen/weixin/overtime";
	}
	
	/** 104.水趣推客_我的账户情况 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "myEarningInfo", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject myEarningInfo(HttpServletRequest request) {
		String result = weiXinService.myEarningInfo(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 105.保存我的提现账号 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "saveEarningAccount", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject saveEarningAccount(HttpServletRequest request, String alipayNo, String alipayName, String dealPass, String mappingTel, String identityId) {
		String result = weiXinService.saveEarningAccount(getSessionClientIdentityCode(request), alipayNo, alipayName, dealPass, mappingTel, identityId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 106.修改提现密码获取验证码，返回错误码 E90000、E90001 (E90001时message为剩余秒数) ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "createEarningpassSms", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject createEarningpassSms(HttpServletRequest request) {
		if (request.getSession().getAttribute("bindMsgTime") != null) {
			Long nextSendMsgTimeOut = Long.parseLong(request.getSession().getAttribute("bindMsgTime").toString());
			Long currentTimeMilli = System.currentTimeMillis() / 1000;
			if (currentTimeMilli > nextSendMsgTimeOut){
				nextSendMsgTimeOut = System.currentTimeMillis() / 1000 + Integer.parseInt(getSysParamMapValue(SysParamNames.ParamCustomerMobileMsgStep));
				request.getSession().setAttribute("bindMsgTime", nextSendMsgTimeOut);
			}else{
				return formatJsonResult("E90001",String.valueOf((nextSendMsgTimeOut - currentTimeMilli) < 0 ? 0 : (nextSendMsgTimeOut - currentTimeMilli)));
			}
		}
		
		String result = weiXinService.createEarningpassSms(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		if(result.contains("E00000")){
			Long nextSendMsgTimeOut = System.currentTimeMillis() / 1000 + Integer.parseInt(getSysParamMapValue(SysParamNames.ParamCustomerMobileMsgStep));
			request.getSession().setAttribute("bindMsgTime", nextSendMsgTimeOut);
			return formatJsonResult("E00000", getSysParamMapValue(SysParamNames.ParamCustomerMobileMsgStep));
		}
		
		return JSONObject.fromObject(result);
	}

	/** 107.修改提现密码 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "updateEarningpass", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject updateEarningpass(HttpServletRequest request, String smsCode, String mappingTel, String oldPass, String dealPass) {
		String result = weiXinService.updateEarningpass(getSessionClientIdentityCode(request), smsCode, mappingTel, oldPass, dealPass,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 108.获取单个模板明细/以及分享产品图片 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getShareTemplateDetail", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getShareTemplateDetail(HttpServletRequest request , Integer tid) {
		if(tid == null)
			return formatJsonResult("E90000", "参数不能为空");
		
		String result = weiXinService.getShareTemplateDetail(tid, getSessionClientIdentityCode(request), true,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 109.保存我的分享记录(弃用) ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "saveShareRecord", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject saveShareRecord(HttpServletRequest request, Integer tid) {
		String result = "";
		return JSONObject.fromObject(result);
	}

	/** 112.分享注册页面的分享人数据 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getShareRecordToRegedit", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getShareRecordToRegedit(HttpServletRequest request, Integer shareId) {
		String result = weiXinService.getShareRecordToRegedit(shareId, getSessionWeiXinId(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 111.人人分享注册获取验证码 ，返回错误码 E90000、E90001 (E90001时message为剩余秒数) ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getCode", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getCode(HttpServletRequest request, String cellphone) {
		if(StringUtils.isEmpty(cellphone))
			return formatJsonResult("E90000", "参数不能为空");
		
		if (request.getSession().getAttribute("bindMsgTime") != null) {
			Long nextSendMsgTimeOut = Long.parseLong(request.getSession().getAttribute("bindMsgTime").toString());
			Long currentTimeMilli = System.currentTimeMillis() / 1000;
			if (currentTimeMilli > nextSendMsgTimeOut){
				nextSendMsgTimeOut = System.currentTimeMillis() / 1000 + Integer.parseInt(getSysParamMapValue(SysParamNames.ParamCustomerMobileMsgStep));
				request.getSession().setAttribute("bindMsgTime", nextSendMsgTimeOut);
			}else{
				return formatJsonResult("E90001",String.valueOf((nextSendMsgTimeOut - currentTimeMilli) < 0 ? 0 : (nextSendMsgTimeOut - currentTimeMilli)));
			}
		}
		
		String result = weiXinService.getCode(cellphone, getSessionWeiXinId(request), getSessionWXAppId(request));
		if(result.contains("E00000")){
			Long nextSendMsgTimeOut = System.currentTimeMillis() / 1000 + Integer.parseInt(getSysParamMapValue(SysParamNames.ParamCustomerMobileMsgStep));
			request.getSession().setAttribute("bindMsgTime", nextSendMsgTimeOut);
			return formatJsonResult("E00000", getSysParamMapValue(SysParamNames.ParamCustomerMobileMsgStep));
		}
		return JSONObject.fromObject(result);
	}

	/** 114.人人分享完成用户注册 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "shareComplateRegedit", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject shareComplateRegedit(HttpServletRequest request, String cellphone, String code, Integer shareId) {
		if(StringUtils.isEmpty(cellphone) || StringUtils.isEmpty(code) || shareId == null){
			return formatJsonResult("E90000", "参数不能为空");
		}
		String result = weiXinService.shareComplateRegedit(cellphone, code, getSessionWeiXinId(request), shareId,getSessionWXAppId(request));
		if(result.indexOf("E00000") > 0){
			ClientModel userInfo = weiXinService.getWxLogin(getSessionWeiXinId(request),getSessionWXAppId(request));
			request.getSession().setAttribute(SessionConstantDefine.CLIENT_INFO, userInfo);
		}
		return JSONObject.fromObject(result);
	}

	/** 118.找回提现密码获取验证码 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "findEarningPassSmsCode", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject findEarningPassSmsCode(HttpServletRequest request) {
		if (request.getSession().getAttribute("bindMsgTime") != null) {
			Long nextSendMsgTimeOut = Long.parseLong(request.getSession().getAttribute("bindMsgTime").toString());
			Long currentTimeMilli = System.currentTimeMillis() / 1000;
			if (currentTimeMilli > nextSendMsgTimeOut){
				nextSendMsgTimeOut = System.currentTimeMillis() / 1000 + Integer.parseInt(getSysParamMapValue(SysParamNames.ParamCustomerMobileMsgStep));
				request.getSession().setAttribute("bindMsgTime", nextSendMsgTimeOut);
			}else{
				return formatJsonResult("E90001",String.valueOf((nextSendMsgTimeOut - currentTimeMilli) < 0 ? 0 : (nextSendMsgTimeOut - currentTimeMilli)));
			}
		}
		
		String result = weiXinService.findEarningPassSmsCode(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		if(result.contains("E00000")){
			Long nextSendMsgTimeOut = System.currentTimeMillis() / 1000 + Integer.parseInt(getSysParamMapValue(SysParamNames.ParamCustomerMobileMsgStep));
			request.getSession().setAttribute("bindMsgTime", nextSendMsgTimeOut);
			return formatJsonResult("E00000", getSysParamMapValue(SysParamNames.ParamCustomerMobileMsgStep));
		}
		
		return JSONObject.fromObject(result);
	}

	/** 119.找回修改提现密码 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "findEarningpass", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject findEarningpass(HttpServletRequest request, String smsCode, String dealPass) {
		String result = weiXinService.findEarningpass(getSessionClientIdentityCode(request), smsCode, dealPass,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 120.用户提现金额到支付宝 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "userGetMoney", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject userGetMoney(HttpServletRequest request, String alipayNo, Double money) {
		String result = weiXinService.userGetMoney(getSessionWeiXinId(request), alipayNo, money,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 121.用户提现明细 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getTiXianRecords", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getTiXianRecords(HttpServletRequest request) {
		String result = weiXinService.getTiXianRecords(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 122.用户收益明细 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getEarningsRecords", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getEarningsRecords(HttpServletRequest request) {
		String result = weiXinService.getEarningsRecords(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 123.水趣推客我的客户 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getDevelopClients", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getDevelopClients(HttpServletRequest request, Integer type, Integer pageIndex) {
		if(type == null)
			type = 0;
		if(pageIndex == null)
			pageIndex = 1;
		String result = weiXinService.getDevelopClients(getSessionClientIdentityCode(request), type, pageIndex,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	

	/** 海报生成第一步，需要传输tid，pictureid，productPicUrl；特别参数：clientType 来源（1：微信, 2：APP） vm */
	@CheckWeiXinLogin
	@RequestMapping(value = "viewPic", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject viewPic(Model model, HttpServletRequest request) {
		BaseControllerHelper helper = new ShareTemplateOneHelper_New(model, request);
		/*if(WebStyle.getStyle().equals("dalishuishou")){
			helper = new ShareTemplateOneHelper(model, request);
		}*/
		helper.Init();
		return JSONObject.fromObject(helper.getResult());
	}
	
	/** 海报生成第二步，需要传输shareId（打印二维码和发送） 如果是从历史生成模板中选择，则直接调用该方法。特别参数：clientType 来源（1：微信, 2：APP） vm */
	@CheckWeiXinLogin
	@RequestMapping(value = "makeAndSendPic", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject makeAndSendPic(Model model, HttpServletRequest request) {
		BaseControllerHelper helper = new ShareTemplateTwoHelper_New(model, request);
		/*if(WebStyle.getStyle().equals("dalishuishou")){
			helper = new ShareTemplateTwoHelper(model, request);
		}*/
		helper.Init();
		return JSONObject.fromObject(helper.getResult());
	}
	
	
	/** 小程序店铺码模板生成，需要传输tid，shopCode；特别参数：clientType 来源（1：微信, 2：APP） vm */
	@RequestMapping(value = "liteappTemplateShopCode", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject liteappTemplateShopCode(Model model, HttpServletRequest request) {
		BaseControllerHelper helper = new LiteappTemplateShopCodeHelper(model, request);
		/*if(WebStyle.getStyle().equals("dalishuishou")){
			helper = new ShareTemplateOneHelper(model, request);
		}*/
		helper.Init();
		return JSONObject.fromObject(helper.getResult());
	}
	
	
	
	/** APP水趣推客嵌套页面 vm */
	@RequestMapping(value = "everyShopApp", method = RequestMethod.GET)
	public String everyShopApp(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/everyShopApp";
	}

	/** APP分享海报嵌套页面 vm */
	@RequestMapping(value = "posterApp", method = RequestMethod.GET)
	public String posterApp(Model model, HttpServletRequest request, Integer tid) {
		new PublicHelper(model, request);
		model.addAttribute("tid", tid);
		return "screen/weixin/posterApp";
	}
	
	
	
	
	
	/** 客户发票管理 vm */
	@RequestMapping(value = "receipt", method = RequestMethod.GET)
	public String receipt(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/receipt";
	}
	
	/** 126.获取客户的增值税发票信息 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getClientVats", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getClientVats(HttpServletRequest request) {
		String result = weiXinService.getClientVats(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 127.保存增值税发票信息(新增、修改、默认、删除) ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "saveClientVAT", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject saveClientVAT(HttpServletRequest request, String dataVat, String tag) {
		if(StringUtils.isEmpty(dataVat) || StringUtils.isEmpty(tag)){
			return formatJsonResult("E90000", "参数不能为空");
		}
		String result = weiXinService.saveClientVAT(getSessionClientIdentityCode(request), dataVat, tag,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 128.单据确认界面根据水店ID获取发票信息 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getEidInvoiceGuiGe", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getEidInvoiceGuiGe(HttpServletRequest request, Integer shopId) {
		if(shopId == null){
			return formatJsonResult("E90000", "参数不能为空");
		}
		String result = weiXinService.getEidInvoiceGuiGe(getSessionClientIdentityCode(request), shopId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	
	
	
	/** 电子票 vm */
	@RequestMapping(value = "myTicket", method = RequestMethod.GET)
	public String myTicket(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/myTicket";
	}
	
	/** 130.电子票余量列表 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getValidTicketList", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getValidTicketList(HttpServletRequest request) {
		String result = weiXinService.getValidTicketList(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 131.电子票消费记录列表 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getConsumeTicketList", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getConsumeTicketList(HttpServletRequest request) {
		String result = weiXinService.getConsumeTicketList(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 132.电子票消费详情 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getConsumeTicketDetail", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getConsumeTicketDetail(HttpServletRequest request, Integer ticketId, Integer orderId, Integer eid) {
		if(ticketId == null || orderId == null || eid == null){
			return formatJsonResult("E90000", "参数不能为空");
		}
		String result = weiXinService.getConsumeTicketDetail(getSessionClientIdentityCode(request), ticketId, orderId, eid,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 133.立即使用电子票 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "useTicketToCart", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject useTicketToCart(HttpServletRequest request, Integer shopId, Integer pid) {
		if(shopId == null || pid == null){
			return formatJsonResult("E90000", "参数不能为空");
		}
		String result = weiXinService.useTicketToCart(getSessionClientIdentityCode(request), shopId, pid,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	

	/** 134.获取企业押桶说明 vm */
	@RequestMapping(value = "getenterpriseyatong")
 	public String getenterpriseyatong(HttpServletRequest request,Model model, Integer eid) {
		if(eid == null){
			model.addAttribute("msg", "参数不能为空");
			return "screen/weixin/myTicket";
		}
		String result = weiXinService.getenterpriseyatong(eid);
		try{
			JSONObject jsonObj = JSONObject.fromObject(result);
			String autoScreen = "<meta name='viewport' content='width=device-width, initial-scale=1.0'>";
			model.addAttribute("msg", autoScreen + jsonObj.getJSONObject("data").getString("remark"));
		}catch(Exception ex){
			logger.info("获取压桶说明错误："+eid + " | "+result +" | " + ex.toString());
			model.addAttribute("msg", "查找不到数据。");
		}
		return "screen/weixin/getenterpriseyatong";
	}
	
	

	/** 137.一桶一码扫码验证 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "verifyCodeByApp", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject verifyCodeByApp(HttpServletRequest request, String codeUrl) {
		if(StringUtils.isEmpty(codeUrl)){
			return formatJsonResult("E90000", "参数不能为空");
		}
		String result = weiXinService.verifyCodeByApp(getIp(request), codeUrl,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	

	/** 水趣推客申请 vm */
	@RequestMapping(value = "shopApply", method = RequestMethod.GET)
	public String shopApply(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/shopApply";
	}
	
//	/** 水趣推客拒绝 vm */
//	@RequestMapping(value = "shopRefuse", method = RequestMethod.GET)
//	public String shopRefuse(Model model, HttpServletRequest request) {
//		new PublicHelper(model, request);
//		return "screen/weixin/shopRefuse";
//	}
	
	/** 138.水趣推客 获取申请信息 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getShareApplyInfo", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getShareApplyInfo(HttpServletRequest request) {
		String result = weiXinService.getShareApplyInfo(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 139.水趣推客 保存开店申请 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "saveShareApplyInfo", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject saveShareApplyInfo(HttpServletRequest request, Integer eid, String fullname, String tel, String resource, String location) {
		String result = weiXinService.saveShareApplyInfo(getSessionClientIdentityCode(request), eid, fullname, tel, resource, getIp(request), location,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 140.水趣推客 保存开店申请上传图片 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "saveShareApplyPics", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject saveShareApplyPics(HttpServletRequest request, String applyId, String pic1, String pic2, String pic3, String pic4, String pic5) {
		String result = weiXinService.saveShareApplyPics(getSessionClientIdentityCode(request), applyId, pic1, pic2, pic3, pic4, pic5,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	
	/** 141.配送员详情 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getStaffDetail", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getStaffDetail(HttpServletRequest request, Integer eid, Integer staffId) {
		String result = weiXinService.getStaffDetail(getSessionClientIdentityCode(request), eid, staffId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	/** 142.根据坐标、百度ID ,获取所在企业信息 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "getEnterPriseByLoc", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getEnterPriseByLoc(HttpServletRequest request, String location, Integer citycode) {
		String result = weiXinService.getEnterPriseByLoc(location, citycode,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	
	
	
	
	
	/** 二维码扫描 vm */
	@RequestMapping(value = "antifake")
 	public String antifake(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		
		return "screen/weixin/antifake";
	}
	
	
	
	/** 通用样式文件 vm */
	@RequestMapping(value = "indexCss", method = RequestMethod.GET)
	public String indexCss(Model model, HttpServletRequest request) {
		model.addAttribute("webStyle", WebStyle.getStyle());
		return "screen/weixin/indexCss";
	}

	/** 通用样式文件 vm */
	@RequestMapping(value = "shopCss", method = RequestMethod.GET)
	public String shopCss(Model model, HttpServletRequest request) {
		model.addAttribute("webStyle", WebStyle.getStyle());
		return "screen/weixin/shopCss";
	}

	/** 趣发现样式 vm */
	@RequestMapping(value = "foundCss", method = RequestMethod.GET)
	public String foundCss(Model model, HttpServletRequest request) {
		model.addAttribute("webStyle", WebStyle.getStyle());
		return "screen/weixin/foundCss";
	}	

	/** 网站类型 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "webStyle")
 	public @ResponseBody String webStyle(HttpServletRequest request) {
		return WebStyle.getStyle();
	}

	
	
	
	
	/** 删除人人分享模板图片，APP分享完毕调用 ajax(json) */
	@RequestMapping(value = "delTemplatePic", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject delTemplatePic(HttpServletRequest request, String picUrl) {
		if(StringUtils.isEmpty(picUrl)){
			return formatJsonResult("E90000", "参数不能为空");
		}
		
		String picUrls = picUrl.replace("http://" + getSysParamMapValue(SysParamNames.ParamWXWebSit) + "/", "");
		picUrls =  request.getSession().getServletContext().getRealPath("/") + picUrls;
		try{
			File file = new File(picUrls);
			if(!file.isFile()){
				logger.info("删除文件失败:" + picUrls + "非文件！");
				return formatJsonResult("E90001", "非文件");
			}
			
			if (!file.exists()) {
				logger.info("删除文件失败:" + picUrls + "不存在！");
				return formatJsonResult("E90001", "文件不存在");
	        } else {
	        	file.delete();
	        	return formatJsonResult("E00000", "操作成功");
	        }
		}catch(Exception ex){
			logger.info("删除水趣推客生成图片失败：" + picUrl);
			return formatJsonResult("E90001", "操作失败");
		}
	}
	
	
	
	/** 143.单据我要催单 ajax(json) */
	@CheckWeiXinId
	@RequestMapping(value = "ordercuishui", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject ordercuishui(HttpServletRequest request, Integer orderId, String remark) {
		if (orderId == null)
			return formatJsonResult("E90000", "参数不能为空");
		
		if (StringUtils.isEmpty(remark))
			return formatJsonResult("E90000", "参数不能为空");
		
		String result = weiXinService.ordercuishui(getSessionClientIdentityCode(request), orderId, remark,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	

	/** 145.水店注册微信  Json */
	@CheckWeiXinLogin
	@RequestMapping(value = "shopregeditweixin", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject shopregeditweixin( HttpServletRequest request, String ename, String shopNo, String keeperCode, String keeperMobile){
		if (ename == null)
			return formatJsonResult("E90000", "参数不能为空");
		
		if (StringUtils.isEmpty(shopNo))
			return formatJsonResult("E90000", "参数不能为空");
		
		if (StringUtils.isEmpty(keeperCode))
			return formatJsonResult("E90000", "参数不能为空");
		
		if (StringUtils.isEmpty(keeperMobile))
			return formatJsonResult("E90000", "参数不能为空");
		
		
		String result = weiXinService.shopregeditweixin(ename, shopNo, keeperCode, keeperMobile, getSessionWeiXinId(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	

	/** 151.客户押桶记录查询 ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getClientYatongs", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject getClientYatongs(HttpServletRequest request) {
		String result = weiXinService.getClientYatongs(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 推客经理要审核的推客信息 vm */
	@RequestMapping(value = "tuiKeManagerAudit")
	public String tuiKeManagerAudit(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/tuiKeManagerAudit";
	}
	
	
	/** 152.获取推客经理要审核的推客信息  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getManagerTuiKes", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getManagerTuiKes(HttpServletRequest request) {
		String result = weiXinService.getManagerTuiKes(getSessionClientIdentityCode(request), getSessionWeiXinId(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	

	/** 153.微信端审核推客  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "auditTuike", produces = "application/json; charset=utf-8")
 	public @ResponseBody JSONObject auditTuike(HttpServletRequest request, Integer applyId, Integer eid, Integer status, String remark) {
		String result = weiXinService.auditTuike(getSessionClientIdentityCode(request),applyId, eid, status, remark,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 154.推客收益相关客户明细  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "tuikeEarningsDetail", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject tuikeEarningsDetail(HttpServletRequest request,Integer xgcid,Integer month) {
		String result = weiXinService.tuikeEarningsDetail(xgcid,getSessionClientIdentityCode(request), month,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 155.保存我的提现账号-银行卡号  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "saveEarningBankAccount", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject saveEarningBankAccount(HttpServletRequest request,String bankNo,String bankName ,String openBank ,String openSmallBank,String dealPass, String mappingTel , String identityId) {
		String result = weiXinService.saveEarningBankAccount(getSessionClientIdentityCode(request),bankNo,bankName ,openBank ,openSmallBank,dealPass, mappingTel ,identityId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	/** 156.推客获取系统或者企业模板  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getSysEnterPriseTemplate", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getSysEnterPriseTemplate(HttpServletRequest request,Integer type) {
		String result = weiXinService.getSysEnterPriseTemplate(getSessionClientIdentityCode(request),type,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	
	/** 157.根据条码获取码对应信息  ajax(json) */
	@RequestMapping(value = "getShopManagerCode", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getShopManagerCode(HttpServletRequest request,Long shopCode,String identityCode) {
		String result = null;
		if(StringUtils.isEmpty(identityCode)){
			if (request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO) == null 
					|| StringUtils.isEmpty(((ClientModel)request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO)).getClientId())) {// 如果验证成功返回true（这里直接写false来模拟验证失败的处理）
				return JSONObject.fromObject(JsonResponseHelper.getWeiXinNoJsonModel().toString());
			}
			result = weiXinService.getShopManagerCode(getSessionClientIdentityCode(request),shopCode,getSessionWXAppId(request));
		}else{
			result = weiXinService.getShopManagerCode(identityCode,shopCode,getSessionWXAppId(request));
		}
		return JSONObject.fromObject(result);
	}
	
	
	
	/** 158.根据手机号获取绑定相关信息，验证码  ajax(json) */
	@RequestMapping(value = "getShopRegeditGetCode", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getShopRegeditGetCode(HttpServletRequest request,Integer eid,String cellphone,String identityCode) {
		String result = null;
		if(StringUtils.isEmpty(identityCode)){
			if (request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO) == null 
					|| StringUtils.isEmpty(((ClientModel)request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO)).getClientId())) {// 如果验证成功返回true（这里直接写false来模拟验证失败的处理）
				return JSONObject.fromObject(JsonResponseHelper.getWeiXinNoJsonModel().toString());
			}
			result = weiXinService.getShopRegeditGetCode(getSessionClientIdentityCode(request),eid,cellphone,getSessionWXAppId(request));
		}else{
			result = weiXinService.getShopRegeditGetCode(identityCode,eid,cellphone,getSessionWXAppId(request));
		}
		return JSONObject.fromObject(result);
	}
	
	
	
	/** 159.根据手机号验证码保存推客经理绑定  ajax(json) */
	@RequestMapping(value = "saveShopManagerCode", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject saveShopManagerCode(HttpServletRequest request,String cellphone,String nickname,Integer existCid,String code,Long shopCode,Integer shopId,String identityCode) {
		String result = null;	
		if(StringUtils.isEmpty(identityCode)){
			if (request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO) == null 
					|| StringUtils.isEmpty(((ClientModel)request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO)).getClientId())) {// 如果验证成功返回true（这里直接写false来模拟验证失败的处理）
				return JSONObject.fromObject(JsonResponseHelper.getWeiXinNoJsonModel().toString());
			}
			result = weiXinService.saveShopManagerCode(getSessionClientIdentityCode(request),getSessionWeiXinId(request),cellphone,existCid,nickname,code,shopCode,shopId,getSessionWXAppId(request));
		}else{
			result = weiXinService.saveShopManagerCode(identityCode,"",cellphone,existCid,nickname,code,shopCode,shopId,getSessionWXAppId(request));
		}
		return JSONObject.fromObject(result);
	}
		
	
	
	/** 160.APP,微信公众号发起支付记录  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "paysendrecord", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject paysendrecord(HttpServletRequest request,String order_no,String platform,String money,Integer source) {
		String result = weiXinService.paysendrecord(getSessionClientIdentityCode(request),order_no,platform,money,source,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 161.推客经理相关信息  ajax(json) */
	@RequestMapping(value = "getClientShareInfo", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getClientShareInfo(HttpServletRequest request,String identityCode) {
		if(StringUtils.isEmpty(identityCode)){
			if (request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO) == null 
					|| StringUtils.isEmpty(((ClientModel)request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO)).getClientId())) {// 如果验证成功返回true（这里直接写false来模拟验证失败的处理）
				return JSONObject.fromObject(JsonResponseHelper.getWeiXinNoJsonModel().toString());
			}
		}
		String result = null;
		if(identityCode==null||identityCode==""){
			result = weiXinService.getClientShareInfo(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		}else{
			result = weiXinService.getClientShareInfo(identityCode,getSessionWXAppId(request));
		}
		return JSONObject.fromObject(result);
	}
	
	/** 162.推客经理汇总报表  Json */
	@RequestMapping(value = "getShareTotalReport", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getShareTotalReport(HttpServletRequest request, String identityCode,Integer eid) {
		if(StringUtils.isEmpty(identityCode)){
			if (request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO) == null 
					|| StringUtils.isEmpty(((ClientModel)request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO)).getClientId())) {// 如果验证成功返回true（这里直接写false来模拟验证失败的处理）
				return JSONObject.fromObject(JsonResponseHelper.getWeiXinNoJsonModel().toString());
			}
		}
		
		String result = null;
		if(identityCode==null||identityCode==""){
			result = weiXinService.getShareTotalReport(getSessionClientIdentityCode(request),eid,"");
		}else{
			result = weiXinService.getShareTotalReport(identityCode,eid,"");
		}
		return JSONObject.fromObject(result);
	}
	
	/** 163.店铺编码明细  Json */
	@RequestMapping(value = "getShopCodeRegeditReport", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getShopCodeRegeditReport(HttpServletRequest request,Integer eid,String identityCode,String endDate) {
		if(StringUtils.isEmpty(identityCode)){
			if (request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO) == null 
					|| StringUtils.isEmpty(((ClientModel)request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO)).getClientId())) {// 如果验证成功返回true（这里直接写false来模拟验证失败的处理）
				return JSONObject.fromObject(JsonResponseHelper.getWeiXinNoJsonModel().toString());
			}
		}
		
		String result = null;
		if(identityCode==null||identityCode==""){
			result = weiXinService.getShopCodeRegeditReport(getSessionClientIdentityCode(request),eid,endDate,"");
		}else{
			result = weiXinService.getShopCodeRegeditReport(identityCode,eid,endDate,"");
		}
		return JSONObject.fromObject(result);
	}
	
	/** 164.店铺推客明细  Json */
	@RequestMapping(value = "getShopClientRegeditReport", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getShopClientRegeditReport(HttpServletRequest request,String identityCode,Integer eid,String endDate) {
		String result = null;
		if(StringUtils.isEmpty(identityCode)){
			if (request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO) == null 
					|| StringUtils.isEmpty(((ClientModel)request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO)).getClientId())) {// 如果验证成功返回true（这里直接写false来模拟验证失败的处理）
				return JSONObject.fromObject(JsonResponseHelper.getWeiXinNoJsonModel().toString());
			}
		}
		if(identityCode==null||identityCode==""){
			result=weiXinService.getShopClientRegeditReport(getSessionClientIdentityCode(request),eid,endDate,"");
		}else{
			result=weiXinService.getShopClientRegeditReport(identityCode,eid,endDate,"");
		}
		return JSONObject.fromObject(result);
	}
	
	/** 165.店铺关联配送员账号  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getShopCourierAccount", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getShopCourierAccount(HttpServletRequest request) {
		String result = weiXinService.getShopCourierAccount(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}

	
	/** 166.店铺关联配送员发送短信验证码  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "sendShopCourierSMS", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject sendShopCourierSMS(HttpServletRequest request,String cellphone,String shopCode) {
		// 获取ip
		String ip = request.getHeader("x-forwarded-for");  
		         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		             ip = request.getHeader("Proxy-Client-IP");  
		         }  
		         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		             ip = request.getHeader("WL-Proxy-Client-IP");  
		         }  
		         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		             ip = request.getHeader("HTTP_CLIENT_IP");  
		         }  
		         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		             ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
		         }  
		         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		             ip = request.getRemoteAddr();  
		         }  
		String result = weiXinService.sendShopCourierSMS(getSessionClientIdentityCode(request),cellphone,ip,shopCode,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	
	/** 167.保存店铺关联配送员  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "saveShopCourier", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject saveShopCourier(HttpServletRequest request,String cellphone,String eName,String shopCode,String userName,String smsCode) {
		String result = weiXinService.saveShopCourier(getSessionClientIdentityCode(request),cellphone,eName,shopCode,userName,getSessionWeiXinId(request),smsCode,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 168.微信中配送订单查询  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "queryWXsendOrder", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject queryWXsendOrder(HttpServletRequest request,Integer status,Integer lastId) {
		String result = weiXinService.queryWXsendOrder(getSessionClientIdentityCode(request),status,lastId,getSessionWXAppId(request));
		return JSONObject.fromObject(result);
	}
	
	/** 171.用户存在小程序推荐码信息  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "existsLiteAppCode", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject existsLiteAppCode(HttpServletRequest request) {
		String result = weiXinService.existsLiteAppCode(getSessionClientIdentityCode(request),getSessionWXAppId(request));
		logger.info("333333333333333result = "+result);
		return JSONObject.fromObject(result);
	}
	
	
	/** 173.店铺推广_我的账户情况  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "myLiteAppEarningInfo", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject myLiteAppEarningInfo(HttpServletRequest request,Integer type) {
		String result = weiXinService.myLiteAppEarningInfo(getSessionWXAppId(request),getSessionClientIdentityCode(request),type);
		return JSONObject.fromObject(result);
	}
	
	
	/** 174.微信钱包实名认证  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "saveEarningWXAutonym", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject saveEarningWXAutonym(HttpServletRequest request,String autonym,Boolean isAutonym,String dealPass) {
		String result = weiXinService.saveEarningWXAutonym(getSessionWXAppId(request),getSessionClientIdentityCode(request),getSessionWeiXinId(request),autonym,isAutonym,dealPass);
		return JSONObject.fromObject(result);
	}
	
	/** 175.用户提现到微信钱包  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "txToWxWallet", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject txToWxWallet(HttpServletRequest request,Double money,String dealPass) {
		String result = weiXinService.txToWxWallet(getSessionWXAppId(request),getSessionClientIdentityCode(request),getSessionWeiXinId(request),money,dealPass);
		return JSONObject.fromObject(result);
	}
	
	/** 176.用户推客所属公众号,是否界面可跳转  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "tuikeBelongEnterprise", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject tuikeBelongEnterprise(HttpServletRequest request) {
		String result = weiXinService.tuikeBelongEnterprise(getSessionWXAppId(request),getSessionClientIdentityCode(request));
		return JSONObject.fromObject(result);
	}
	
	/** 178.企业防伪品牌  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "getEnterPriseAntifake", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getEnterPriseAntifake(HttpServletRequest request,Integer citycode,String location) {
		String result = weiXinService.getEnterPriseAntifake(getSessionWXAppId(request),citycode,location);
		return JSONObject.fromObject(result);
	}
	
	/** 179.是否存在注册送券活动  ajax(json) */
//	@CheckWeiXinLogin
	@RequestMapping(value = "exitRegeditSendQuan", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject exitRegeditSendQuan(HttpServletRequest request,Integer cityId) {
//		if(getSessionCity(request).getCityId()!=null&&getSessionClient(request)==null){
		Integer eid = null;
		if(request.getSession().getAttribute(SessionConstantDefine.WX_USERENTINFO)!=null){
			logger.info("e4444444444444 = ");
			WxUserEntInfoModel wxUserEntInfoModel =(WxUserEntInfoModel) request.getSession().getAttribute(SessionConstantDefine.WX_USERENTINFO);
			eid=wxUserEntInfoModel.getEid();
		}else{
			logger.info("e3333333333333 = ");
			return JSONObject.fromObject("{\"code\":\"E00000\",\"message\":\"成功\",\"data\":{\"exit\":false}}");
		}
		if(getSessionClient(request)==null||getSessionClient(request).getClientId()==null){
			logger.info("e111111111");
			String result = weiXinService.exitRegeditSendQuan(getSessionWXAppId(request),eid,cityId);
			return JSONObject.fromObject(result);
		}else{
			logger.info("e222222222222 = "+getSessionClient(request));
			return JSONObject.fromObject("{\"code\":\"E00000\",\"message\":\"成功\",\"data\":{\"exit\":false}}");
		}
	}
	

	/** 180.判断是否有输入编码送券活动  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "existDiscountsCoupon", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject existDiscountsCoupon(HttpServletRequest request,Integer cityId,Integer citycode) {
		System.out.println(getSessionCity(request));
		String result = weiXinService.existDiscountsCoupon(getSessionWXAppId(request),getSessionCity(request).getCityId(),0);
		return JSONObject.fromObject(result);
	}
	
	
	/** 181.编码送券领取  ajax(json) */
	@CheckWeiXinLogin
	@RequestMapping(value = "imaCouponConvert", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject imaCouponConvert(HttpServletRequest request,Integer activityId,String code_no) {
		String result = weiXinService.imaCouponConvert(getSessionWXAppId(request),activityId,getSessionClientIdentityCode(request),code_no,getSessionCity(request).getCityId(),0);
		return JSONObject.fromObject(result);
	}
	
	/** 182.H5获取积分规则   ajax(json) */
	@RequestMapping(value = "getIntegralRule", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getIntegralRule(HttpServletRequest request,Integer eid) {
		String result = null;
		if(eid!=null){
			result = weiXinService.getIntegralRule(eid);
		}else{
			WxUserEntInfoModel wxUserEntInfoModel =(WxUserEntInfoModel) request.getSession().getAttribute(SessionConstantDefine.WX_USERENTINFO);
			eid=wxUserEntInfoModel.getEid();
			result = weiXinService.getIntegralRule(eid);
		}
		JSONObject fromObject = JSONObject.fromObject(result);
		if(fromObject!=null){
			JSONObject jsonObject = fromObject.getJSONObject("data");
			if(jsonObject!=null){
				String jsonObject3 = jsonObject.getString("rule");
				if(jsonObject3!=null){
					String string = HttpClientUtil.get(jsonObject3);
					jsonObject.put("rule", string);
					fromObject.put("data", jsonObject);
				}
			}
		}
		return fromObject;
	}
	
	/** 183.H5获取我的积分明细   ajax(json) */
	@RequestMapping(value = "getIntegralRecord", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getIntegralRecord(HttpServletRequest request,String identityCode,Integer eid,Integer typeId,Integer pageIndex,Integer pageSize) {
		System.out.println("identityCode = "+identityCode+" eid = "+eid+" typeId = "+typeId+" pageIndex = "+pageIndex+" pageSize = "+pageSize);
		if(StringUtils.isEmpty(identityCode)){
			if (request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO) == null 
					|| StringUtils.isEmpty(((ClientModel)request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO)).getClientId())) {// 如果验证成功返回true（这里直接写false来模拟验证失败的处理）
				return JSONObject.fromObject(JsonResponseHelper.getWeiXinNoJsonModel().toString());
			}
		}
		
		String result = null;
		if(identityCode==null||identityCode==""){
			WxUserEntInfoModel wxUserEntInfoModel =(WxUserEntInfoModel) request.getSession().getAttribute(SessionConstantDefine.WX_USERENTINFO);
			eid=wxUserEntInfoModel.getEid();
			result = weiXinService.getIntegralRecord(getSessionClientIdentityCode(request),eid,typeId,pageIndex,pageSize);
		}else{
			result = weiXinService.getIntegralRecord(identityCode,eid,typeId,pageIndex,pageSize);
		}
		return JSONObject.fromObject(result);
	}
	
	/** 184.H5获取积分商城首页数据(劵详情)   ajax(json) */
	@RequestMapping(value = "getHomeIntegralShop", produces = "application/json; charset=utf-8")
	public @ResponseBody JSONObject getHomeIntegralShop(HttpServletRequest request,String identityCode,Integer eid) {
		if(StringUtils.isEmpty(identityCode)){
			if (request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO) == null 
					|| StringUtils.isEmpty(((ClientModel)request.getSession().getAttribute(SessionConstantDefine.CLIENT_INFO)).getClientId())) {// 如果验证成功返回true（这里直接写false来模拟验证失败的处理）
				return JSONObject.fromObject(JsonResponseHelper.getWeiXinNoJsonModel().toString());
			}
		}
		String result = null;
		if(identityCode==null||identityCode==""){
			WxUserEntInfoModel wxUserEntInfoModel =(WxUserEntInfoModel) request.getSession().getAttribute(SessionConstantDefine.WX_USERENTINFO);
			eid=wxUserEntInfoModel.getEid();
			result = weiXinService.getHomeIntegralShop(getSessionClientIdentityCode(request),eid);
		}else{
			result = weiXinService.getHomeIntegralShop(identityCode,eid);
		}
		return JSONObject.fromObject(result);
	}
	
	/** 趣防伪 vm */
	@RequestMapping(value = "interestAntifake")
	public String interestAntifake(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/interestAntifake";
	}
	
	/** 店铺配送接单员 vm */
	@RequestMapping(value = "shopDistributor")
	public String shopDistributor(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/shopDistributor";
	}
	
	/** 店铺配送接单员订单查询 vm */
	@RequestMapping(value = "orderDistributor")
	public String orderDistributor(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/orderDistributor";
	}
	
	/** 推客经理绑定推广店铺 vm */
	@RequestMapping(value = "tuiKeManagerShop")
	public String tuiKeManagerShop(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/tuiKeManagerShop";
	}
	
	
	/** 店铺编码明细 vm */
	@RequestMapping(value = "shopCodeDetail")
	public String shopCodeDetaul(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/shopCodeDetail";
	}
	
	/** 店铺推客明细 vm */
	@RequestMapping(value = "shopTuikeDetail")
	public String shopTuikeDetaul(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/shopTuikeDetail";
	}
	
	/** 推客经理推广店铺统计 vm */
	@RequestMapping(value = "tuiKeShopStatistics")
	public String tuiKeShopStatistics(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/tuiKeShopStatistics";
	}
	
	
	/** 推客提现 vm */
	@RequestMapping(value = "withdrawDeposit")
	public String withdrawDeposit(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/withdrawDeposit";
	}
	
	/** 推客提现 vm */
	@RequestMapping(value = "editWxAccount")
	public String editWxAccount(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/editWxAccount";
	}
	
	/** 推客找回密码 vm */
	@RequestMapping(value = "tuiKeRetrievePwd")
	public String tuiKeRetrievePwd(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/tuiKeRetrievePwd";
	}
	
	/** 我的积分 vm */
	@RequestMapping(value = "myIntegral")
	public String myIntegral(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/myIntegral";
	}
	
	/** 在线客服 vm */
	@RequestMapping(value = "onlineCustomerService")
	public String onlineCustomerService(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/onlineCustomerService";
	}
	
	/** 积分商城 vm */
	@RequestMapping(value = "integralMall")
	public String integralMall(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/integralMall";
	}
	
	
	/** 测试页面 */
	//@CheckCity
	//@CheckWeiXinId
	@RequestMapping(value = "test", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
 	public @ResponseBody String test(Model model, HttpServletRequest request) {
		String webContent = HttpClientUtil.get("https://admin.shuiqoo.cn/SheqooApi/WxKeywordReply?keyword=1&eid=1&timestamp=1516938898933&sign=D4CEE772AF65CB4A434BA7DA78B1221C");
		
		JSONObject objJson = JSONObject.fromObject(webContent);
 		if(!objJson.has("code") || !objJson.getString("code").equals("E00000")){
 			
			System.out.println("11111");
 		}

 		if(objJson.has("entity") && objJson.getJSONObject("entity").getInt("replyType") == 1){
 			System.out.println("2222222222");
 		}else if(objJson.has("dataList") && objJson.getJSONArray("dataList").size() > 0){
 			StringBuffer sb = new StringBuffer();
 			sb.append("<xml><ToUserName><![CDATA[tt]]></ToUserName><FromUserName><![CDATA[fffffffffff]]></FromUserName><CreateTime>" + String.valueOf(System.currentTimeMillis()) + "</CreateTime><MsgType><![CDATA[news]]></MsgType><ArticleCount>" + objJson.getJSONArray("dataList").size() + "</ArticleCount><Articles>");
 			for(java.util.Iterator tor=objJson.getJSONArray("dataList").iterator(); tor.hasNext();){
				JSONObject job = (JSONObject)tor.next();
            	sb.append("<item><Title><![CDATA[" + job.getString("title") + "]]></Title><Description><![CDATA[" + job.getString("digest") + "]]></Description><PicUrl><![CDATA[" + job.getString("coverPic") + "]]></PicUrl><Url><![CDATA[" + job.getString("contentSourceUrl") + "]]></Url></item>");
            }
            sb.append("</Articles></xml>");
            System.out.println(sb.toString());
 		}
 		return webContent;
	}

	/** 测试页面  vm */
	@RequestMapping(value = "testPage", method = RequestMethod.GET)
	public String testPage(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		return "screen/weixin/testPage";
	}
	
	/** 测试入口 */
	@RequestMapping(value = "wxTest", method = RequestMethod.GET)
	public @ResponseBody String wxTest(String openid, String appId, HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute(SessionConstantDefine.WX_OPENID, openid);
		
		String rootPath = request.getSession().getServletContext().getRealPath("/");
    	rootPath = rootPath + "WeiXinFile/" + DateUtil.getStringDateShort();
    	File filePath = new File(rootPath);
    	if (!filePath.exists()  && !filePath.isDirectory())
    	{
    		//System.out.println("asdf");
    		filePath.mkdirs();
    	}
		
    	String result = request.getSession().getAttribute(SessionConstantDefine.WX_OPENID).toString() + "<br /><br /><br /><br /><a href='/weixin/login?appId="+appId+"'>登录</a><br /><br /><br /><br />";
    	
    	String url = "http://wx.qlogo.cn/mmopen/3x1JLYelTwczxbYssg4dLzHicJs2vKXvQ5ic0Xvic6KFIMBLvjfNRLd0BISSDibM99IB9znEkYVPHaQsnadibwjO5UwA3fwMsOnKs/0.jpg";
    	String houZhui = url.substring(url.length() - 3);
		return result + "       |||   " + WebStyle.getStyle()+"      "+ houZhui;
	}

	/** 测试入口 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public @ResponseBody String login(HttpServletRequest request, Model model, String appId) {
		//System.out.println(request.getSession().getAttribute(SessionConstantDefine.WX_OPENID).toString());
		/*ClientModel userInfo = weiXinService.getWxLogin(getSessionWeiXinId(request));
		if(userInfo==null){
			request.getSession().removeAttribute(SessionConstantDefine.CLIENT_INFO);
			return "登录失败";
		}else{
			
			WxCityInfoModel curCity = weiXinService.getWxUserCityInfo(getSessionWeiXinId(request));
			if(curCity != null){
				request.getSession().setAttribute(SessionConstantDefine.WX_CITY, curCity);
				request.getSession().setMaxInactiveInterval(30*60);
			}
			request.getSession().setAttribute(SessionConstantDefine.CLIENT_INFO, userInfo);
			return userInfo.toString() + "<br />" + userInfo.getClientId().replace("+", "%2b") + "<br /><a href='/weixin/home' target='_black'>首页</a>" + " <br /><br />"+ weiXinService.getWxLoginStr(getSessionWeiXinId(request));
		}*/ 
		
//		request.getSession().setAttribute(SessionConstantDefine.WX_APPID, "wxe96781856240e981");
		request.getSession().setAttribute(SessionConstantDefine.WX_APPID,(StringUtils.isEmpty(appId)||"null".equals(appId)?"wxe96781856240e981":appId));	
		logger.info("appId = "+request.getSession().getAttribute(SessionConstantDefine.WX_APPID));
		logger.info("sessionAppid = "+appId);
//		request.getSession().setAttribute(SessionConstantDefine.WX_APPID, "wx2fb271d8672596c1");	
		
		String result = weiXinService.getWxPublicDetail(getSessionWXAppId(request));
		JSONObject fromObject = JSONObject.fromObject(result).getJSONObject("data");
		String liteappName = (String)fromObject.get("liteappName");
		String styleCode = (String)fromObject.get("styleCode");
		request.getSession().setAttribute("liteappName",liteappName);
		if(styleCode==null){
			request.getSession().setAttribute("themeColor",4);
		}else{
			request.getSession().setAttribute("themeColor",styleCode);
		}
//		WxUserEntInfoModel wxUserEntInfo = weiXinService.getWxUserEntInfo("wx0a34b46f332fc21a");
		WxUserEntInfoModel wxUserEntInfo = weiXinService.getWxUserEntInfo(getSessionWXAppId(request));
		request.getSession().setAttribute(SessionConstantDefine.WX_USERENTINFO, wxUserEntInfo);
		
		ClientModel userInfo = weiXinService.getWxLogin(getSessionWeiXinId(request),getSessionWXAppId(request));
		logger.info("weixinId = "+getSessionWeiXinId(request) +", appId = "+getSessionWXAppId(request));
		String webcontentString = weiXinService.getWxLoginStr(getSessionWeiXinId(request),getSessionWXAppId(request));
		if(userInfo==null){
			request.getSession().removeAttribute(SessionConstantDefine.CLIENT_INFO);
			return "登录失败"+ " <br /><br />"+ webcontentString;
		}else{
			if(userInfo.getCitys() == null){
				return "登录失败，没有任何信息。"+ " <br /><br />"+ webcontentString;
			}else{
				request.getSession().setAttribute(SessionConstantDefine.WX_CITY, userInfo.getCitys());
			}
			
			
			
			if(StringUtils.isEmpty(userInfo.getClientId())){
				request.getSession().removeAttribute(SessionConstantDefine.CLIENT_INFO);
				return "没有绑定手机，仅仅保存了城市定位"+ " <br /><br />"+ webcontentString +"   "+ userInfo.getExists();
			}else{
				request.getSession().setAttribute(SessionConstantDefine.CLIENT_INFO, userInfo);
			}
			
			return userInfo.toString() + "<br />" + "<br /><a href='/weixin/home' target='_black'>首页</a>" + " <br /><br />"+ webcontentString;
			
		}
	}

	/** 测试入口2 */
	@RequestMapping(value = "wxTest2", method = RequestMethod.GET)
	public @ResponseBody String wxTest2(String openid, HttpServletRequest request) {
		String result = weiXinService.getSysPram();
        return result;
	}
	
	/** 支付测试 */
	@RequestMapping(value = "testPay", method = RequestMethod.GET)
 	public String testPay(Model model, HttpServletRequest request) {
		new PublicHelper(model, request);
		model.addAttribute("appId", "wxccc2727c9193b038");
		model.addAttribute("TimeStamp", "1489655541");
		model.addAttribute("NonceStr", "44F683A84163B3523AFE57C2E008BC8C");
		model.addAttribute("Package", "prepay_id=wx2017031617111334771dcab10173020386");
		model.addAttribute("paySign", "B61CD2A5FE23DC9FE0AEEB79288ACB3B");
		return "screen/weixin/testPay";
	}

	
}








