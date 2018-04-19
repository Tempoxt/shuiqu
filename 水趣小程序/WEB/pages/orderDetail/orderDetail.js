// pages/orderDetail/orderDetail.js
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
  //   滑动切换tab栏
  bindChange: function (e) {

    var that = this;
    that.setData({ currentTab: e.detail.current });

  },
  //   点击切换tab栏
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
  //   联系商家
  callSeller: function (e) {
    var tel = e.currentTarget.dataset.tel;
    //   console.log(e.currentTarget.dataset.tel);
    wx.makePhoneCall({
      phoneNumber: tel
    })
  },
  /*取消订单 */
  cancelOrder: function (e) {
    var orderId = e.currentTarget.dataset.orderid;
    console.log(orderId);
    wx.navigateTo({
      url: '../cancelOrder/cancelOrder?orderId=' + orderId,
    })
  },
  /*我要退货 */
  returnedPurchase: function (e) {
    var that = this;
    var orderId = e.currentTarget.dataset.orderid;
    console.log(orderId);
    wx.navigateTo({
      url: '../tuihuo/tuihuo?orderId=' + orderId,
    });
  },
  /*查看退货 */
  showPurchase: function (e) {
    var that = this;
    var orderId = e.currentTarget.dataset.orderid;
    wx.navigateTo({
      url: '../purchaseDetail/purchaseDetail?orderId=' + orderId,
    });
  },
  /*继续支付*/
  continuePay: function (e) {
    var that = this;
    var orderId = e.currentTarget.dataset.orderid;
    var group = e.currentTarget.dataset.ordergroup;
    console.log(orderId, group);
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/payGroup",
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      data: {
        cacheKey: app.globalData.cacheKey,
        c: app.globalData.c,
        group: group
      },
      success: function (res) {

        var code = res.data.code;
        if (code == 'E00000') {

          wx.requestPayment({
            'timeStamp': res.data.timeStamp,
            'nonceStr': res.data.nonceStr,
            'package': res.data.package,
            'signType': 'MD5',
            'paySign': res.data.paySign,
            'success': function (res) {
              console.log('成功' + res);
              wx.setStorageSync('orderFlag', true)
              that.orderDetailData(orderId);
            },
            'fail': function (err) {
              console.log('失败' + JSON.stringify(err));
            }
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
          showCancel: false,
          content: err
        })
      }
    });
  },
  /*订单详情*/
  orderDetailData: function (orderId) {
    var that = this;
    /**
    * 物流信息
    */
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/getOrderLog",
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      data: {
        cacheKey: app.globalData.cacheKey,
        c: app.globalData.c,
        orderId: orderId
      },
      success: function (result) {
        console.log(result.data.data);
        wx.hideLoading();
        that.setData({
          orderLog: result.data.data
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
    /***
     * 获取订单详情
     */
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/getOrderDetail",
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      data: {
        cacheKey: app.globalData.cacheKey,
        c: app.globalData.c,
        orderId: orderId
      },
      success: function (result) {
        var totalCash = 0;
        var totalVoucherMoney = 0;
        var eticket = 0;
        var ticket = 0;
        var totalNum = 0;
        // console.log(result.data.data);
        var orderDetail = result.data.data;
        orderDetail.details.forEach(function (item) {
          totalCash += item.total;
          totalVoucherMoney = item.voucherMoney;
          totalNum += item.number;
          if (item.settlementType == '电子票') {
            eticket += item.number;
          } else if (item.settlementType == '水票') {
            ticket += item.number;
          }

        });
        console.log(totalCash);
        that.setData({
          orderDetail: orderDetail,
          totalCash: totalCash,
          totalVoucherMoney: totalVoucherMoney,
          ticket: ticket,
          eticket: eticket,
          totalNum: totalNum
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
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var orderId = options.orderId;
    wx.setStorageSync('orderId', orderId)
    // var orderId = 5519;
    //调用应用实例的方法获取全局数据  
    app.getUserInfo(function (userInfo) {
      //更新数据  
      that.setData({
        userInfo: userInfo,
        theme: app.globalData.theme
      })
      that.orderDetailData(orderId);
    });
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          winWidth: res.windowWidth,
          winHeight: res.windowHeight
        });
      }

    });
    wx.showLoading({
      title: '加载中...',
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
    console.log('onShow');
    var that = this;
    var orderFlag = wx.getStorageSync('orderFlag');
    if (orderFlag) {
      //   wx.removeStorageSync('orderFlag');
      var orderId = wx.getStorageSync('orderId')
      that.orderDetailData(orderId);
    }
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