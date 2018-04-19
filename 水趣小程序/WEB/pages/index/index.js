//index.js
//获取应用实例
var app = getApp();
Page({
  data: {
    userInfo: {},
    currentCity: "选城市",
    cacheKey: null,
    // -3:未登录，-2:未绑定，1:已登录
    hasBind: -2,
    hasCity: false,
    modalWrap: "",
    selectStatus: true,
    modal: "",
    phoneNum: "",
    getCodeText: "获取验证码",
    getCodeColor: "",
    validateCode: "",
    modalWrap: "",
    commitColor: "#ddd",
    fontColor: "#666",
    textColor: "#fff",
    getCodeStatus: false,
    bindStatus: true
  },
  /**
* 关闭注册窗口
*/
  // closeModal: function () {
  //     this.setData({
  //         modalWrap: false,
  //         modal: false
  //     });
  // },
  /**
   * 打开注册窗口
   */
  // openModal: function () {
  //     var that = this;
  //     this.setData({
  //         modalWrap: true,
  //         modal: true
  //     });
  //     wx.request({
  //         url: app.globalData.domain + "/wxLiteapp/getEnterpriseCity",
  //         method: 'POST',
  //         header: {
  //             'content-type': 'application/x-www-form-urlencoded'
  //         },
  //         data: {
  //             cacheKey: app.globalData.cacheKey,
  //             c: app.globalData.c
  //         },
  //         success: function (res) {
  //             var code = res.data.code;
  //             // wx.hideLoading();
  //             if (code == 'E00000') {
  //                 var result = res.data.data;
  //                 if (result.length == 1) {
  //                     var cityId = result[0].cityId;
  //                     var location = result[0].location;
  //                     var cityName = result[0].cityName;
  //                     wx.request({
  //                         url: app.globalData.domain + "/wxLiteapp/saveCity",
  //                         method: 'POST',
  //                         header: {
  //                             'content-type': 'application/x-www-form-urlencoded'
  //                         },
  //                         data: {
  //                             cacheKey: app.globalData.cacheKey,
  //                             c: app.globalData.c,
  //                             cityId: cityId,
  //                             location: location

  //                         },
  //                         success: function (res) {
  //                             var code = res.data.code;
  //                             if (code == 'E00000') {
  //                                 app.globalData.cityId = cityId;
  //                                 that.closeCityList();
  //                                 that.setData({
  //                                     currentCity: cityName,
  //                                     cityId:cityId,
  //                                     location: location,
  //                                     oneCity: true
  //                                 });

  //                                 wx.hideLoading();
  //                             } else {
  //                                 wx.showModal({
  //                                     content: res.data.message,
  //                                     showCancel: false
  //                                 })
  //                                 wx.hideLoading();
  //                             }

  //                         },
  //                         fail: function (err) {
  //                             wx.showModal({
  //                                 showCancel: false,
  //                                 content: err
  //                             })
  //                             wx.hideLoading();
  //                         }
  //                     });

  //                 } else {
  //                     that.setData({ currentCity: '定位中...' })
  //                     wx.getLocation({
  //                         type: 'wgs84',
  //                         success: function (res) {
  //                             var latitude = res.latitude
  //                             var longitude = res.longitude
  //                             var speed = res.speed
  //                             var accuracy = res.accuracy
  //                             console.log(res);
  //                             wx.request({
  //                                 url: app.globalData.domain + "/wxLiteapp/changeWxLocationToBaidu",
  //                                 method: 'POST',
  //                                 header: {
  //                                     'content-type': 'application/x-www-form-urlencoded'
  //                                 },
  //                                 data: {
  //                                     cacheKey: app.globalData.cacheKey,
  //                                     c: app.globalData.c,
  //                                     lat: latitude,
  //                                     lon: longitude,
  //                                     source: 1
  //                                 },
  //                                 success: function (res) {
  //                                     // wx.hideLoading();
  //                                     var code = res.data.code;
  //                                     if (code == 'E00000') {
  //                                         //去除城市中的市字
  //                                         var city = res.data.city.substr(0, res.data.city.lastIndexOf('市'));
  //                                         for (var i = 0; i < result.length; i++) {
  //                                             if (result[i].cityName == city) {
  //                                                 var flag = true;
  //                                                 that.setData({
  //                                                     currentCity: city
  //                                                 });
  //                                                 break;
  //                                             }
  //                                         }
  //                                         if (!flag) {
  //                                             that.setData({
  //                                                 currentCity: '选城市'
  //                                             });
  //                                         }
  //                                         for (var i = 0; i < result.length; i++) {
  //                                             if (that.data.currentCity == result[i].cityName) {
  //                                                 var cityId = result[i].cityId;
  //                                                 var location = result[i].location;
  //                                                 var cityName = result[i].cityName;
  //                                                 wx.request({
  //                                                     url: app.globalData.domain + "/wxLiteapp/saveCity",
  //                                                     method: 'POST',
  //                                                     header: {
  //                                                         'content-type': 'application/x-www-form-urlencoded'
  //                                                     },
  //                                                     data: {
  //                                                         cacheKey: app.globalData.cacheKey,
  //                                                         c: app.globalData.c,
  //                                                         cityId: cityId,
  //                                                         location: location

  //                                                     },
  //                                                     success: function (res) {
  //                                                         var code = res.data.code;
  //                                                         if (code == 'E00000') {
  //                                                             app.globalData.cityId = cityId;
  //                                                             that.closeCityList();
  //                                                             that.setData({
  //                                                                 currentCity: cityName,
  //                                                                 cityId:cityId,
  //                                                                 location: location
  //                                                             });

  //                                                             wx.hideLoading();
  //                                                         } else {
  //                                                             wx.showModal({
  //                                                                 content: res.data.message,
  //                                                                 showCancel: false
  //                                                             })
  //                                                             wx.hideLoading();
  //                                                         }

  //                                                     },
  //                                                     fail: function (err) {
  //                                                         wx.showModal({
  //                                                             showCancel: false,
  //                                                             content: err
  //                                                         })
  //                                                         wx.hideLoading();
  //                                                     }
  //                                                 });

  //                                             }
  //                                         }

  //                                     } else {
  //                                         wx.showModal({
  //                                             content: res.data.message,
  //                                             showCancel: false
  //                                         })
  //                                     }


  //                                 },
  //                                 fail: function (err) {
  //                                     wx.showModal({
  //                                         content: err,
  //                                         showCancel: false
  //                                     })
  //                                 }
  //                             })
  //                         }
  //                     })
  //                 }
  //             } else if (code == '-3') {
  //                 app.login(function () {
  //                     wx.request({
  //                         url: app.globalData.domain + "/wxLiteapp/getEnterpriseCity",
  //                         method: 'POST',
  //                         header: {
  //                             'content-type': 'application/x-www-form-urlencoded'
  //                         },
  //                         data: {
  //                             cacheKey: app.globalData.cacheKey,
  //                             c: app.globalData.c
  //                         },
  //                         success: function (res) {
  //                             var code = res.data.code;
  //                             var result = res.data.data;
  //                             if (result.length == 1) {
  //                                 var cityId = result[0].cityId;
  //                                 var location = result[0].location;
  //                                 var cityName = result[0].cityName;
  //                                 wx.request({
  //                                     url: app.globalData.domain + "/wxLiteapp/saveCity",
  //                                     method: 'POST',
  //                                     header: {
  //                                         'content-type': 'application/x-www-form-urlencoded'
  //                                     },
  //                                     data: {
  //                                         cacheKey: app.globalData.cacheKey,
  //                                         c: app.globalData.c,
  //                                         cityId: cityId,
  //                                         location: location

  //                                     },
  //                                     success: function (res) {
  //                                         var code = res.data.code;
  //                                         if (code == 'E00000') {
  //                                             app.globalData.cityId = cityId;
  //                                             that.closeCityList();
  //                                             that.setData({
  //                                                 currentCity: cityName,
  //                                                 cityId:cityId,
  //                                                 location: location,
  //                                                 oneCity: true
  //                                             });

  //                                             wx.hideLoading();
  //                                         } else {
  //                                             wx.showModal({
  //                                                 content: res.data.message,
  //                                                 showCancel: false
  //                                             })
  //                                             wx.hideLoading();
  //                                         }

  //                                     },
  //                                     fail: function (err) {
  //                                         wx.showModal({
  //                                             showCancel: false,
  //                                             content: err
  //                                         })
  //                                         wx.hideLoading();
  //                                     }
  //                                 });

  //                             } else {
  //                                 that.setData({ currentCity: '定位中...' })
  //                                 wx.getLocation({
  //                                     type: 'wgs84',
  //                                     success: function (res) {
  //                                         var latitude = res.latitude
  //                                         var longitude = res.longitude
  //                                         var speed = res.speed
  //                                         var accuracy = res.accuracy
  //                                         console.log(res);
  //                                         wx.request({
  //                                             url: app.globalData.domain + "/wxLiteapp/changeWxLocationToBaidu",
  //                                             method: 'POST',
  //                                             header: {
  //                                                 'content-type': 'application/x-www-form-urlencoded'
  //                                             },
  //                                             data: {
  //                                                 cacheKey: app.globalData.cacheKey,
  //                                                 c: app.globalData.c,
  //                                                 lat: latitude,
  //                                                 lon: longitude,
  //                                                 source: 1
  //                                             },
  //                                             success: function (res) {
  //                                                 // wx.hideLoading();
  //                                                 var code = res.data.code;
  //                                                 if (code == 'E00000') {
  //                                                     //去除城市中的市字
  //                                                     var city = res.data.city.substr(0, res.data.city.lastIndexOf('市'));
  //                                                     for (var i = 0; i < result.length; i++) {
  //                                                         if (result[i].cityName == city) {
  //                                                             var flag = true;
  //                                                             that.setData({
  //                                                                 currentCity: city
  //                                                             });
  //                                                             break;
  //                                                         }
  //                                                     }
  //                                                     if (!flag) {
  //                                                         that.setData({
  //                                                             currentCity: '选城市'
  //                                                         });
  //                                                     }
  //                                                 } else {
  //                                                     wx.showModal({
  //                                                         content: res.data.message,
  //                                                         showCancel: false
  //                                                     })
  //                                                 }


  //                                             },
  //                                             fail: function (err) {
  //                                                 wx.showModal({
  //                                                     content: err,
  //                                                     showCancel: false
  //                                                 })
  //                                             }
  //                                         })
  //                                     }
  //                                 })
  //                             }

  //                         },
  //                         fail: function (err) {
  //                             wx.showModal({
  //                                 content: err,
  //                                 showCancel: false
  //                             })
  //                         }
  //                     })
  //                 });
  //             } else {
  //                 wx.hideLoading();
  //                 wx.showModal({
  //                     content: res.data.message,
  //                     showCancel: false
  //                 })
  //             }

  //         },
  //         fail: function (err) {
  //             wx.showModal({
  //                 content: err,
  //                 showCancel: false
  //             })
  //         }
  //     })
  // },
  /**
   * 获取验证码
   */
  // getCode: function (e) {
  //     var regPhone = /^1[3|4|5|7|8][0-9]{9}$/;
  //     //校验手机号失败
  //     if (this.data.phoneNum == "") {
  //         wx.showToast({
  //             title: '手机号不能为空',
  //             image: '../../images/error_popover.png'
  //         });
  //     }
  //     else if (!regPhone.test(this.data.phoneNum)) {
  //         wx.showToast({
  //             title: '手机号不正确',
  //             image: '../../images/error_popover.png'
  //         });
  //     } else {
  //         var that = this;
  //         function timeOver() {
  //             if (that.data.getCodeText == 0) {
  //                 clearInterval(Countdown);
  //                 that.setData({
  //                     getCodeText: "获取验证码",
  //                     getCodeColor: "",
  //                     textColor:"#fff",
  //                     getCodeStatus: false
  //                 });
  //             }
  //         }
  //         //获取手机验证码
  //         wx.request({
  //             url: app.globalData.domain + "/wxLiteapp/moblieMsg",
  //             method: 'POST',
  //             header: {
  //                 'content-type': 'application/x-www-form-urlencoded'
  //             },
  //             data: {
  //                 cacheKey: app.globalData.cacheKey,
  //                 c: app.globalData.c,
  //                 cellphone: that.data.phoneNum
  //             },
  //             success: function (res) {
  //                 var code = res.data.code;
  //                 if(code=="E00000"){
  //                     console.log(res);
  //                 } else if (code =="-3"){
  //                     app.login(function(){
  //                         wx.request({
  //                             url: app.globalData.domain + "/wxLiteapp/moblieMsg",
  //                             method: 'POST',
  //                             header: {
  //                                 'content-type': 'application/x-www-form-urlencoded'
  //                             },
  //                             data: {
  //                                 cacheKey: app.globalData.cacheKey,
  //                                 c: app.globalData.c,
  //                                 cellphone: that.data.phoneNum
  //                             },
  //                             success: function (res) {
  //                                 var code = res.data.code;
  //                                 if (code == "E00000") {
  //                                     console.log(res);
  //                                 } else{
  //                                     wx.showModal({
  //                                         content: res.data.message,
  //                                         showCancel: false
  //                                     })
  //                                 }

  //                             }
  //                         })
  //                     });
  //                 }else{
  //                     wx.showModal({
  //                         content: res.data.message,
  //                         showCancel: false
  //                     })
  //                 }

  //             }
  //         })
  //         //验证码倒计时，改变状态，不可点击
  //         this.setData({
  //             getCodeColor: "#ddd",
  //             getCodeText: "60",
  //             getCodeStatus: true,
  //             textColor: "#666"
  //         });
  //         var Countdown = setInterval(function () {
  //             that.setData({
  //                 getCodeText: that.data.getCodeText - 1,
  //             });
  //             timeOver();
  //         }, 1000)
  //     }
  // },
  /**
   * 输入手机号时
   */
  // inputPhoneNum: function (e) {
  //     var that = this;
  //     this.setData({
  //         phoneNum: e.detail.value
  //     });
  //     if (this.data.phoneNum && this.data.validateCode && this.data.selectStatus) {
  //         if (this.data.theme == 1) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#F6CC47,#FFAD52)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         } else if (this.data.theme == 2) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#7b00d3,#b25aff)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         } else if (this.data.theme == 3){
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#cf1731,#ff4060)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         } else if (this.data.theme == 4) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#15a0ff,#3ebaff)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         } else if (this.data.theme == 5) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#2cc136,#5de04f)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         }


  //     } else {
  //         this.setData({
  //             commitColor: "#ddd",
  //             fontColor: "#666",
  //             bindStatus: true
  //         });

  //     }
  //     console.log(this.data.phoneNum);
  // },
  /**
   * 输入验证码时
   */
  // inputValidateCode: function (e) {
  //     this.setData({
  //         validateCode: e.detail.value
  //     });
  //     if (this.data.phoneNum && this.data.validateCode && this.data.selectStatus) {
  //         if (this.data.theme == 1) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#F6CC47,#FFAD52)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         } else if (this.data.theme == 2) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#7b00d3,#b25aff)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         } else if (this.data.theme == 3) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#cf1731,#ff4060)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         } else if (this.data.theme == 4) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#15a0ff,#3ebaff)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         } else if (this.data.theme == 5) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#2cc136,#5de04f)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         }


  //     } else {
  //         this.setData({
  //             commitColor: "#ddd",
  //             fontColor: "#666",
  //             bindStatus: true
  //         });

  //     }
  //     console.log(this.data.validateCode);
  // },
  /**
   * 切换注册窗口的单选框
   */
  // toggleStatus: function () {
  //     this.setData({
  //         selectStatus: !this.data.selectStatus
  //     })
  //     if (this.data.phoneNum && this.data.validateCode && this.data.selectStatus) {
  //         if (this.data.theme == 1) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#F6CC47,#FFAD52)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         } else if (this.data.theme == 2) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#7b00d3,#b25aff)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         } else if (this.data.theme == 3) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#cf1731,#ff4060)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         } else if (this.data.theme == 4) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#15a0ff,#3ebaff)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         } else if (this.data.theme == 5) {
  //             this.setData({
  //                 commitColor: "linear-gradient(to right,#2cc136,#5de04f)",
  //                 fontColor: "#fff",
  //                 bindStatus: false
  //             });
  //         }


  //     } else {
  //         this.setData({
  //             commitColor: "#ddd",
  //             fontColor: "#666",
  //             bindStatus: true
  //         });

  //     }
  // },
  // 通过微信授权获取手机号
  // getPhoneNumber: function (e) {
  //     // console.log(e.detail.errMsg)
  //     var that = this;
  //     var iv = e.detail.iv;
  //     var encryptedData = e.detail.encryptedData;
  //     console.log(encryptedData);
  //     // 取消授权
  //     if (e.detail.errMsg == 'getPhoneNumber:fail user deny') {

  //     } else {
  //         // 确认授权
  //         var cityId = that.data.cityId;
  //         var location = that.data.location;
  //         console.log(cityId);
  //         // 没获取到城市id和坐标，默认传0
  //         if (!location){
  //             var cityId = 0;
  //             var location = 0;
  //         }
  //         wx.request({
  //             url: app.globalData.domain + "/wxLiteapp/getCellphone",
  //             method: 'POST',
  //             header: {
  //                 'content-type': 'application/x-www-form-urlencoded'
  //             },
  //             data: {
  //                 cacheKey: app.globalData.cacheKey,
  //                 c: app.globalData.c,
  //                 encryptedData: encryptedData,
  //                 iv: iv,
  //                 cityId:cityId,
  //                 location:location
  //             },
  //             success: function (res) {
  //                 console.log(res);
  //                 var code = res.data.code;
  //                 if (code == "E00000") {
  //                     app.globalData.hasBind = 1;
  //                     that.setData({
  //                         hasBind: 1,
  //                         blurBg: false,
  //                         unBindBg: false
  //                     });
  //                 } else if (code == "-3") {
  //                     app.login(function () {
  //                         wx.request({
  //                             url: app.globalData.domain + "/wxLiteapp/getCellphone",
  //                             method: 'POST',
  //                             header: {
  //                                 'content-type': 'application/x-www-form-urlencoded'
  //                             },
  //                             data: {
  //                                 cacheKey: app.globalData.cacheKey,
  //                                 c: app.globalData.c,
  //                                 encryptedData: encryptedData,
  //                                 iv: iv,
  //                                 cityId: that.data.cityId,
  //                                 location: that.data.location
  //                             },
  //                             success: function (res) {
  //                                 console.log(res);
  //                                 var code = res.data.code;
  //                                 if (code == "E00000") {
  //                                     app.globalData.hasBind = 1;
  //                                     that.setData({
  //                                         hasBind: 1,
  //                                         blurBg: false,
  //                                         unBindBg: false
  //                                     });
  //                                 } else if (code == "-3") {
  //                                     wx.showModal({
  //                                         showCancel: false,
  //                                         content: res.data.message
  //                                     })
  //                                 }

  //                             },
  //                             fail: function (err) {
  //                                 wx.showModal({
  //                                     title: '',
  //                                     showCancel: false,
  //                                     content: "网络异常！"
  //                                 })
  //                             }
  //                         })
  //                     });

  //                 }

  //             },
  //             fail: function (err) {
  //                 wx.showModal({
  //                     title: '',
  //                     showCancel: false,
  //                     content: "网络异常！"
  //                 })
  //             }
  //         })
  //     }



  // }, 
  /**
   * 绑定手机号
   */
  // commit: function (e) {
  //     var that = this;
  //     var regPhone = /^1[3|4|5|7|8][0-9]{9}$/;
  //     //校验手机号失败
  //     if (!regPhone.test(this.data.phoneNum)) {
  //         wx.showToast({
  //             title: '手机号不正确',
  //             image: '../../images/error_popover.png'
  //         })
  //     } else {
  //         var that = this;
  //         if (that.data.currentCity != "选城市" && that.data.currentCity != "") {
  //             wx.request({
  //                 url: app.globalData.domain + "/wxLiteapp/saveBind",
  //                 method: 'POST',
  //                 header: {
  //                     'content-type': 'application/x-www-form-urlencoded'
  //                 },
  //                 data: {
  //                     cacheKey: app.globalData.cacheKey,
  //                     c: app.globalData.c,
  //                     cellphone: that.data.phoneNum,
  //                     code: that.data.validateCode
  //                 },
  //                 success: function (result) {
  //                     console.log(result);
  //                     if (result.data.code != "E00000") {
  //                         wx.showModal({
  //                             showCancel: false,
  //                             content: result.data.message
  //                         })
  //                     } else {
  //                         if (result.data.data.isBindExistsAccount) {
  //                             wx.showModal({
  //                                 showCancel: false,
  //                                 content: "绑定成功"
  //                             })
  //                         } else {
  //                             wx.showModal({
  //                                 showCancel: false,
  //                                 content: "注册成功"
  //                             })
  //                         }
  //                         that.setData({
  //                             hasBind: 1,
  //                             blurBg: false,
  //                             unBindBg: false
  //                         });
  //                         app.globalData.hasBind = 1;
  //                         that.closeModal();
  //                     }

  //                 },
  //                 fail: function (err) {
  //                     wx.showModal({
  //                         title: '',
  //                         showCancel: false,
  //                         content: "网络异常！"
  //                     })
  //                 }
  //             })
  //         } else {
  //             wx.showToast({
  //                 title: '请选择城市',
  //                 image: '../../images/error_popover.png'
  //             })
  //         }


  //     }

  // },
  /**
   * 选择城市
   */
  // getCity: function () {
  //     var that = this;
  //     if (this.data.oneCity) {
  //         console.log('城市列表中有定位的城市');
  //     } else {
  //         wx.showLoading({
  //             title: '加载中...',
  //         })
  //         this.setData({
  //             shadeOn: true
  //         })
  //         wx.request({
  //             url: app.globalData.domain + "/wxLiteapp/getEnterpriseCity",
  //             method: 'POST',
  //             header: {
  //                 'content-type': 'application/x-www-form-urlencoded'
  //             },
  //             data: {
  //                 cacheKey: app.globalData.cacheKey,
  //                 c: app.globalData.c
  //             },
  //             success: function (res) {
  //                 var code = res.data.code;
  //                 if (code == 'E00000'){
  //                     var result = res.data.data;

  //                     var cityList = [];

  //                     for (var i = 0; i < result.length; i++) {
  //                         cityList[i] = result[i].cityName;
  //                     }
  //                     console.log(result);

  //                     wx.showActionSheet({
  //                         itemList: cityList,
  //                         success: function (res) {

  //                             console.log(result[res.tapIndex]);
  //                             var cityId = result[res.tapIndex].cityId;
  //                             var location = result[res.tapIndex].location;
  //                             var cityName = result[res.tapIndex].cityName;
  //                             //保存城市坐标
  //                             wx.request({
  //                                 url: app.globalData.domain + "/wxLiteapp/saveCity",
  //                                 method: 'POST',
  //                                 header: {
  //                                     'content-type': 'application/x-www-form-urlencoded'
  //                                 },
  //                                 data: {
  //                                     cacheKey: app.globalData.cacheKey,
  //                                     c: app.globalData.c,
  //                                     cityId: cityId,
  //                                     location: location

  //                                 },
  //                                 success: function (res) {
  //                                     var code = res.data.code;
  //                                     if (code == 'E00000') {
  //                                         app.globalData.cityId = cityId;
  //                                         that.closeCityList();
  //                                         that.setData({
  //                                             cityId:cityId,
  //                                             currentCity: cityName,
  //                                             location: location
  //                                         });
  //                                     } else {
  //                                         wx.showModal({
  //                                             content: res.data.message,
  //                                             showCancel: false
  //                                         })
  //                                     }

  //                                 }, 
  //                                 fail: function (err) {
  //                                     wx.showModal({
  //                                         showCancel: false,
  //                                         content: err
  //                                     })
  //                                 }
  //                             });
  //                         },
  //                         fail: function (res) {
  //                             console.log(res.errMsg)
  //                         }
  //                     });
  //                     wx.hideLoading();
  //                 }else if(code == '-3'){
  //                     app.login(function(){
  //                         wx.request({
  //                             url: app.globalData.domain + "/wxLiteapp/getEnterpriseCity",
  //                             method: 'POST',
  //                             header: {
  //                                 'content-type': 'application/x-www-form-urlencoded'
  //                             },
  //                             data: {
  //                                 cacheKey: app.globalData.cacheKey,
  //                                 c: app.globalData.c
  //                             },
  //                             success: function (res) {
  //                                 var code = res.data.code;
  //                                 if (code == 'E00000') {
  //                                     var result = res.data.data;

  //                                     var cityList = [];

  //                                     for (var i = 0; i < result.length; i++) {
  //                                         cityList[i] = result[i].cityName;
  //                                     }
  //                                     console.log(result);

  //                                     wx.showActionSheet({
  //                                         itemList: cityList,
  //                                         success: function (res) {

  //                                             console.log(result[res.tapIndex]);
  //                                             var cityId = result[res.tapIndex].cityId;
  //                                             var location = result[res.tapIndex].location;
  //                                             var cityName = result[res.tapIndex].cityName;
  //                                             //保存城市坐标
  //                                             wx.request({
  //                                                 url: app.globalData.domain + "/wxLiteapp/saveCity",
  //                                                 method: 'POST',
  //                                                 header: {
  //                                                     'content-type': 'application/x-www-form-urlencoded'
  //                                                 },
  //                                                 data: {
  //                                                     cacheKey: app.globalData.cacheKey,
  //                                                     c: app.globalData.c,
  //                                                     cityId: cityId,
  //                                                     location: location

  //                                                 },
  //                                                 success: function (res) {
  //                                                     var code = res.data.code;
  //                                                     if (code == 'E00000') {
  //                                                         app.globalData.cityId = cityId;
  //                                                         that.closeCityList();
  //                                                         that.setData({
  //                                                             currentCity: cityName,
  //                                                             cityId:cityId,
  //                                                             location: location
  //                                                         });
  //                                                     } else {
  //                                                         wx.showModal({
  //                                                             content: res.data.message,
  //                                                             showCancel: false
  //                                                         })
  //                                                     }

  //                                                 }, fail: function (err) {
  //                                                     wx.showModal({
  //                                                         showCancel: false,
  //                                                         content: err
  //                                                     })
  //                                                 }
  //                                             });
  //                                         },
  //                                         fail: function (res) {
  //                                             console.log(res.errMsg)
  //                                         }
  //                                     });
  //                                     wx.hideLoading();
  //                                 } else{
  //                                     wx.showModal({
  //                                         content: res.data.message,
  //                                         showCancel: false
  //                                     })
  //                                 }


  //                             },
  //                             fail:function(err){
  //                                 wx.showModal({
  //                                     content: err,
  //                                     showCancel: false
  //                                 })
  //                             }
  //                         })
  //                     });
  //                 }else{
  //                     wx.showModal({
  //                         content: res.data.message,
  //                         showCancel:false
  //                     })
  //                 }


  //             },
  //             fail:function(err){
  //                 wx.showModal({
  //                     content: err,
  //                     showCancel:false
  //                 })
  //             }
  //         })
  //     }

  // },
  // closeCityList: function () {
  //     this.setData({
  //         cityList: false
  //     });
  // },
  /**
   * 快速订水
   */
  quickOrderWater: function () {
    var that = this;
    if (this.data.hasBind == 1) {
      //如果有消费习惯
      if (app.globalData.hasHabit) {
        wx.request({
          url: app.globalData.domain + "/wxLiteapp/habitToCart",
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
              var shopId = app.globalData.shopId;
              wx.navigateTo({
                url: '../shopCart/shopCart?shopId=' + shopId
              });
            } else if (code == '-3') {
              app.login(function () {
                wx.request({
                  url: app.globalData.domain + "/wxLiteapp/habitToCart",
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
                      var shopId = app.globalData.shopId;
                      wx.navigateTo({
                        url: '../shopCart/shopCart?shopId=' + shopId
                      });
                    } else {
                      wx.showModal({
                        content: res.data.message,
                        showCancel: false
                      })
                    }

                  },
                  fail: function (err) {
                    console.log(err);
                    wx.showModal({
                      content: err,
                      showCancel: false
                    })
                  }
                })
              });
            } else {
              console.log(res.data.message);
              wx.showModal({
                content: res.data.message,
                showCancel: false
              })
            }

          },
          fail: function (err) {
            console.log(err);
            wx.showModal({
              content: err,
              showCancel: false
            })
          }
        })
      } else {
        //没有消费习惯跳到常用店铺
        wx.navigateTo({
          url: '../oftenUseShop/oftenUseShop'
        });
      }

    } else {
      wx.navigateTo({ url: '../oftenUseShop/oftenUseShop' })
    }

  },
  /**
   * 常用店铺
   */
  oftenUseShop: function () {
    wx.navigateTo({ url: '../oftenUseShop/oftenUseShop' })
  },
  /**
   * 我的订单
   */
  myOrders: function () {
    if (this.data.hasBind == 1) {
      wx.navigateTo({ url: '../myOrder/myOrder?currentTab=0' })
    } else {
      wx.navigateTo({ url: '../oftenUseShop/oftenUseShop' })
    }
  },
  /**
   * 我的优惠券
   */
  myCoupons: function () {
    if (this.data.hasBind == 1) {
      wx.navigateTo({ url: '../myCoupons/myCoupons' })
    } else {
      wx.navigateTo({ url: '../oftenUseShop/oftenUseShop' })
    }
  },
  /**
   * 我的地址
   */
  myAddress: function () {
    if (this.data.hasBind == 1) {
      wx.navigateTo({ url: '../address/address' })
    } else {
      wx.navigateTo({ url: '../oftenUseShop/oftenUseShop' })
    }
  },
  /**
   * 关于我们
   */
  aboutUs: function () {
    if (this.data.hasBind == 1) {
      wx.navigateTo({ url: '../aboutUs/aboutUs' })
    } else {
      wx.navigateTo({ url: '../oftenUseShop/oftenUseShop' })
    }
  },
  loadData: function () {
    var that = this;
    wx.showLoading({
      title: '加载中...',
    })
    // 获取消息通知
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
        console.log(111);
        if (code == "E00000") {
          that.setData({
            textMsg: res.data.data,
            shadeOn: false
          });
          wx.request({
            url: app.globalData.domain + "/wxLiteapp/getIntegralCodeNum",
            method: 'POST',
            header: {
              'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
              cacheKey: app.globalData.cacheKey,
              c: app.globalData.c
            },
            success: function (res) {
              console.log(res.data);
              var code = res.data.code;
              wx.hideLoading();
              if (code == 'E00000') {
                that.setData({
                  integral: res.data.data.integral,
                  ticket_number: res.data.data.ticket_number
                });

              } else if (code == "-3") {
                app.login(function () {
                  wx.request({
                    url: app.globalData.domain + "/wxLiteapp/getIntegralCodeNum",
                    method: 'POST',
                    header: {
                      'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                      cacheKey: app.globalData.cacheKey,
                      c: app.globalData.c
                    },
                    success: function (res) {
                      console.log(res.data);
                      var code = res.data.code;
                      wx.hideLoading();
                      if (code == 'E00000') {
                        that.setData({
                          integral: res.data.data.integral,
                          ticket_number: res.data.data.ticket_number
                        });
                      } else if (code == "-3") {
                        wx.showModal({
                          showCancel: false,
                          content: res.data.message
                        })
                      }

                    },
                    fail: function (err) {
                      clearInterval(timers);
                      wx.hideLoading();
                      wx.showModal({
                        showCancel: false,
                        content: err
                      })
                    }
                  })
                });

              }

            },
            fail: function (err) {
              clearInterval(timers);
              wx.hideLoading();
              wx.showModal({
                showCancel: false,
                content: err
              })
            }
          })
          console.log(that.data.textMsg);
        } else if (code == "-3") {
          app.login(function () {
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
                if (code == "E00000") {
                  that.setData({
                    textMsg: res.data.data,
                    shadeOn: false
                  });
                  wx.request({
                    url: app.globalData.domain + "/wxLiteapp/getIntegralCodeNum",
                    method: 'POST',
                    header: {
                      'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                      cacheKey: app.globalData.cacheKey,
                      c: app.globalData.c
                    },
                    success: function (res) {
                      console.log(res.data);
                      var code = res.data.code;
                      wx.hideLoading();
                      if (code == 'E00000') {
                        that.setData({
                          integral: res.data.data.integral,
                          ticket_number: res.data.data.ticket_number
                        });

                      } else if (code == "-3") {
                        app.login(function () {
                          wx.request({
                            url: app.globalData.domain + "/wxLiteapp/getIntegralCodeNum",
                            method: 'POST',
                            header: {
                              'content-type': 'application/x-www-form-urlencoded'
                            },
                            data: {
                              cacheKey: app.globalData.cacheKey,
                              c: app.globalData.c
                            },
                            success: function (res) {
                              console.log(res.data);
                              var code = res.data.code;
                              wx.hideLoading();
                              if (code == 'E00000') {
                                that.setData({
                                  integral: res.data.data.integral,
                                  ticket_number: res.data.data.ticket_number
                                });
                              } else if (code == "-3") {
                                wx.showModal({
                                  showCancel: false,
                                  content: res.data.message
                                })
                              }

                            },
                            fail: function (err) {
                              clearInterval(timers);
                              wx.hideLoading();
                              wx.showModal({
                                showCancel: false,
                                content: err
                              })
                            }
                          })
                        });

                      }

                    },
                    fail: function (err) {
                      clearInterval(timers);
                      wx.hideLoading();
                      wx.showModal({
                        showCancel: false,
                        content: err
                      })
                    }
                  })
                  console.log(that.data.textMsg);
                } else if (code == "C00086") {
                  that.setData({
                    textMsg: res.data.data,
                    shadeOn: false
                  });
                  wx.request({
                    url: app.globalData.domain + "/wxLiteapp/getIntegralCodeNum",
                    method: 'POST',
                    header: {
                      'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                      cacheKey: app.globalData.cacheKey,
                      c: app.globalData.c
                    },
                    success: function (res) {
                      console.log(res.data);
                      var code = res.data.code;
                      wx.hideLoading();
                      if (code == 'E00000') {
                        that.setData({
                          integral: res.data.data.integral,
                          ticket_number: res.data.data.ticket_number
                        });

                      } else if (code == "-3") {
                        app.login(function () {
                          wx.request({
                            url: app.globalData.domain + "/wxLiteapp/getIntegralCodeNum",
                            method: 'POST',
                            header: {
                              'content-type': 'application/x-www-form-urlencoded'
                            },
                            data: {
                              cacheKey: app.globalData.cacheKey,
                              c: app.globalData.c
                            },
                            success: function (res) {
                              console.log(res.data);
                              var code = res.data.code;
                              wx.hideLoading();
                              if (code == 'E00000') {
                                that.setData({
                                  integral: res.data.data.integral,
                                  ticket_number: res.data.data.ticket_number
                                });
                              } else if (code == "-3") {
                                wx.showModal({
                                  showCancel: false,
                                  content: res.data.message
                                })
                              }

                            },
                            fail: function (err) {
                              clearInterval(timers);
                              wx.hideLoading();
                              wx.showModal({
                                showCancel: false,
                                content: err
                              })
                            }
                          })
                        });

                      }

                    },
                    fail: function (err) {
                      clearInterval(timers);
                      wx.hideLoading();
                      wx.showModal({
                        showCancel: false,
                        content: err
                      })
                    }
                  })
                  console.log(that.data.textMsg);
                } else {
                  wx.showModal({
                    showCancel: false,
                    content: res.data.message
                  })
                }

              },
              fail: function (err) {
                wx.showModal({
                  showCancel: false,
                  content: err
                })
              }
            })
          });
        } else if (code == "C00086") {
          that.setData({
            textMsg: res.data.data,
            shadeOn: false
          });
          wx.request({
            url: app.globalData.domain + "/wxLiteapp/getIntegralCodeNum",
            method: 'POST',
            header: {
              'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
              cacheKey: app.globalData.cacheKey,
              c: app.globalData.c
            },
            success: function (res) {
              console.log(res.data);
              var code = res.data.code;
              wx.hideLoading();
              if (code == 'E00000') {
                that.setData({
                  integral: res.data.data.integral,
                  ticket_number: res.data.data.ticket_number
                });

              } else if (code == "-3") {
                app.login(function () {
                  wx.request({
                    url: app.globalData.domain + "/wxLiteapp/getIntegralCodeNum",
                    method: 'POST',
                    header: {
                      'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                      cacheKey: app.globalData.cacheKey,
                      c: app.globalData.c
                    },
                    success: function (res) {
                      console.log(res.data);
                      var code = res.data.code;
                      wx.hideLoading();
                      if (code == 'E00000') {
                        that.setData({
                          integral: res.data.data.integral,
                          ticket_number: res.data.data.ticket_number
                        });
                      } else if (code == "-3") {
                        wx.showModal({
                          showCancel: false,
                          content: res.data.message
                        })
                      }

                    },
                    fail: function (err) {
                      clearInterval(timers);
                      wx.hideLoading();
                      wx.showModal({
                        showCancel: false,
                        content: err
                      })
                    }
                  })
                });

              }

            },
            fail: function (err) {
              clearInterval(timers);
              wx.hideLoading();
              wx.showModal({
                showCancel: false,
                content: err
              })
            }
          })
          console.log(that.data.textMsg);
        }

      },
      fail: function (err) {
        wx.showModal({
          showCancel: false,
          content: err
        })
      }
    })

  },
  autoLocation: function () {
    var that = this;
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/getEnterpriseCity",
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
        // wx.hideLoading();
        if (code == 'E00000') {
          var result = res.data.data;
          if (result.length == 1) {
            var cityId = result[0].cityId;
            var location = result[0].location;
            var cityName = result[0].cityName;
            wx.request({
              url: app.globalData.domain + "/wxLiteapp/saveCity",
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: app.globalData.cacheKey,
                c: app.globalData.c,
                cityId: cityId,
                location: location

              },
              success: function (res) {
                var code = res.data.code;
                if (code == 'E00000') {
                  app.globalData.cityId = cityId;
                  that.closeCityList();
                  that.setData({
                    currentCity: cityName,
                    cityId: cityId,
                    location: location,
                    oneCity: true
                  });

                  wx.hideLoading();
                } else {
                  wx.showModal({
                    content: res.data.message,
                    showCancel: false
                  })
                  wx.hideLoading();
                }

              },
              fail: function (err) {
                wx.showModal({
                  showCancel: false,
                  content: err
                })
                wx.hideLoading();
              }
            });

          } else {
            wx.getLocation({
              type: 'wgs84',
              success: function (res) {
                var latitude = res.latitude
                var longitude = res.longitude
                var speed = res.speed
                var accuracy = res.accuracy
                console.log(res);
                wx.request({
                  url: app.globalData.domain + "/wxLiteapp/changeWxLocationToBaidu",
                  method: 'POST',
                  header: {
                    'content-type': 'application/x-www-form-urlencoded'
                  },
                  data: {
                    cacheKey: app.globalData.cacheKey,
                    c: app.globalData.c,
                    lat: latitude,
                    lon: longitude,
                    source: 1
                  },
                  success: function (res) {
                    // wx.hideLoading();
                    var code = res.data.code;
                    if (code == 'E00000') {
                      //去除城市中的市字
                      var city = res.data.city.substr(0, res.data.city.lastIndexOf('市'));
                      for (var i = 0; i < result.length; i++) {
                        if (result[i].cityName == city) {
                          var flag = true;
                          that.setData({
                            currentCity: city
                          });
                          break;
                        }
                      }
                      // 匹配城市
                      for (var i = 0; i < result.length; i++) {
                        if (that.data.currentCity == result[i].cityName) {
                          var cityMatch = true;
                          var cityId = result[i].cityId;
                          var location = result[i].location;
                          var cityName = result[i].cityName;
                          wx.request({
                            url: app.globalData.domain + "/wxLiteapp/saveCity",
                            method: 'POST',
                            header: {
                              'content-type': 'application/x-www-form-urlencoded'
                            },
                            data: {
                              cacheKey: app.globalData.cacheKey,
                              c: app.globalData.c,
                              cityId: cityId,
                              location: location

                            },
                            success: function (res) {
                              var code = res.data.code;
                              if (code == 'E00000') {
                                app.globalData.cityId = cityId;
                                that.closeCityList();
                                that.setData({
                                  currentCity: cityName,
                                  location: location,
                                  cityId: cityId
                                });

                                wx.hideLoading();
                              } else {
                                wx.showModal({
                                  content: res.data.message,
                                  showCancel: false
                                })
                                wx.hideLoading();
                              }

                            },
                            fail: function (err) {
                              wx.showModal({
                                showCancel: false,
                                content: err
                              })
                              wx.hideLoading();
                            }
                          });

                        }
                      }
                      // 没匹配到城市默认选择第一个
                      if (!cityMatch) {
                        var cityId = result[0].cityId;
                        var location = result[0].location;
                        var cityName = result[0].cityName;
                        wx.request({
                          url: app.globalData.domain + "/wxLiteapp/saveCity",
                          method: 'POST',
                          header: {
                            'content-type': 'application/x-www-form-urlencoded'
                          },
                          data: {
                            cacheKey: app.globalData.cacheKey,
                            c: app.globalData.c,
                            cityId: cityId,
                            location: location

                          },
                          success: function (res) {
                            var code = res.data.code;
                            if (code == 'E00000') {
                              app.globalData.cityId = cityId;
                              that.closeCityList();
                              that.setData({
                                cityId: cityId,
                                currentCity: cityName,
                                location: location
                              });

                              wx.hideLoading();
                            } else {
                              wx.showModal({
                                content: res.data.message,
                                showCancel: false
                              })
                              wx.hideLoading();
                            }

                          },
                          fail: function (err) {
                            wx.showModal({
                              showCancel: false,
                              content: err
                            })
                            wx.hideLoading();
                          }
                        });
                      }
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
            })
          }
        } else if (code == '-3') {
          app.login(function () {
            wx.request({
              url: app.globalData.domain + "/wxLiteapp/getEnterpriseCity",
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
                var result = res.data.data;
                if (result.length == 1) {
                  var cityId = result[0].cityId;
                  var location = result[0].location;
                  var cityName = result[0].cityName;
                  wx.request({
                    url: app.globalData.domain + "/wxLiteapp/saveCity",
                    method: 'POST',
                    header: {
                      'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                      cacheKey: app.globalData.cacheKey,
                      c: app.globalData.c,
                      cityId: cityId,
                      location: location

                    },
                    success: function (res) {
                      var code = res.data.code;
                      if (code == 'E00000') {
                        app.globalData.cityId = cityId;
                        that.closeCityList();
                        that.setData({
                          currentCity: cityName,
                          cityId: cityId,
                          location: location,
                          oneCity: true
                        });

                        wx.hideLoading();
                      } else {
                        wx.showModal({
                          content: res.data.message,
                          showCancel: false
                        })
                        wx.hideLoading();
                      }

                    },
                    fail: function (err) {
                      wx.showModal({
                        showCancel: false,
                        content: err
                      })
                      wx.hideLoading();
                    }
                  });

                } else {
                  that.setData({ currentCity: '定位中...' })
                  wx.getLocation({
                    type: 'wgs84',
                    success: function (res) {
                      var latitude = res.latitude
                      var longitude = res.longitude
                      var speed = res.speed
                      var accuracy = res.accuracy
                      console.log(res);
                      wx.request({
                        url: app.globalData.domain + "/wxLiteapp/changeWxLocationToBaidu",
                        method: 'POST',
                        header: {
                          'content-type': 'application/x-www-form-urlencoded'
                        },
                        data: {
                          cacheKey: app.globalData.cacheKey,
                          c: app.globalData.c,
                          lat: latitude,
                          lon: longitude,
                          source: 1
                        },
                        success: function (res) {
                          // wx.hideLoading();
                          var code = res.data.code;
                          if (code == 'E00000') {
                            //去除城市中的市字
                            var city = res.data.city.substr(0, res.data.city.lastIndexOf('市'));
                            for (var i = 0; i < result.length; i++) {
                              if (result[i].cityName == city) {
                                var flag = true;
                                that.setData({
                                  currentCity: city
                                });
                                break;
                              }
                            }
                            if (!flag) {
                              that.setData({
                                currentCity: '选城市'
                              });
                            }
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
  },
  onLoad: function (options) {
    let that=this;
    app.login().then(function (res) {
  
      // 设置默认页面主题色
      that.setData({
        theme: app.globalData.theme
      });
      wx.showLoading({
        title: '加载中...',
      });
      // 未加载完毕时不让用户点击
      that.setData({
        shadeOn: true
      })

  
      //调用应用实例的方法获取全局数据  
      app.getUserInfo(function (userInfo) {
        console.log("userInfo",userInfo)
        //计时器
        var computed = 0;
        var timer = setInterval(function () {
          computed++;
          if (!app.globalData.cacheKey) {
            //网络请求超过20秒提示超时
            if (computed == 40) {
              clearInterval(timer);
              // 获取第三方平台的参数，版本号、企业名、主题色
              if (app.globalData.version) {
                that.setData({
                  version: app.globalData.version,
                  ename: app.globalData.ename,
                  theme: app.globalData.theme
                });
              }
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
            var cacheKey = app.globalData.cacheKey;
            var hasBind = app.globalData.hasBind;
            wx.setNavigationBarTitle({
              title: app.globalData.title
            })
            if (app.globalData.version) {
              that.setData({
                version: app.globalData.version,
                ename: app.globalData.ename,
                theme: app.globalData.theme
              });
            }
            if (cacheKey && hasBind) {
              if (hasBind != 1) {
                // that.autoLocation();
                that.setData({
                  unBindBg: true
                });
              }
              that.setData({
                userInfo: userInfo,
                cacheKey: cacheKey,
                hasBind: hasBind
              });
              clearInterval(timer);
            }
            // 商家在中台扫码进入页面才会触发
            // 验证是否配置好支付参数
            if (options.scene && options.scene == 'payvalid') {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/validPayParam",
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
                  if (code = 'E00000') {
                    wx.showModal({
                      content: res.data.message,
                      showCancel: false,
                      success: function (res) {
                        if (res.confirm) {
                          wx.navigateBack({
                            delta: 0
                          })
                        }
                      }
                    })
                  } else {
                    wx.showModal({
                      content: res.data.message,
                      showCancel: false,
                      success: function (res) {
                        if (res.confirm) {
                          wx.navigateBack({
                            delta: 0
                          })
                        }
                      }
                    })
                  }

                }
              })
            }
            if (that.data.hasBind == 1) {
              that.loadData();
            } else {
              wx.hideLoading();
            }

          }
        }, 500);


      });
    })
  },
  onUnload: function () {
    // app.globalData.status = true;
  },
  onShow: function () {
    wx.getSystemInfo({
      success: function (res) {
        var version = res.SDKVersion;
        version = version.replace(/\./g, "")
        if (parseInt(version) > 199) {// 小于1.2.0的版本
          const updateManager = wx.getUpdateManager();
          updateManager.onCheckForUpdate(function (res) {
            // 请求完新版本信息的回调
            console.log(res.hasUpdate)
          })
          updateManager.onUpdateReady(function () {
            wx.showModal({
              title: '更新提示',
              content: '新版本已经准备好，是否马上重启小程序？',
              success: function (res) {
                if (res.confirm) {
                  // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
                  updateManager.applyUpdate()
                }
              }
            })
          })
        }
      }
    });
      
  },
  msgDetail: function () {
    if (this.data.hasBind == 1) {
      wx.navigateTo({
        url: '../msgDetail/msgDetail',
      })
    } else {
      wx.navigateTo({ url: '../oftenUseShop/oftenUseShop' })
    }
  },
  /**
   * 跳转至积分页
   */
  integral: function () {


   
    wx.navigateTo({
      url: '/pages/webIntegral/webIntegral',
    })
    return
 
 
    if (this.data.hasBind == 1) {
      wx.showModal({
        content: '您确定要跳转到其它小程序？',
        success: function (res) {
          if (res.confirm) {
            wx.navigateToMiniProgram({
              appId: 'wx2e6c5ab609666413',
              path: 'pages/index/index',
              extraData: {
                foo: '测试数据传输'
              },
              success(res) {
                // 打开成功
              }
            })
          } else if (res.cancel) {

          }
        }
      })
    } else {
      wx.navigateTo({ url: '../oftenUseShop/oftenUseShop' })
    }


  },
  myAccount(){
    if (this.data.hasBind != 1) {
      wx.navigateTo({
        url: '../oftenUseShop/oftenUseShop',
      })
    } else{
      wx.navigateTo({
        url: '../myAccount/myAccount',
      })
    }
   
  },
  /**
   * 跳转至电子票页面
   */
  eTicket: function () {
    if (this.data.hasBind == 1) {
      wx.navigateTo({
        url: '../myTicket/myTicket',
      })
    } else {
      wx.navigateTo({ url: '../oftenUseShop/oftenUseShop' })
    }
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
