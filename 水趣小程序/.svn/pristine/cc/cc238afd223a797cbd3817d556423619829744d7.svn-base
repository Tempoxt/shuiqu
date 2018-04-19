// pages/myCoupons/myCoupons.js
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
    hadBind: -2
  },
  // 使用优惠券
  useCoupon: function (e) {
    // console.log(e.currentTarget.dataset);
    var classname = e.currentTarget.dataset.classname;
    var code = e.currentTarget.dataset.code;
    var typename = e.currentTarget.dataset.typename;
    var logourl = e.currentTarget.dataset.logourl;
    var begintime = e.currentTarget.dataset.begintime;
    var overtime = e.currentTarget.dataset.overtime;
    var tel = e.currentTarget.dataset.tel;
    // console.log(logourl);
    wx.redirectTo({
      url: '../couponShops/couponShops?code_id=' + code
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    wx.showLoading({
      title: '加载中...',
    })
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
      // 获取我的优惠券
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/getMyVouchers",
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
            wx.hideLoading();
            console.log(res.data.data.vouchers);
            var validCoupons = [];
            var invalidCoupons = [];
            for (var i = 0; i < res.data.data.vouchers.length; i++) {
              //如果优惠券对应城市和注册所属城市一致
              if (res.data.data.vouchers[i].cityId == app.globalData.cityId) {
                res.data.data.vouchers[i].convert_money = res.data.data.vouchers[i].convert_money.toFixed(2)
                if (!res.data.data.vouchers[i].ifUse && !res.data.data.vouchers[i].ifovertime) {

                  validCoupons.push(res.data.data.vouchers[i]);
                } else {
                  invalidCoupons.push(res.data.data.vouchers[i]);
                }
              }

            }

            that.setData({
              validCoupons: validCoupons,
              invalidCoupons: invalidCoupons
            });
          } else if (code == '-3') {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/getMyVouchers",
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
                    wx.hideLoading();
                    console.log(res.data.data.vouchers);
                    var validCoupons = [];
                    var invalidCoupons = [];
                    for (var i = 0; i < res.data.data.vouchers.length; i++) {
                      if (!res.data.data.vouchers[i].ifUse && !res.data.data.vouchers[i].ifovertime) {
                        validCoupons.push(res.data.data.vouchers[i]);
                      } else {
                        invalidCoupons.push(res.data.data.vouchers[i]);
                      }
                    }

                    that.setData({
                      validCoupons: validCoupons,
                      invalidCoupons: invalidCoupons
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
                  wx.hideLoading();
                  wx.showModal({
                    content: err,
                    showCancel: false
                  })
                }
              })
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
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          winWidth: res.windowWidth,
          winHeight: res.windowHeight
        });
      }

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
  // 滑动切换tab栏
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
  // useIt: function (e) {
  //     console.log(e);
  //     var pid = e.currentTarget.dataset.convertpid;
  //     var shopId = e.currentTarget.dataset.shopid;
  //     var that = this;
  //     wx.request({
  //         url: app.globalData.domain + "/wxLiteapp/useTicketToCart",
  //         method: 'POST',
  //         header: {
  //             'content-type': 'application/x-www-form-urlencoded'
  //         },
  //         data: {
  //             cacheKey: app.globalData.cacheKey,
  //             c: app.globalData.c,
  //             pid: pid,
  //             shopId: shopId
  //         },
  //         success: function (res) {
  //             var code = res.data.code;
  //             if (code == 'E00000') {
  //                 wx.navigateTo({
  //                     url: '../shopCart/shopCart?shopId=' + shopId
  //                 })
  //             } else {
  //                 wx.showModal({
  //                     content: res.data.message,
  //                     showCancel: false
  //                 })
  //             }

  //         },
  //         fail: function (err) {
  //             wx.showModal({
  //                 content: err,
  //                 showCancel: false
  //             })
  //         }
  //     })
  // },
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