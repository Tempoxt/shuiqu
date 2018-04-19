// pages/products/products.js
const app = getApp();

Page({
  data: {
    hasBind: -2,
    currentCity: "选城市",
    modalWrap: "",
    modal: "",
    getCodeText: "获取验证码",
    getCodeColor: "linear-gradient(to right,#F6CC47,#FFAD52)",
    selectStatus: true,
    userInfo: {},
    shopProducts:null,//购物车数据
    toView: '0',
    offsetTop: [],
    totalPrice: 0,// 总价格
    totalCount: 0, // 总商品数
    carArray: [],
    cartShow: 'none',
    status: 0,
    shopId: '',
    cats: 0,
    shopMap: '',
    evaluateList: '',
    getCodeColor: "",
    commitColor: "#ddd",
    fontColor: "#666",
    textColor: "#fff",
    getCodeStatus: false,
    bindStatus: true
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
    var that = this;
    this.setData({
      modalWrap: true,
      modal: true
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
            that.setData({ currentCity: '定位中...' })
            wx.getLocation({
              type: 'wgs84',
              success: function (res) {
                var latitude = res.latitude
                var longitude = res.longitude
                var speed = res.speed
                var accuracy = res.accuracy

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
                      for (var i = 0; i < result.length; i++) {
                        if (that.data.currentCity == result[i].cityName) {
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
                          location: location,
                          cityId: cityId,
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
  /**
   * 获取验证码
   */
  getCode: function (e) {
    var regPhone = /^1[3|4|5|7|8][0-9]{9}$/;
    //校验手机号失败
    if (this.data.phoneNum == "") {
      wx.showToast({
        title: '手机号不能为空',
        image: '../../images/error_popover.png'
      });
    }
    else if (!regPhone.test(this.data.phoneNum)) {
      wx.showToast({
        title: '手机号不正确',
        image: '../../images/error_popover.png'
      });
    } else {
      var that = this;
      function timeOver() {
        if (that.data.getCodeText == 0) {
          clearInterval(Countdown);
          that.setData({
            getCodeText: "获取验证码",
            getCodeColor: "",
            textColor: "#fff",
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
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          cellphone: that.data.phoneNum
        },
        success: function (res) {
          var code = res.data.code;
          if (code == "E00000") {
            console.log(res);
          } else if (code == "-3") {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/moblieMsg",
                method: 'POST',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  cellphone: that.data.phoneNum
                },
                success: function (res) {
                  var code = res.data.code;
                  if (code == "E00000") {
                    console.log(res);
                  } else {
                    wx.showModal({
                      content: res.data.message,
                      showCancel: false
                    })
                  }

                }
              })
            });
          } else {
            wx.showModal({
              content: res.data.message,
              showCancel: false
            })
          }

        }
      })
      //验证码倒计时，改变状态，不可点击
      this.setData({
        getCodeColor: "#ddd",
        getCodeText: "60",
        getCodeStatus: true,
        textColor: "#666"
      });
      var Countdown = setInterval(function () {
        that.setData({
          getCodeText: that.data.getCodeText - 1,
        });
        timeOver();
      }, 1000)
    }
  },
  /**
   * 获取用户输入手机号并去除所有空格
   */
  inputPhoneNum: function (e) {
    var that = this;

    this.setData({
      phoneNum: e.detail.value.replace(/\s+/g, "")
    });
    if (this.data.phoneNum && this.data.validateCode && this.data.selectStatus) {
      if (this.data.theme == 1) {
        this.setData({
          commitColor: "linear-gradient(to right,#F6CC47,#FFAD52)",
          fontColor: "#fff",
          bindStatus: false
        });
      } else if (this.data.theme == 2) {
        this.setData({
          commitColor: "linear-gradient(to right,#7b00d3,#b25aff)",
          fontColor: "#fff",
          bindStatus: false
        });
      } else if (this.data.theme == 3) {
        this.setData({
          commitColor: "linear-gradient(to right,#cf1731,#ff4060)",
          fontColor: "#fff",
          bindStatus: false
        });
      } else if (this.data.theme == 4) {
        this.setData({
          commitColor: "linear-gradient(to right,#15a0ff,#3ebaff)",
          fontColor: "#fff",
          bindStatus: false
        });
      } else if (this.data.theme == 5) {
        this.setData({
          commitColor: "linear-gradient(to right,#2cc136,#5de04f)",
          fontColor: "#fff",
          bindStatus: false
        });
      }


    } else {
      this.setData({
        commitColor: "#ddd",
        fontColor: "#666",
        bindStatus: true
      });

    }
    console.log(this.data.phoneNum);
  },
  // 自动定位
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
  /**
   * 获取用户输入的验证码并去除所有空格
   */
  inputValidateCode: function (e) {
    this.setData({
      validateCode: e.detail.value.replace(/\s+/g, "")
    });
    if (this.data.phoneNum && this.data.validateCode && this.data.selectStatus) {
      if (this.data.theme == 1) {
        this.setData({
          commitColor: "linear-gradient(to right,#F6CC47,#FFAD52)",
          fontColor: "#fff",
          bindStatus: false
        });
      } else if (this.data.theme == 2) {
        this.setData({
          commitColor: "linear-gradient(to right,#7b00d3,#b25aff)",
          fontColor: "#fff",
          bindStatus: false
        });
      } else if (this.data.theme == 3) {
        this.setData({
          commitColor: "linear-gradient(to right,#cf1731,#ff4060)",
          fontColor: "#fff",
          bindStatus: false
        });
      } else if (this.data.theme == 4) {
        this.setData({
          commitColor: "linear-gradient(to right,#15a0ff,#3ebaff)",
          fontColor: "#fff",
          bindStatus: false
        });
      } else if (this.data.theme == 5) {
        this.setData({
          commitColor: "linear-gradient(to right,#2cc136,#5de04f)",
          fontColor: "#fff",
          bindStatus: false
        });
      }


    } else {
      this.setData({
        commitColor: "#ddd",
        fontColor: "#666",
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

    if (this.data.phoneNum && this.data.validateCode && this.data.selectStatus) {
      if (this.data.theme == 1) {
        this.setData({
          commitColor: "linear-gradient(to right,#F6CC47,#FFAD52)",
          fontColor: "#fff",
          bindStatus: false
        });
      } else if (this.data.theme == 2) {
        this.setData({
          commitColor: "linear-gradient(to right,#7b00d3,#b25aff)",
          fontColor: "#fff",
          bindStatus: false
        });
      } else if (this.data.theme == 3) {
        this.setData({
          commitColor: "linear-gradient(to right,#cf1731,#ff4060)",
          fontColor: "#fff",
          bindStatus: false
        });
      } else if (this.data.theme == 4) {
        this.setData({
          commitColor: "linear-gradient(to right,#15a0ff,#3ebaff)",
          fontColor: "#fff",
          bindStatus: false
        });
      } else if (this.data.theme == 5) {
        this.setData({
          commitColor: "linear-gradient(to right,#2cc136,#5de04f)",
          fontColor: "#fff",
          bindStatus: false
        });
      }


    } else {
      this.setData({
        commitColor: "#ddd",
        fontColor: "#666",
        bindStatus: true
      });

    }
  },
  getPhoneNumber: function (e) {
    // console.log(e.detail.errMsg)
    var that = this;
    var iv = e.detail.iv;
    var encryptedData = e.detail.encryptedData;
    console.log(encryptedData);
    // 取消授权
    if (e.detail.errMsg == 'getPhoneNumber:fail user deny') {

    } else {
      wx.showLoading({
        title: '加载中...',
      })
      // 确认授权
      var cityId = that.data.cityId;
      var location = that.data.location;
      // 没获取到城市id和坐标，默认传0
      if (!location) {
        var cityId = 0;
        var location = 0;
      }
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/getCellphone",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          encryptedData: encryptedData,
          iv: iv,
          cityId: cityId,
          location: location
        },
        success: function (res) {
          console.log(res);
          var code = res.data.code;
          if (code == "E00000") {
            app.loginData=null;
            wx.hideLoading();
            app.globalData.hasBind = 1;
            that.setData({
              hasBind: 1,
              blurBg: false,
              unBindBg: false,
              minActiveImg: false
            });

          } else if (code == "-3") {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/getCellphone",
                method: 'POST',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  encryptedData: encryptedData,
                  iv: iv,
                  cityId: that.data.cityId,
                  location: that.data.location
                },
                success: function (res) {
                  console.log(res);
                  var code = res.data.code;
                  if (code == "E00000") {
                    wx.hideLoading();
                    app.globalData.hasBind = 1;
                    that.setData({
                      hasBind: 1,
                      blurBg: false,
                      unBindBg: false
                    });
                  } else if (code == "-3") {
                    wx.hideLoading();
                    wx.showModal({
                      showCancel: false,
                      content: res.data.message
                    })
                  }

                },
                fail: function (err) {
                  wx.hideLoading();
                  wx.showModal({
                    title: '',
                    showCancel: false,
                    content: "网络异常！"
                  })
                }
              })
            });

          }

        },
        fail: function (err) {
          wx.hideLoading();
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
   * 绑定手机号
   */
  commit: function (e) {
    var that = this;
    var regPhone = /^1[3|4|5|7|8][0-9]{9}$/;
    //校验手机号失败
    if (!regPhone.test(this.data.phoneNum)) {
      wx.showToast({
        title: '手机号不正确',
        image: '../../images/error_popover.png'
      })
    } else {
      var that = this;
      if (that.data.currentCity != "选城市" && that.data.currentCity != "") {
        wx.request({
          url: app.globalData.domain + "/wxLiteapp/saveBind",
          method: 'POST',
          header: {
            'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
            cacheKey: app.globalData.cacheKey,
            c: app.globalData.c,
            cellphone: that.data.phoneNum,
            code: that.data.validateCode
          },
          success: function (result) {
            console.log(result);
            if (result.data.code != "E00000") {
              wx.showModal({
                showCancel: false,
                content: result.data.message
              })
            } else {
              if (result.data.data.isBindExistsAccount) {
                wx.showModal({
                  showCancel: false,
                  content: "绑定成功"
                })
              } else {
                wx.showModal({
                  showCancel: false,
                  content: "注册成功"
                })
              }
              that.setData({
                hasBind: 1,
                blurBg: false,
                unBindBg: false,
                minActiveImg: false
              });
              app.globalData.hasBind = 1;
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
      } else {
        wx.showToast({
          title: '请选择城市',
          image: '../../images/error_popover.png'
        })
      }


    }

  },
  /**
   * 选择城市
   */
  getCity: function () {
    var that = this;
    if (this.data.oneCity) {
      console.log('城市列表中有定位的城市');
    } else {
      wx.showLoading({
        title: '加载中...',
      })
      this.setData({
        shadeOn: true
      })
    
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
                //保存城市坐标
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
                        location: location
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
                      showCancel: false,
                      content: err
                    })
                  }
                });
              },
              fail: function (res) {
                console.log(res.errMsg)
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
                        //保存城市坐标
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
                                location: location
                              });
                            } else {
                              wx.showModal({
                                content: res.data.message,
                                showCancel: false
                              })
                            }

                          }, fail: function (err) {
                            wx.showModal({
                              showCancel: false,
                              content: err
                            })
                          }
                        });
                      },
                      fail: function (res) {
                        console.log(res.errMsg)
                      }
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
    }

  },
  closeCityList: function () {
    this.setData({
      cityList: false
    });
  },
  /**
   * 选择商品分类菜单
   */
  selectMenu: function (e) {
    var index = e.currentTarget.dataset.itemIndex;
    this.setData({
      toView: 'order' + index.toString(),
      cats: index.toString()
    })
    console.log(this.data.toView);
  },
  //移除商品
  decreaseCart: function (e) {

    var that = this;
    var parentIndex = e.currentTarget.dataset.parentindex;
    var itemIndex = e.currentTarget.dataset.itemIndex;
    //判断是否登录
    var hasBind = app.globalData.hasBind;
    if (hasBind != 1) {
      that.openModal();
      return;
    }
    //当前商品
    var pid = this.data.shopProducts[parentIndex].products[itemIndex].pid;
    var shopCarData = [];
    shopCarData.push({
      shopId: that.data.shopId,
      pid: pid,
      number: that.data.shopProducts[parentIndex].products[itemIndex].number - 1,
      settleStyle: "现金"
    });
    for (var i = 0, len = that.data.shopProducts.length; i < len; i++) {
      for (var j = 0; j < that.data.shopProducts[i].products.length; j++) {
        if (pid == that.data.shopProducts[i].products[j].pid) {
          that.data.shopProducts[i].products[j].number--;

        }
      }
    }
    //商品数量为0

    if (shopCarData[0].number == 0) {


      wx.request({
        url: app.globalData.domain + "/wxLiteapp/editShopCart",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          tag: 'DEL',
          products: JSON.stringify(shopCarData)
        },
        success: function (res) {
          var code = res.data.code;
          if (code == 'E00000') {
            console.log(res);
          } else if (code == '-3') {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/editShopCart",
                method: 'POST',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  tag: 'DEL',
                  products: JSON.stringify(shopCarData)
                },
                success: function (res) {
                  var code = res.data.code;
                  if (code == 'E00000') {
                    console.log(res);
                  } else {
                    wx.hideLoading();
                    wx.showModal({
                      content: res.data.message,
                      showCancel: false
                    })
                  }

                },
                fail: function (err) {
                  wx.hideLoading();
                  wx.showModal({
                    showCancel: false,
                    content: err
                  })
                }
              });
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
          wx.hideLoading();
          wx.showModal({
            showCancel: false,
            content: err
          })
        }
      });
    } else {

      wx.request({
        url: app.globalData.domain + "/wxLiteapp/editShopCart",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          products: JSON.stringify(shopCarData)
        },
        success: function (res) {
          var code = res.data.code;
          if (code == 'E00000') {
            console.log(res);
          } else if (code == '-3') {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/editShopCart",
                method: 'POST',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  products: JSON.stringify(shopCarData)
                },
                success: function (res) {
                  var code = res.data.code;
                  if (code == 'E00000') {
                    console.log(res);
                  } else {
                    wx.showModal({
                      content: res.data.message,
                      showCancel: false
                    })
                  }

                },
                fail: function (err) {
                  wx.showModal({
                    showCancel: false,
                    content: err
                  })
                }
              });
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
            showCancel: false,
            content: err
          })
        }
      });
    };
    //商品总数
    that.data.totalCount--;
    //获取缓存数据

    this.setData({
      shopProducts: this.data.shopProducts,
      totalCount: this.data.totalCount--
    })

  },
  // 商品详情页
  productDetail: function (e) {
    var that = this;
    var pid = e.currentTarget.dataset.pid;
    var shopId = e.currentTarget.dataset.shopid;

    if (this.data.hasBind != 1) {
      that.openModal();
    } else {
      wx.navigateTo({
        url: '../productDetail/productDetail?shopId=' + shopId + '&pid=' + pid
      })
    }

  },
  //添加商品
  addCart(e) {

    var that = this;
    var parentIndex = e.currentTarget.dataset.parentindex;
    var itemIndex = e.currentTarget.dataset.itemIndex;
    //判断是否登录
    var hasBind = app.globalData.hasBind;
    console.log(hasBind)
    if (hasBind != 1) {
      // that.openModal();
      that.setData({
        unBindBg: true
      })
      return;
    }
    //当前商品
    var pid = this.data.shopProducts[parentIndex].products[itemIndex].pid;
    var shopCarData = [];
    shopCarData.push({
      shopId: that.data.shopId,
      pid: pid,
      number: that.data.shopProducts[parentIndex].products[itemIndex].number + 1,
      settleStyle: "现金"
    });
    for (var i = 0, len = that.data.shopProducts.length; i < len; i++) {
      for (var j = 0; j < that.data.shopProducts[i].products.length; j++) {
        if (pid == that.data.shopProducts[i].products[j].pid) {
          that.data.shopProducts[i].products[j].number++
        }
      }
    }

    //商品总数
    this.data.totalCount++;

    //获取缓存数据

    this.setData({
      shopCarData: shopCarData,
      shopProducts: this.data.shopProducts,
      totalCount: this.data.totalCount++
    });
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/editShopCart",
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      data: {
        cacheKey: app.globalData.cacheKey,
        c: app.globalData.c,
        products: JSON.stringify(shopCarData)
      },
      success: function (res) {
        var code = res.data.code;
        if (code == 'E00000') {
          console.log(res);
        } else if (code == '-3') {
          app.login(function () {
            wx.request({
              url: app.globalData.domain + "/wxLiteapp/editShopCart",
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: app.globalData.cacheKey,
                c: app.globalData.c,
                products: JSON.stringify(shopCarData)
              },
              success: function (res) {
                var code = res.data.code;
                if (code == 'E00000') {
                  console.log(res);
                } else {
                  wx.showModal({
                    content: res.data.message,
                    showCancel: false
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
          showCancel: false,
          content: err
        })
      }
    })
  },
  //結算
  pay() {
    var that = this;

    if (this.data.totalCount > 0) {

      //判断是否登录
      var hasBind = app.globalData.hasBind;
      if (hasBind != 1) {
        that.openModal();
        return;
      } else {
        wx.navigateTo({
          url: '../shopCart/shopCart?shopId=' + that.data.shopId
        });
      }

    } else {
      return;
    }

  },
  // 返回首页
  goHome: function () {
    // wx.navigateTo({
    //     url: '../index/index',
    // })
    wx.reLaunch({
      url: '../index/index',
    })
  },
  // 分享
  share: function () {
    var that = this;
    wx.showShareMenu({
      withShareTicket: true,
      success: function (res) {
        console.log(res);
      }
    })

  },
  /**
   * 切换选项卡
   */
  tabChange: function (e) {
    var showtype = e.target.dataset.type;
    var that = this;
    this.setData({
      status: showtype,
    });
    //评价
    if (showtype == 1) {
      var shopId = that.data.shopId;
      var evaluateId = 0;
      if (that.data.evaluateList) {
        return;
      } else {
        that.getEvaluate(shopId, evaluateId);
      }
      //商家    
    } else if (showtype == 2) {
      if (that.data.shopMap) {
        return;
      } else {
        wx.showLoading({
          title: '加载中...',
        });
        that.getShopInfo();
      }

    }
  },
  /**
   * 拨打水店电话
   */
  callShop: function (e) {
    var phoneNumber = e.currentTarget.dataset.tel;
    console.log(e.currentTarget.dataset.tel);
    wx.makePhoneCall({
      phoneNumber: phoneNumber
    })
  },
  /***
   * 查看资质证照
   */
  previewlmg: function (e) {
    var that = this;
    let arrCode=  that.data.shopMap.publicWxCode.split(',')
    console.log(that.data.shopMap)
    wx.previewImage({
      current: arrCode[0], // 当前显示图片的http链接
      urls: arrCode // 需要预览的图片http链接列表
    })
  },
  onLoad: function (options) {

    console.log('转发参数');
    console.log(options);
    console.log('二维码参数');
    console.log(options.scene);

    wx.showLoading({
      title: '加载中...',
    });
    this.setData({
      options: options,
    })
    //调用应用实例的方法获取全局数据  
    this._initData();
  },
  _initData() {
    var that = this;
    let options = this.data.options;
    app.login().then(function (res) {
  
    app.getUserInfo(function (userInfo) {
      that.setData({
        theme: app.globalData.theme
      });
      console.log('getUserInfo');
      var cacheKey = app.globalData.cacheKey;
      var computed = 0;
      var timer = setInterval(function () {
        computed++;
        if (!app.globalData.cacheKey) {
          console.log(computed);
          if (computed == 20) {
            clearInterval(timer);
            wx.showModal({
              content: '网络超时',
              showCancel: false
            })
          }
        } else {
          console.log('获取二维码');
          clearInterval(timer);
          //获取二维码携带的参数
          if (options.scene) {
            var shopCode = decodeURIComponent(options.scene);
            console.log('shopCode');
            // 保存最后一次浏览的水店Id
            //把参数转成shopId
            wx.request({
              url: app.globalData.domain + "/wxLiteapp/saveLastScanShopId",
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: app.globalData.cacheKey,
                c: app.globalData.c,
                shopCode: shopCode
              },
              success: function (res) {
                console.log('saveLastScanShopId');
                console.log(res.data);
                var code = res.data.code;
                if (code == 'E00000') {
                  //更新数据
                  var shopId = res.data.data.shopId;
                  // app.globalData.shopId = shopId;

                  var cacheKey = app.globalData.cacheKey;
                  var hasBind = app.globalData.hasBind;
                  if (cacheKey && hasBind) {
                    that.setData({
                      userInfo: userInfo,
                      cacheKey: cacheKey,
                      hasBind: hasBind,
                      shopId: shopId
                    })

                    that.getshopList(shopId, cacheKey);
                  }

                } else if (code == '-3') {
                  app.login(function () {
                    wx.request({
                      url: app.globalData.domain + "/wxLiteapp/saveLastScanShopId",
                      method: 'POST',
                      header: {
                        'content-type': 'application/x-www-form-urlencoded'
                      },
                      data: {
                        cacheKey: app.globalData.cacheKey,
                        c: app.globalData.c,
                        shopCode: shopCode
                      },
                      success: function (res) {
                        console.log('第二次saveLastScanShopId');
                        console.log(res.data);
                        var code = res.data.code;
                        if (code == 'E00000') {
                          //更新数据
                          var shopId = res.data.data.shopId;
                          // app.globalData.shopId = shopId;

                          var cacheKey = app.globalData.cacheKey;
                          var hasBind = app.globalData.hasBind;

                          that.setData({
                            userInfo: userInfo,
                            cacheKey: cacheKey,
                            hasBind: hasBind,
                            shopId: shopId
                          })

                          that.getshopList(shopId, cacheKey);
                        } else {
                          wx.showModal({
                            showCancel: false,
                            content: res.data.message
                          })
                        }

                      },
                      fail: function (err) {
                        wx.hideLoading();
                        wx.showModal({
                          showCancel: false,
                          content: err
                        })
                      }
                    })
                  });

                } else if (code == 'C00087') {
                  wx.reLaunch({
                    url: '../inactive/inactive',
                  })
                } else {
                  wx.showModal({
                    content: res.data.message,
                    showCancel: false
                  })
                }

              },
              fail: function (err) {
                wx.hideLoading();
                wx.showModal({
                  showCancel: false,
                  content: err
                })
              }
            })

          } else {
            var shopId = options.shopId || app.globalData.shopId;

            console.log('从上个页面进入或转发进入');
            console.log('缓存中的shopId');
            console.log(app.globalData.shopId);
            console.log('参数中的shopId');
            console.log(options.shopId);
            // var shopId = 'MD0000001';
            // app.globalData.shopId = shopId;
            //更新数据
            var cacheKey = app.globalData.cacheKey;
            var hasBind = app.globalData.hasBind;

            that.setData({
              userInfo: userInfo,
              cacheKey: cacheKey,
              hasBind: hasBind,
              shopId: shopId
            })

            that.getshopList(shopId, cacheKey);
          }
          //如果是通过群分享进入
          if (app.globalData.scene == 1008 && app.globalData.shareClientId && app.globalData.shareShopId) {
            console.log('通过群分享进入');
            console.log(options);
            wx.request({
              url: app.globalData.domain + "/wxLiteapp/saveShareShopVisitLog",
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: app.globalData.cacheKey,
                c: app.globalData.c,
                shopId: app.globalData.shareShopId,
                clientId: app.globalData.shareClientId
              },
              success: function (res) {
                var code = res.data.code;
                if (code == 'E00000') {
                  console.log('保持分享的shopId成功');
                } else if (code == '-3') {
                  console.log('saveShareShopLog状态码为-3');
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
        }
      }, 500);

      wx.getSystemInfo({
        success: function (res) {
          that.setData({
            winWidth: res.windowWidth,
            winHeight: res.windowHeight
          });
        }

      });
    });
    })
  },
  // 重新刷新数据
  // refresh: function () {
  //   wx.showLoading({
  //     title: '加载中...',
  //   });
  //   var that = this;
  //   app.getUserInfo(function (userInfo) {
  //     var shopId = that.data.shopId || app.globalData.shopId;
  //     // var shopId = that.data.shopId;
  //     console.log("分享数据", shopId)

  //     //更新数据
  //     var cacheKey = app.globalData.cacheKey;
  //     var hasBind = app.globalData.hasBind;
  //     that.setData({
  //       userInfo: userInfo,
  //       cacheKey: cacheKey,
  //       hasBind: hasBind,
  //       shopId: shopId
  //     })
  //     that.getshopList(shopId, cacheKey);


  //     wx.getSystemInfo({
  //       success: function (res) {
  //         that.setData({
  //           winWidth: res.windowWidth,
  //           winHeight: res.windowHeight
  //         });
  //       }

  //     });
  //   });
  // },
  /**
   * 获取水店列表
   */
  getshopList: function (shopId, cacheKey) {
    var that = this;
    console.log('getshopList');
    console.log(shopId);
    wx.request({
      url: app.globalData.domain + '/wxLiteapp/getShopProduct_v2?random=' + Math.random(),
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      data: {
        cacheKey: cacheKey,
        c: app.globalData.c,
        shopId: shopId
      },
      success: function (res) {
        console.log(res);
        console.log("门店参数", cacheKey, app.globalData.c, shopId);

        var code = res.data.code;

        if (code == 'E00000') {
          console.log('获取水店location');
          console.log(res.data);
          wx.setStorageSync('wxLocation', res.data.data.location);
          wx.setStorageSync('cityName', res.data.data.cityName);
          wx.setStorageSync('districtName', res.data.data.districtName);
          wx.hideLoading();

          //统计购物车商品数量
          var arr = [];
          var newArr = [];
          var totalCount = 0;
          for (var i = 0; i < res.data.data.shopProducts.length; i++) {
            for (var j = 0; j < res.data.data.shopProducts[i].products.length; j++) {
              if (res.data.data.shopProducts[i].products[j].number) {
                // console.log(res.data.data.shopProducts[i].products[j]);
                arr.push({ "pid": res.data.data.shopProducts[i].products[j].pid, "number": res.data.data.shopProducts[i].products[j].number });
              }

            }
          }

          for (var i = 0; i < arr.length; i++) {
            //假设第一个元素和后面的每一个都不相同
            var flag = false;
            for (var j = i + 1; j < arr.length; j++) {
              if (arr[i].pid == arr[j].pid) {
                //有相同元素
                flag = true;
                break;
              }
            }
            //没有相同的元素
            if (flag == false) {
              newArr.push(arr[i]);
            }
          }
          // console.log(newArr);
          for (var i = 0; i < newArr.length; i++) {
            totalCount += newArr[i].number;
          }

          wx.setNavigationBarTitle({
            title: res.data.data.shopName
          });
          that.setData({
            currentCity: res.data.data.cityName,
            location: res.data.data.location,
            cityId: res.data.data.cityId,
            shopProducts: res.data.data.shopProducts,
            shopId: shopId,
            totalCount: totalCount
          });
          var offsetTopArr = []
          for (var i = 0; i < that.data.shopProducts.length; i++) {

            offsetTopArr.push("order" + i);

          }
          app.globalData.isOnLoad = 1;
          if (app.globalData.hasBind != 1) {
            // 是否存在注册送券活动
            wx.request({
              url: app.globalData.domain + '/wxLiteapp/exitRegeditSendQuan',
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: cacheKey,
                c: app.globalData.c,
                cityId: that.data.cityId
              },
              success: function (res) {
                console.log(res.data.data.exit);
                wx.hideLoading();
                var code = res.data.code;
                console.log(code);
                if (code == 'E00000') {
                  that.setData({
                    sendDuanData: res.data.data,
                  })
                  // 未注册有活动状态
                  if (res.data.data.exit) {
                    // 自动定位
                    that.autoLocation();
                    that.setData({
                      blurBg: true,
                      minActiveImg: true,
                    });
                    // 未注册无活动状态
                  } else {
                    // that.autoLocation();
                    // that.setData({
                    //     unBindBg: true
                    // });
                  }

                } else {
                  wx.showModal({ 
                    showCancel: false,
                    content: res.data.message
                  })
                }



              },
              fail: function (err) {
                wx.hideLoading();
                wx.showModal({
                  showCancel: false,
                  content: err
                })
              }
            });
          }

        } else if (code == '-3') {
          app.login(function () {
            wx.request({
              url: app.globalData.domain + '/wxLiteapp/getShopProduct_v2?random=' + Math.random(),
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: cacheKey,
                c: app.globalData.c,
                shopId: shopId
              },
              success: function (res) {
                console.log('第二次getShopProduct_v2');
                console.log(res.data);
                wx.hideLoading();
                var code = res.data.code;
                console.log(code);
                if (code == 'E00000') {
                  // console.log('重新请求接口成功');
                  // console.log(res.data);
                  wx.setStorageSync(wxLocation, res.data.data.location);
                  wx.setStorageSync(cityName, res.data.data.cityName);
                  wx.setStorageSync(districtName, res.data.data.districtName);


                  //统计购物车商品数量
                  var arr = [];
                  var newArr = [];
                  var totalCount = 0;
                  for (var i = 0; i < res.data.data.shopProducts.length; i++) {
                    for (var j = 0; j < res.data.data.shopProducts[i].products.length; j++) {
                      if (res.data.data.shopProducts[i].products[j].number) {
                        // console.log(res.data.data.shopProducts[i].products[j]);
                        arr.push({ "pid": res.data.data.shopProducts[i].products[j].pid, "number": res.data.data.shopProducts[i].products[j].number });
                      }

                    }
                  }

                  for (var i = 0; i < arr.length; i++) {
                    //假设第一个元素和后面的每一个都不相同
                    var flag = false;
                    for (var j = i + 1; j < arr.length; j++) {
                      if (arr[i].pid == arr[j].pid) {
                        //有相同元素
                        flag = true;
                        break;
                      }
                    }
                    //没有相同的元素
                    if (flag == false) {
                      newArr.push(arr[i]);
                    }
                  }

                  for (var i = 0; i < newArr.length; i++) {
                    totalCount += newArr[i].number;
                  }

                  wx.setNavigationBarTitle({
                    title: res.data.data.shopName
                  });
                  that.setData({
                    shopProducts: res.data.data.shopProducts,
                    shopId: shopId,
                    totalCount: totalCount
                  });
                  var offsetTopArr = []
                  for (var i = 0; i < that.data.shopProducts.length; i++) {

                    offsetTopArr.push("order" + i);

                  }
                  app.globalData.isOnLoad = 1;
                } else {
                  wx.showModal({
                    showCancel: false,
                    content: res.data.message
                  })
                }



              },
              fail: function (err) {
                wx.hideLoading();
                wx.showModal({
                  showCancel: false,
                  content: err
                })
              }
            });
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
        wx.hideLoading();
        wx.showModal({
          showCancel: false,
          content: err
        })
      }
    });
  },
  // 关闭活动框
  closeBg: function () {
    this.setData({
      blurBg: false,
      unBindBg: false
    });
  },
  /**
   * 未注册小图片点击放大方法
   */
  minImgTap() {
    this.setData({
      blurBg: true
    })
  },
  getCoupon: function () {
    this.setData({
      blurBg: false,
      unBindBg: true
    });
    // this.openModal();
  },
  /**
   * 获取评价
   */
  getEvaluate: function (shopId, evaluateId) {
    var that = this;
    wx.showLoading({
      title: '加载中...',
    });
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/getShopEvaluate",
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      data: {
        cacheKey: app.globalData.cacheKey,
        c: app.globalData.c,
        evaluateId: evaluateId,
        shopId: shopId
      },
      success: function (res) {
        var code = res.data.code;
        wx.hideLoading();
        if (code == 'E00000') {
          var evaluateList = res.data.data;
          var count = evaluateList.count;
          console.log(count);
          if (evaluateList.evaluates.length > 0) { var evaluateId = evaluateList.evaluates[count - 1].evaluateId; }
           
  
          that.setData({
            evaluateList: evaluateList,
            evaluateId: evaluateId
          });
      
        } else if (code == '-3') {
          app.login(function () {
            wx.request({
              url: app.globalData.domain + "/wxLiteapp/getShopEvaluate",
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: app.globalData.cacheKey,
                c: app.globalData.c,
                evaluateId: evaluateId,
                shopId: shopId
              },
              success: function (res) {
                var code = res.data.code;
                wx.hideLoading();
                if (code == 'E00000') {
                  that.setData({
                    evaluateList: res.data.data
                  });
                  console.log(res.data.data);
                } else {
                  wx.showModal({
                    showCancel: false,
                    content: res.data.message
                  })
                }

              },
              fail: function (err) {
                wx.hideLoading();
                wx.showModal({
                  showCancel: false,
                  content: err
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
        wx.hideLoading();
        wx.showModal({
          showCancel: false,
          content: err
        })
      }
    })
  },
  /**上拉加载 */
  loadMore: function () {
    wx.showLoading({
      title: '加载更多',
    })
    var that = this;
    var shopId = that.data.shopId;
    var evaluateId = that.data.evaluateId;
    // that.getEvaluate(shopId, evaluateId);
    console.log('下拉加载');
    // console.log(that.data.evaluateList);
    setTimeout(function () {
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/getShopEvaluate",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          evaluateId: evaluateId,
          shopId: shopId
        },
        success: function (res) {
          var code = res.data.code;
          if (code == 'E00000') {
            var oldData = that.data.evaluateList;
            var evaluateList = res.data.data;

            var count = evaluateList.count;
            if (count == 0) {
              wx.hideLoading();
              wx.showToast({
                title: '没有更多了',
                image: '../../images/error_popover.png'
              })
              return;
            } else {
              var evaluateId = evaluateList.evaluates[count - 1].evaluateId;
              for (var i = 0; i < evaluateList.evaluates.length; i++) {
                oldData.evaluates.push(evaluateList.evaluates[i]);
              }
              console.log(evaluateList);
              that.setData({
                evaluateList: oldData,
                evaluateId: evaluateId
              });
              wx.hideLoading();
            }



            console.log(res.data.data);
          } else if (code == '-3') {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/getShopEvaluate",
                method: 'POST',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  evaluateId: evaluateId,
                  shopId: shopId
                },
                success: function (res) {
                  var code = res.data.code;

                  if (code == 'E00000') {
                    var oldData = that.data.evaluateList;
                    var evaluateList = res.data.data;

                    var count = evaluateList.count;
                    if (count == 0) {
                      wx.hideLoading();
                      return;
                    } else {
                      var evaluateId = evaluateList.evaluates[count - 1].evaluateId;
                      for (var i = 0; i < evaluateList.evaluates.length; i++) {
                        oldData.evaluates.push(evaluateList.evaluates[i]);
                      }
                      console.log(evaluateList);
                      that.setData({
                        evaluateList: oldData,
                        evaluateId: evaluateId
                      });
                      wx.hideLoading();
                    }


                    console.log(res.data.data);
                  } else {
                    wx.showModal({
                      showCancel: false,
                      content: res.data.message
                    })
                  }

                },
                fail: function (err) {
                  wx.hideLoading();
                  wx.showModal({
                    showCancel: false,
                    content: err
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
          wx.hideLoading();
          wx.showModal({
            showCancel: false,
            content: err
          })
        }
      })
    }, 1000);

  },
  /**
   * 获取商家信息
   */
  getShopInfo: function () {
    var that = this;
    wx.request({
      url: app.globalData.domain + "/wxLiteapp/getShopById",
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      data: {
        cacheKey: app.globalData.cacheKey,
        c: app.globalData.c,
        shopId: that.data.shopId
      },
      success: function (res) {
        var code = res.data.code;
        wx.hideLoading();
        if (code == 'E00000') {
          var certificate = res.data.data.shopMap.certificate.split(";");
          res.data.data.shopMap.certificate = certificate;
          let n = res.data.data.shopMap.integrityYear;
          let l = res.data.data.shopMap.chainNum;
          res.data.data.shopMap.integrityYear = [n.replace(/[^0-9]/ig, ""), n.replace(/[0-9]/ig, "")]
          res.data.data.shopMap.chainNum = [l.replace(/[^0-9]/ig, ""), l.replace(/[0-9]/ig, "")]
          
          that.setData({
            shopMap: res.data.data.shopMap,
            brands: res.data.data.brands
          });
        } else if (code == '-3') {
          app.login(function () {
            wx.request({
              url: app.globalData.domain + "/wxLiteapp/getShopById",
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                cacheKey: app.globalData.cacheKey,
                c: app.globalData.c,
                shopId: that.data.shopId
              },
              success: function (res) {
                var code = res.data.code;
                wx.hideLoading();
                if (code == 'E00000') {
                  var certificate = res.data.data.shopMap.certificate.split(";");
                  res.data.data.shopMap.certificate = certificate;
                  that.setData({
                    shopMap: res.data.data.shopMap
                  });
                } else {
                  wx.showModal({
                    content: res.data.message,
                    showCancel: false
                  })
                }

              },
              fail: function (err) {
                wx.hideLoading();
                wx.showModal({
                  showCancel: false,
                  content: err
                })
              }
            });
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
        wx.hideLoading();
        wx.showModal({
          showCancel: false,
          content: err
        })
      }
    });
  },
  // 联系客服
  kefu: function () {
    wx.navigateTo({
      url: '../customService/customService',
    })
  },
  scroll: function (e) {
    console.log(e);
  },
  onReady: function () {

  },
  onShow: function () {
    var that = this;
  
    /* if (isOnLoad == 1) {
       console.log('onShow');
       that.refresh();
     }*/

    if (this.data.shopProducts!=null){
       this._initData();
     }
  
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    var that = this;
    if (app.globalData.hasBind == 1) {

      return {
        title: app.globalData.title,
        imageUrl: app.globalData.liteappShearPic || '../../images/shareImg.jpg',
        path: 'pages/products/products?shopId=' + that.data.shopId + '&clientId=' + app.globalData.clientId,
        success: function () {
          wx.request({
            url: app.globalData.domain + "/wxLiteapp/saveShareShopLog",
            method: 'POST',
            header: {
              'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
              cacheKey: app.globalData.cacheKey,
              c: app.globalData.c,
              shopId: that.data.shopId
            },
            success: function (res) {
              var code = res.data.code;
              if (code == 'E00000') {
                console.log('分享shopId成功');
              } else if (code == '-3') {
                console.log('saveShareShopLog状态码为-3');
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
      }
    } else {

      return {
        title: app.globalData.title,
        path: 'pages/products/products?shopId=' + that.data.shopId
      }
    }

  },
  goTo(){
 
    wx.setStorageSync("certificateImg", this.data.shopMap.certificate)
    wx.navigateTo({
      url: '../certificateImg/certificateImg',
   
    })
  },
  closeBindBg() {
    this.setData({
      unBindBg: false
    })
  }
})
