//index.js
//获取应用实例
var app = getApp();
Page({
    data: {
        userInfo:{},
        currentCity: "选城市",
        cacheKey:null,
        // -3:未登录，-2:未绑定，1:已登录
        hasBind:1,
        hasCity:false,
        modalWrap:"",
        selectStatus:true,
        modal:"",
        phoneNum:"",
        getCodeText:"获取验证码",
        getCodeColor: "#EECE1F",
        validateCode:"",
        commitColor: "#ddd",
        getCodeStatus:false,
        bindStatus:true
    },
    /**
  * 关闭注册窗口
  */
    closeModal: function () {
        this.setData({
            modalWrap: false,
            modal: false
        });
    },
    /**
     * 打开注册窗口
     */
    openModal: function () {
        this.setData({
            modalWrap: true,
            modal: true
        });
    },
    /**
     * 获取验证码
     */
    getCode: function (e) {
        var regPhone = /^1[3|4|5|7|8][0-9]{9}$/;
        //校验手机号失败
        if (this.data.phoneNum==""){
            wx.showToast({
                title: '手机号不能为空',
            });
        }
        else if (!regPhone.test(this.data.phoneNum)) {
            wx.showToast({
                title: '手机号不正确',
            });
        } else {
            var that = this;
            function timeOver() {
                if (that.data.getCodeText == 0) {
                    clearInterval(Countdown);
                    that.setData({
                        getCodeText: "获取验证码",
                        getCodeColor: "#EECE1F",
                        getCodeStatus: false
                    });
                }
            }
            //获取手机验证码
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/moblieMsg",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    cellphone: that.data.phoneNum
                },
                success: function (result) {

                    console.log(result);
                }
            })
            //验证码倒计时，改变状态，不可点击
            this.setData({
                getCodeColor: "#ddd",
                getCodeText: "60",
                getCodeStatus: true
            });
            var Countdown = setInterval(function () {
                that.setData({
                    getCodeText: that.data.getCodeText - 1
                });
                timeOver();
            }, 1000)
        }
    },
    /**
     * 输入手机号时
     */
    inputPhoneNum: function (e) {
        var that = this;
        this.setData({
            phoneNum: e.detail.value
        });
        console.log(this.data.phoneNum);
    },
    /**
     * 输入验证码时
     */
    inputValidateCode: function (e) {
        this.setData({
            validateCode: e.detail.value
        });
        if (this.data.phoneNum != "" && this.data.validateCode != "" && this.data.selectStatus) {
            this.setData({
                commitColor: "#EECE1F",
                bindStatus: false
            });
        } else {
            this.setData({
                commitColor: "#ddd",
                bindStatus: true
            });
        }
        console.log(this.data.validateCode);
    },
    /**
     * 切换注册窗口的单选框
     */
    toggleStatus: function () {
        this.setData({
            selectStatus: !this.data.selectStatus
        })
        if (this.data.phoneNum != "" && this.data.validateCode != "" && this.data.selectStatus) {
            this.setData({
                commitColor: "#EECE1F",
                bindStatus: false
            });
        } else {
            this.setData({
                commitColor: "#ddd",
                bindStatus: true
            });
        }
    },
    /**
     * 绑定手机号
     */
    commit: function (e) {
        var that = this;
        var regPhone = /^1[3|4|5|7|8][0-9]{9}$/;
        //校验手机号失败
        if (!regPhone.test(this.data.phoneNum)) {
            wx.showToast({
                title: '手机号不正确',
            })
        } else {
            var that = this;
            if (that.data.currentCity != "选城市" && that.data.currentCity != ""){
                wx.request({
                    url: app.globalData.domain + "/wxLiteapp/saveBind",
                    method: 'POST',
                    header: {
                        'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                        cacheKey: wx.getStorageSync('cacheKey'),
                        c: app.globalData.c,
                        cellphone: that.data.phoneNum,
                        code: that.data.validateCode
                    },
                    success: function (result) {
                        if (result.data.code != "E00000") {
                            wx.showModal({
                                title: '',
                                showCancel: false,
                                content: result.data.message
                            })
                        } else {
                            wx.showModal({
                                title: '',
                                showCancel: false,
                                content: "注册成功"
                            })
                            that.setData({
                                hasBind: 1
                            });
                            wx.setStorageSync('hasBind', 1);
                            that.closeModal();
                        }

                    },
                    fail: function (err) {
                        wx.showModal({
                            title: '',
                            showCancel: false,
                            content: "网络异常！"
                        })
                    }
                })
            }else{
                wx.showToast({
                    title: '请选择城市'
                })
            }
            

        }

    },
    /**
     * 选择城市
     */
    getCity: function () {
        wx.showLoading({
            title: '加载中',
        })
        var that = this;
        wx.request({
            url: app.globalData.domain + "/wxLiteapp/cityJson",
            method: 'POST',
            header: {
                'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
                cacheKey: that.data.cacheKey,
                c: app.globalData.c
            },
            success: function (res) {
                wx.hideLoading();
                var citys = {};
                var allCitys = [];
                citys.hotCitys = res.data.hotCitys;
                //循环所有城市
                for (var j = 0; j < res.data.allCitys.length; j++) {
                    var flag = false
                    for (var i = 0; i < allCitys.length; i++) {
                        //如果新数组里有这个首字母
                        // console.log(res.data.allCitys[j].firstLetter);
                        if (allCitys[i].firstLetter == res.data.allCitys[j].firstLetter) {
                            flag = true;
                            allCitys[i]['cityInfo'].push({ "baiduCode": res.data.allCitys[j].baiduCode, "cityId": res.data.allCitys[j].cityId, "cityName": res.data.allCitys[j].cityName, "location": res.data.allCitys[j].location });
                        }
                    }
                    //没有这个首字母
                    if (!flag) {
                        allCitys.push({ "firstLetter": res.data.allCitys[j].firstLetter, "cityInfo": [{ "baiduCode": res.data.allCitys[j].baiduCode, "cityId": res.data.allCitys[j].cityId, "cityName": res.data.allCitys[j].cityName, "location": res.data.allCitys[j].location }] })

                    }




                }
                citys.allCitys = allCitys;
                console.log(citys);
                that.setData({
                    cityList: citys,
                    shopList: null,
                    sugData: ''
                });
            }
        })     
    },
    closeCityList: function () {
        this.setData({
            cityList: false
        });
    },
    selectCity: function (e) {
        var that = this;
        // console.log(e);
        var cityname = e.currentTarget.dataset.cityname;
        var cityId = e.currentTarget.dataset.id;
        var location = e.currentTarget.dataset.location;
        //获取积分和电子票数据
        wx.request({
            url: app.globalData.domain + "/wxLiteapp/saveCity",
            method: 'POST',
            header: {
                'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
                cacheKey: wx.getStorageSync('cacheKey'),
                c: app.globalData.c,
                cityId:cityId,
                location:location

            },
            success: function (res) {
                var code = res.data.code;
                if (code == 'E00000'){
                    that.closeCityList();
                    that.setData({
                        currentCity: cityname,
                        location: location
                    });
                }else{
                    wx.showModal({
                        content: res.data.message,
                        showCancel: false
                    })
                }

            }, fail: function (err) {
                wx.showModal({
                    showCancel: false,
                    content: err
                })
            }
        });  
        console.log(cityname,location);
        
    },
    /**
     * 快速订水
     */
    quickOrderWater:function(){
        // wx.showLoading({
        //     title: '加载中',
        // })
        var that = this;
        if (this.data.hasBind == 1){
            
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/getLastBuyShopProduct",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c
                },
                success: function (res) {
                    var code = res.data.code;
                    if (code == 'E00000') {
                        var shopId = res.data.data.shopId;
                        // wx.navigateTo({ url: '../selectShop/selectShop' });
                        if (shopId) {
                            wx.navigateTo({ url: '../products/products?shopId=' + shopId });
                        } else {
                            wx.navigateTo({ url: '../selectShop/selectShop' });
                        }
                        
                    } else if(code == '-3') {
                        app.c(function(){
                            wx.request({
                                url: app.globalData.domain + "/wxLiteapp/getLastBuyShopProduct",
                                method: 'POST',
                                header: {
                                    'content-type': 'application/x-www-form-urlencoded'
                                },
                                data: {
                                    cacheKey: wx.getStorageSync('cacheKey'),
                                    c: app.globalData.c
                                },
                                success: function (res) {
                                    var code = res.data.code;
                                    if (code == 'E00000') {
                                        var shopId = res.data.data.shopId;
                                        // wx.navigateTo({ url: '../selectShop/selectShop' });
                                        if (shopId) {
                                            wx.navigateTo({ url: '../products/products?shopId=' + shopId });
                                        } else {
                                            wx.navigateTo({ url: '../selectShop/selectShop' });
                                        }

                                    } else if (code == '-3') {
                                        wx.showModal({
                                            showCancel: false,
                                            content: res.data.message
                                        })

                                    }

                                },
                                fail: function (err) {
                                    wx.showModal({
                                        showCancel: false,
                                        content: err
                                    })
                                }
                            })
                        });
                        
                    
                    }
                    
                    // console.log(result.data.data.shopId);
                    // wx.hideLoading();
                    
                    
                },
                fail:function(err){
                    wx.showModal({
                        showCancel: false,
                        content: err
                    })
                }
            })
            
        }else{
            this.openModal()
        }
        
    },
    /**
     * 常用店铺
     */
    oftenUseShop:function(){
        this.data.hasBind == 1 ? wx.navigateTo({ url: '../oftenUseShop/oftenUseShop'}) : this.openModal();
    },
    /**
     * 我的订单
     */
    myOrders:function(){
        this.data.hasBind == 1 ? wx.navigateTo({ url: '../myOrder/myOrder' }) : this.openModal();
    },
    /**
     * 我的地址
     */
    myAddress:function(){
        this.data.hasBind == 1 ? wx.navigateTo({ url: '../address/address' }) : this.openModal();
    },
    /**
     * 分享有礼
     */
    shareGetGift:function(){
        this.data.hasBind == 1 ? wx.showModal({ title: '', content: '开发中...', showCancel: false }) : this.openModal();
    },
    /**
     * 设置
     */
    setInfo:function(){
        this.data.hasBind == 1 ? wx.showModal({ title: '', content: '开发中...', showCancel: false }) : this.openModal();
    },
    onLaunch:function(){
        
    },
    onLoad: function () {
        
        // var that = this;
        
         
        
        
    },
    onReady:function(){
        wx.showLoading({
            title: '加载中',
        });
        var that = this;
        //调用应用实例的方法获取全局数据  
        app.getUserInfo(function (userInfo) {
                
            //获取积分和电子票数据
            var cacheKey = wx.getStorageSync('cacheKey');
            var hasBind = wx.getStorageSync('hasBind');
            that.setData({
                userInfo: userInfo,
                cacheKey: cacheKey,
                hasBind: hasBind
            });
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/getIntegralCodeNum",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c
                },
                success: function (res) {
                    console.log(res.data);
                    var code = res.data.code;
                    // console.log(code);
                    wx.hideLoading();
                    if (code == 'E00000') {
                        // clearInterval(times);
                        that.setData({
                            integral: res.data.data.integral,
                            ticket_number: res.data.data.ticket_number
                        });
                    }else if(code=="-3"){
                        app.login(function(){
                            wx.request({
                                url: app.globalData.domain + "/wxLiteapp/getIntegralCodeNum",
                                method: 'POST',
                                header: {
                                    'content-type': 'application/x-www-form-urlencoded'
                                },
                                data: {
                                    cacheKey: wx.getStorageSync('cacheKey'),
                                    c: app.globalData.c
                                },
                                success: function (res) {
                                    console.log(res.data);
                                    var code = res.data.code;
                                    // console.log(code);
                                    wx.hideLoading();
                                    if (code == 'E00000') {
                                        // clearInterval(times);
                                        that.setData({
                                            integral: res.data.data.integral,
                                            ticket_number: res.data.data.ticket_number
                                        });
                                    } else if (code == "-3") {
                                        wx.showModal({
                                            showCancel: false,
                                            content: res.data.message
                                        })
                                    }

                                },
                                fail: function (err) {
                                    clearInterval(timers);
                                    wx.hideLoading();
                                    wx.showModal({
                                        showCancel: false,
                                        content: err
                                    })
                                }
                            })
                        });
                        
                    }

                },
                fail: function (err) {
                    clearInterval(timers);
                    wx.hideLoading();
                    wx.showModal({
                        showCancel: false,
                        content: err
                    })
                }
            })
                   
        });
        
    },
    /**
     * 跳转至积分页
     */
    integral:function(){
        this.data.hasBind == 1 ? wx.navigateTo({
            url: '../integral/integral',
        }) : this.openModal();
    },
    /**
     * 跳转至电子票页面
     */
    eTicket:function(){
        this.data.hasBind == 1 ? wx.navigateTo({
            url: '../myTicket/myTicket',
        }) : this.openModal();
    },
    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {
        return {
            title: app.globalData.productName,
            desc: '最具人气的订水平台!',
            path: 'pages/index/index'
        }
    }
})
