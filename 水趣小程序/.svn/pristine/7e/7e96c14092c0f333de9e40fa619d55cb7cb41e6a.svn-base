// pages/bind/bind.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
      userInfo: {},
      phoneNum:'',
      getCodeColor:"#ddd",
      buttonColor: "#ddd",
      getCodeStatus:true,
      bindStatus:true,
      second:'60'
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var that = this;
      //调用应用实例的方法获取全局数据  
      app.getUserInfo(function (userInfo) {
          //更新数据  
          that.setData({
              userInfo: userInfo
          })
      }); 
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
      var phoneNum = wx.getStorageSync('phoneNum');
      var that = this;
      function timeOver(){
          if (that.data.second == 0) {
              clearInterval(Countdown);
              that.setData({
                  second:"获取验证码",
                  getCodeColor: "#EECE1F",
                  getCodeStatus:false
              });
          }
      }
      this.setData({
          phoneNum:phoneNum
      });
      if (this.data.getCodeStatus){
        
        var Countdown = setInterval(function(){
            that.setData({
                second: that.data.second-1
            });
            timeOver();
        },1000)
      }
  }, 
  getCode:function(e){
    console.log(this.data.phoneNum);
    wx.showModal({
        title: '',
        content: '获取验证码',
        showCancel: false
    })
  },
  inputHandler:function(e){
    // console.log(e);
    if(e.detail.value!=""){
        this.setData({
            buttonColor:"#EECE1F",
            bindStatus:false
        });
    }else{
        this.setData({
            buttonColor: "#ddd",
            bindStatus: true
        });
    }
  },
  bindAccount:function(e){
      wx.showModal({
          title: '',
          content: '绑定成功',
          showCancel: false
      })
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