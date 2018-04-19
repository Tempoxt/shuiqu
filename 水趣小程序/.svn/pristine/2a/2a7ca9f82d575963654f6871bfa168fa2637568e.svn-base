// pages/productDetail/productDetail.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    
  },
  /*加入购物车 */
  addCart:function(e){
    var that = this;
    var shopId = this.data.shopId;
    var pid = this.data.pid;
    var shopProducts = this.data.shopProducts;
    for (var i = 0; i < shopProducts.length;i++){
        for (var j = 0; j < shopProducts[i].products.length;j++){
            if (shopProducts[i].products[j].pid==pid){
                var num = shopProducts[i].products[j].number + 1;
                console.log(num);
                var shopCarData = [{ shopId: shopId, pid: pid, number: num, settleStyle: '现金' }];
                // 添加商品
                wx.request({
                    url: app.globalData.domain + "/wxLiteapp/editShopCart",
                    method: 'POST',
                    header: {
                        'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                        cacheKey: app.globalData.cacheKey,
                        c: app.globalData.c,
                        products: JSON.stringify(shopCarData)
                    },
                    success: function (res) {
                        var code = res.data.code;
                        if (code == 'E00000') {
                            // 查询水店商品
                            wx.request({
                                url: app.globalData.domain + '/wxLiteapp/getShopProduct_v2?random=' + Math.random(),
                                method: 'POST',
                                header: {
                                    'content-type': 'application/x-www-form-urlencoded'
                                },
                                data: {
                                    cacheKey: app.globalData.cacheKey,
                                    c: app.globalData.c,
                                    shopId: shopId
                                },
                                success: function (res) {
                                    console.log('getShopProduct_v2');
                                    console.log(res.data);

                                    var code = res.data.code;

                                    if (code == 'E00000') {
                                        wx.showToast({
                                            title: '操作成功',
                                        })
                                        //统计购物车商品数量
                                        var arr = [];
                                        var newArr = [];
                                        var totalCount = 0;
                                        var shopProducts = res.data.data.shopProducts;
                                        for (var i = 0; i < shopProducts.length; i++) {
                                            for (var j = 0; j < shopProducts[i].products.length; j++) {
                                                if (shopProducts[i].products[j].number) {
                                                    arr.push({ "pid": shopProducts[i].products[j].pid, "number": shopProducts[i].products[j].number });

                                                }
                                            }
                                        }
                                        for (var i = 0; i < arr.length; i++) {
                                            //假设第一个元素和后面的每一个都不相同
                                            var flag = false;
                                            for (var j = i + 1; j < arr.length; j++) {
                                                if (arr[i].pid == arr[j].pid) {
                                                    //有相同元素
                                                    flag = true;
                                                    break;
                                                }
                                            }
                                            //没有相同的元素
                                            if (flag == false) {
                                                newArr.push(arr[i]);
                                            }
                                        }
                                        // console.log(newArr);
                                        for (var i = 0; i < newArr.length; i++) {
                                            totalCount += newArr[i].number;
                                        }
                                        that.setData({
                                            shopProducts: shopProducts,
                                            totalCount: totalCount
                                        });

                                    } else if (code == '-3') {

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
                                        products: JSON.stringify(shopCarData)
                                    },
                                    success: function (res) {
                                        var code = res.data.code;
                                        if (code == 'E00000') {
                                            console.log(res);
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
                return;
            }
        }
    }
  },
  /*立即购买*/
  buyNow:function(e){
      wx.showLoading({
          title: '加载中...',
      })
      var that = this;
      var shopId = this.data.shopId;
      var pid = this.data.pid;
      var shopProducts = this.data.shopProducts;
      for (var i = 0; i < shopProducts.length; i++) {
          for (var j = 0; j < shopProducts[i].products.length; j++) {
              if (shopProducts[i].products[j].pid == pid) {
                  var num = shopProducts[i].products[j].number + 1;
                  console.log(num);
                  var shopCarData = [{ shopId: shopId, pid: pid, number: num, settleStyle: '现金' }];
                  wx.request({
                      url: app.globalData.domain + "/wxLiteapp/editShopCart",
                      method: 'POST',
                      header: {
                          'content-type': 'application/x-www-form-urlencoded'
                      },
                      data: {
                          cacheKey: app.globalData.cacheKey,
                          c: app.globalData.c,
                          products: JSON.stringify(shopCarData)
                      },
                      success: function (res) {
                          var code = res.data.code;
                          wx.hideLoading();
                          if (code == 'E00000') {
                              wx.redirectTo({
                                  url: '../shopCart/shopCart?shopId='+that.data.shopId,
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
                                          products: JSON.stringify(shopCarData)
                                      },
                                      success: function (res) {
                                          var code = res.data.code;
                                          if (code == 'E00000') {
                                              wx.redirectTo({
                                                  url: '../shopCart/shopCart?shopId='+that.data.shopId,
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
                  return;
              }
          }
      }
  },
  // 返回首页
  goHome: function () {
      wx.reLaunch({
          url: '../index/index',
      })
  },
  /*进店逛逛*/
  goShop:function(){
      wx.navigateBack({
          delta:1
      })
    //   wx.redirectTo({
    //       url: '../products/products?shopId='+this.data.shopId
    //   })
  },
  /*套餐详情*/
  goTaocan:function(e){
      var pid = e.currentTarget.dataset.pid;
      var shopId = this.data.shopId;
      wx.redirectTo({
          url: '../productDetail/productDetail?shopId='+shopId+'&pid='+pid
      })
    //   console.log(e.currentTarget.dataset.pid);
  },
  /*去押桶*/
  goYatong:function(e){
      var pid = e.currentTarget.dataset.pid;
      var shopId = this.data.shopId;
      wx.redirectTo({
          url: '../productDetail/productDetail?shopId=' + shopId + '&pid=' + pid
      })
  },
  /*查看商品评价*/
  productEvaluate:function(e){
      var pid = this.data.pid;
      wx.redirectTo({
          url: '../productEvaluate/productEvaluate?pid='+pid,
      })
  },
  //联系客服
  kefu:function(){
    wx.navigateTo({
        url: '../customService/customService'
    })
  }, 
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var that = this;
      var shopId = options.shopId;
      var pid = options.pid;
      app.getUserInfo(function (userInfo) {
          //更新数据  
          that.setData({
              userInfo: userInfo,
              shopId: shopId,
              pid: pid,
              theme:app.globalData.theme 
          })
          wx.showLoading({
              title: '加载中...',
          })
        //   that.setData({ });
          console.log(shopId, pid);
          wx.request({
              url: app.globalData.domain + "/wxLiteapp/getProductDetail",
              method: 'POST',
              header: {
                  'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  shopId: shopId,
                  pid: pid
              },
              success: function (res) {
                  var code = res.data.code;
                  if (code == 'E00000') {
                      console.log(res);
                      that.setData({ result: res.data.data });
                      wx.request({
                          url: app.globalData.domain + '/wxLiteapp/getShopProduct_v2?random=' + Math.random(),
                          method: 'POST',
                          header: {
                              'content-type': 'application/x-www-form-urlencoded'
                          },
                          data: {
                              cacheKey: app.globalData.cacheKey,
                              c: app.globalData.c,
                              shopId: shopId
                          },
                          success: function (res) {
                              console.log('getShopProduct_v2');
                              console.log(res.data);

                              var code = res.data.code;

                              if (code == 'E00000') {
                                  //统计购物车商品数量
                                  var arr = [];
                                  var newArr = [];
                                  var totalCount = 0;
                                  var shopProducts = res.data.data.shopProducts;
                                  for (var i = 0; i < shopProducts.length; i++) {
                                      for (var j = 0; j < shopProducts[i].products.length; j++) {
                                          if (shopProducts[i].products[j].number) {
                                              arr.push({ "pid": shopProducts[i].products[j].pid, "number": shopProducts[i].products[j].number });

                                          }
                                      }
                                  }
                                  for (var i = 0; i < arr.length; i++) {
                                      //假设第一个元素和后面的每一个都不相同
                                      var flag = false;
                                      for (var j = i + 1; j < arr.length; j++) {
                                          if (arr[i].pid == arr[j].pid) {
                                              //有相同元素
                                              flag = true;
                                              break;
                                          }
                                      }
                                      //没有相同的元素
                                      if (flag == false) {
                                          newArr.push(arr[i]);
                                      }
                                  }
                                  // console.log(newArr);
                                  for (var i = 0; i < newArr.length; i++) {
                                      totalCount += newArr[i].number;
                                  }
                                  that.setData({
                                      shopProducts: shopProducts,
                                      totalCount: totalCount
                                  });
                                  wx.hideLoading();

                              } else if (code == '-3') {

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
                  } else if (code == '-3') {

                  }

              },
              fail: function (err) {
                  wx.showModal({
                      showCancel: false,
                      content: err
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