//app.js
App({
    data:{
        
    },
    onLoad:function(){
    
    }, 
    onLaunch: function () {
        var that = this;
        // 获取用户信息
        that.login();
    },
    login: function (callback){
        var that = this;
        wx.login({
            success: function (res) {
                
                if (res.code) {
                    
                    //发起网络请求
                    wx.request({
                        url: that.globalData.domain + '/wxLiteapp/login',
                        method: 'POST',
                        header: {
                            'content-type': 'application/x-www-form-urlencoded'
                        },
                        data: {
                            code: res.code,
                            c: that.globalData.c
                        },
                        success: function (res) {
                            var code = res.data.code;
                            if (code == 'E00000') {
                                
                                wx.setStorageSync('cacheKey', res.data.data.cacheKey);
                                wx.setStorageSync('hasCity', res.data.data.hasCity);
                                wx.setStorageSync('hasBind', res.data.data.hasBind);
                                wx.setStorageSync('isOnLoad',0);
                                typeof callback == "function" && callback();
                            } else {
                                wx.showModal({
                                    content: res.data.message,
                                    showCancel: false
                                })
                            }

                        }
                    })
                } else {
                    wx.showModal({
                        content: '获取用户登录态失败！' + res.errMsg,
                        showCancel: false
                    })
                    //   console.log('获取用户登录态失败！' + res.errMsg)
                }
            }
        });
        
    },
    getSetting(){
        // 获取用户信息
        wx.getSetting({
            success: res => {
                if (res.authSetting['scope.userInfo']) {
                    // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
                    wx.getUserInfo({
                        success: res => {
                            // 可以将 res 发送给后台解码出 unionId
                            this.globalData.userInfo = res.userInfo

                            // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
                            // 所以此处加入 callback 以防止这种情况
                            if (this.userInfoReadyCallback) {
                                this.userInfoReadyCallback(res)
                            }
                        }
                    })
                }
            }
        })
    },
    getUserInfo: function (cb) {
        var that = this;
        
        // wx.checkSession({
        //     success: function (res) {
        //         //session 未过期，并且在本生命周期一直有效
        //         // console.log(res);
        //     },
        //     fail: function () {
        //         //登录态过期
        //         wx.login({
        //             success: function (res) {

        //                 if (res.code) {
        //                     //发起网络请求
        //                     wx.request({
        //                         url: that.globalData.domain + '/wxLiteapp/login',
        //                         method: 'POST',
        //                         header: {
        //                             'content-type': 'application/x-www-form-urlencoded'
        //                         },
        //                         data: {
        //                             code: res.code,
        //                             c: that.globalData.c
        //                         },
        //                         success: function (res) {
        //                             // console.log(res);
        //                             var code = res.data.code;
        //                             if (code == 'E00000') {

        //                                 wx.setStorageSync('cacheKey', res.data.data.cacheKey);
        //                                 wx.setStorageSync('hasCity', res.data.data.hasCity);
        //                                 wx.setStorageSync('hasBind', res.data.data.hasBind);
        //                             } else {
        //                                 wx.showModal({
        //                                     content: res.data.message,
        //                                     showCancel: false
        //                                 })
        //                             }

        //                         }
        //                     })
        //                 } else {
        //                     wx.showModal({
        //                         content: '获取用户登录态失败！' + res.errMsg,
        //                         showCancel: false
        //                     })
        //                     // console.log('获取用户登录态失败！' + res.errMsg)
        //                 }
        //             }
        //         }); //重新登录
        //     }
        // })
        
        if (this.globalData.userInfo) {
            typeof cb == "function" && cb(this.globalData.userInfo)
            
        } else {
            //调用登录接口
            
            wx.login({
                success: function () {
                    wx.getUserInfo({
                        success: function (res) {
                            that.globalData.userInfo = res.userInfo
                            typeof cb == "function" && cb(that.globalData.userInfo)
                        }
                    })
                }
            })
        }
    },
    globalData: {
        userInfo: null,
        domain: "https://x1.shuiqoo.cn",
        // 1:水趣 44:绿源
        productName:"水趣",
        c:1
    }
})

