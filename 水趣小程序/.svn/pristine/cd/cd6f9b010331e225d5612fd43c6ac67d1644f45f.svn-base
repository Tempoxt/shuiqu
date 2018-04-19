// pages/addAddress/addAddress.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {},
    city: '',
    tagIndex: null
  },
  //   选择所在位置
  getAddress: function (e) {
    var that = this;
    wx.chooseLocation({
      success: function (res) {
        console.log(res);
        //GCJ-02(火星)转BD-09坐标（百度地图）方法  ，项目统一使用百度坐标
        function MapabcEncryptToBdmap(gg_lat, gg_lon) {

          var point = new Object();
          var x_pi = 3.14159265358979324 * 3000.0 / 180.0;
          var x = new Number(gg_lon);
          var y = new Number(gg_lat);
          var z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
          var theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
          var bd_lon = z * Math.cos(theta) + 0.0065;
          var bd_lat = z * Math.sin(theta) + 0.006;
          point.lng = bd_lon;
          point.lat = bd_lat;
          console.log(point);
          wx.request({
            url: "https://api.map.baidu.com/geocoder/v2/",
            method: 'POST',
            header: {
              'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
              output: 'json',
              ak: 'Xfqil7j1OG6yBLhAoqQKQev4',
              location: point.lat + ',' + point.lng,
              'qq-pf-to': 'pcqq.c2c'
            },
            success: function (res) {
              console.log(res.data.result);
              that.setData({
                streetName: res.data.result.addressComponent.street,
                streetDescribe: res.data.result.addressComponent.city + res.data.result.addressComponent.district,
                location: res.data.result.location.lng + ',' + res.data.result.location.lat
              });
            },
            fail: function (err) {
              wx.showModal({
                content: err,
                showCancel: false
              })
            }
          });
          return point;
        }
        MapabcEncryptToBdmap(res.latitude, res.longitude);
      },
    })


    //   wx.navigateTo({
    //       url: '../map/map',
    //   })
    //   wx.chooseLocation({
    //       success: function (res) {
    //           console.log(res);
    //           var lat = res.latitude;
    //           var lon = res.longitude;
    //           wx.request({
    //               url: app.globalData.domain + "/wxLiteapp/changeWxLocationToBaidu",
    //               method: 'POST',
    //               header: {
    //                   'content-type': 'application/x-www-form-urlencoded'
    //               },
    //               data: {
    //                   cacheKey: that.data.cacheKey,
    //                   c: app.globalData.c,
    //                   lat: lat,
    //                   lon: lon
    //               },
    //               success: function (result) {
    //                   // wx.hideLoading();
    //                   console.log(result);


    //                   that.setData({
    //                       streetName: result.data.street,
    //                       streetDescribe: result.data.city + result.data.district,
    //                       address:
    //                   });

    //               }
    //           })
    //         //   var inputCiy = res.address;
    //         //   if (res.address.length > 15) {
    //         //       res.address = res.address.substr(0, 15) + '...'
    //         //   }
    //         //   that.setData({
    //         //       city: res.address,
    //         //       inputCiy: inputCiy
    //         //   })
    //         //   console.log(inputCiy);
    //       },
    //   })
  },
  //   选择地址标签  家庭/单位
  activeTag: function (e) {
    var tag = e.currentTarget.dataset.val
    console.log(e.currentTarget.dataset.val);
    this.setData({
      tagIndex: e.target.dataset.index,
      tag: tag
    });
  },
  //   保存地址
  saveAddress: function (e) {
    var contacts = this.data.contacts;
    var phone = this.data.phone;
    var streetDescribe = this.data.streetDescribe;
    var streetName = this.data.streetName;
    var doorplate = this.data.doorplate;
    var tag = this.data.tag;
    var location = this.data.location;
    var address = streetDescribe + streetName + doorplate;
    console.log(phone, contacts, streetDescribe, streetName, doorplate, location, tag, address);
    if (!contacts) {
      wx.showToast({
        title: '联系人不能为空',
        image: '../../images/error_popover.png'
      })
      return;
    } else if (!phone) {
      wx.showToast({
        title: '手机号不能为空',
        image: '../../images/error_popover.png'
      })
      return;
    } else if (!streetDescribe) {
      wx.showToast({
        title: '收获地址不能为空',
        image: '../../images/error_popover.png'
      });
      return;
    } else if (!doorplate) {
      wx.showToast({
        title: '详细地址不能为空',
        image: '../../images/error_popover.png'
      });
      return;
    } else if (!tag) {
      wx.showToast({
        title: '请选择地址标签',
        image: '../../images/error_popover.png'
      });
      return;
    }
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/addAddress_v2",
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      data: {
        cacheKey: app.globalData.cacheKey,
        c: app.globalData.c,
        streetName: streetName,
        streetDescribe: streetDescribe,
        address: address,
        doorplate: doorplate,
        phone: phone,
        contacts: contacts,
        location: location,
        sdkType: 'littleApp',
        tag: tag,
        shopId: app.globalData.shopId
      },
      success: function (res) {
        var code = res.data.code;
        if (code == 'E00000') {
          // wx.redirectTo({
          //     url: '../address/address',
          // });
          wx.navigateBack({
            delta: 1
          })
        } else if (code == '-3') {
          app.login(function () {
            wx.request({
              url: app.globalData.domain + "/wxLiteapp/addAddress_v2",
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: app.globalData.cacheKey,
                c: app.globalData.c,
                streetName: streetName,
                streetDescribe: streetDescribe,
                address: address,
                doorplate: doorplate,
                phone: phone,
                contacts: contacts,
                location: location,
                sdkType: 'littleApp',
                tag: tag,
                shopId: app.globalData.shopId
              },
              success: function (res) {
                var code = res.data.code;
                if (code == 'E00000') {
                  // wx.redirectTo({
                  //     url: '../address/address',
                  // });
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
                  content: err,
                  showCancel: false
                })
              }
            });
          });

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
  //   联系人
  inputUsername: function (e) {
    console.log(e.detail.value);
    this.setData({
      contacts: e.detail.value
    });
  },
  //   手机号
  inputTel: function (e) {
    console.log(e.detail.value);
    this.setData({
      phone: e.detail.value
    });
  },
  //   详细地址 
  inputDoorplate: function (e) {
    console.log(e.detail.value);
    this.setData({
      doorplate: e.detail.value
    });
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
        userInfo: userInfo,
        theme: app.globalData.theme
      })
    });
    //   获取地址标签   家庭/单位
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/getAddressTags",
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
        wx.setStorageSync('isRefeshAddress', 1);
        if (code == 'E00000') {
          that.setData({
            tags: res.data.data.tags
          });
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
    //   获取用户手机号
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/getUserCellphone",
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
          that.setData({
            phone: res.data.data
          });
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