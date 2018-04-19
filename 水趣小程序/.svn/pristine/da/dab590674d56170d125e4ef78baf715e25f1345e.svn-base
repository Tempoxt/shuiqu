// pages/oftenUseShop/oftenUseShop.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {},
    currentCity: "选城市",
    cacheKey: null,
    // -3:未登录，-2:未绑定，1:已登录
    hasBind: -3,
    hasCity: false,
    modalWrap: "",
    selectStatus: false,
    modal: "",
    phoneNum: "",
    getCodeText: "获取验证码",
    getCodeColor: "#EECE1F",
    validateCode: "",
    commitColor: "#ddd",
    getCodeStatus: false,
    bindStatus: true
  },
  searchShop: function () {
    wx.redirectTo({
      url: '../search/search',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  goShop: function (e) {
    var hasBind = app.globalData.hasBind;
    var shopId = e.currentTarget.dataset.shopid;
    hasBind == 1 ?
      wx.redirectTo({
        url: '/pages/products/products?shopId=' + shopId,
      }) : this.openModal();
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    var that = this;
    //调用应用实例的方法获取全局数据  
    app.getUserInfo(function (userInfo) {
      //更新数据  
      that.setData({
        userInfo: userInfo,
        theme: app.globalData.theme
      })
    });

    // 获取常用店铺
    if (app.globalData.hasBind == 1) {
      wx.showLoading({
        title: '加载中...',
      })
      wx.request({
        url: app.globalData.domain + '/wxLiteapp/getClientCollectShop',
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
            var shops = res.data.data.shops;
            that.setData({
              shops: shops
            });
            wx.hideLoading();
            console.log(res.data.data);
          } else if (code == '-3') {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + '/wxLiteapp/getClientCollectShop',
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
                    var shops = res.data.data.shops;
                    that.setData({
                      shops: shops
                    });
                    wx.hideLoading();
                    console.log(res.data.data);
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
      })
    }


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