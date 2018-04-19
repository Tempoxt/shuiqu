// pages/myTicket/myTicket.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    winWidth: 0,
    winHeight: 0,
    // tab切换 
    currentTab: 0,
    hadBind: -3
  },
  /**
    * 生命周期函数--监听页面加载
    **/
  onLoad: function (options) {
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          winWidth: res.windowWidth,
          winHeight: res.windowHeight
        });
      }

    });
    //调用应用实例的方法获取全局数据  
    app.getUserInfo(function (userInfo) {
      //更新数据  
      var cacheKey = app.globalData.cacheKey;
      var hasBind = app.globalData.hasBind;

      that.setData({
        userInfo: userInfo,
        cacheKey: cacheKey,
        hasBind: hasBind,
        theme: app.globalData.theme
      });
      //   获取电子票余量
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/getValidTicketList",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c
        },
        success: function (res) {
          var code = res.data.code;
          if (code == 'E00000') {
            that.setData({
              surplus: res.data.data
            });
          } else {
            wx.hideLoading();
            wx.showModal({
              content: res.data.message,
              showCancel: false
            })
          }

        },
        fail: function (err) {
          wx.showModal({
            content: err,
            showCancel: false
          })
        }
      })
      //   获取电子票消费记录列表
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/getConsumeTicketList",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c
        },
        success: function (res) {
          var code = res.data.code;
          if (code == 'E00000') {
            that.setData({
              record: res.data.data
            });
          } else {
            wx.hideLoading();
            wx.showModal({
              content: res.data.message,
              showCancel: false
            })
          }

        },
        fail: function (err) {
          wx.showModal({
            content: err,
            showCancel: false
          })
        }
      })
    });

  },
  /**
   * 跳转到电子票详情页面
   */
  ticketDetail: function (e) {
    var eid = e.currentTarget.dataset.eid;
    var orderId = e.currentTarget.dataset.orderid;
    var ticketId = e.currentTarget.dataset.ticketid;
    var pname = e.currentTarget.dataset.pname;
    var sellDate = e.currentTarget.dataset.selldate;
    var sellNum = e.currentTarget.dataset.sellnum;
    var surplusNum = e.currentTarget.dataset.surplusnum;
    var beginDate = e.currentTarget.dataset.begindate;
    var endDate = e.currentTarget.dataset.enddate;
    wx.navigateTo({
      url: '../ticketDetail/ticketDetail?eid=' + eid + "&orderId=" + orderId + "&ticketId=" + ticketId + "&sellDate=" + sellDate + "&sellNum=" + sellNum + "&surplusNum=" + surplusNum + "&beginDate=" + beginDate + "&endDate=" + endDate + "&pname=" + pname
    })
  },
  //   滑动切换tab栏
  bindChange: function (e) {

    var that = this;
    that.setData({ currentTab: e.detail.current });

  },
  // 点击切换tab栏
  swichNav: function (e) {

    var that = this;
    console.log(e.target.dataset);
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      })
    }
  },
  //   使用电子票
  useIt: function (e) {
    console.log(e);
    var pid = e.currentTarget.dataset.convertpid;
    var shopId = e.currentTarget.dataset.shopid;
    var that = this;
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/useTicketToCart",
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      data: {
        cacheKey: app.globalData.cacheKey,
        c: app.globalData.c,
        pid: pid,
        shopId: shopId
      },
      success: function (res) {
        var code = res.data.code;
        if (code == 'E00000') {
          wx.navigateTo({
            url: '../shopCart/shopCart?shopId=' + shopId
          })
        } else {
          wx.showModal({
            content: res.data.message,
            showCancel: false
          })
        }

      },
      fail: function (err) {
        wx.showModal({
          content: err,
          showCancel: false
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    var that = this;

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