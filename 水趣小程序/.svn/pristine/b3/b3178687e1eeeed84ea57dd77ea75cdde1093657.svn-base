// function formatTime(date) {
//   var year = date.getFullYear()
//   var month = date.getMonth() + 1
//   var day = date.getDate()

//   var hour = date.getHours()
//   var minute = date.getMinutes()
//   var second = date.getSeconds()


//   return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
// }

// function formatNumber(n) {
//   n = n.toString()
//   return n[1] ? n : '0' + n
// }

// module.exports = {
//   formatTime: formatTime
// }
/**
     * 关闭注册窗口
     */
function closeModal() {
    this.setData({
        modalWrap: false,
        modal: false
    });
}
/**
 * 打开注册窗口
 */
function openModal() {
    this.setData({
        modalWrap: true,
        modal: true
    });
}
/**
 * 获取验证码
 */
function getCode(e) {
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
                c: wx.getStorageSync('c'),
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
}
/**
 * 输入手机号时
 */
function inputPhoneNum(e) {
    var that = this;
    this.setData({
        phoneNum: e.detail.value
    });
    console.log(this.data.phoneNum);
}
/**
 * 输入验证码时
 */
function inputValidateCode(e) {
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
}
/**
 * 切换注册窗口的单选框
 */
function toggleStatus(e) {
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
}
/**
 * 绑定手机号
 */
function commit(e) {
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

}
