//app.js
App({
  onLaunch: function (options) {
      if (options.referrerInfo){
          console.log(options.referrerInfo.extraData.foo);
        //   wx.showModal({
        //       content: options.referrerInfo.extraData.foo,
        //       showCancel:false
        //   })
      }
      
    //   if (options){
    //       wx.showModal({
    //           content: options.extraData.foo,
    //       })
    //   }
      
    // // 展示本地存储能力
    // var logs = wx.getStorageSync('logs') || []
    // logs.unshift(Date.now())
    // wx.setStorageSync('logs', logs)

    // // 登录
    // wx.login({
    //   success: res => {
    //     // 发送 res.code 到后台换取 openId, sessionKey, unionId
    //   }
    // })
    // // 获取用户信息
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

      if (this.globalData.userInfo) {
          typeof cb == "function" && cb(this.globalData.userInfo);
        //   console.log('授权成功');
      } else {
          wx.getUserInfo({
              success: function (res) {
                  that.globalData.userInfo = res.userInfo;
                  console.log(res.userInfo);
                  // console.log(111111);
                  console.log('授权成功');
                  // console.log(res.userInfo);
                  typeof cb == "function" && cb(that.globalData.userInfo);

                  // if(!that.globalData.userInfo){
                  //     wx.request({
                  //         url: app.globalData.domain + "/wxLiteapp/saveSubscribeInfo",
                  //         method: 'POST',
                  //         header: {
                  //             'content-type': 'application/x-www-form-urlencoded'
                  //         },
                  //         data: {
                  //             cacheKey: wx.getStorageSync('cacheKey'),
                  //             c: app.globalData.c,
                  //             orderId: orderId
                  //         },
                  //         success: function (res) {
                  //             console.log(res.data.data);
                  //             var th_reason = res.data.data.th_reasons[0];
                  //             wx.hideLoading();
                  //             that.setData({
                  //                 tuihuo: res.data.data,
                  //                 th_reason: th_reason,
                  //                 tuihuoType: '我要退款（无需退货）'
                  //             });
                  //         },
                  //         fail: function (err) {
                  //             wx.showModal({
                  //                 title: '',
                  //                 showCancel: false,
                  //                 content: "网络异常！"
                  //             })
                  //         }
                  //     })
                  // }
              },
              fail: function () {
                  that.globalData.userInfo = null;
                  wx.hideLoading();
                //   wx.navigateTo({
                //       url: '../unauthorized/unauthorized',
                //   })
                 
              }
          })
      }
  },
  globalData: {
    userInfo: null
  }
})