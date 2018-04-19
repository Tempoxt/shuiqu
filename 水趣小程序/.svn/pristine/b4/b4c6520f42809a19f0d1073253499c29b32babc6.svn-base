// pages/address/address.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
      userInfo: {}
  },
  selectAddress:function(e){
      if (this.data.fromWhere=='confirmCar'){
          
          var did = e.currentTarget.dataset.did;
          wx.setStorageSync('did', did)
          wx.redirectTo({
              url: '../confirmCar/confirmCar',
          })
      }else{
          return;
      }
      
    
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var that = this;
      var  fromWhere= options.from;

    //   console.log(from);
      wx.showLoading({
          title: '加载中'
      })
      
      //调用应用实例的方法获取全局数据  
      app.getUserInfo(function (userInfo) {
          //更新数据  
          that.setData({
              userInfo: userInfo,
              fromWhere: fromWhere
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
      wx.request({
          url: app.globalData.domain + "/wxLiteapp/userAddress",
          method: 'POST',
          header: {
              'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
              cacheKey: wx.getStorageSync('cacheKey'),
              c: app.globalData.c
          },
          success: function (res) {
              var code = res.data.code;
              if (code == 'E00000') {
                  if (res.data.data.length == 1 && res.data.data[0].ifDefault == false) {
                      wx.request({
                          url: app.globalData.domain + "/wxLiteapp/setDefaultAddress",
                          method: 'POST',
                          header: {
                              'content-type': 'application/x-www-form-urlencoded'
                          },
                          data: {
                              cacheKey: wx.getStorageSync('cacheKey'),
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
                                          cacheKey: wx.getStorageSync('cacheKey'),
                                          c: app.globalData.c
                                      },
                                      success: function (res) {
                                          var code = res.data.code;
                                          if (code == 'E00000') {
                                              that.setData({
                                                  address: res.data.data,

                                              });
                                          }else{
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
                              }else{
                                  wx.showModal({
                                      content: res.data.message,
                                      showCancel: false
                                  })
                              }
                              

                          },
                          fail:function(err){
                              wx.showModal({
                                  content: err,
                                  showCancel: false
                              })
                          }
                      });
                  }
                  wx.hideLoading();
                  that.setData({
                      address: res.data.data,
                      addLenth: res.data.data.length
                  }); 
              }else{
                  wx.showModal({
                      content: res.data.message,
                      showCancel: false
                  })
              }
               
          },
          fail:function(err){
              wx.showModal({
                  content: err,
                  showCancel: false
              })
          }
      }); 
  },
  setDefault:function(e){
    var that = this;
    var ifDefault = e.currentTarget.dataset.ifdefault;
    var did = e.currentTarget.dataset.did;
    console.log(e.currentTarget.dataset.ifdefault);
    
    if (!ifDefault){
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
                            cacheKey: wx.getStorageSync('cacheKey'),
                            c: app.globalData.c,
                            did:did
                        },
                        success: function (res) {
                            var code = res.data.code;
                            if (code == 'E00000'){
                                wx.request({
                                    url: app.globalData.domain + "/wxLiteapp/userAddress",
                                    method: 'POST',
                                    header: {
                                        'content-type': 'application/x-www-form-urlencoded'
                                    },
                                    data: {
                                        cacheKey: wx.getStorageSync('cacheKey'),
                                        c: app.globalData.c
                                    },
                                    success: function (res) {
                                        console.log(res.data.data);
                                        // console.log('长度'+result.data.data.length);
                                        that.setData({
                                            address: res.data.data,

                                        });
                                    }
                                }); 
                            }else{
                                wx.showModal({
                                    content: res.data.message,
                                    showCancel: false
                                })
                            }
                            
                        },
                        fail:function(err){
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
  deleteAddress:function(e){
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
                        cacheKey: wx.getStorageSync('cacheKey'),
                        c: app.globalData.c,
                        did:did
                    },
                    success: function (res) {
                        var code = res.data.code;
                        if (code == 'E00000'){
                            wx.request({
                                url: app.globalData.domain + "/wxLiteapp/userAddress",
                                method: 'POST',
                                header: {
                                    'content-type': 'application/x-www-form-urlencoded'
                                },
                                data: {
                                    cacheKey: wx.getStorageSync('cacheKey'),
                                    c: app.globalData.c
                                },
                                success: function (res) {
                                    var code = res.data.code;
                                    if (code == 'E00000'){
                                        that.setData({
                                            address: res.data.data,
                                            addLenth: res.data.data.length
                                        });
                                    }else{
                                        wx.showModal({
                                            content: res.data.message,
                                            showCancel: false
                                        })
                                    }
                                    
                                },
                                fail:function(err){
                                    wx.showModal({
                                        content: err,
                                        showCancel: false
                                    })
                                }
                            });
                        }else{
                            wx.showModal({
                                content: res.data.message,
                                showCancel: false
                            })
                        }
                        
                    },
                    fail:function(err){
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
  editAddress:function(e){
      var that = this;
      var did = e.currentTarget.dataset.did;
      console.log(e);
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  
  },
  addAddress:function(){
      if (this.data.addLenth >=6){
            return;
      }else{
          wx.navigateTo({
              url: '../addAddress/addAddress',
          })
      }
      
  },
  reFresh:function(){
      var that = this;
      wx.request({
          url: app.globalData.domain + "/wxLiteapp/userAddress",
          method: 'POST',
          header: {
              'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
              cacheKey: wx.getStorageSync('cacheKey'),
              c: app.globalData.c
          },
          success: function (res) {
              var code = res.data.code;
              if (code == 'E00000') {
                  wx.hideLoading();
                  that.setData({
                      address: res.data.data,
                      addLenth: res.data.data.length
                  });
                  wx.setStorageSync('isRefeshAddress',0)
                //   if (res.data.data.length == 1){
                //       wx.request({
                //           url: app.globalData.domain + "/wxLiteapp/setDefaultAddress",
                //           method: 'POST',
                //           header: {
                //               'content-type': 'application/x-www-form-urlencoded'
                //           },
                //           data: {
                //               cacheKey: wx.getStorageSync('cacheKey'),
                //               c: app.globalData.c,
                //               did: res.data.data[0].did
                //           },
                //           success: function (res) {
                //               var code = res.data.code;
                //               if (code == 'E00000') {
                //                   wx.request({
                //                       url: app.globalData.domain + "/wxLiteapp/userAddress",
                //                       method: 'POST',
                //                       header: {
                //                           'content-type': 'application/x-www-form-urlencoded'
                //                       },
                //                       data: {
                //                           cacheKey: wx.getStorageSync('cacheKey'),
                //                           c: app.globalData.c
                //                       },
                //                       success: function (res) {
                //                           console.log(res.data.data);
                                          
                //                           that.setData({
                //                               address: res.data.data,

                //                           });
                //                       }
                //                   });
                //               } else {
                //                   wx.showModal({
                //                       content: res.data.message,
                //                       showCancel: false
                //                   })
                //               }

                //           },
                //           fail: function (err) {
                //               wx.showModal({
                //                   content: res.data.message,
                //                   showCancel: false
                //               })
                //           }
                //       });
                //   }
                  
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
      if (isRefeshAddress==1){
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
          title: app.globalData.productName,
          desc: '最具人气的订水平台!',
          path: 'pages/index/index'
      }
  }
})