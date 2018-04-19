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
      hadBind:-3
  },
  /**
  * 关闭注册窗口
  */
  closeModal: function () {
      this.setData({
          modalWrap: false,
          modal: false
      });
  },
  /**
   * 打开注册窗口
   */
  openModal: function () {
      this.setData({
          modalWrap: true,
          modal: true
      });
  },
  /**
   * 获取验证码
   */
  getCode: function (e) {
      var regPhone = /^1[3|4|5|7|8][0-9]{9}$/;
      //校验手机号失败
      if (!regPhone.test(this.data.phoneNum)) {
          wx.showModal({
              title: '',
              content: '手机号不正确',
              showCancel: false
          })
      } else {
          var that = this;
          function timeOver() {
              if (that.data.getCodeText == 0) {
                  clearInterval(Countdown);
                  that.setData({
                      getCodeText: "获取验证码",
                      getCodeColor: "#EECE1F",
                      getCodeStatus: false
                  });
              }
          }
          //获取手机验证码
          wx.request({
              url: app.globalData.domain + "/wxLiteapp/moblieMsg",
              method: 'POST',
              header: {
                  'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                  cacheKey: wx.getStorageSync('cacheKey'),
                  c: app.globalData.c,
                  cellphone: that.data.phoneNum
              },
              success: function (result) {

                  console.log(result);
              }
          })
          //验证码倒计时，改变状态，不可点击
          this.setData({
              getCodeColor: "#ddd",
              getCodeText: "60",
              getCodeStatus: true
          });
          var Countdown = setInterval(function () {
              that.setData({
                  getCodeText: that.data.getCodeText - 1
              });
              timeOver();
          }, 1000)
      }
  },
  /**
   * 输入手机号时
   */
  inputPhoneNum: function (e) {
      var that = this;
      this.setData({
          phoneNum: e.detail.value
      });
      console.log(this.data.phoneNum);
  },
  /**
   * 输入验证码时
   */
  inputValidateCode: function (e) {
      this.setData({
          validateCode: e.detail.value
      });
      if (this.data.phoneNum != "" && this.data.validateCode != "" && this.data.selectStatus) {
          this.setData({
              commitColor: "#EECE1F",
              bindStatus: false
          });
      } else {
          this.setData({
              commitColor: "#ddd",
              bindStatus: true
          });
      }
      console.log(this.data.validateCode);
  },
  /**
   * 切换注册窗口的单选框
   */
  toggleStatus: function () {
      this.setData({
          selectStatus: !this.data.selectStatus
      })
      if (this.data.phoneNum != "" && this.data.validateCode != "" && this.data.selectStatus) {
          this.setData({
              commitColor: "#EECE1F",
              bindStatus: false
          });
      } else {
          this.setData({
              commitColor: "#ddd",
              bindStatus: true
          });
      }
  },
  /**
   * 绑定手机号
   */
  commit: function (e) {
      var that = this;
      var regPhone = /^1[3|4|5|7|8][0-9]{9}$/;
      //校验手机号失败
      if (!regPhone.test(this.data.phoneNum)) {
          wx.showModal({
              title: '',
              content: '手机号不正确',
              showCancel: false
          })
      } else {
          var that = this;
          wx.request({
              url: app.globalData.domain + "/wxLiteapp/saveBind",
              method: 'POST',
              header: {
                  'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                  cacheKey: wx.getStorageSync('cacheKey'),
                  c: app.globalData.c,
                  cellphone: that.data.phoneNum,
                  code: that.data.validateCode
              },
              success: function (result) {
                  if (result.data.code != "E00000") {
                      wx.showModal({
                          title: '',
                          showCancel: false,
                          content: result.data.message
                      })
                  } else {
                      wx.showModal({
                          title: '',
                          showCancel: false,
                          content: "注册成功"
                      })
                      that.setData({
                          hasBind: 1
                      });
                      wx.setStorageSync('hasBind', 1);
                      that.closeModal();
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

      }

  },  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var that = this;
      //调用应用实例的方法获取全局数据  
      app.getUserInfo(function (userInfo) {
          //更新数据  
          var cacheKey = wx.getStorageSync('cacheKey');
          var hasBind = wx.getStorageSync('hasBind');
          that.setData({
              userInfo: userInfo,
              cacheKey: cacheKey,
              hasBind: hasBind
          });
          wx.request({
              url: app.globalData.domain + "/wxLiteapp/getValidTicketList",
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
                          surplus: res.data.data
                      });
                  } else {
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
          })
          wx.request({
              url: app.globalData.domain + "/wxLiteapp/getConsumeTicketList",
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
                          record: res.data.data
                      });
                  } else {
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
  ticketDetail:function(e){
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
  bindChange: function (e) {

      var that = this;
      that.setData({ currentTab: e.detail.current });
     
  },

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
  useIt:function(e){
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
              cacheKey: wx.getStorageSync('cacheKey'),
              c: app.globalData.c,
              pid:pid,
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
          fail:function(err){
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
          title: app.globalData.productName,
          desc: '最具人气的订水平台!',
          path: 'pages/index/index'
      }
  }
})