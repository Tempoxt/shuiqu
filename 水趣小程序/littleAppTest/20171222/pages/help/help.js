// pages/help/help.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
  
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var that = this;
      //调用应用实例的方法获取全局数据  
      app.getUserInfo(function (userInfo) {
          //更新数据  
          that.setData({
              userInfo: userInfo
          })
      });
      var cacheKey = wx.getStorageSync('cacheKey');
      var hasBind = wx.getStorageSync('hasBind');
      this.setData({
          cacheKey: cacheKey,
          hasBind: hasBind
      });
      wx.request({
          url: app.globalData.domain + "/wxLiteapp/getIntegralRule",
          method: 'POST',
          header: {
              'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
              cacheKey: wx.getStorageSync('cacheKey'),
              c: app.globalData.c
          },
          success: function (result) {
              var code = result.data.code;
              if (code == 'E00000') {
                  that.setData({
                      rule: result.data.data.rule
                  });
              }else{
                  wx.showModal({
                      content: res.data.message,
                      showCancel: false
                  })
              }
            
          },
          fail:function(err){
              wx.hideLoading();
              wx.showModal({
                  showCancel: false,
                  content: err
              })
          }
      })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  
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