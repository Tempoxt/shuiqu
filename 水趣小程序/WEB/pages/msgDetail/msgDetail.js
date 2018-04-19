// pages/msgDetail/msgDetail.js
const app =getApp();
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
    //   获取文本消息通知详情
      wx.request({
          url: app.globalData.domain + "/wxLiteapp/getNotify",
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
              var code = res.data.code;
              // console.log(code);
              var textMsg = res.data.data;
              for(var i=0;i<textMsg.length;i++){
                  var time = new Date(textMsg[i].createTime);
                  var year = time.getFullYear();
                  var month = time.getMonth()+1;
                  var day   = time.getDate();
                  console.log(year+'年'+month+'月'+day+'日');
                  textMsg[i].createTime = year + '年' + month + '月' + day + '日';
              }  
              that.setData({
                  textMsg: res.data.data
              });
              console.log(that.data.textMsg);

          },
          fail: function (err) {
              wx.showModal({
                  showCancel: false,
                  content: err
              })
          }
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