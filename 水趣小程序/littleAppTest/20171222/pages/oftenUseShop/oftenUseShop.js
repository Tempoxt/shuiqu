// pages/oftenUseShop/oftenUseShop.js
// pages/shopCart/shopCart.js
const app = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        userInfo: {},
        currentCity: "选城市",
        cacheKey: null,
        // -3:未登录，-2:未绑定，1:已登录
        hasBind: -3,
        hasCity: false,
        modalWrap: "",
        selectStatus: false,
        modal: "",
        phoneNum: "",
        getCodeText: "获取验证码",
        getCodeColor: "#EECE1F",
        validateCode: "",
        commitColor: "#ddd",
        getCodeStatus: false,
        bindStatus: true
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
            wx.showToast({
                title: '手机号不正确',
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
            wx.showToast({
                title: '手机号不正确',
            })
        } else {
            var that = this;
            if (that.data.currentCity != "选城市" && that.data.currentCity != "") {
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
            } else {
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
                cityId: cityId,
                location: location

            },
            success: function (result) {
                if (result.data.message == "成功") {
                    that.closeCityList();
                    that.setData({
                        currentCity: cityname,
                        location: location
                    });
                } else {
                    console.log('网络异常');
                }

            }, fail: function (err) {
                wx.showToast({
                    title: '网络异常！',
                })
            }
        });
        console.log(cityname, location);

    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        
        //   console.log(wx.getStorageSync('shopCarData'));
    },
    goShop:function(e){
        var hasBind = wx.getStorageSync('hasBind');
        var shopId = e.currentTarget.dataset.shopid;
        hasBind == 1 ?
            wx.redirectTo({
                url: '/pages/products/products?shopId=' + shopId,
            }) : this.openModal(); 
    },
    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {
        var that = this;
        //调用应用实例的方法获取全局数据  
        app.getUserInfo(function (userInfo) {
            //更新数据  
            that.setData({
                userInfo: userInfo
            })
        });
        wx.showLoading({
            title: '加载中',
        })
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
                    success: function (result) {

                        wx.request({
                            url: app.globalData.domain + '/wxLiteapp/getClientCollectShop',
                            method: 'POST',
                            header: {
                                'content-type': 'application/x-www-form-urlencoded'
                            },
                            data: {
                                cacheKey: wx.getStorageSync('cacheKey'),
                                c: app.globalData.c,
                                location: longitude + "," + latitude
                            },
                            success: function (res) {
                                var shops = res.data.data.shops;
                                wx.hideLoading();
                                console.log(res.data.data);


                                shops.forEach(function (item) {

                                    if (item.distance >= 1000) {
                                        item.distance = (item.distance / 1000).toFixed(2) + 'km';
                                    } else {
                                        item.distance = (item.distance).toFixed(0) + 'm';
                                    }

                                });
                                that.setData({
                                    shops: shops
                                });
                            }
                        })
                    }
                })
            }
        })
        wx.request({
            url: app.globalData.domain + '/wxLiteapp/getCollectShops',
            method: 'POST',
            header: {
                'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
                cacheKey: wx.getStorageSync('cacheKey'),
                c: app.globalData.c
            },
            success: function (res) {
                wx.hideLoading();
                console.log(res.data.data);
                that.setData({
                    result: res.data.data
                });
            }
        })
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        
    },
    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

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