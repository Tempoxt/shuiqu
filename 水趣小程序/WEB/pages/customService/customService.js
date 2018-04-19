// pages/customService/customService.js
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
      wx.showLoading({
          title: '加载中...;',
      });
      app.getUserInfo(function (userInfo) {
          //计时器
          var computed = 0;
          var timer = setInterval(function () {
              computed++;
              if (!app.globalData.c) {
                  //网络请求超过20秒提示超时
                  if (computed == 40) {
                      console.log(computed);
                      clearInterval(timer);
                      wx.hideLoading();
                      wx.showModal({
                          content: '网络超时',
                          showCancel: false,
                          success: function (res) {
                              app.globalData.status = true;
                              if (res.confirm) {
                                  wx.navigateBack({
                                      delta: -1
                                  })
                              }
                          }
                      })
                  }
              } else {
                    wx.hideLoading();
                    clearInterval(timer);
                    wx.request({
                        url: app.globalData.domain + '/wxLiteapp/getSysFaq',
                        method: 'POST',
                        header: {
                            'content-type': 'application/x-www-form-urlencoded'
                        },
                        data: {
                            c: app.globalData.c
                        },
                        success: function (res) {
                            console.log(res.data.data.faqs);
                            var code = res.data.code;
                            if (code == 'E00000') {
                                that.setData({
                                    faqs: res.data.data.faqs
                                });
                            } else if (code == '-3') {

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
                  
              }
          }, 500);
      });  
  },
//   常见问题
  faq:function(e){
      var answer = e.currentTarget.dataset.a;
      var question = e.currentTarget.dataset.q;
      wx.navigateTo({
          url: '../faq/faq?answer=' + answer + '&question=' + question
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