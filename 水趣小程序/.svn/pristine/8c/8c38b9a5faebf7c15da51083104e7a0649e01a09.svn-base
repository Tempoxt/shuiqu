// pages/myOrder/myOrder.js
var app = getApp()
Page({
  data: {
    userInfo: {},
    winWidth: 0,
    winHeight: 0,
    // tab切换 
    currentTab: 0,
    pageIndex: 1
  },
  // 确认收货
  confirmArrived: function (e) {
    var that = this;
    var orderId = e.currentTarget.dataset.orderid;
    console.log(e.currentTarget.dataset.orderid);
    wx.showModal({
      content: '是否确认收货?',
      success: function (res) {
        if (res.confirm) {
          wx.request({
            url: app.globalData.domain + "/wxLiteapp/reapOrder",
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
              var code = res.data.code;
              if (code == 'E00000') {
                wx.showModal({
                  content: '确认成功',
                  showCancel: false,
                  success: function () {
                    that.toggleTab();
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
                title: '',
                showCancel: false,
                content: "网络异常！"
              })
            }
          })

        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })

  },
  /**取消订单**/
  cancelOrder: function (e) {
    var orderId = e.currentTarget.dataset.orderid;
    wx.navigateTo({
      url: '../cancelOrder/cancelOrder?orderId=' + orderId,
    })
  },
  /**继续支付 */
  continuePay: function (e) {
    var that = this;
    var orderId = e.currentTarget.dataset.orderid;
    var group = e.currentTarget.dataset.ordergroup;
    console.log(e.currentTarget.dataset.ordergroup);
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
              wx.redirectTo({
                url: '../myOrder/myOrder?currentTab=1',
              })
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
  /**上拉加载 */
  loadMore: function () {
    var that = this;
    var evaluate = this.data.evaluate;
    var pageIndex = this.data.pageIndex + 1;
    console.log('上拉加载');
    var currentTab = that.data.currentTab;
    console.log(currentTab);
    wx.showLoading({
      title: '加载更多',
    })
    setTimeout(function () {
      if (currentTab == 0) {
        wx.request({
          url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
          method: 'POST',
          header: {
            'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
            cacheKey: app.globalData.cacheKey,
            c: app.globalData.c,
            pageIndex: pageIndex
          },
          success: function (res) {
            var code = res.data.code;
            console.log(res);
            if (code == "E00000") {
              wx.hideLoading();
              if (res.data.data.orderMaps.length > 0) {

                var all = that.data.all;
                for (var i = 0; i < res.data.data.orderMaps.length; i++) {
                  all.orderMaps.push(res.data.data.orderMaps[i]);
                }
                that.setData({
                  all: all,
                  pageIndex: pageIndex
                });

              } else {
                wx.hideLoading();
                wx.showToast({
                  title: '没有更多了',
                  image: '../../images/error_popover.png'
                })
              }
            } else if (code == -3) {
              app.login(function () {
                wx.request({
                  url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                  method: 'POST',
                  header: {
                    'content-type': 'application/x-www-form-urlencoded'
                  },
                  data: {
                    cacheKey: app.globalData.cacheKey,
                    c: app.globalData.c,
                    pageIndex: pageIndex
                  },
                  success: function (res) {
                    var code = res.data.code;
                    console.log(res);
                    if (code == "E00000") {
                      wx.hideLoading();
                      if (res.data.data.orderMaps.length > 0) {

                        var all = that.data.all;
                        for (var i = 0; i < res.data.data.orderMaps.length; i++) {
                          all.orderMaps.push(res.data.data.orderMaps[i]);
                        }
                        that.setData({
                          all: all,
                          pageIndex: pageIndex
                        });

                      } else {
                        wx.hideLoading();
                        wx.showToast({
                          title: '没有更多了',
                          image: '../../images/error_popover.png'
                        })
                      }
                    } else {
                      wx.hideLoading();
                      console.log(res.data.message);
                      wx.showModal({
                        title: res.data.message,
                        showCancel: false
                      })
                    }
                  },
                  fail: function (err) {
                    wx.hideLoading();
                    console.log(err);
                    wx.showModal({
                      title: err,
                      showCancel: false
                    })
                  }
                })
              });
            }
          },
          fail: function (err) {
            wx.hideLoading();
            console.log(err);
            wx.showModal({
              title: err,
              showCancel: false
            })
          }
        })


      } else if (currentTab == 1) {
        wx.request({
          url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
          method: 'POST',
          header: {
            'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
            cacheKey: app.globalData.cacheKey,
            c: app.globalData.c,
            statusId: 100,
            pageIndex: pageIndex
          },
          success: function (res) {
            var code = res.data.code;
            if (code == "E00000") {
              console.log(res);
              wx.hideLoading();

              if (res.data.data.orderMaps.length > 0) {

                var daifukuan = that.data.daifukuan;
                for (var i = 0; i < res.data.data.orderMaps.length; i++) {
                  daifukuan.orderMaps.push(res.data.data.orderMaps[i]);
                }
                that.setData({
                  daifukuan: daifukuan,
                  pageIndex: pageIndex
                });

              } else {
                wx.hideLoading();
                wx.showToast({
                  title: '没有更多了',
                  image: '../../images/error_popover.png'
                })
              }
            } else if (code == -3) {
              app.login(function () {
                wx.request({
                  url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                  method: 'POST',
                  header: {
                    'content-type': 'application/x-www-form-urlencoded'
                  },
                  data: {
                    cacheKey: app.globalData.cacheKey,
                    c: app.globalData.c,
                    statusId: 100,
                    pageIndex: pageIndex
                  },
                  success: function (res) {
                    if (code == "E00000") {
                      console.log(res);
                      wx.hideLoading();

                      if (res.data.data.orderMaps.length > 0) {

                        var daifukuan = that.data.daifukuan;
                        for (var i = 0; i < res.data.data.orderMaps.length; i++) {
                          daifukuan.orderMaps.push(res.data.data.orderMaps[i]);
                        }
                        that.setData({
                          daifukuan: daifukuan,
                          pageIndex: pageIndex
                        });

                      } else {
                        wx.hideLoading();
                        wx.showToast({
                          title: '没有更多了',
                          image: '../../images/error_popover.png'
                        })
                      }
                    } else {
                      wx.hideLoading();
                      console.log(res.data.message);
                      wx.showModal({
                        title: res.data.message,
                        showCancel: false
                      })
                    }

                  },
                  fail: function (err) {
                    wx.hideLoading();
                    console.log(err);
                    wx.showModal({
                      title: err,
                      showCancel: false
                    })
                  }
                })
              })
            }

          },
          fail: function (err) {
            wx.hideLoading();
            console.log(err);
            wx.showModal({
              title: err,
              showCancel: false
            })
          }
        })
      } else if (currentTab == 2) {
        wx.request({
          url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
          method: 'POST',
          header: {
            'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
            cacheKey: app.globalData.cacheKey,
            c: app.globalData.c,
            statusId: 101,
            pageIndex: pageIndex
          },
          success: function (res) {
            var code = res.data.code;
            if (code == "E00000") {
              wx.hideLoading();
              if (res.data.data.orderMaps.length > 0) {

                var daifahuo = that.data.daifahuo;
                for (var i = 0; i < res.data.data.orderMaps.length; i++) {
                  daifahuo.orderMaps.push(res.data.data.orderMaps[i]);
                }
                that.setData({
                  daifahuo: daifahuo,
                  pageIndex: pageIndex
                });

              } else {
                wx.hideLoading();
                wx.showToast({
                  title: '没有更多了',
                  image: '../../images/error_popover.png'
                })
              }
            } else if (code == -3) {
              app.login(function () {
                wx.request({
                  url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                  method: 'POST',
                  header: {
                    'content-type': 'application/x-www-form-urlencoded'
                  },
                  data: {
                    cacheKey: app.globalData.cacheKey,
                    c: app.globalData.c,
                    statusId: 101,
                    pageIndex: pageIndex
                  },
                  success: function (res) {
                    var code = res.data.code;
                    if (code == "E00000") {
                      wx.hideLoading();
                      if (res.data.data.orderMaps.length > 0) {

                        var daifahuo = that.data.daifahuo;
                        for (var i = 0; i < res.data.data.orderMaps.length; i++) {
                          daifahuo.orderMaps.push(res.data.data.orderMaps[i]);
                        }
                        that.setData({
                          daifahuo: daifahuo,
                          pageIndex: pageIndex
                        });

                      } else {
                        wx.hideLoading();
                        wx.showToast({
                          title: '没有更多了',
                          image: '../../images/error_popover.png'
                        })
                      }
                    } else {
                      wx.hideLoading();
                      console.log(res.data.message);
                      wx.showModal({
                        title: res.data.message,
                        showCancel: false
                      })
                    }
                  },
                  fail: function (err) {
                    console.log(err);
                    wx.hideLoading();
                    wx.showModal({
                      title: err,
                      showCancel: false
                    })
                  }
                })
              });
            }
          },
          fail: function (err) {
            console.log(err);
            wx.hideLoading();
            wx.showModal({
              title: err,
              showCancel: false
            })
          }
        })
      } else if (currentTab == 3) {
        wx.request({
          url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
          method: 'POST',
          header: {
            'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
            cacheKey: app.globalData.cacheKey,
            c: app.globalData.c,
            statusId: 102,
            pageIndex: pageIndex
          },
          success: function (res) {
            var code = res.data.code;
            if (code == "E00000") {
              console.log(res);
              wx.hideLoading();

              if (res.data.data.orderMaps.length > 0) {
                var daishouhuo = that.data.daishouhuo;
                for (var i = 0; i < res.data.data.orderMaps.length; i++) {
                  daishouhuo.orderMaps.push(res.data.data.orderMaps[i]);
                }
                that.setData({
                  daishouhuo: daishouhuo,
                  pageIndex: pageIndex
                });

              } else {
                wx.hideLoading();
                wx.showToast({
                  title: '没有更多了',
                  image: '../../images/error_popover.png'
                })
              }
            } else if (code == -3) {
              app.login(function () {
                wx.request({
                  url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                  method: 'POST',
                  header: {
                    'content-type': 'application/x-www-form-urlencoded'
                  },
                  data: {
                    cacheKey: app.globalData.cacheKey,
                    c: app.globalData.c,
                    statusId: 102,
                    pageIndex: pageIndex
                  },
                  success: function (res) {
                    var code = res.data.code;
                    if (code == "E00000") {
                      console.log(res);
                      wx.hideLoading();

                      if (res.data.data.orderMaps.length > 0) {
                        var daishouhuo = that.data.daishouhuo;
                        for (var i = 0; i < res.data.data.orderMaps.length; i++) {
                          daishouhuo.orderMaps.push(res.data.data.orderMaps[i]);
                        }
                        that.setData({
                          daishouhuo: daishouhuo,
                          pageIndex: pageIndex
                        });

                      } else {
                        wx.hideLoading();
                        wx.showToast({
                          title: '没有更多了',
                          image: '../../images/error_popover.png'
                        })
                      }
                    } else {
                      wx.hideLoading();
                      console.log(res.data.message);
                      wx.showModal({
                        title: res.data.message,
                        showCancel: false
                      })
                    }
                  },
                  fail: function (err) {
                    wx.showModal({
                      title: err,
                      showCancel: false
                    })
                  }
                })
              });
            }
          },
          fail: function (err) {
            wx.showModal({
              title: err,
              showCancel: false
            })
          }
        })
      } else if (currentTab == 4) {
        wx.request({
          url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
          method: 'POST',
          header: {
            'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
            cacheKey: app.globalData.cacheKey,
            c: app.globalData.c,
            statusId: 103,
            pageIndex: pageIndex
          },
          success: function (res) {
            var code = res.data.code;
            console.log(res);
            wx.hideLoading();
            if (code == "E00000") {
              if (res.data.data.orderMaps.length > 0) {

                var daipingjia = that.data.daipingjia;
                for (var i = 0; i < res.data.data.orderMaps.length; i++) {
                  daipingjia.orderMaps.push(res.data.data.orderMaps[i]);
                }
                that.setData({
                  daipingjia: daipingjia,
                  pageIndex: pageIndex
                });

              } else {
                wx.hideLoading();
                wx.showToast({
                  title: '没有更多了',
                  image: '../../images/error_popover.png'
                })
              }
            } else if (code == -3) {
              app.login(function () {
                wx.request({
                  url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                  method: 'POST',
                  header: {
                    'content-type': 'application/x-www-form-urlencoded'
                  },
                  data: {
                    cacheKey: app.globalData.cacheKey,
                    c: app.globalData.c,
                    statusId: 103,
                    pageIndex: pageIndex
                  },
                  success: function (res) {
                    var code = res.data.code;
                    console.log(res);
                    if (code == "E00000") {
                      wx.hideLoading();

                      if (res.data.data.orderMaps.length > 0) {

                        var daipingjia = that.data.daipingjia;
                        for (var i = 0; i < res.data.data.orderMaps.length; i++) {
                          daipingjia.orderMaps.push(res.data.data.orderMaps[i]);
                        }
                        that.setData({
                          daipingjia: daipingjia,
                          pageIndex: pageIndex
                        });

                      } else {
                        wx.hideLoading();
                        wx.showToast({
                          title: '没有更多了',
                          image: '../../images/error_popover.png'
                        })
                      }
                    } else {
                      wx.hideLoading();
                      console.log(res.data.message);
                      wx.showModal({
                        title: res.data.message,
                        showCancel: false
                      })
                    }
                  },
                  fail: function (err) {
                    wx.hideLoading();
                    console.log(err);
                    wx.showModal({
                      title: err,
                      showCancel: false
                    })
                  }
                })
              });
            }
          },
          fail: function (err) {
            wx.hideLoading();
            console.log(err);
            wx.showModal({
              title: err,
              showCancel: false
            })
          }
        })
      }
    }, 1000);

  },
  /**评价 */
  evaluate: function (e) {
    var orderId = e.currentTarget.dataset.orderid;
    var shopId = e.currentTarget.dataset.shopid;
    console.log(orderId, shopId);
    wx.navigateTo({
      url: '../evaluate/evaluate?orderId=' + orderId + '&shopId=' + shopId,
    })
  },
  onLoad: function (options) {
    wx.showLoading({ title: '加载中...' });

    var that = this;
    //调用应用实例的方法获取全局数据  
    app.getUserInfo(function (userInfo) {
      wx.removeStorageSync('currentTab');
      wx.removeStorageSync('orderFlag');
      var currentTab = options.currentTab;
      console.log('currentTab=' + currentTab);
      wx.setStorageSync('currentTab', currentTab);
      var computed = 0;
      //更新数据  
      that.setData({
        userInfo: userInfo,
        currentTab: currentTab,
        pageIndex: 1,
        theme: app.globalData.theme
      })
      var timer = setInterval(function () {
        computed++;
        if (!app.globalData.cacheKey) {
          console.log(computed);
          if (computed == 10) {
            clearInterval(timer);
            wx.showModal({
              content: '网络超时',
              showCancel: false
            })
          }
        } else {
          clearInterval(timer);
          if (currentTab == 1) {
            wx.request({
              url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: app.globalData.cacheKey,
                c: app.globalData.c,
                statusId: 100,
                pageIndex: 1
              },
              success: function (res) {
                wx.hideLoading();
                var code = res.data.code;
                if (code == 'E00000') {
                  console.log('待付款');
                  console.log(res);

                  clearInterval(timer);
                  that.setData({
                    daifukuan: res.data.data,
                    currentTab: currentTab
                  });
                } else if (code == '-3') {
                  app.login(function () {
                    wx.request({
                      url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                      method: 'POST',
                      header: {
                        'content-type': 'application/x-www-form-urlencoded'
                      },
                      data: {
                        cacheKey: app.globalData.cacheKey,
                        c: app.globalData.c,
                        statusId: 100,
                        pageIndex: 1
                      },
                      success: function (res) {
                        var code = res.data.code;
                        wx.hideLoading();
                        if (code == 'E00000') {
                          console.log('待付款');
                          console.log(res);

                          clearInterval(timer);
                          that.setData({
                            daifukuan: res.data.data,
                            currentTab: currentTab
                          });
                        } else {
                          wx.showModal({
                            showCancel: false,
                            content: res.data.message
                          })
                        }

                      },
                      fail: function (err) {
                        wx.showModal({
                          showCancel: false,
                          content: "网络异常！"
                        })
                      }
                    })
                  });

                }

              },
              fail: function (err) {
                wx.showModal({
                  title: '',
                  showCancel: false,
                  content: "网络异常！"
                })
              }
            })
          } else {

            wx.request({
              url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: app.globalData.cacheKey,
                c: app.globalData.c,
                pageIndex: 1
              },
              success: function (res) {
                var code = res.data.code;
                wx.hideLoading();
                if (code == "E00000") {
                  console.log(res);


                  that.setData({
                    all: res.data.data
                  });

                } else if (code == '-3') {
                  app.login(function () {
                    wx.request({
                      url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                      method: 'POST',
                      header: {
                        'content-type': 'application/x-www-form-urlencoded'
                      },
                      data: {
                        cacheKey: app.globalData.cacheKey,
                        c: app.globalData.c,
                        pageIndex: 1
                      },
                      success: function (res) {
                        var code = res.data.code;
                        wx.hideLoading();
                        if (code == "E00000") {
                          console.log(res);

                          clearInterval(timer);
                          that.setData({
                            all: res.data.data
                          });
                        } else {
                          clearInterval(timer);
                          wx.showModal({
                            showCancel: false,
                            content: res.data.message
                          })
                        }

                      },
                      fail: function (err) {
                        wx.showModal({
                          showCancel: false,
                          content: "网络异常！"
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
                  showCancel: false,
                  content: "网络异常！"
                })
              }
            })
          }
        }
      }, 500)

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
  onShow: function () {
    console.log('onShow');
    var that = this;
    var orderFlag = wx.getStorageSync('orderFlag');
    var evaluateDone = wx.getStorageSync('evaluateDone');
    if (orderFlag || evaluateDone) {
      wx.removeStorageSync('orderFlag');
      wx.removeStorageSync('evaluateDone');
      var currentTab = wx.getStorageSync('currentTab');
      that.setData({ currentTab: currentTab });
      that.toggleTab();

    }

  },
  onReady: function () {

  },
  // 订单详情
  orderDetail: function (e) {
    var orderId = e.currentTarget.dataset.orderid;
    console.log(e.currentTarget.dataset.orderid);
    wx.navigateTo({
      url: '../orderDetail/orderDetail?orderId=' + orderId
    })
  },
  /**滑动切换tab栏 */
  bindChange: function (e) {
    var that = this;
    var currentTab = e.detail.current;
    if (this.data.changeStyle == 'click') {
      this.setData({
        changeStyle: 'slide'
      });
      return;
    } else {
      console.log(e);
      console.log('滑动切换');

      that.setData({ currentTab: currentTab });
      that.toggleTab();
    }

  },
  toggleTab: function () {
    var that = this;
    wx.showLoading({
      title: '加载中...',
    })
    var currentTab = that.data.currentTab;
    wx.setStorageSync('currentTab', currentTab);
    this.setData({ pageIndex: 1 });
    if (currentTab == 0) {

      wx.request({
        url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          pageIndex: 1
        },
        success: function (res) {
          var code = res.data.code;
          if (code == 'E00000') {
            console.log(res);
            wx.hideLoading();
            that.setData({
              all: res.data.data
            });
          } else if (code == -3) {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  pageIndex: 1
                },
                success: function (res) {
                  var code = res.data.code;
                  if (code == 'E00000') {
                    console.log(res);
                    wx.hideLoading();
                    that.setData({
                      all: res.data.data
                    });
                  } else {
                    wx.hideLoading();
                    wx.showModal({
                      title: res.data.message,
                      showCancel: false
                    })
                  }

                },
                fail: function (err) {
                  wx.hideLoading();
                  console.log(err);
                  wx.showModal({
                    title: err,
                    showCancel: false
                  })
                }
              })
            });
          }

        },
        fail: function (err) {
          wx.hideLoading();
          console.log(err);
          wx.showModal({
            title: err,
            showCancel: false
          })
        }
      })


    } else if (currentTab == 1) {
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          statusId: 100,
          pageIndex: 1
        },
        success: function (res) {
          var code = res.data.code;
          if (code == 'E00000') {
            console.log(res);
            wx.hideLoading();
            that.setData({
              daifukuan: res.data.data
            });
          } else if (code == -3) {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  statusId: 100,
                  pageIndex: 1
                },
                success: function (res) {
                  var code = res.data.code;
                  if (code == 'E00000') {
                    console.log(res);
                    wx.hideLoading();
                    that.setData({
                      daifukuan: res.data.data
                    });
                  } else {
                    wx.hideLoading();
                    wx.showModal({
                      title: res.data.message,
                      showCancel: false
                    })
                  }

                },
                fail: function (err) {
                  wx.hideLoading();
                  console.log(err);
                  wx.showModal({
                    title: err,
                    showCancel: false
                  })
                }
              })
            });
          }

        },
        fail: function (err) {
          wx.hideLoading();
          console.log(err);
          wx.showModal({
            title: err,
            showCancel: false
          })
        }
      })
    } else if (currentTab == 2) {
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          statusId: 101,
          pageIndex: 1
        },
        success: function (res) {
          var code = res.data.code;
          if (code == 'E00000') {
            console.log(res);
            wx.hideLoading();
            that.setData({
              daifahuo: res.data.data
            });
          } else if (code == -3) {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  statusId: 101,
                  pageIndex: 1
                },
                success: function (res) {
                  var code = res.data.code;
                  if (code == 'E00000') {
                    console.log(res);
                    wx.hideLoading();
                    that.setData({
                      daifahuo: res.data.data
                    });
                  } else {
                    wx.hideLoading();
                    wx.showModal({
                      title: res.data.message,
                      showCancel: false
                    })
                  }

                },
                fail: function (err) {
                  wx.hideLoading();
                  console.log(err);
                  wx.showModal({
                    title: err,
                    showCancel: false
                  })
                }
              })
            });
          }

        },
        fail: function (err) {
          wx.hideLoading();
          console.log(err);
          wx.showModal({
            title: err,
            showCancel: false
          })
        }
      })
    } else if (currentTab == 3) {
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          statusId: 102,
          pageIndex: 1
        },
        success: function (res) {
          var code = res.data.code;
          if (code == 'E00000') {
            console.log(res);
            wx.hideLoading();
            that.setData({
              daishouhuo: res.data.data
            });
          } else if (code == -3) {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  statusId: 102,
                  pageIndex: 1
                },
                success: function (res) {
                  var code = res.data.code;
                  if (code == 'E00000') {
                    console.log(res);
                    wx.hideLoading();
                    that.setData({
                      daishouhuo: res.data.data
                    });
                  } else {
                    wx.hideLoading();
                    wx.showModal({
                      title: res.data.message,
                      showCancel: false
                    })
                  }

                },
                fail: function (err) {
                  wx.hideLoading();
                  console.log(err);
                  wx.showModal({
                    title: err,
                    showCancel: false
                  })
                }
              })
            });
          }

        },
        fail: function (err) {
          wx.hideLoading();
          console.log(err);
          wx.showModal({
            title: err,
            showCancel: false
          })
        }
      })
    } else if (currentTab == 4) {
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          statusId: 103,
          pageIndex: 1
        },
        success: function (res) {
          var code = res.data.code;
          if (code == 'E00000') {
            console.log(res);
            wx.hideLoading();
            that.setData({
              daipingjia: res.data.data
            });
          } else if (code == -3) {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/getStatusOrders",
                method: 'POST',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  statusId: 103,
                  pageIndex: 1
                },
                success: function (res) {
                  var code = res.data.code;
                  if (code == 'E00000') {
                    console.log(res);
                    wx.hideLoading();
                    that.setData({
                      daipingjia: res.data.data
                    });
                  } else {
                    wx.hideLoading();
                    wx.showModal({
                      title: res.data.message,
                      showCancel: false
                    })
                  }

                },
                fail: function (err) {
                  wx.hideLoading();
                  console.log(err);
                  wx.showModal({
                    title: err,
                    showCancel: false
                  })
                }
              })
            });
          }

        },
        fail: function (err) {
          wx.hideLoading();
          console.log(err);
          wx.showModal({
            title: err,
            showCancel: false
          })
        }
      })
    }
  },
  /**点击切换 */
  swichNav: function (e) {
    var that = this;
    var currentTab = e.target.dataset.current;
    this.setData({
      changeStyle: 'click'
    });
    console.log('点击切换');
    // wx.showLoading({ title: '加载中...' });
    var that = this;

    // 100:待付款,101:待发货,102:待收货,103:待评价，104退款/取消
    console.log(e.target.dataset);
    if (this.data.currentTab === currentTab) {
      return false;
    } else {
      that.setData({
        currentTab: currentTab
      })
    }
    that.toggleTab();

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
