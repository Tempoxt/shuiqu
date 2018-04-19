// pages/address/address.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {}
  },
  //   选择地址
  selectAddress: function (e) {
    if (this.data.fromWhere == 'confirmCar') {

      var did = e.currentTarget.dataset.did;
      wx.setStorageSync('did', did)
      wx.redirectTo({
        url: '../confirmCar/confirmCar',
      })
    } else {
      return;
    }


  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var fromWhere = options.from;

    //   console.log(from);
    wx.showLoading({
      title: '加载中...'
    })

    //调用应用实例的方法获取全局数据  
    app.getUserInfo(function (userInfo) {
      //更新数据  
      that.setData({
        userInfo: userInfo,
        fromWhere: fromWhere,
        theme: app.globalData.theme
      })
    });
    //获取用户地址    
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/userAddress",
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
          //   如果只有一个地址则设为默认地址
          if (res.data.data.length == 1 && res.data.data[0].ifDefault == false) {

            wx.request({
              url: app.globalData.domain + "/wxLiteapp/setDefaultAddress",
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: app.globalData.cacheKey,
                c: app.globalData.c,
                did: res.data.data[0].did
              },
              success: function (res) {
                var code = res.data.code;
                if (code == 'E00000') {
                  //   重新渲染地址列表
                  wx.request({
                    url: app.globalData.domain + "/wxLiteapp/userAddress",
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
                        wx.setStorageSync('addressFlag', true);
                        that.setData({
                          address: res.data.data,

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
            });
          } else if (res.data.data.length == 0) {
            wx.removeStorageSync('did');
          }
          wx.hideLoading();
          that.setData({
            address: res.data.data,
            addLenth: res.data.data.length
          });
          //   登录令牌过期
        } else if (code == '-3') {
          wx.request({
            url: app.globalData.domain + "/wxLiteapp/userAddress",
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
                //   如果只有一个地址则设为默认地址
                if (res.data.data.length == 1 && res.data.data[0].ifDefault == false) {
                  wx.request({
                    url: app.globalData.domain + "/wxLiteapp/setDefaultAddress",
                    method: 'POST',
                    header: {
                      'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                      cacheKey: app.globalData.cacheKey,
                      c: app.globalData.c,
                      did: res.data.data[0].did
                    },
                    success: function (res) {
                      var code = res.data.code;
                      if (code == 'E00000') {
                        //   重新渲染地址列表
                        wx.request({
                          url: app.globalData.domain + "/wxLiteapp/userAddress",
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
                              wx.setStorageSync('addressFlag', true);
                              that.setData({
                                address: res.data.data,

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
                  });
                } else if (res.data.data.length == 0) {
                  wx.removeStorageSync('did');
                }
                wx.hideLoading();
                that.setData({
                  address: res.data.data,
                  addLenth: res.data.data.length
                });
              } else if (code == '-3') {

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
    });
  },
  //   设置默认地址
  setDefault: function (e) {
    var that = this;
    var ifDefault = e.currentTarget.dataset.ifdefault;
    var did = e.currentTarget.dataset.did;
    console.log(e.currentTarget.dataset.ifdefault);
    // 不是默认地址则设为默认地址，已经是默认地址则不处理
    if (!ifDefault) {
      wx.showModal({
        title: '',
        content: '您确定设置该地址为默认地址吗？',
        success: function (res) {
          if (res.confirm) {
            wx.request({
              url: app.globalData.domain + "/wxLiteapp/setDefaultAddress",
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: app.globalData.cacheKey,
                c: app.globalData.c,
                did: did
              },
              success: function (res) {
                var code = res.data.code;
                if (code == 'E00000') {
                  wx.request({
                    url: app.globalData.domain + "/wxLiteapp/userAddress",
                    method: 'POST',
                    header: {
                      'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                      cacheKey: app.globalData.cacheKey,
                      c: app.globalData.c
                    },
                    success: function (res) {
                      console.log(res.data.data);
                      wx.setStorageSync('addressFlag', true);
                      that.setData({
                        address: res.data.data,

                      });
                    }
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
                  content: res.data.message,
                  showCancel: false
                })
              }
            });
          } else if (res.cancel) {
            console.log('用户点击取消')
          }
        }
      })

    }

  },
  /**
   * 删除地址
   */
  deleteAddress: function (e) {
    var that = this;
    var did = e.currentTarget.dataset.did;
    wx.showModal({
      title: '',
      content: '您确定删除该地址吗？',
      success: function (res) {
        if (res.confirm) {
          wx.request({
            url: app.globalData.domain + "/wxLiteapp/delAddress",
            method: 'POST',
            header: {
              'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
              cacheKey: app.globalData.cacheKey,
              c: app.globalData.c,
              did: did
            },
            success: function (res) {
              var code = res.data.code;
              if (code == 'E00000') {
                wx.request({
                  url: app.globalData.domain + "/wxLiteapp/userAddress",
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
                      wx.setStorageSync('addressFlag', true);
                      if (res.data.data.length == 0) {
                        wx.removeStorageSync('did');
                      }
                      that.setData({
                        address: res.data.data,
                        addLenth: res.data.data.length
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
          });
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })
  },
  /**
   * 编辑地址
   */
  editAddress: function (e) {
    var that = this;
    var did = e.currentTarget.dataset.did;
    console.log(e);
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  //   添加地址，地址个数大于或等于6个时不允许再添加
  addAddress: function () {
    if (this.data.addLenth >= 6) {
      return;
    } else {
      wx.navigateTo({
        url: '../addAddress/addAddress',
      })
    }

  },
  //   重新获取用户地址
  reFresh: function () {
    var that = this;
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/userAddress",
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
        wx.setStorageSync('isRefeshAddress', 0)
        if (code == 'E00000') {
          wx.setStorageSync('addressFlag', true);
          if (res.data.data.length == 1 && res.data.data[0].ifDefault == false) {
            wx.request({
              url: app.globalData.domain + "/wxLiteapp/setDefaultAddress",
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: app.globalData.cacheKey,
                c: app.globalData.c,
                did: res.data.data[0].did
              },
              success: function (res) {
                var code = res.data.code;
                if (code == 'E00000') {
                  wx.request({
                    url: app.globalData.domain + "/wxLiteapp/userAddress",
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
                          address: res.data.data,

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
            });
          } else if (res.data.data.length == 0) {
            wx.removeStorageSync('did');
          }
          wx.hideLoading();
          that.setData({
            address: res.data.data,
            addLenth: res.data.data.length
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
    });

  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var isRefeshAddress = wx.getStorageSync('isRefeshAddress');
    if (isRefeshAddress == 1) {
      this.reFresh();
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
      title: app.globalData.title ,
      imageUrl: app.globalData.liteappShearPic || '../../images/shareImg.jpg',
      path: 'pages/index/index'
    }
  }
})