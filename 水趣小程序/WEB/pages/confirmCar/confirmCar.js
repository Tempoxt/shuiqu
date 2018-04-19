// pages/confirmCar/confirmCar.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    index: 0,
    multiIndex: [0, 0],
    address: '',
    noScope: 1,
    dayIndex: 0,
    timeIndex: 0,
    privilegeId: -1,
    activityType: 0,
    orderRemark: "",
    invoiceType: -1,
    invoicoName: "",
    invoiceDetail: "",
    submitFlag: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.showLoading({
      title: '加载中...',
    })
    var that = this;
    //调用应用实例的方法获取全局数据  
    app.getUserInfo(function (userInfo) {
      //更新数据
      var cacheKey = app.globalData.cacheKey;
      var hasBind = app.globalData.hasBind;
      wx.removeStorageSync('addressFlag');
      that.setData({
        userInfo: userInfo,
        cacheKey: cacheKey,
        hasBind: hasBind,
        theme: app.globalData.theme
      })
      //   获取确认订单数据
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/confirmCarJson",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          quickSpJson: JSON.stringify(wx.getStorageSync('quickSpJson'))
        },
        success: function (res) {
          var code = res.data.code;

          wx.hideLoading();
          if (code == 'E00000') {
             res.data.model[0].discounts == null && (res.data.model[0].discounts=[]);
            res.data.model[0].discounts.push({ activityId: -1, discount: 0, activityName: "没有优惠券", codeNo: "" });
            var timeStampSuffix = res.data.model[0].timeStampSuffix;

            var timeStamps = res.data.model[0].timeStamps;
            var timeStamps2 = [];
            for (var i = 0; i < timeStamps.length; i++) {
              timeStamps2.push(timeStamps[i]);
            }
            if (timeStampSuffix == 0) {
              var currentTime = timeStamps2.splice(timeStampSuffix);
              res.data.model[0].timeStamps = [];
              res.data.model[0].timeStamps.push(['明天', '后天'], currentTime);
            } else {
              var currentTime = timeStamps2.splice(timeStampSuffix - 1);
              res.data.model[0].timeStamps = [];
              res.data.model[0].timeStamps.push(['今天', '明天', '后天'], currentTime);
            }


            // console.log(timeStampSuffix, timeStamps, currentTime);


            var num = 0, cash = 0, eticket = 0, ticket = 0, totalMoney = 0;
            var privilegeId = res.data.model[0].discounts[0].activityId;
            console.log(privilegeId);

            res.data.model[0].products.forEach(function (item) {
              if (privilegeId == -1) {
                item.activityId = -1,
                  item.codeNo = ''
              } else {
                res.data.model[0].discounts.forEach(function (discount) {
                  if (item.pid == discount.pid && discount.activityId != 0) {
                    console.log(item.pid + " | " + discount.pid + " | " + item.activityId + " | " + discount.activityId)
                    item.activityId = discount.activityId;
                  }
                })
              }
              if (item.buyType == 1) {
                num += item.number;
                cash += item.vipPrice * item.number;

              } else if (item.buyType == 3) {
                ticket += item.number;
              } else if (item.buyType == 2) {
                eticket += item.number;
              } else if (item.buyType == 4) {
                num += item.number;
                cash += item.vipPrice * item.number;
              }

            });

            totalMoney = cash - res.data.model[0].discounts[0].discount + res.data.model[0].freight;
            totalMoney = totalMoney.toFixed(2);
            //活动id


            res.data.totalMoney = totalMoney;
            //是否在线支付
            if (res.data.isOnlinePay) {
              if (totalMoney == 0) {
                res.data.cashOn = true;
                res.data.weixinPay = false;
              } else {
                res.data.weixinPay = true;
                res.data.cashOn = false;
              }
            } else {
              if (totalMoney == 0) {
                res.data.cashOn = true;
                res.data.weixinPay = false;
              } else {
                res.data.weixinPay = true;
                res.data.cashOn = false;
              }

            }
            that.setData({
              result: res.data,
              baiduRail: JSON.stringify(res.data.model[0].sendLocations),
              num: num,
              cash: cash.toFixed(2),
              ticket: ticket,
              eticket: eticket,
              timeStamps: timeStamps,
              privilegeId: privilegeId,
              currentTime: currentTime
            });
            console.log(that.data.result);
            //   获取送货地址
            wx.request({
              url: app.globalData.domain + "/wxLiteapp/userAddress",
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

                  var did = wx.getStorageSync('did');
                  console.log('111111111111');
                  console.log(did);
                  var didFlag;
                  if (did) {
                    res.data.data.forEach(function (item) {
                      if (did == item.did) {
                        var didFlag = true;
                        that.setData({
                          username: item.contacts,
                          telphone: item.phone,
                          address: item.streetDescribe,
                          did: item.did,
                          addressLocation: item.location
                        });
                      }

                    });
                    if (!didFlag) {
                      res.data.data.forEach(function (item) {
                        if (item.ifDefault) {
                          that.setData({
                            username: item.contacts,
                            telphone: item.phone,
                            address: item.streetDescribe,
                            did: item.did
                          });
                        }

                      });
                    }
                  } else {
                    if (res.data.data.length) {
                      console.log('有地址');
                      res.data.data.forEach(function (item) {
                        if (item.ifDefault) {
                          that.setData({
                            username: item.contacts,
                            telphone: item.phone,
                            address: item.streetDescribe,
                            did: item.did
                          });
                        }

                      });
                    } else {
                      console.log('没有地址');
                      that.setData({
                        address: ''
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
                  showCancel: false,
                  content: err
                })
              }
            });
          } else if (code == '-3') {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/confirmCarJson",
                method: 'POST',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  quickSpJson: JSON.stringify(wx.getStorageSync('quickSpJson'))
                },
                success: function (res) {
                  var code = res.data.code;

                  wx.hideLoading();
                  if (code == 'E00000') {
                    res.data.model[0].discounts.push({ activityId: -1, discount: 0, activityName: "没有优惠券", codeNo: "" });
                    var timeStampSuffix = res.data.model[0].timeStampSuffix;

                    var timeStamps = res.data.model[0].timeStamps;
                    var timeStamps2 = [];
                    for (var i = 0; i < timeStamps.length; i++) {
                      timeStamps2.push(timeStamps[i]);
                    }
                    var currentTime = timeStamps2.splice(timeStampSuffix - 1);

                    // console.log(timeStampSuffix, timeStamps, currentTime);
                    res.data.model[0].timeStamps = [];
                    res.data.model[0].timeStamps.push(['今天', '明天', '后天'], currentTime);

                    var num = 0, cash = 0, eticket = 0, ticket = 0, totalMoney = 0;
                    //活动id
                    var privilegeId = res.data.model[0].discounts[0].activityId;
                    res.data.model[0].products.forEach(function (item) {
                      if (privilegeId == -1) {
                        item.activityId = -1,
                          item.codeNo = ''
                      } else {
                        res.data.model[0].discounts.forEach(function (discount) {
                          if (item.pid == discount.pid && discount.activityId != 0) {
                            console.log(item.pid + " | " + discount.pid + " | " + item.activityId + " | " + discount.activityId)
                            item.activityId = discount.activityId;
                          }
                        })
                      }
                      if (item.buyType == 1) {
                        num += item.number;
                        cash += item.vipPrice * item.number;

                      } else if (item.buyType == 3) {
                        ticket += item.number;
                      } else if (item.buyType == 2) {
                        eticket += item.number;
                      } else if (item.buyType == 4) {
                        num += item.number;
                        cash += item.vipPrice * item.number;
                      }

                    });
                    totalMoney = cash - res.data.model[0].discounts[0].discount + res.data.model[0].freight;
                    totalMoney = totalMoney.toFixed(2);
                    res.data.totalMoney = totalMoney;
                    if (res.data.isOnlinePay) {
                      if (totalMoney == 0) {
                        res.data.cashOn = true;
                        res.data.weixinPay = false;
                      } else {
                        res.data.weixinPay = true;
                        res.data.cashOn = false;
                      }
                    } else {

                      if (totalMoney == 0) {
                        res.data.cashOn = true;
                        res.data.weixinPay = false;
                      } else {
                        res.data.weixinPay = true;
                        res.data.cashOn = false;
                      }

                    }
                    that.setData({
                      result: res.data,
                      baiduRail: JSON.stringify(res.data.model[0].sendLocations),
                      num: num,
                      cash: cash.toFixed(2),
                      ticket: ticket,
                      eticket: eticket,
                      timeStamps: timeStamps,
                      privilegeId: privilegeId,
                      currentTime: currentTime
                    });
                    wx.request({
                      url: app.globalData.domain + "/wxLiteapp/userAddress",
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
                          var did = wx.getStorageSync('did');
                          var didFlag;
                          if (did) {
                            res.data.data.forEach(function (item) {
                              if (did == item.did) {
                                var didFlag = true;
                                that.setData({
                                  username: item.contacts,
                                  telphone: item.phone,
                                  address: item.streetDescribe,
                                  did: item.did,
                                  addressLocation: item.location
                                });
                              }

                            });
                            if (!didFlag) {
                              res.data.data.forEach(function (item) {
                                if (item.ifDefault) {
                                  that.setData({
                                    username: item.contacts,
                                    telphone: item.phone,
                                    address: item.streetDescribe,
                                    did: item.did
                                  });
                                }

                              });
                            }
                          } else {
                            res.data.data.forEach(function (item) {
                              if (item.ifDefault) {
                                that.setData({
                                  username: item.contacts,
                                  telphone: item.phone,
                                  address: item.streetDescribe,
                                  did: item.did
                                });
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
                          showCancel: false,
                          content: err
                        })
                      }
                    });
                  } else if (code == '-3') {
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
  },
  //   选择配送地址
  selectAddress: function () {

    var that = this;
    var shopId = this.data.result.model[0].shopId;
    console.log(shopId);
    wx.navigateTo({
      url: '../address/address?from=confirmCar&shopId=' + shopId,
    })
    // wx.chooseAddress({
    //     success: function (res) {
    //         console.log(res);
    //         var username = res.userName;
    //         var telphone = res.telNumber;
    //         var address = res.cityName + res.countyName + res.detailInfo;
    //         // console.log(username,telphone,address);
    //         that.setData({
    //             username: username,
    //             telphone: telphone,
    //             address: address
    //         });
    //     }
    // })

  },
  //   选择支付方式
  choosePayment: function (e) {
    var payment = e.currentTarget.dataset.payment;
    //   console.log(e.currentTarget.dataset.payment);
    var result = this.data.result;
    //   console.log(result.weixinPay);
    if (payment == "微信支付") {
      result.weixinPay = true;
      result.cashOn = false;
    } else if (payment == "货到付款") {
      result.cashOn = true;
      result.weixinPay = false;
    }
    this.setData({
      result: result
    });
  },
  /**改变店铺优惠 */
  bindActivityChange: function (e) {
    var that = this;
    var nocode = "";
    //   console.log(e);
    var index = e.detail.value;
    var cash = this.data.cash;
    var discount = this.data.result.model[0].discounts[index].discount;
    var freight = this.data.result.model[0].freight;
    var privilegeId = this.data.result.model[0].discounts[index].activityId;
    var totalMoney = (cash - discount + freight).toFixed(2);
    var result = that.data.result;

    result.model[0].products.forEach(function (item) {
      //   console.log(item);
      result.model[0].discounts.forEach(function (discount) {
        if (item.pid == discount.pid && discount.activityId > 0) {
          console.log(item.activityId);
          item.activityId = discount.activityId;
        } else if (item.pid == discount.pid && discount.activityId == 0) {
          item.activityId = privilegeId;
          item.code_no = discount.codeNo;
        } else if (privilegeId == -1) {
          item.activityId = privilegeId;
          item.code_no = discount.codeNo;
        }
      })
    })
    //   totalcash
    console.log('picker发送选择改变，携带值为', e.detail.value);
    result.totalMoney = totalMoney;
    if (result.isOnlinePay) {

      if (totalMoney == 0) {
        result.cashOn = true;
        result.weixinPay = false;
      } else {
        result.cashOn = false;
        result.weixinPay = true;
      }
    } else {
      if (totalMoney == 0) {
        result.cashOn = true;
        result.weixinPay = false;
      } else {
        result.cashOn = false;
        result.weixinPay = true;
      }

    }
    this.setData({
      index: e.detail.value,
      result: result,
      privilegeId: privilegeId
    });
    console.log(that.data.result);
  },
  /**
   * 其他要求
   */
  orderRemark: function (e) {
    var orderRemark = e.detail.value;
    this.setData({
      orderRemark: orderRemark
    });
    console.log(e.detail.value);
  },
  /**
   * 提交订单
   */
  submitOrder: function (e) {
    var formId = e.detail.formId;
    //   console.log(e.detail.formId);
    //   return;
    var isValid = app.globalData.isValid;
    if (!isValid) {
      console.log('小程序支付正在完善中...');
      wx.showModal({
        content: '小程序支付正在完善中...',
        showCancel: false
      })
    } else {
      this.data.submitFlag++;
      wx.showLoading({
        title: '加载中...',
      })
      var result = this.data.result;
      var shopId = result.model[0].shopId;
      var eid = result.model[0].eid;
      var did = this.data.did;
      var privilegeId = this.data.privilegeId;
      var activityType = this.data.activityType;
      var appointmentTime = result.model[0].timeStamps[0][this.data.dayIndex] + " " + result.model[0].timeStamps[1][this.data.timeIndex];
      var sendRemark = result.model[0].timeStamps[1][this.data.timeIndex];
      var orderRemark = this.data.orderRemark;
      var vatId = result.model[0].vatId;
      var invoiceType = this.data.invoiceType;
      var invoicoName = this.data.invoicoName;
      var invoiceDetail = this.data.invoiceDetail;
      if (!this.data.address) {
        this.data.submitFlag = 0;
        wx.hideLoading();
        wx.showToast({
          title: '请选择收货地址',
          image: '../../images/error_popover.png'
        })
        return;
      }

      var products = result.model[0].products;
      console.log(products);
      if (this.data.result.weixinPay) {
        var payMode = 2;
      } else if (this.data.result.cashOn) {
        var payMode = 1;
      }
      var quickSpJson = [{ shopId: shopId, eid: eid, activityType: activityType, did: did, privilegeId: privilegeId, appointmentTime: appointmentTime, sendRemark: sendRemark, orderRemark: orderRemark, payMode: payMode, vatId: vatId, invoiceType: invoiceType, invoicoName: invoicoName, invoiceDetail: invoiceDetail, products: products }];
      // console.log(quickSpJson);
      // return;
      if (this.data.submitFlag > 1) {
        console.log(this.data.submitFlag);
        return;
      } else {
        //   保存订单
        wx.request({
          url: app.globalData.domain + "/wxLiteapp/shoppingCarSave",
          method: 'POST',
          header: {
            'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
            cacheKey: app.globalData.cacheKey,
            c: app.globalData.c,
            quickSpJson: JSON.stringify(quickSpJson),
            formId: formId
          },
          success: function (res) {
            var code = res.data.code;
            if (code == 'E00000') {
              wx.hideLoading();
              var group = res.data.data.group;
              //   货到付款
              if (payMode == 1) {
                console.log('货到付款');
                wx.setStorageSync('payResult', true);

                wx.redirectTo({
                  url: '../payResult/payResult?result=success',
                })

                //   现金支付
              } else if (payMode == 2) {

                wx.request({
                  url: app.globalData.domain + "/wxLiteapp/payGroup",
                  method: 'POST',
                  header: {
                    'content-type': 'application/x-www-form-urlencoded'
                  },
                  data: {
                    cacheKey: app.globalData.cacheKey,
                    c: app.globalData.c,
                    group: group
                  },
                  success: function (res) {
                    var code = res.data.code;
                    wx.setStorageSync('payResult', true);
                    if (code == 'E00000') {

                      wx.requestPayment({
                        'timeStamp': res.data.timeStamp,
                        'nonceStr': res.data.nonceStr,
                        'package': res.data.package,
                        'signType': 'MD5',
                        'paySign': res.data.paySign,
                        'success': function (res) {
                          console.log(res);
                          console.log('成功' + res);

                          wx.redirectTo({
                            url: '../payResult/payResult?result=success',
                          })
                        },
                        'fail': function (err) {
                          console.log('失败' + JSON.stringify(err));
                          wx.redirectTo({
                            url: '../payResult/payResult?result=fail',
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
                      showCancel: false,
                      content: err
                    })
                  }
                });
              }
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
      }

      console.log("shopId:" + shopId + ",eid:" + eid + ",activityType:" + activityType + ",did:" + did + ",privilegeId:" + privilegeId + ",appointmentTime:" + appointmentTime + ",sendRemark:" + sendRemark + ",orderRemark:" + orderRemark + ",payMode:" + payMode + ",vatId:" + vatId + ",invoiceType:" + invoiceType + ",invoicoName:" + invoicoName + ",invoiceDetail:" + invoiceDetail + ",products:" + products);
    }



  },
  /**
   * 确认修改时间
   */
  confirmChange: function (e) {
    console.log(e);
    var dayIndex = e.detail.value[0];
    var timeIndex = e.detail.value[1];
    this.setData({
      dayIndex: dayIndex,
      timeIndex: timeIndex
    });
  },
  /*
  *修改时间
  */
  columnChange: function (e) {
    console.log(e);
    var result = this.data.result;
    if (e.detail.column == "0") {
      //今天
      if (e.detail.value == "0") {

        result.model[0].timeStamps[1] = this.data.currentTime;
        this.setData({
          result: result
        });
        //明天或后天
      } else {

        result.model[0].timeStamps[1] = this.data.timeStamps;
        this.setData({
          result: result
        });
        console.log(this.data.result.model[0].timeStamps[1]);
      }
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
    //   
    var flag = wx.getStorageSync('addressFlag');
    if (flag) {
      this.onLoad();
    }
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
      title: app.globalData.title ,
      imageUrl: app.globalData.liteappShearPic || '../../images/shareImg.jpg',
      path: 'pages/index/index'
    }
  }
})