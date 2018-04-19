//index.js
//获取应用实例
const app = getApp();

Page({
  data: {
  },
  onLoad: function (options) {
      var that = this;
    app.getUserInfo(function(){
        that.setData({userInfo:app.globalData.userInfo});
        console.log(app.globalData.userInfo.gender);
        wx.request({
            url: 'http://localhost:8080/addUserInfo',
            data:{
                userinfo:that.data.userInfo
            },
            success:function(res){
                wx.request({
                    url: 'http://localhost:8080/showUserinfo',
                    success: function (res) {
                        console.log(res);
                    }
                })
            }
        })
    });
      
  },
  publish:function(){
    //   wx.showActionSheet({
    //       itemList: ['发布活动'],
    //       success: function (res) {
    //         console.log(res.tapIndex)
            
    //         wx.navigateTo({
    //             url: '../publish/publish',
    //         })
    //       },
    //       fail: function (res) {
    //           console.log(res.errMsg)
    //       }
    //   })
    wx.navigateTo({
        url: '../publish/publish',
    })
  },
  getUserInfo: function(e) {
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  }
})
