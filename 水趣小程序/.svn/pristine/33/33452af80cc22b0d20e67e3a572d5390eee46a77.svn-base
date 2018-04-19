// 引入SDK核心类
// var QQMapWX = require('../../utils/qqmap-wx-jssdk.js');
// var qqmapsdk;

// 引用百度地图微信小程序JSAPI模块 
var bmap = require('../../utils/bmap-wx.min.js'); 
const app = getApp();
Page({
    data:{
        userInfo: {},
        currentCity:"定位中...",
        address:"",
        cityList:"",
        sugData: '',
        shopList:null,
        hasBind: -3,
        modalWrap: "",
        modal: "",
        getCodeText: "获取验证码",
        getCodeColor: "#EECE1F"
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
        if (!regPhone.test(this.data.phoneNum)) {
            wx.showModal({
                title: '',
                content: '手机号不正确',
                showCancel: false
            })
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
            wx.showModal({
                title: '',
                content: '手机号不正确',
                showCancel: false
            })
        } else {
            var that = this;
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

        }

    },
    onLoad: function () {
        var that = this;
        //调用应用实例的方法获取全局数据  
        app.getUserInfo(function (userInfo) {
            //更新数据
            var cacheKey = wx.getStorageSync('cacheKey');
            var hasBind = wx.getStorageSync('hasBind');
            wx.setStorageSync('isOnLoad',0);  
            that.setData({
                userInfo: userInfo,
                cacheKey: cacheKey,
                hasBind: hasBind
            });
            
            
            that.autoLocation();
        }); 
    },
    onShow: function () {

    },
    /**
     * 顶部选择城市
     */
    topGetCity:function(){
        var that = this;
        if (this.data.hasBind == 1){
            if (wx.getStorageSync('currentCity')){
                this.setData({
                    currentCity: wx.getStorageSync('currentCity'),
                    cityId: wx.getStorageSync('cityId') 
                });
            }else{
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
                        var code = res.data.code;
                        
                        if (code = 'E00000') {
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
                        } else {
                            wx.showModal({
                                content: res.data.message,
                                showCancel: false
                            })
                        }
                        
                    },
                    fail:function(err){
                        wx.hideLoading();
                        wx.showModal({
                            showCancel: false,
                            content: err
                        })
                    }
                })
            }
            
        }else{
            this.openModal();
        }
        
    },
    /**
     * 选择城市
     */
    getCity: function () {
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
    closeCityList:function(){
        this.setData({
            cityList: false
        });
    },
    selectCity:function(e){
        console.log(e);
        this.closeCityList();
        this.setData({
            address:""
        });
    },
    /**
     * 打开页面自动定位
     */
    autoLocation:function(){
        wx.showLoading({
            title: '加载中',
        });
        var that = this;
        wx.getLocation({
            type: 'wgs84',
            success: function (res) {
                var errMsg = res.errMsg;
                if (errMsg =='getLocation:ok'){
                    var latitude = res.latitude
                    var longitude = res.longitude
                    var speed = res.speed
                    var accuracy = res.accuracy

                    wx.request({
                        url: app.globalData.domain + "/wxLiteapp/changeWxLocationToBaidu",
                        method: 'POST',
                        header: {
                            'content-type': 'application/x-www-form-urlencoded'
                        },
                        data: {
                            cacheKey: that.data.cacheKey,
                            c: app.globalData.c,
                            lat: latitude,
                            lon: longitude
                        },
                        success: function (res) {
                            // wx.hideLoading();
                            var code = res.data.code;
                            if (code ='E00000'){
                                //去除城市中的市字
                                var city = res.data.city.substr(0, res.data.city.lastIndexOf('市'));
                                that.setData({
                                    currentCity: city,
                                    address: res.data.district + res.data.street
                                });
                                wx.request({
                                    url: app.globalData.domain + "/wxLiteapp/nearbyShops",
                                    method: 'POST',
                                    header: {
                                        'content-type': 'application/x-www-form-urlencoded'
                                    },
                                    data: {
                                        cacheKey: that.data.cacheKey,
                                        c: app.globalData.c,
                                        location: longitude + "," + latitude
                                    },
                                    success: function (res) {
                                        wx.hideLoading();
                                        var code = res.data.code;
                                        if (code = 'E00000') {
                                            var shops = res.data.data.shops;
                                            shops.forEach(function (item) {

                                                if (item.distance >= 1000) {
                                                    item.distance = (item.distance / 1000).toFixed(2) + 'km';
                                                } else {
                                                    item.distance = (item.distance).toFixed(0) + 'm';
                                                }

                                            });

                                            that.setData({
                                                shopList: shops
                                            });
                                        }else{
                                            wx.showModal({
                                                content: res.data.message,
                                                showCancel: false
                                            })
                                        }
                                        
                                        
                                    },
                                    fail:function(err){
                                        wx.showModal({
                                            content: err,
                                            showCancel: false
                                        })
                                    }
                                })
                            }else{
                                wx.showModal({
                                    content: res.data.message,
                                    showCancel: false
                                })
                            }
                            
                            
                        },
                        fail:function(err){
                            wx.showModal({
                                content: err,
                                showCancel: false
                            })
                        }
                    })
                }
                
            },
            fail:function(err){
                wx.showModal({
                    content: err,
                    showCancel:false
                })
            }
        });
    },
    /**
     * 重新定位
     */
    locationAgain:function(){
        var that = this;
        this.data.hasBind == 1 ? 
        wx.getLocation({
            type: 'wgs84',
            success: function (res) {
                wx.showLoading({
                    title: '加载中',
                });
                var latitude = res.latitude
                var longitude = res.longitude
                var speed = res.speed
                var accuracy = res.accuracy
                // console.log(res);
                wx.request({
                    url: app.globalData.domain+"/wxLiteapp/changeWxLocationToBaidu",
                    method: 'POST',
                    header: {
                        'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                        cacheKey: that.data.cacheKey,
                        c: app.globalData.c,
                        lat: latitude,
                        lon: longitude
                    },
                    success:function(result){
                        // wx.hideLoading();
                        console.log(result);
                        //去除城市中的市字
                        var city = result.data.city.substr(0,result.data.city.lastIndexOf('市'));
                        
                        that.setData({
                            currentCity:city,
                            address: result.data.district+result.data.street
                        });
                        wx.request({
                            url: app.globalData.domain + "/wxLiteapp/nearbyShops",
                            method: 'POST',
                            header: {
                                'content-type': 'application/x-www-form-urlencoded'
                            },
                            data: {
                                cacheKey: that.data.cacheKey,
                                c: app.globalData.c,
                                location: longitude+"," +latitude
                            },
                            success: function (result) {
                                var shops = result.data.data.shops;
                                shops.forEach(function (item) {

                                    if (item.distance >= 1000) {
                                        item.distance = (item.distance / 1000).toFixed(2) + 'km';
                                    } else {
                                        item.distance = (item.distance).toFixed(0) + 'm';
                                    }

                                });
                                
                                that.setData({
                                    shopList: result.data.data.shops
                                });
                                wx.hideLoading();
                            }
                        })
                    }
                })
            }
        }) : this.openModal();
    },
    baiduMap: function (streetName){
        var that = this;
        var BMap = new bmap.BMapWX({
            ak: 'CFecNbZbabHRfdW8QCz40xqkvrNrB3Bu'
        });
        var fail = function (data) {
            console.log(data.errMsg);
        };
        var success = function (data) {
            console.log(data.message);
            // var sugData = '';
            // for (var i = 0; i < data.result.length; i++) {
            //     sugData =  data.result[i].name;
            // }
            that.setData({
                sugData: data.result,
                inputHandler:''
            });
        }
        // 发起suggestion检索请求 
        BMap.suggestion({
            query: streetName,
            region: this.data.currentCity,
            city_limit: true,
            fail: fail,
            success: success
        });
    },
    /**
     * 手动搜索附近水店
     */
    inputHandler:function(e){
        var that = this;
        var inputHandler = e.detail.value;
        this.setData({
            inputHandler: inputHandler
        });
    },
    search: function (e) {
        var inputHandler = this.data.inputHandler;
        this.data.hasBind == 1 ? this.baiduMap(inputHandler):this.openModal();
    },
    goShop:function(e){
        var shopId = e.currentTarget.dataset.shopid;
        this.data.hasBind == 1 ?
        wx.redirectTo({
            url: '/pages/products/products?shopId='+shopId,
        }):this.openModal(); 
       
    },
    /**
     * 选择搜索结果，获取附近水店
     */
    searchNearby:function(e){
        // console.log(111);
        var that = this;
        var city      = e.currentTarget.dataset.city;
        var district  = e.currentTarget.dataset.district;
        var street    = e.currentTarget.dataset.street;
        var latitude  = e.currentTarget.dataset.lat;
        var longitude = e.currentTarget.dataset.lng;
        console.log(e);
        that.setData({
            address:district + street,
            sugData:''
        });
        this.data.hasBind == 1?
        wx.request({
            url: app.globalData.domain + "/wxLiteapp/nearbyShops",
            method: 'POST',
            header: {
                'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
                cacheKey: that.data.cacheKey,
                c: app.globalData.c,
                location: longitude + "," + latitude
            },
            success: function (res) {
                wx.hideLoading();
                var code = res.data.code;
                if (code = 'E00000') {
                    var shops = res.data.data.shops;
                    shops.forEach(function (item) {

                        if (item.distance >= 1000) {
                            item.distance = (item.distance / 1000).toFixed(2) + 'km';
                        } else {
                            item.distance = (item.distance).toFixed(0) + 'm';
                        }

                    });
                    that.setData({
                        shopList: shops
                    });
                    console.log(res.data.data.shops);
                }else{
                    wx.hideLoading();
                    wx.showModal({
                        content: res.data.message,
                        showCancel: false
                    })
                }
                
            },
            fail:function(err){
                wx.showModal({
                    showCancel: false,
                    content: err
                })
            }
        }):this.openModal();
        
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