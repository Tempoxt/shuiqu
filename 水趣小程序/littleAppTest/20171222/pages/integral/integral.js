// pages/integral/integral.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
      userInfo: {},
      array:['近三个月','近半年','近一年'],
      index:0,
      hasBind:-3,
      modalWrap: "",
      modal: "",
      getCodeText: "获取验证码",
      getCodeColor: "#EECE1F"
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
                url: app.globalData.domain + "/wxLiteapp/getIntegralRecord",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    month: 3
                },
                success: function (res) {
                    var code = res.data.code;
                    if (code == 'E00000') {
                        that.setData({
                            total: res.data.data.total,
                            surplus: res.data.data.total
                        });
                    } else {
                        wx.showModal({
                            content: res.data.message,
                            showCancel: false
                        })
                    }
                    
                }
            }) 
        }); 
        
        
  },
  /**
   * 筛选积分明细条件
   */
  bindPickerChange: function (e) {
      console.log('picker发送选择改变，携带值为', e.detail.value)
      this.data.hasBind == 1 ?
      this.setData({
          index: e.detail.value
      }) : this.openModal();
  },
  help:function(e){
    this.data.hasBind == 1 ?
    wx.navigateTo({
        url: '../help/help',
    }) : this.openModal();
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
      return {
          title: app.globalData.productName,
          desc: '最具人气的订水平台!',
          path: 'pages/index/index'
      }
  }
})