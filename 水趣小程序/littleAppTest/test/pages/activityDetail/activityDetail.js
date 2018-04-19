// pages/activityDetail/activityDetail.js
const app = getApp();

Page({
    data: {
        files: []
    },
    onLoad: function (options) {
        var that = this;
        var title = options.title;
        var notice = options.notice;
        var files = options.files;
        var author = options.author;
        var time = options.time;
        var showPublish = options.showPublish;
        console.log(options);
        //   console.log(files);
        app.getUserInfo(function () {
            if (options.title) {
                if (files) {
                    files = JSON.parse(files);
                }
                that.setData({
                    title: title,
                    notice: notice,
                    files: files,
                    author: author,
                    time: time,
                    showPublish: showPublish
                });
            }
        });
    },
    publish: function () {
        wx.navigateTo({
            url: '../publish/publish',
        })
    },
    share: function () {
        wx.showShareMenu({
            withShareTicket: true
        })
    },
    getUserInfo: function (e) {
        console.log(e)
        app.globalData.userInfo = e.detail.userInfo
        this.setData({
            userInfo: e.detail.userInfo,
            hasUserInfo: true
        })
    },
    onShareAppMessage: function (res) {
        var that = this;
        var title = this.data.title;
        var notice = this.data.title;
        var files = this.data.files;
        var author = this.data.author;
        var time = this.data.time;
        if (res.from === 'button') {
            // 来自页面内转发按钮
            console.log('来自页面转发按钮');
            console.log(res.target)
        }
        return {
            title: '会议通知',
            path: 'pages/activityDetail/activityDetail?title=' + title + '&notice=' + notice + '&files=' + files + '&author=' + author + '&time=' + time,
            success: function (res) {
                // 转发成功
                console.log('转发成功');
                console.log(res);
                that.setData({ 'showPublish': '' });
            },
            fail: function (res) {
                // 转发失败
                console.log('转发失败');
                console.log(res);
            }
        }
    }
})