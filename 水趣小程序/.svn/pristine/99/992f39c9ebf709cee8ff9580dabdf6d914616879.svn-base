// pages/joinList/joinList.js
const app = getApp();

Page({
    data: {
    },
    onLoad: function (options) {
        app.getUserInfo(function () {

        });
    },
    publish: function () {
        // wx.showActionSheet({
        //     itemList: ['发布活动'],
        //     success: function (res) {
        //         console.log(res.tapIndex)

        //         wx.navigateTo({
        //             url: '../publish/publish',
        //         })
        //     },
        //     fail: function (res) {
        //         console.log(res.errMsg)
        //     }
        // })
        wx.navigateTo({
            url: '../publish/publish'
        })
    },
    getUserInfo: function (e) {
        console.log(e)
        app.globalData.userInfo = e.detail.userInfo
        this.setData({
            userInfo: e.detail.userInfo,
            hasUserInfo: true
        })
    }
})