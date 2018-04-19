// pages/shopCart/shopCart.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {},
    shopCartData: "",
    total: 0,
    eticket: 0,
    ticket: 0
  },
  /*押桶说明*/
  bucketExplain: function (e) {
    wx.navigateTo({
      url: '../bucketExplain/bucketExplain',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.showLoading({
      title: '加载中...',
    });
    var that = this;
    //调用应用实例的方法获取全局数据  
    app.login().then(res=>{ 
    app.getUserInfo(function (userInfo) {
      var shopId = options.shopId;
      app.globalData.shopId = shopId;
      var cacheKey = app.globalData.cacheKey;
      var hasBind = app.globalData.hasBind;
      //更新数据  
      that.setData({
        userInfo: userInfo,
        shopId: shopId,
        cacheKey: cacheKey,
        hasBind: hasBind,
        theme: app.globalData.theme
      })
      //   获取购物车内的商品
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/getShopCartProductJson",
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
            var shopCartData = res.data.data;
            //把选中状态加到新数组里面
            shopCartData.allSelectStatus = true;
            for (var i = 0; i < shopCartData.shopcarts.length; i++) {
              //购物车状态
              shopCartData.shopcarts[i].status = "编辑";
              for (var j = 0; j < shopCartData.shopcarts[i].products.length; j++) {
                shopCartData.shopcarts[i].products[j].productSelectStatus = true;
              }
            }
            that.setData({
              shopCartData: shopCartData
            });
            that.totalAll(that.data.shopCartData);

          } else if (code == '-3') {
            app.login(function () {
              wx.request({
                url: app.globalData.domain + "/wxLiteapp/getShopCartProductJson",
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
                    var shopCartData = res.data.data;
                    //把选中状态加到新数组里面
                    shopCartData.allSelectStatus = true;
                    for (var i = 0; i < shopCartData.shopcarts.length; i++) {
                      //购物车状态
                      shopCartData.shopcarts[i].status = "编辑";
                      for (var j = 0; j < shopCartData.shopcarts[i].products.length; j++) {
                        shopCartData.shopcarts[i].products[j].productSelectStatus = true;
                      }
                    }
                    that.setData({
                      shopCartData: shopCartData
                    });
                    that.totalAll(that.data.shopCartData);
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
          wx.hideLoading();

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
    })


  },
  //   返回水店
  backShop: function (e) {
    console.log(e.currentTarget.dataset.shopid);
    var shopId = e.currentTarget.dataset.shopid;
    wx.redirectTo({
      url: '../products/products?shopId=' + shopId,
    })
  },
  // 联系客服
  kefu: function () {
    wx.navigateTo({
      url: '../customService/customService'
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  },
  /**
   * 单选切换
   */
  toggleStatus: function (e,isCheck) {
  
    var that = this;
    //当前商品pid
    var pid = e.currentTarget.dataset.pid;
    var fpid = e.currentTarget.dataset.fpid;
    var shopCartData = this.data.shopCartData;
    console.log('popop', pid, fpid);
    shopCartData.shopcarts[0].products.forEach(function (item) {

      if (pid == -1 && fpid==item.fpid){
        item.productSelectStatus =false;
      }

      if (pid == item.pid && fpid == item.fpid) {
        console.log(item);
       /** 赠品点击父级要打勾**/
        if (fpid != 0 && !item.productSelectStatus) {
          console.log(132131231232)
          that.toggleStatus({
            currentTarget: {
              dataset: {
                fpid: 0,
                pid: item.fpid
              }
            }
          },true)
        }/**父级取消打勾子元素要取消**/
        else if (item.productSelectStatus && fpid == 0) {
          that.toggleStatus({
            currentTarget: {
              dataset: {
                fpid: item.pid,
                pid:-1,
              }
            }
          })
        }
       
        if (isCheck) { item.productSelectStatus=true}else{
          item.productSelectStatus = !item.productSelectStatus;
        }
        if (!item.productSelectStatus) {
          shopCartData.allSelectStatus = false;
        }
 

      }
    })
    //做个标记，判断是否全部商品都选中

    var flag = true;
    shopCartData.shopcarts[0].products.forEach(function (item) {
      //如果有商品没有选中
      if (!item.productSelectStatus) {
        //   console.log(111);
        flag = false;
        return;
      }
    })
    flag ? shopCartData.allSelectStatus = true : "";

    this.setData({
      shopCartData: shopCartData
    });
    this.totalAll(that.data.shopCartData);

  },
  /**
   * 全选切换
   */
  toggleSelectAll: function () {
    var that = this;
    this.data.shopCartData.allSelectStatus = !this.data.shopCartData.allSelectStatus;
    var shopCartData = this.data.shopCartData;
    shopCartData.shopcarts.forEach(function (item) {
      //   item.shopSelectStatus = that.data.shopCartData.allSelectStatus;
      item.products.forEach(function (item) {
        item.productSelectStatus = that.data.shopCartData.allSelectStatus;
      })
    })
    this.setData({
      shopCartData: this.data.shopCartData
    });
    this.totalAll(that.data.shopCartData);
  },
  /**
   * 编辑/完成状态
   */
  edit: function (e) {
    var that = this;
    var shopCartData = this.data.shopCartData;
    if (shopCartData.shopcarts[0].status == "编辑") {

      shopCartData.shopcarts.forEach(function (item) {
        item.status = "完成";
      });

      this.setData({
        shopCartData: shopCartData
      });
    } else if (shopCartData.shopcarts[0].status == "完成") {
      //   debugger;  
      var products = [];
      var shopId = shopCartData.shopcarts[0].shopId;
      shopCartData.shopcarts.forEach(function (item) {
        item.status = "编辑";
        item.products.forEach(function (item) {
          products.push({ shopId: shopId, pid: item.pid, number: item.number, fpid: item.fpid, maxnum: item.maxnum });
        });
      })
      console.log(products);
      wx.request({
        url: app.globalData.domain + "/wxLiteapp/editShopCart",
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          cacheKey: app.globalData.cacheKey,
          c: app.globalData.c,
          products: JSON.stringify(products)
        },
        success: function (res) {
          var code = res.data.code;
          if (code == 'E00000') {
            that.setData({
              shopCartData: shopCartData
            });
            that.totalAll(that.data.shopCartData);
            wx.showToast({
              title: '操作成功'
            })
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
                  products: JSON.stringify(products)
                },
                success: function (res) {
                  var code = res.data.code;
                  if (code == 'E00000') {
                    that.setData({
                      shopCartData: shopCartData
                    });
                    that.totalAll(that.data.shopCartData);
                    wx.showToast({
                      title: '操作成功',
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
            });
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
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    //   if(app.globalData.order == 'done'){
    //       this.onLoad();
    //   }
  },
  /**
   * 计算价格、水票数量、电子票数量
   */
  totalAll: function (data) {
    var len = data.shopcarts.length,
      //现金
      total = 0,
      //电子票数量
      eticket = 0,
      //水票数量
      ticket = 0;

    for (var i = 0; i < len; i++) {

      for (var j = 0; j < data.shopcarts[i].products.length; j++) {
        //统计现金总价
        if (data.shopcarts[i].products[j].productSelectStatus && data.shopcarts[i].products[j].settleStyle == "现金") {

          //解决js小数问题，先把所有数扩大再计算，算完之后再缩小
          total = (total * 100 + data.shopcarts[i].products[j].number * data.shopcarts[i].products[j].vipPrice * 100) / 100;

          //加上押桶价格  
        } else if (data.shopcarts[i].products[j].productSelectStatus && data.shopcarts[i].products[j].settleStyle == "押桶") {

          total = (total * 100 + data.shopcarts[i].products[j].number * data.shopcarts[i].products[j].vipPrice * 100) / 100;

          //统计电子票数量
        } else if (data.shopcarts[i].products[j].productSelectStatus && data.shopcarts[i].products[j].settleStyle == "电子票") {

          eticket += data.shopcarts[i].products[j].number;

          //统计水票数量
        } else if (data.shopcarts[i].products[j].productSelectStatus && data.shopcarts[i].products[j].settleStyle == "水票") {

          ticket += data.shopcarts[i].products[j].number;

        }
      }

    }
    this.setData({
      total: total.toFixed(2),
      eticket: eticket,
      ticket: ticket
    });
  },
  /**
   * 新增商品
   */
  addCart: function (e) {
    console.log(e);
    var pid = e.currentTarget.dataset.pid;
    var fpid = e.currentTarget.dataset.fpid;
    var maxnum = e.currentTarget.dataset.maxnum;
    var setmealNum;
    //剩余电子票数量
    var surplusNum = e.currentTarget.dataset.surplusnum;

    var shopCartData = this.data.shopCartData;
    //获取赠品对应的套餐
    if (fpid) {
      shopCartData.shopcarts[0].products.forEach(function (item) {
        if (fpid == item.pid) {
          setmealNum = item.number;
        }
      })
    }

    shopCartData.shopcarts[0].products.forEach(function (item) {
      if (pid == item.pid && fpid == item.fpid) {
        item.number++;
      }
      //限制赠品最大购买量
      if (fpid) {
        if (item.number > setmealNum * maxnum) {
          item.number = setmealNum * maxnum;
          wx.showToast({
            title: '超出配送数量',
            image: '../../images/error_popover.png'
          });
        }

      } else {

        //限制电子票最大数量
        if (surplusNum > 0 && item.number > surplusNum && item.settleStyle == "电子票") {
          item.number = surplusNum;
          wx.showToast({
            title: '超出配送数量',
            image: '../../images/error_popover.png'
          });
        }
      }
    });
    this.setData({
      shopCartData: shopCartData
    });
  },
  /**
   * 删减商品
   */
  decreaseCart: function (e) {
    var pid = e.currentTarget.dataset.pid;
    var fpid = e.currentTarget.dataset.fpid;
    var shopCartData = this.data.shopCartData;
    var hashTicket = e.currentTarget.dataset.hashticket;
    shopCartData.shopcarts[0].products.forEach(function (item) {
      if (pid == item.pid && fpid == item.fpid) {
        if (item.number == 1) {
          return;
        } else {
          item.number--;

          var number = item.number;
          //如果是电子票套餐
          if (hashTicket) {
            shopCartData.shopcarts[0].products.forEach(function (item) {
              if (pid == item.fpid && item.number > number * item.maxnum) {
                //赠品最大数量=套餐数量*最大配送基础  
                item.number = number * item.maxnum;
              }
            })
          }
        }

      }
    });

    this.setData({
      shopCartData: shopCartData
    });
  },
  /**
   * 提交订单
   */
  submitOrder: function () {
    if (this.data.shopCartData.shopcarts[0].status == "完成") {
      return;
    } else {
      var shopcart = this.data.shopCartData.shopcarts[0];
      var products = []
      shopcart.products.forEach(function (item) {
        if (item.settleStyle == "现金") {
          var buyType = 1;
        } else if (item.settleStyle == "电子票") {
          var buyType = 2;
        } else if (item.settleStyle == "水票") {
          var buyType = 3;
        } else if (item.settleStyle == "押桶") {
          var buyType = 4;
        }
        if (item.productSelectStatus) {
          products.push({ pid: item.pid, buyType: buyType, price: item.vipPrice, number: item.number, fpid: item.fpid, maxnum: item.maxnum });
        }


      })
      if (products.length > 0) {
        var lock = false;
        shopcart.products.forEach(function (item) {
          if (item.productSelectStatus && item.number == 0) {
            wx.showToast({
              title: '商品数量不能为0',
              image: '../../images/error_popover.png'
            });
            lock = true;
            return;
          }

        });
        //如果赠品数量为0
        if (!lock) {
          var quickSpJson = [{ shopId: shopcart.shopId, eid: shopcart.eid, products: products }];
          wx.setStorageSync('quickSpJson', quickSpJson);
          wx.redirectTo({
            url: '../confirmCar/confirmCar'
          })
        }

      } else {
        wx.showToast({
          title: '请选择商品!',
          image: '../../images/error_popover.png'
        })
      }


      //   return;
      //   wx.chooseAddress({
      //       success: function (res) {
      //           console.log(res);
      //       }
      //   }) 
    }

  },
  //删除商品
  delete: function (e) {
    var that = this;
    var shopId = that.data.shopId;
    var number = e.currentTarget.dataset.number;
    var pid = e.currentTarget.dataset.pid;
    var products = [{ shopId: shopId, pid: pid, number: number }];
    wx.showModal({
      title: '',
      content: '您确定要删除该商品吗？',
      success: function (res) {
        console.log("确定删除");
        if (res.confirm) {

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
              products: JSON.stringify(products)
            },
            success: function (result) {
              console.log("删除成功");
              console.log(result.data);
              var code = result.data.code;
              if (code == "E00000") {

                wx.request({
                  url: app.globalData.domain + "/wxLiteapp/getShopCartProductJson",
                  method: 'POST',
                  header: {
                    'content-type': 'application/x-www-form-urlencoded'
                  },
                  data: {
                    cacheKey: app.globalData.cacheKey,
                    c: app.globalData.c,
                    shopId: that.data.shopId
                  },
                  success: function (result) {

                    var code = result.data.code;
                    if (code == "E00000") {
                      var shopCartData = result.data.data;
                      //把选中状态加到新数组里面
                      shopCartData.allSelectStatus = true;
                      for (var i = 0; i < shopCartData.shopcarts.length; i++) {
                        //购物车状态
                        shopCartData.shopcarts[i].status = "编辑";
                        for (var j = 0; j < shopCartData.shopcarts[i].products.length; j++) {
                          shopCartData.shopcarts[i].products[j].productSelectStatus = true;
                        }
                      }
                      that.setData({
                        shopCartData: shopCartData
                      });
                      wx.hideLoading();
                      that.totalAll(that.data.shopCartData);
                    } else {
                      wx.showModal({
                        content: result.data.message,
                        showCancel: false
                      })
                    }

                  }
                })
              } else if (code == "-3") {
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
                    products: JSON.stringify(products)
                  },
                  success: function (result) {
                    var code = result.data.code;
                    if (code == "E00000") {
                      wx.request({
                        url: app.globalData.domain + "/wxLiteapp/getShopCartProductJson",
                        method: 'POST',
                        header: {
                          'content-type': 'application/x-www-form-urlencoded'
                        },
                        data: {
                          cacheKey: app.globalData.cacheKey,
                          c: app.globalData.c,
                          shopId: that.data.shopId
                        },
                        success: function (result) {
                          var shopCartData = result.data.data;
                          //把选中状态加到新数组里面
                          shopCartData.allSelectStatus = true;
                          for (var i = 0; i < shopCartData.shopcarts.length; i++) {
                            //购物车状态
                            shopCartData.shopcarts[i].status = "编辑";
                            for (var j = 0; j < shopCartData.shopcarts[i].products.length; j++) {
                              shopCartData.shopcarts[i].products[j].productSelectStatus = true;
                            }
                          }
                          that.setData({
                            shopCartData: shopCartData
                          });
                          wx.hideLoading();
                          that.totalAll(that.data.shopCartData);
                        }
                      })
                    } else {
                      wx.hideLoading();
                      wx.showModal({
                        content: result.data.message,
                        showCancel: false
                      })
                    }


                  }
                });
              } else {
                wx.showModal({
                  content: result.data.message,
                  showCancel: false
                })
              }


            }
          });

        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })

  },
  /**
   * 回首页
   */
  goHome: function () {
    var that = this;

    //   wx.navigateTo({
    //       url: '../products/products?shopId=' + that.data.shopId
    //   });
    wx.reLaunch({
      url: '../index/index'
    })
  },
  /**
   * 切换结算方式
   */
  toggleSettleStyle: function (e) {
    var that = this;
    var pid = e.currentTarget.dataset.pid;
    var shopCartData = this.data.shopCartData;
    var name = e.currentTarget.dataset.name;
    shopCartData.shopcarts[0].products.forEach(function (item) {
      if (pid == item.pid) {
        if (name == "电子票" && item.number > item.surplusNum) {
          wx.showToast({
            title: '电子票数量不足',
            image: '../../images/error_popover.png'
          });
          return;
        }
        item.settleStyle = name;
      }
    });
    this.setData({
      shopCartData: shopCartData
    });
    that.totalAll(that.data.shopCartData);
    //   console.log(e.currentTarget.dataset.name);
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