// pages/cancelOrder/cancelOrder.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
      otherReason:''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var that = this;
      var orderId = options.orderId;
      wx.setStorageSync('orderFlag', true);
      this.setData({
          theme:app.globalData.theme
      });
      wx.request({
          url: app.globalData.domain + "/wxLiteapp/orderCancelReason",
          method: 'POST',
          header: {
              'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
              cacheKey: app.globalData.cachekey,
              c: app.globalData.c
          },
          success: function (res) {
              var code = res.data.code;
              if (code == 'E00000') {
                  var result = res.data.data.reasons;
                  var reasons = [];
                  for (var i = 0; i < result.length;i++){
                      reasons.push({'reason':result[i],'selected':false}); 
                  }
                  that.setData({
                      reasons:reasons,
                      orderId:orderId
                  });  
                  console.log(reasons);
              } else if (code == '-3') {
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
  },
//   选择取消原因
  selectReason:function(e){
      var index = e.currentTarget.dataset.index;

      var reasons = this.data.reasons;  
      reasons.forEach(function(item){
          item.selected = false;
      });
      reasons[index].selected = true;
      this.setData({
          reasons:reasons
      });
    //   console.log(reason);
  },
//   输入其他原因
  inputReason:function(e){
      
      var otherReason = e.detail.value;
      console.log(otherReason);
      this.setData({
          otherReason: otherReason
      });
  },
//   确认取消
  confirmCancel:function(){
    var that = this;
    var reasons = this.data.reasons;
    var reason = '';
    var otherReason = this.data.otherReason;
    reasons.forEach(function(item){
        if(item.selected==true){
            reason = item.reason;    
        }
    });
    if (reason || otherReason){
        console.log(reason+','+otherReason);
        wx.showModal({
            content: '是否确认取消当前订单?',
            success: function (res) {
                if (res.confirm) {
                    wx.request({
                        url: app.globalData.domain + "/wxLiteapp/cancelOrder",
                        method: 'POST',
                        header: {
                            'content-type': 'application/x-www-form-urlencoded'
                        },
                        data: {
                            cacheKey: app.globalData.cacheKey,
                            c: app.globalData.c,
                            orderId: that.data.orderId,
                            reason: reason + ',' + otherReason
                        },
                        success: function (res) {
                            var code = res.data.code;
                            if (code == 'E00000') {
                                
                                wx.navigateBack({
                                    delta: 1
                                })

                            } else if (code == '-3') {
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
       
    }else{
        wx.showToast({
            title: '请选择原因',
            image: '../../images/error_popover.png'
        })
    }
    
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