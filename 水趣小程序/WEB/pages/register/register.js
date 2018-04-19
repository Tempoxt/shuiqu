// pages/register/register.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {},
    buttonColor: "#ddd",
    clickStatus: true,
    selectStatus: false,
    phoneNum: ''
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
  toggleStatus: function (e) {
    this.setData({
      selectStatus: !this.data.selectStatus
    })
    if (this.data.phoneNum != "" && this.data.selectStatus == true) {
      this.setData({
        buttonColor: "#EECE1F",
        clickStatus: false
      });
    } else {
      this.setData({
        buttonColor: "#ddd",
        clickStatus: true
      });
    }
  },
  inputHandler: function (e) {
    var that = this;
    this.setData({
      phoneNum: e.detail.value
    });
    if (this.data.phoneNum != "" && this.data.selectStatus == true) {
      this.setData({
        buttonColor: "#EECE1F",
        clickStatus: false
      });
    } else {
      this.setData({
        buttonColor: "#ddd",
        clickStatus: true
      });
    }
    console.log(this.data.phoneNum);
  },
  getCode: function () {
    var regPhone = /^1[3|4|5|7|8][0-9]{9}$/;
    if (!regPhone.test(this.data.phoneNum)) {
      wx.showModal({
        title: '',
        content: '手机号不正确',
        showCancel: false
      })
    } else {
      wx.setStorageSync('phoneNum', this.data.phoneNum);
      wx.navigateTo({
        url: '../bind/bind'
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
    return {
      title: app.globalData.title,
      imageUrl: app.globalData.liteappShearPic || '../../images/shareImg.jpg',
      path: 'pages/index/index'
    }
  }
})