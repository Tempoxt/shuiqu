// pages/purchaseDetail/purchaseDetail.js
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
      var orderId = options.orderId;
    //   退款退货详情
      wx.request({
          url: app.globalData.domain + "/wxLiteapp/orderTHDetail",
          method: 'POST',
          header: {
              'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
              cacheKey: app.globalData.cacheKey,
              c: app.globalData.c,
              orderId: orderId
          },
          success: function (res) {
              console.log(res.data.data);
              wx.hideLoading();
              that.setData({
                  tuihuo: res.data.data,
                  showDetail:true
              });
          },
          fail: function (err) {
              wx.showModal({
                  title: '',
                  showCancel: false,
                  content: "网络异常！"
              })
          }
      })
  },
//   是否显示进度详情
  changeShow:function(){
    var show = this.data.showDetail;
    show = !show;
    this.setData({showDetail:show});
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
  
  }
})