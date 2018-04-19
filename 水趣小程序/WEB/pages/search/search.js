// pages/search/search.js

// 引用百度地图微信小程序JSAPI模块 
var bmap = require('../../utils/bmap-wx.min.js');
const app = getApp();
Page({
  data: {
    userInfo: {},
    currentCity: "定位中...",
    address: "",
    cityList: "",
    sugData: '',
    shopList: null,
    hasBind: -3,
    modalWrap: "",
    modal: "",
    getCodeText: "获取验证码",
    getCodeColor: "#EECE1F"
  },
  onLoad: function () {
    var that = this;
    //调用应用实例的方法获取全局数据  
    app.getUserInfo(function (userInfo) {
      that.setData({
        theme: app.globalData.theme
      });
      //更新数据
      var computed = 0;
      var timer = setInterval(function () {
        computed++;
        if (!app.globalData.cacheKey) {
          if (computed == 20) {
            console.log(computed);
            clearInterval(timer);
            wx.showModal({
              content: '网络超时',
              showCancel: false
            })
          }
        } else {
          clearInterval(timer);
          var cacheKey = app.globalData.cacheKey;
          var hasBind = app.globalData.hasBind;
          app.globalData.isOnLoad = 0;
          that.setData({
            userInfo: userInfo,
            cacheKey: cacheKey,
            hasBind: hasBind
          });


          that.autoLocation();
        }
      }, 500);

    });
  },
  onShow: function () {


  },
  /**
   * 顶部选择城市
   */
  topGetCity: function () {
    var that = this;
    wx.showLoading({
      title: '加载中...',
    })
    // 获取企业对应的水店城市列表
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
        if (code == 'E00000') {
          var result = res.data.data;

          var cityList = [];

          for (var i = 0; i < result.length; i++) {
            cityList[i] = result[i].cityName;
          }
          console.log(result);

          wx.showActionSheet({
            itemList: cityList,
            success: function (res) {

              console.log(result[res.tapIndex]);
              var cityId = result[res.tapIndex].cityId;
              var location = result[res.tapIndex].location;
              var cityName = result[res.tapIndex].cityName;

              that.closeCityList();
              that.setData({
                currentCity: cityName,
                location: location
              });

            }


          });
          wx.hideLoading();
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
                if (code == 'E00000') {
                  var result = res.data.data;

                  var cityList = [];

                  for (var i = 0; i < result.length; i++) {
                    cityList[i] = result[i].cityName;
                  }
                  console.log(result);

                  wx.showActionSheet({
                    itemList: cityList,
                    success: function (res) {

                      console.log(result[res.tapIndex]);
                      var cityId = result[res.tapIndex].cityId;
                      var location = result[res.tapIndex].location;
                      var cityName = result[res.tapIndex].cityName;

                    },

                  });
                  wx.hideLoading();
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
  /**
   * 选择城市
   */
  getCity: function () {

  },
  closeCityList: function () {
    this.setData({
      cityList: false
    });
  },
  /**
   * 打开页面自动定位
   */
  autoLocation: function () {
    wx.showLoading({
      title: '加载中...',
    });
    var that = this;
    var BMap = new bmap.BMapWX({
      ak: 'CFecNbZbabHRfdW8QCz40xqkvrNrB3Bu'
    });
    var fail = function (err) {
      wx.showModal({
        content: err,
        showCancel: false
      })
    }
    var success = function (res) {
      var wxMarkerData = res.wxMarkerData;
      if (wxMarkerData) {
        var latitude = wxMarkerData[0].latitude;
        var longitude = wxMarkerData[0].longitude;
        // var speed = res.speed
        // var accuracy = res.accuracy
        wx.request({
          url: app.globalData.domain + "/wxLiteapp/changeWxLocationToBaidu",
          method: 'POST',
          header: {
            'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
            cacheKey: that.data.cacheKey,
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
              that.setData({
             
                address: res.data.district + res.data.street
              });
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
                  if (code == 'E00000') {
                    var result = res.data.data;

                    // var cityList = [];

                    for (var i = 0; i < result.length; i++) {
                      if (city == result[i].cityName) {
                     
                        that.setData({
                          noService: false,
                          currentCity: city,
                        });
                      } else if (i == result.length-1){
                        that.setData({
                          currentCity: result[0].cityName,
                        })
                      }

                    }
                    // if(!that.data.cityFlag){
                    //   that.setData({
                    //     currentCity: result[0].cityName,
                    //     address: res.data.district + res.data.street
                    //   });
                    // }
                    console.log(result);
                    // console.log(cityList);


                    wx.hideLoading();
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
                          if (code == 'E00000') {
                            var result = res.data.data;

                            for (var i = 0; i < result.length; i++) {
                              if (that.data.currentCity == result[i].cityName) {
                                that.setData({
                                  noService: false
                                });
                              }

                            }
                            console.log(result);


                            wx.hideLoading();
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
              // 如果定位的地址在地址列表内才去搜索附近的水店
              
                wx.request({
                  url: app.globalData.domain + "/wxLiteapp/nearbyShops",
                  method: 'POST',
                  header: {
                    'content-type': 'application/x-www-form-urlencoded'
                  },
                  data: {
                    cacheKey: that.data.cacheKey,
                    c: app.globalData.c,
                    location: longitude + "," + latitude
                  },
                  success: function (res) {
                    console.log({
                      cacheKey: that.data.cacheKey,
                      c: app.globalData.c,
                      location: longitude + "," + latitude
                    })


                    console.log(res)



                    wx.hideLoading();
                    var code = res.data.code;
                    if (code == 'E00000') {
                      var shops = res.data.data.shops;
                      shops.forEach(function (item) {

                        if (item.distance >= 1000) {
                          item.distance = (item.distance / 1000).toFixed(2) + 'km';
                        } else {
                          item.distance = (item.distance).toFixed(0) + 'm';
                        }

                      });

                      that.setData({
                        shopList: shops
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
                })
              
              
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

    };
    // 发起regeocoding检索请求 
    BMap.regeocoding({
      fail: fail,
      success: success,

    });
  },
  /**
   * 重新定位
   */
  locationAgain: function () {
    var that = this;
    var BMap = new bmap.BMapWX({
      ak: 'CFecNbZbabHRfdW8QCz40xqkvrNrB3Bu'
    });
    this.data.hasBind == 1 ?
      wx.getLocation({
        type: 'wgs84',
        success: function (res) {
          wx.showLoading({
            title: '加载中...',
          });
          var latitude = res.latitude
          var longitude = res.longitude
          // var speed = res.speed
          // var accuracy = res.accuracy
          // console.log(res);
          wx.request({
            url: app.globalData.domain + "/wxLiteapp/changeWxLocationToBaidu",
            method: 'POST',
            header: {
              'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
              cacheKey: that.data.cacheKey,
              c: app.globalData.c,
              lat: latitude,
              lon: longitude,
              source: 1
            },
            success: function (result) {
              console.log(result)
              // wx.hideLoading();
              console.log(result);
              //去除城市中的市字
              var city = result.data.city.substr(0, result.data.city.lastIndexOf('市'));

              that.setData({
                currentCity: city,
                address: result.data.district + result.data.street
              });
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/nearbyShops",
                method: 'POST',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  cacheKey: that.data.cacheKey,
                  c: app.globalData.c,
                  location: longitude + "," + latitude
                },
                success: function (result) {
                  var shops = result.data.data.shops;
                  shops.forEach(function (item) {

                    if (item.distance >= 1000) {
                      item.distance = (item.distance / 1000).toFixed(2) + 'km';
                    } else {
                      item.distance = (item.distance).toFixed(0) + 'm';
                    }

                  });

                  that.setData({
                    shopList: result.data.data.shops
                  });
                  wx.hideLoading();
                }
              })
            }
          })
        }
      }) : this.openModal();
  },
  baiduMap: function (streetName) {
    // https://api.map.baidu.com/place/v2/search?query=%E8%B6%8A%E7%A7%80%E5%85%AC%E5%9B%AD&region=%E5%B9%BF%E5%B7%9E&output=json&ak=CFecNbZbabHRfdW8QCz40xqkvrNrB3Bu

    if (streetName) {
      var that = this;
      wx.request({
        url: "https://api.map.baidu.com/place/v2/search",
        method: 'GET',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          query: streetName,
          region: that.data.currentCity,
          output: 'json',
          ak: 'CFecNbZbabHRfdW8QCz40xqkvrNrB3Bu'
        },
        success: function (res) {
          var status = res.data.status;
          console.log(status);
          if (status == '0') {
            console.log(res.data.results);
            for (var i = 0; i < res.data.results.length; i++) {
              var sugData = res.data.results[i].name;
            }
            that.setData({
              sugData: res.data.results
            });
          } else {
            wx.showModal({
              content: res.data.message
            })
          }

        }
      })
    }

  },
  /**
   * 手动搜索附近水店
   */
  inputHandler: function (e) {
    var that = this;
    var inputHandler = e.detail.value;
    this.setData({
      inputHandler: inputHandler
    });
  },
  search: function (e) {
    var inputHandler = this.data.inputHandler;
    this.baiduMap(inputHandler)
    // this.data.hasBind == 1 ? this.baiduMap(inputHandler) : this.openModal();
  },
  // 进入水店
  goShop: function (e) {
    var shopId = e.currentTarget.dataset.shopid;
    console.log(shopId);
    wx.redirectTo({
      url: '/pages/products/products?shopId=' + shopId,
    })
    // this.data.hasBind == 1 ?
    //     wx.redirectTo({
    //         url: '/pages/products/products?shopId='+shopId,
    //     }) : this.openModal();

  },
  /**
   * 选择搜索结果，获取附近水店
   */
  searchNearby: function (e) {
    // console.log(111);
    var that = this;
    var city = e.currentTarget.dataset.city;
    var district = e.currentTarget.dataset.district;
    var street = e.currentTarget.dataset.street;
    var latitude = e.currentTarget.dataset.lat;
    var longitude = e.currentTarget.dataset.lng;
    console.log(e);
    that.setData({
      address: district + street,
      sugData: ''
    });
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/changeWxLocationToBaidu",
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      data: {
        cacheKey: that.data.cacheKey,
        c: app.globalData.c,
        lat: latitude,
        lon: longitude,
        source: 1
      },
      success: function (result) {
        // wx.hideLoading();
        console.log(result);
        //去除城市中的市字
        var city = result.data.city.substr(0, result.data.city.lastIndexOf('市'));

        that.setData({
          currentCity: city,
          address: result.data.district + result.data.street
        });
        wx.request({
          url: app.globalData.domain + "/wxLiteapp/nearbyShops",
          method: 'POST',
          header: {
            'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
            cacheKey: that.data.cacheKey,
            c: app.globalData.c,
            location: longitude + "," + latitude
          },
          success: function (res) {
            wx.hideLoading();
            var code = res.data.code;
            if (code == 'E00000') {
              var shops = res.data.data.shops;
              shops.forEach(function (item) {

                if (item.distance >= 1000) {
                  item.distance = (item.distance / 1000).toFixed(2) + 'km';
                } else {
                  item.distance = (item.distance).toFixed(0) + 'm';
                }

              });
              that.setData({
                shopList: shops
              });
              console.log(res.data.data.shops);
            } else {
              wx.hideLoading();
              wx.showModal({
                content: res.data.message,
                showCancel: false
              })
            }
          }
        })
      }
    })
    // this.data.hasBind == 1 ?
    //     wx.request({
    //         url: app.globalData.domain + "/wxLiteapp/changeWxLocationToBaidu",
    //         method: 'POST',
    //         header: {
    //             'content-type': 'application/x-www-form-urlencoded'
    //         },
    //         data: {
    //             cacheKey: that.data.cacheKey,
    //             c: app.globalData.c,
    //             lat: latitude,
    //             lon: longitude,
    //             source:1
    //         },
    //         success: function (result) {
    //             // wx.hideLoading();
    //             console.log(result);
    //             //去除城市中的市字
    //             var city = result.data.city.substr(0, result.data.city.lastIndexOf('市'));

    //             that.setData({
    //                 currentCity: city,
    //                 address: result.data.district + result.data.street
    //             });
    //             wx.request({
    //                 url: app.globalData.domain + "/wxLiteapp/nearbyShops",
    //                 method: 'POST',
    //                 header: {
    //                     'content-type': 'application/x-www-form-urlencoded'
    //                 },
    //                 data: {
    //                     cacheKey: that.data.cacheKey,
    //                     c: app.globalData.c,
    //                     location: longitude + "," +latitude
    //                 },
    //                 success: function (res) {
    //                     wx.hideLoading();
    //                     var code = res.data.code;
    //                     if (code == 'E00000') {
    //                         var shops = res.data.data.shops;
    //                         shops.forEach(function (item) {

    //                             if (item.distance >= 1000) {
    //                                 item.distance = (item.distance / 1000).toFixed(2) + 'km';
    //                             } else {
    //                                 item.distance = (item.distance).toFixed(0) + 'm';
    //                             }

    //                         });
    //                         that.setData({
    //                             shopList: shops
    //                         });
    //                         console.log(res.data.data.shops);
    //                     } else {
    //                         wx.hideLoading();
    //                         wx.showModal({
    //                             content: res.data.message,
    //                             showCancel: false
    //                         })
    //                     }
    //                 }
    //             })
    //         }
    //     }) : this.openModal();


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