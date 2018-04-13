function CheckLogin() {
    var checkLoginResult = false;
    $.ajax({
        url: "/weixin/checkWeiXinLogin",
        type: "get",
        async: false,
        dataType:"json",
        success: function (result) {
            if (result.code != "E00000") {
                var uinfo = "";
                
                try {
                    uinfo = app.UserInfoFromApp();
                } catch (e) { 
                    try{uinfo = UserInfoFromApp();}catch(ee){$.alert("请在APP端浏览!");}
                }
                //uinfo = "b+i8/JFRSmaAJ6jmFAC8xVDHnxg6jHEJc51l0l+ocz9XGVx9U2+HbmTVShAZSTUTZfRYqkEB6UGmDJEMytaChAF4AZ/P5xoCpix3j9JN4Sc=|2";
                
                if (uinfo == "") {
                    try {
                        app.PageToLogin();
                    } catch (e) { 
                        try{uinfo = PageToLogin();}catch(ee){$.alert("呼唤APP端登录");}
                    }
                }
                else {
                    $.ajax({
                        url: "/weixin/appLogin",
                        type: "post",
                        data: { uinfo: uinfo },
                        async: false,
                        dataType:"json",
                        success: function (result) {
                            if (result.code == "E00000") {
                                //$.toast("CheckLogin -》登录成功啦!");
                                checkLoginResult = true;
                            }
                            else {
                                $.toast("CheckLogin -》登录失败啦!"+result.message);
                                checkLoginResult = false;
                            }
                        }, error: function (msg) {

                            $.toast("CheckLogin -》登录异常啦!");
                            checkLoginResult = false;
                        }
                    });
                }
            }else{
            	checkLoginResult = true;
            }

        }, error: function (msg) {
            $.toast("CheckLogin -》操作异常!");
            checkLoginResult = false;
        }
    });
    return checkLoginResult;
}
function GetKey() {
    var numkey = Math.random();
    numkey = Math.round(numkey * 10000);
    return numkey;
}
function isAppFrame(){
    try {
        app.hasApp();
        return true;
    } catch (e) {
        try {
            hasApp();
            return true;
        } catch (ee) {
            return false;
        }
    }
}