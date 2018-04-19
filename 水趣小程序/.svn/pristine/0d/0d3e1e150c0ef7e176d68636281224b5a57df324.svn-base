// pages/payResult/payResult.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  //   查看已支付订单
  showDetail: function () {
    wx.redirectTo({
      url: '../myOrder/myOrder?currentTab=0'
    });
    app.globalData.order = 'done';
  },
  //   查看未支付订单
  paymentAgain: function () {
    wx.redirectTo({
      url: '../myOrder/myOrder?currentTab=1'
    })
    app.globalData.order = 'done';
  },
  //   继续购买，回到商品列表页
  goHome: function () {
    app.globalData.order = 'done';
    wx.reLaunch({
      url: '../products/products'
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    app.globalData.order = 'done';
    app.globalData.status = true;
    console.log('订单结果');
    var result = options.result;
    this.setData({
      result: result,
      theme: app.globalData.theme
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
      title: app.globalData.title,
      imageUrl: app.globalData.liteappShearPic || '../../images/shareImg.jpg',
      path: 'pages/index/index'
    }
  }
})