// pages/payResult/payResult.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
  
  },
  showDetail:function(){
      wx.redirectTo({
          url: '../myOrder/myOrder'
      });
      app.globalData.order = 'done';  
  },
  paymentAgain:function(){
      wx.redirectTo({
          url: '../myOrder/myOrder?currentTab=1'
      })
      app.globalData.order = 'done';
  },
  goHome:function(){
      app.globalData.order = 'done';
      wx.reLaunch({
          url: '../products/products'
    })
  },  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      app.globalData.order = 'done'
      var result = options.result;
      this.setData({
          result:result
      });
      
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