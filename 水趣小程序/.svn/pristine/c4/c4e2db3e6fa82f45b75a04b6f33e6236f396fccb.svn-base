// pages/myOrder/myOrder.js
var app = getApp()
Page({
    data: {
        userInfo: {},
        winWidth: 0,
        winHeight: 0,
        // tab切换 
        currentTab: 0,
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
    confirmOrder:function(e){
        var orderId = e.currentTarget.dataset.orderid;
        console.log(e.currentTarget.dataset.orderid);
        // wx.request({
        //     url: app.globalData.domain + "/wxLiteapp/reapOrder",
        //     method: 'POST',
        //     header: {
        //         'content-type': 'application/x-www-form-urlencoded'
        //     },
        //     data: {
        //         cacheKey: wx.getStorageSync('cacheKey'),
        //         c: app.globalData.c,
        //         orderId: orderId
        //     },
        //     success: function (res) {
        //         var code = res.data.code;
        //         if (code == 'E00000') {
        //             wx.showModal({
        //                 content: '确认成功',
        //                 showCancel: false
        //             })
        //         } else {
        //             wx.showModal({
        //                 content: res.data.message,
        //                 showCancel: false
        //             })
        //         }

        //     },
        //     fail: function (err) {
        //         wx.showModal({
        //             title: '',
        //             showCancel: false,
        //             content: "网络异常！"
        //         })
        //     }
        // })
    },
    onLoad: function (options) {
        wx.showLoading({ title: '加载中' });
        
        var that = this;
        //调用应用实例的方法获取全局数据  
        app.getUserInfo(function (userInfo) {
            var currentTab = options.currentTab;
            //更新数据  
            that.setData({
                userInfo: userInfo
            })
            if (currentTab == 1) {
                wx.request({
                    url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                    method: 'POST',
                    header: {
                        'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                        cacheKey: wx.getStorageSync('cacheKey'),
                        c: app.globalData.c,
                        statusId: 100,
                        pageIndex: 1
                    },
                    success: function (result) {
                        console.log(result);
                        wx.hideLoading();
                        that.setData({
                            daifukuan: result.data.data,
                            currentTab: currentTab
                        });
                    }
                })
            } else {
                wx.request({
                    url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                    method: 'POST',
                    header: {
                        'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                        cacheKey: wx.getStorageSync('cacheKey'),
                        c: app.globalData.c,
                        pageIndex: 1
                    },
                    success: function (result) {
                        console.log(result);
                        wx.hideLoading();
                        that.setData({
                            all: result.data.data
                        });
                    }
                })
            }
        }); 


        wx.getSystemInfo({

            success: function (res) {
                that.setData({
                    winWidth: res.windowWidth,
                    winHeight: res.windowHeight
                });
            }

        });
        
    },
    onReady:function(){
        
    },
    orderDetail:function(e){
        var orderId = e.currentTarget.dataset.orderid;
        console.log(e.currentTarget.dataset.orderid);
        wx.navigateTo({
            url: '../orderDetail/orderDetail?orderId='+orderId
        })
    },
    bindChange: function (e) {

        var that = this;
        that.setData({ currentTab: e.detail.current });
        if (this.data.currentTab == 0) {
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    pageIndex: 1
                },
                success: function (result) {
                    console.log(result);
                    wx.hideLoading();
                    that.setData({
                        all: result.data.data
                    });
                }
            })
            wx.hideLoading();

        } else if (this.data.currentTab == 1) {
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    statusId: 100,
                    pageIndex: 1
                },
                success: function (result) {
                    console.log(result);
                    wx.hideLoading();
                    that.setData({
                        daifukuan: result.data.data
                    });
                }
            })
        } else if (this.data.currentTab == 2) {
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    statusId: 101,
                    pageIndex: 1
                },
                success: function (result) {
                    console.log(result);
                    wx.hideLoading();
                    that.setData({
                        daifahuo: result.data.data
                    });
                }
            })
        } else if (this.data.currentTab == 3) {
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    statusId: 102,
                    pageIndex: 1
                },
                success: function (result) {
                    console.log(result);
                    wx.hideLoading();
                    that.setData({
                        daishouhuo: result.data.data
                    });
                }
            })
        } else if (this.data.currentTab == 4) {
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    statusId: 103,
                    pageIndex: 1
                },
                success: function (result) {
                    console.log(result);
                    wx.hideLoading();
                    that.setData({
                        daipingjia: result.data.data
                    });
                }
            })
        }
    },

    swichNav: function (e) {
        wx.showLoading({ title: '加载中' });
        var that = this;
        // 100:待付款,101:待发货,102:待收货,103:待评价，104退款/取消
        console.log(e.target.dataset);
        if (this.data.currentTab === e.target.dataset.current) {
            return false;
        } else {
            that.setData({
                currentTab: e.target.dataset.current
            })
        }
        if (e.target.dataset.current == 0){
            wx.hideLoading();

        }else if (e.target.dataset.current==1){
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    statusId:100,
                    pageIndex: 1
                },
                success: function (result) {
                    console.log(result);
                    wx.hideLoading();
                    that.setData({
                        daifukuan: result.data.data
                    });
                }
            })
        } else if (e.target.dataset.current == 2){
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    statusId: 101,
                    pageIndex: 1
                },
                success: function (result) {
                    console.log(result);
                    wx.hideLoading();
                    that.setData({
                        daifahuo: result.data.data
                    });
                }
            })
        } else if (e.target.dataset.current == 3) {
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    statusId: 102,
                    pageIndex: 1
                },
                success: function (result) {
                    console.log(result);
                    wx.hideLoading();
                    that.setData({
                        daishouhuo: result.data.data
                    });
                }
            })
        } else if (e.target.dataset.current == 4) {
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    statusId: 103,
                    pageIndex: 1
                },
                success: function (result) {
                    console.log(result);
                    wx.hideLoading();
                    that.setData({
                        daipingjia: result.data.data
                    });
                }
            })
        }
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
