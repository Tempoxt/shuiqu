// pages/ticketDetail/ticketDetail.js
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
    console.log(options);
    var that = this;

    //调用应用实例的方法获取全局数据  
    app.getUserInfo(function (userInfo) {
      //更新数据
      var cacheKey = app.globalData.cacheKey,
        hasBind = app.globalData.hasBind,
        eid = options.eid,
        orderId = options.orderId,
        ticketId = options.ticketId,
        pname = options.pname,
        sellDate = options.sellDate,
        sellNum = options.sellNum,
        surplusNum = options.surplusNum,
        beginDate = options.beginDate,
        endDate = options.endDate;

      that.setData({
        userInfo: userInfo,
        cacheKey: cacheKey,
        hasBind: hasBind,
        pname: pname,
        sellDate: sellDate,
        sellNum: sellNum,
        surplusNum: surplusNum,
        beginDate: beginDate,
        endDate: endDate
      });
      //  电子票消费详情
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/getConsumeTicketDetail",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          eid: eid,
          orderId: orderId,
          ticketId: ticketId
        },
        success: function (result) {

          that.setData({
            result: result.data.data
          });
          console.log(result);
        }
      });
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