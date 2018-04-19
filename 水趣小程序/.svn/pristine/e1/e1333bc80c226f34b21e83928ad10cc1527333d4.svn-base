// pages/confirmCar/confirmCar.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    index:0,
    multiIndex: [0, 0],
    address:'',
    noScope:1,
    dayIndex:0,
    timeIndex:0,
    privilegeId:-1,
    activityType: 0,
    orderRemark:"",
    invoiceType:-1,
    invoicoName:"",
    invoiceDetail:""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      wx.showLoading({
          title: '加载中',
      })
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
          })
          wx.request({
              url: app.globalData.domain + "/wxLiteapp/confirmCarJson",
              method: 'POST',
              header: {
                  'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                  cacheKey: wx.getStorageSync('cacheKey'),
                  c: app.globalData.c,
                  quickSpJson: JSON.stringify(wx.getStorageSync('quickSpJson'))
              },
              success: function (res) {
                  var code = res.data.code;
                  
                  wx.hideLoading();
                  if (code == 'E00000') {
                      res.data.model[0].discounts.push({ activityId: -1, discount: 0, activityName: "没有优惠券", codeNo: ""});
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

                      var num = 0, cash = 0, eticket = 0, ticket = 0,totalcash = 0;
                      var privilegeId = res.data.model[0].discounts[0].activityId;
                      console.log(privilegeId); 
                      
                        res.data.model[0].products.forEach(function (item) {
                            if (privilegeId == -1) {
                                item.activityId = -1,
                                item.codeNo = ''
                            }else{
                                res.data.model[0].discounts.forEach(function (discount){
                                    if (item.pid == discount.pid && discount.activityId != 0){
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
                    
                      totalcash = cash - res.data.model[0].discounts[0].discount + res.data.model[0].freight;
                      //活动id
                      
                      if (res.data.isOnlinePay) {
                          res.data.weixinPay = true;
                      } else {
                          if (cash==0){
                              res.data.cashOn = true;
                          }else{
                              res.data.weixinPay = true;
                              res.data.cashOn = false;
                          }
                          
                      }
                      
                      that.setData({
                          result: res.data,
                          baiduRail: JSON.stringify(res.data.model[0].sendLocations),
                          num: num,
                          cash: cash.toFixed(2),
                          totalcash: totalcash.toFixed(2),
                          ticket: ticket,
                          eticket: eticket,
                          timeStamps: timeStamps,
                          privilegeId: privilegeId,
                          currentTime: currentTime
                      });
                      console.log(that.data.result);
                      wx.request({
                          url: app.globalData.domain + "/wxLiteapp/userAddress",
                          method: 'POST',
                          header: {
                              'content-type': 'application/x-www-form-urlencoded'
                          },
                          data: {
                              cacheKey: wx.getStorageSync('cacheKey'),
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



                              //   wx.request({
                              //       url: app.globalData.domain + "/wxLiteapp/baiduRailLocation",
                              //       method: 'POST',
                              //       header: {
                              //           'content-type': 'application/x-www-form-urlencoded'
                              //       },
                              //       data: {
                              //           cacheKey: wx.getStorageSync('cacheKey'),
                              //           c: app.globalData.c,
                              //           baiduRail: that.data.baiduRail,
                              //           addressLocation: that.data.addressLocation
                              //       },
                              //       success: function (res) {
                              //           //   console.log(res.data.data);
                              //           that.setData({
                              //               noScope: res.data
                              //           });

                              //           //   wx.hideLoading();
                              //           //   that.setData({
                              //           //       address: res.data.data,
                              //           //       addLenth: res.data.data.length
                              //           //   });
                              //       }
                              //   });
                          },
                          fail: function (err) {
                              wx.showModal({
                                  showCancel: false,
                                  content: err
                              })
                          }
                      });
                  } else if(code == '-3'){
                      app.login(function(){
                          wx.request({
                              url: app.globalData.domain + "/wxLiteapp/confirmCarJson",
                              method: 'POST',
                              header: {
                                  'content-type': 'application/x-www-form-urlencoded'
                              },
                              data: {
                                  cacheKey: wx.getStorageSync('cacheKey'),
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

                                      var num = 0, cash = 0, eticket = 0, ticket = 0, totalcash = 0;
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
                                      totalcash = cash - res.data.model[0].discounts[0].discount + res.data.model[0].freight;
                                      
                                      if (res.data.isOnlinePay) {
                                          res.data.weixinPay = true;
                                      } else {
                                          if (cash == 0) {
                                              res.data.cashOn = true;
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
                                          totalcash: totalcash.toFixed(2),
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
                                              cacheKey: wx.getStorageSync('cacheKey'),
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
              fail:function(err){
                  wx.hideLoading();
                  wx.showModal({
                      showCancel: false,
                      content: err
                  })
              }
          });
      }); 
  },
  selectAddress:function(){
        
        var that = this;
        console.log(that.data.shopId);
        wx.navigateTo({
            url: '../address/address?from=confirmCar&shopId='+that.data.shopId,
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
  choosePayment:function(e){
      var payment = e.currentTarget.dataset.payment;
    //   console.log(e.currentTarget.dataset.payment);
      var result = this.data.result;
    //   console.log(result.weixinPay);
      if (payment=="微信支付"){
          result.weixinPay = true;
          result.cashOn = false;
      } else if (payment=="货到付款"){
          result.cashOn = true;
          result.weixinPay = false;
      }
      this.setData({
          result: result
      });
  },
  /**改变店铺优惠 */
  bindActivityChange:function(e){
      var that = this;
      var nocode = "";
      console.log(e);
      var index = e.detail.value;
      var cash = this.data.cash;
      var discount = this.data.result.model[0].discounts[index].discount;
      var freight = this.data.result.model[0].freight;
      var privilegeId = this.data.result.model[0].discounts[index].activityId;
      var totalcash = (cash-discount+freight).toFixed(2);
      var result = that.data.result;
      
      result.model[0].products.forEach(function (item) {
          console.log(item);
          result.model[0].discounts.forEach(function (discount) {
              if (item.pid == discount.pid && discount.activityId > 0) {
                  console.log(item.activityId);
                  item.activityId = discount.activityId;
              } else if (item.pid == discount.pid && discount.activityId == 0) {
                  item.activityId = privilegeId;
                  item.code_no = discount.codeNo;
              } else if (privilegeId == -1){
                  item.activityId = privilegeId;
                  item.code_no = discount.codeNo;
              }
          })
      }) 
    //   totalcash
      console.log('picker发送选择改变，携带值为', e.detail.value);
      
      this.setData({
          index: e.detail.value,
          result:result,
          totalcash: totalcash,
          privilegeId: privilegeId
      });
      console.log(that.data.result);
  },
  /**
   * 其他要求
   */
  orderRemark:function(e){
      var orderRemark = e.detail.value;
      this.setData({
          orderRemark: orderRemark
      });
      console.log(e.detail.value);
  },
  /**
   * 提交订单
   */
  submitOrder:function(e){
    //   if (this.data.noScope != 1){
    //     return;
    //   }else{
        var result = this.data.result;
        var shopId = result.model[0].shopId;
        var eid = result.model[0].eid;
        var did = this.data.did;
        var privilegeId = this.data.privilegeId;
        var activityType = this.data.activityType;
        var appointmentTime = result.model[0].timeStamps[0][this.data.dayIndex] +" "+ result.model[0].timeStamps[1][this.data.timeIndex];
        var sendRemark = result.model[0].timeStamps[1][this.data.timeIndex];
        var orderRemark = this.data.orderRemark;
        var vatId = result.model[0].vatId;
        var invoiceType = this.data.invoiceType;
        var invoicoName = this.data.invoicoName;
        var invoiceDetail = this.data.invoiceDetail;
        if(!this.data.address){
            wx.showToast({
                title: '请选择收货地址',
            })
            return;
        }
        
        //   var fpid = 0;
        //   var maxnum = 0;
        //   result.model[0].products.forEach(function(item){
        //       item.fpid = fpid;
        //       item.maxnum = maxnum;
        //   });
        var products = result.model[0].products;
        console.log(products);  
        //   console.log(this.data.result.weixinPay);
        if (this.data.result.weixinPay){
            var payMode = 2;
        } else if (this.data.result.cashOn){
            var payMode = 1;
        } 
        var quickSpJson = [{ shopId: shopId, eid: eid, activityType: activityType, did: did, privilegeId: privilegeId, appointmentTime: appointmentTime, sendRemark: sendRemark, orderRemark: orderRemark, payMode: payMode, vatId: vatId, invoiceType: invoiceType, invoicoName: invoicoName, invoiceDetail: invoiceDetail, products: products}];
        console.log(quickSpJson);
          return;
        wx.request({
            url: app.globalData.domain + "/wxLiteapp/shoppingCarSave",
            method: 'POST',
            header: {
                'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
                cacheKey: wx.getStorageSync('cacheKey'),
                c: app.globalData.c,
                quickSpJson: JSON.stringify(quickSpJson)
            },
            success: function (res) {
                var code = res.data.code;
                if (code == 'E00000') {
                    var group = res.data.data.group;
                    if (payMode == 1) {
                        console.log('货到付款');
                        wx.setStorageSync('payResult', true);
                        //   wx.reLaunch({
                        //       url: '../payResult/payResult?result=success'
                        //   })
                        wx.redirectTo({
                            url: '../payResult/payResult?result=success',
                        })
                        
                        //   wx.navigateTo({
                        //       url: '../payResult/payResult?result=success'
                        //   })
                    } else if (payMode == 2) {

                        wx.request({
                            url: app.globalData.domain + "/wxLiteapp/payGroup",
                            method: 'POST',
                            header: {
                                'content-type': 'application/x-www-form-urlencoded'
                            },
                            data: {
                                cacheKey: wx.getStorageSync('cacheKey'),
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
                                            console.log('成功' + res);
                                            //   wx.reLaunch({
                                            //       url: '../payResult/payResult?result=success'
                                            //   })
                                            //   wx.navigateTo({
                                            //       url: '../payResult/payResult?result=success'
                                            //   })
                                            wx.redirectTo({
                                                url: '../payResult/payResult?result=success',
                                            })
                                        },
                                        'fail': function (err) {
                                            console.log('失败' + JSON.stringify(err));
                                            wx.redirectTo({
                                                url: '../payResult/payResult?result=fail',
                                            })
                                            //   wx.navigateTo({
                                            //       url: '../payResult/payResult?result=fail'
                                            //   })
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
        console.log("shopId:" + shopId + ",eid:" + eid + ",activityType:" + activityType + ",did:" + did + ",privilegeId:" + privilegeId + ",appointmentTime:" + appointmentTime + ",sendRemark:" + sendRemark + ",orderRemark:" + orderRemark + ",payMode:" + payMode + ",vatId:" + vatId + ",invoiceType:" + invoiceType + ",invoicoName:" + invoicoName + ",invoiceDetail:" + invoiceDetail + ",products:" + products);
        // console.log(this.data.result);
    // }

  },
  /**
   * 确认修改时间
   */
  confirmChange:function(e){
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
  columnChange:function(e){
      console.log(e);
      var result = this.data.result;
      if (e.detail.column == "0"){
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
    //   this.onLoad();

    //   var payResult = wx.getStorageSync('payResult');
    //   if (payResult){
    //       wx.setStorageSync('payResult',false);
    //         wx.reLaunch({
    //             url: '../products/products',
    //         })
    //   }
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