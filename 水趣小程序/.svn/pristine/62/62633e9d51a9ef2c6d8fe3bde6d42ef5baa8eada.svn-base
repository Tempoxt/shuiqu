// pages/couponProduct/couponProduct.js
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
      var shopId = options.shopId;
      var code_id = options.code_id;
      var shopName = options.shopName;
      this.setData({
          theme:app.globalData.theme
      });
      wx.showLoading({
          title: '加载中...',
      })
      wx.setNavigationBarTitle({
          title: shopName,
      })
    //   获取可使用优惠券商品
      wx.request({
          url: app.globalData.domain + '/wxLiteapp/voucherToGoods',
          method: 'POST',
          header: {
              'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
              cacheKey: app.globalData.cacheKey,
              c: app.globalData.c,
              shopId: shopId,
              code_id: code_id
          },
          success: function (res) {
              var code = res.data.code;
              if (code == 'E00000') {
                  that.setData({
                      products:res.data.data.products,
                      shopId: shopId
                  });
                  wx.hideLoading();
                  console.log(res.data.data);
              } else if (code == '-3') {
                  app.login(function () {
                      wx.request({
                          url: app.globalData.domain + '/wxLiteapp/voucherToGoods',
                          method: 'POST',
                          header: {
                              'content-type': 'application/x-www-form-urlencoded'
                          },
                          data: {
                              cacheKey: app.globalData.cacheKey,
                              c: app.globalData.c,
                              shopId: shopId,
                              code_id: code_id
                          },
                          success: function (res) {
                              var code = res.data.code;
                              if (code == 'E00000') {
                                //   var shops = res.data.data.shops;
                                //   that.setData({
                                //       shops: shops
                                //   });
                                  wx.hideLoading();
                                  console.log(res.data.data);
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
                  });
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
  useIt:function(e){
      wx.showLoading({
          title: '加载中...',
      })
      console.log(e.currentTarget.dataset.pid);
      var shopId = this.data.shopId;
      var pid = e.currentTarget.dataset.pid;
      var products = [{"shopId":shopId,"pid":pid,number:1}];
      wx.request({
          url: app.globalData.domain + "/wxLiteapp/editShopCart",
          method: 'POST',
          header: {
              'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
              cacheKey: app.globalData.cacheKey,
              c: app.globalData.c,
              products: JSON.stringify(products)
          },
          success: function (res) {
              var code = res.data.code;
              if (code == 'E00000') {
                  wx.hideLoading();
                  wx.navigateTo({
                      url: '../shopCart/shopCart?shopId='+shopId,
                  })
              } else if (code == '-3') {
                  app.login(function () {
                      wx.request({
                          url: app.globalData.domain + "/wxLiteapp/editShopCart",
                          method: 'POST',
                          header: {
                              'content-type': 'application/x-www-form-urlencoded'
                          },
                          data: {
                              cacheKey: app.globalData.cacheKey,
                              c: app.globalData.c,
                              products: JSON.stringify(products)
                          },
                          success: function (res) {
                              var code = res.data.code;
                              if (code == 'E00000') {
                                  wx.hideLoading();
                                  wx.navigateTo({
                                      url: '../shopCart/shopCart?shopId=' + shopId,
                                  })
                              } else {
                                  wx.showModal({
                                      content: res.data.message,
                                      showCancel: false
                                  })
                              }

                          },
                          fail: function (err) {
                              wx.hideLoading();
                              wx.showModal({
                                  showCancel: false,
                                  content: err
                              })
                          }
                      });
                  });
              }

          },
          fail: function (err) {
              wx.hideLoading();
              wx.showModal({
                  showCancel: false,
                  content: err
              })
          }
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
  
  }
})