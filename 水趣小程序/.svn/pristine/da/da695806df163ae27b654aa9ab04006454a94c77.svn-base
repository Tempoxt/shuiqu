// pages/publish/publish.js
//获取应用实例
const app = getApp();

Page({
    data: {
        motto: 'Hello World',
        userInfo: {},
        hasUserInfo: false,
        canIUse: wx.canIUse('button.open-type.getUserInfo'),
        len: 0,
        files: []
    },
    /*通知标题*/
    inputTitle:function(e){
        var title = e.detail.value;
        this.setData({title:title});
        console.log(e.detail.value);
    },
    /*输入通知内容*/
    removeImg: function (e) {
        var files = this.data.files;
        var imgItem = e.currentTarget.dataset.src;
        for (var i = 0; i < files.length; i++) {
            if (imgItem == files[i]) {
                files.splice(i, 1);
                break;
            }
        }
        this.setData({ files: files });
        console.log(e.currentTarget.dataset.src);
    },
    inputContent: function (e) {
        var notice = e.detail.value;
        var len = e.detail.cursor;
        this.setData({ notice:notice,len: len });
        console.log(e.detail.cursor);
    },
    /*发布*/
    publish:function(){
        var title = this.data.title;
        var notice = this.data.notice;
        var files = JSON.stringify(this.data.files);
        var author = app.globalData.userInfo.nickName;

        var timestamp = new Date();
        var year = timestamp.getFullYear();
        var month = timestamp.getMonth() + 1;
        var day = timestamp.getDate();
        var time = year + "年" + month + "月" + day + "日";
        if(title && notice){
            wx.navigateTo({
                url: '../activityDetail/activityDetail?title=' + title + "&notice=" + notice + "&files=" + files + "&author=" + author+"&time="+time+"&showPublish="+true
            })
        }else{
            wx.showModal({
                content: '标题或内容不能为空',
                showCancel: false
            })
        }
        
    },
    /*上传图片*/
    uploadImg: function () {
        var that = this;
        wx.chooseImage({
            sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
            success: function (res) {
                console.log(res);
                // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
                that.setData({
                    files: that.data.files.concat(res.tempFilePaths)
                });
                // if (that.data.files.length==1){
                //     // wx.showModal({
                //     //     content: '只能添加一张图片',
                //     //     showCancel:false
                //     // })
                //     wx.showToast({
                //         title: '只能添加一张图片',
                //         image:'../../images/error_popover.png'
                //     })
                // }else{
                //     that.setData({
                //         files: that.data.files.concat(res.tempFilePaths)
                //     });
                // }
                    
               
                
            }
        })
        //   wx.chooseImage({
        //       success: function (res) {
        //           console.log(res);
        //           var tempFilePaths = res.tempFilePaths
        //           wx.uploadFile({
        //               url: 'https://example.weixin.qq.com/upload', //仅为示例，非真实的接口地址
        //               filePath: tempFilePaths[0],
        //               name: 'file',
        //               formData: {
        //                   'user': 'test'
        //               },
        //               success: function (res) {
        //                   console.log(res);
        //                   var data = res.data
        //                   //do something
        //               }
        //           })
        //       }
        //   })
    },
    onLoad: function () {
        app.getUserInfo(function () {

        });
        // if (app.globalData.userInfo) {
        //   this.setData({
        //     userInfo: app.globalData.userInfo,
        //     hasUserInfo: true
        //   })
        // } else if (this.data.canIUse){
        //   // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
        //   // 所以此处加入 callback 以防止这种情况
        //   app.userInfoReadyCallback = res => {
        //     this.setData({
        //       userInfo: res.userInfo,
        //       hasUserInfo: true
        //     })
        //   }
        // } else {
        //   // 在没有 open-type=getUserInfo 版本的兼容处理
        //   wx.getUserInfo({
        //     success: res => {
        //       app.globalData.userInfo = res.userInfo
        //       this.setData({
        //         userInfo: res.userInfo,
        //         hasUserInfo: true
        //       })
        //     }
        //   })
        // }
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
