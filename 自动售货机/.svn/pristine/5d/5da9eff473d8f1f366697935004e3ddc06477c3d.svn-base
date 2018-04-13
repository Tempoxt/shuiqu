
//获取URL中参数
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}



mui(".mui-bar-tab").on("tap","a",function(){
	var that = this;
	mui.openWindow({
	    url:that.href,
	    extras:{
	       random:Math.random()
	    },
	})
})
function isAlipayOrWechat() {
	
    if (/MicroMessenger/.test(window.navigator.userAgent)) { 
    	//微信授权

    	if(localStorage.weixin){
    		// localStorage.removeItem('weixin');
    	}else{
    		localStorage.weixin = true;
    		window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5288d5e0e47b4f76&redirect_uri=https%3a%2f%2fsvm.shuiqoo.cn%2ffunVm%2fauthorize&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";

    	}

	} else if (/AlipayClient/.test(window.navigator.userAgent)) { 
	    //支付宝授权
	    if(localStorage.alipay){

    	}else{
    		localStorage.alipay = true;
	    	window.location.href="https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2018012602080855&scope=auth_userinfo&redirect_uri=https%3a%2f%2fsvm.shuiqoo.cn/funVm/index";
	    }
	} else {
	    
 	document.body.innerHTML='请在微信或支付宝客户端扫描打开';
 	document.body.setAttribute('style','display: flex;justify-content: center;align-items: center;background: rgba(0,0,0,0.8);color: #fff;font-size:14px;');

	}
}

// isAlipayOrWechat();