
module.exports = {
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
                        getCodeColor: "linear-gradient(to right,#F6CC47,#FFAD52)",
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
                    c: wx.getStorageSync('c'),
                    cellphone: that.data.phoneNum
                },
                success: function (res) {

                    console.log(res);
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
                fontColor:"#fff",
                bindStatus: false
            });
        } else {
            this.setData({
                commitColor: "#ddd",
                fontColor: "#666",
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
                fontColor:"#fff",
                bindStatus: false
            });
        } else {
            this.setData({
                commitColor: "#ddd",
                fontColor:"#666",
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
                    c: wx.getStorageSync('c'),
                    cellphone: that.data.phoneNum,
                    code: that.data.validateCode
                },
                success: function (res) {
                    var code = res.data.code;
                    if (res.data.code != "E00000") {
                        wx.showModal({
                            title: '',
                            showCancel: false,
                            content: res.data.message
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

    }
} 

// wx.request({
//     url: app.globalData.domain + "/wxLiteapp/getenterpriseyatongtxt_v2",
//     method: 'POST',
//     header: {
//         'content-type': 'application/x-www-form-urlencoded'
//     },
//     data: {
//         cacheKey: wx.getStorageSync('cacheKey'),
//         c: app.globalData.c
//     },
//     success: function (res) {
//         var code = res.data.code;

//         if (code == 'E00000') {


//         } else if (code == '-3') {
//             app.login(function () {
//                 wx.request({
//                     url: app.globalData.domain + "/wxLiteapp/getenterpriseyatongtxt_v2",
//                     method: 'POST',
//                     header: {
//                         'content-type': 'application/x-www-form-urlencoded'
//                     },
//                     data: {
//                         cacheKey: wx.getStorageSync('cacheKey'),
//                         c: app.globalData.c
//                     },
//                     success: function (res) {
//                         var code = res.data.code;
//                         wx.hideLoading();
//                         if (code == 'E00000') {

//                         } else {
//                             wx.showModal({
//                                 content: res.data.message,
//                                 showCancel: false
//                             })
//                         }
//                     },
//                     fail: function (err) {
//                         wx.hideLoading();
//                         wx.showModal({
//                             showCancel: false,
//                             content: err
//                         })
//                     }
//                 });
//             });
//         }
//         wx.hideLoading();

//     },
//     fail: function (err) {
//         wx.hideLoading();
//         wx.showModal({
//             showCancel: false,
//             content: err
//         })
//     }
// });

