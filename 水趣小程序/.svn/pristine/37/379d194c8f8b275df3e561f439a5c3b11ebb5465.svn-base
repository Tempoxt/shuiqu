// pages/addAddress/addAddress.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {},
    city:'',
    tagIndex:null,
    region: ['请选择','','']
  },
//   bindRegionChange: function (e) {
//       console.log('picker发送选择改变，携带值为', e.detail.value)
//       this.setData({
//           region: e.detail.value
//       })
//   },
  makertap: function (e) {
      var that = this;
      var id = e.markerId;
      that.showSearchInfo(wxMarkerData, id);
  },
  getAddress:function(e){
      var that = this;
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
  activeTag:function(e){
      var tag = e.currentTarget.dataset.val
      console.log(e.currentTarget.dataset.val);
      this.setData({
          tagIndex: e.target.dataset.index,
          tag:tag
      });
  },
  saveAddress:function(e){
      var contacts = this.data.contacts;
      var phone = this.data.phone;
      var streetDescribe = this.data.streetDescribe;
      var streetName = this.data.streetName;
      var doorplate = this.data.doorplate;
      var tag = this.data.tag;
      var location = this.data.location;
      var address = streetDescribe + streetName + doorplate;
      console.log(phone, contacts, streetDescribe, streetName, doorplate, location, tag, address);
    if(contacts == undefined){
        wx.showToast({
            title: '联系人不能为空',
        })
        return;
    } else if (phone == undefined){
        wx.showToast({
            title: '手机号不能为空',
        })
        return;
    } else if (streetName == undefined){
        wx.showToast({
            title: '详细地址不能为空',
        });
        return;
    } else if (doorplate == undefined){
        wx.showToast({
            title: '门牌号不能为空',
        });
        return;
    } else if (tag == undefined){
        wx.showToast({
            title: '请选择地址标签',
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
            cacheKey: wx.getStorageSync('cacheKey'),
            c: app.globalData.c,
            streetName: streetName,
            streetDescribe: streetDescribe,
            address: address,
            doorplate: doorplate,
            phone: phone,
            contacts: contacts,
            location: location,
            sdkType: 'littleApp',
            tag: tag
        },
        success: function (res) {
            var code = res.data.code;
            if (code =='E00000'){
                // wx.redirectTo({
                //     url: '../address/address',
                // });
                wx.navigateBack({
                    delta: 1
                })
            }else{
                wx.showModal({
                    content: res.data.message,
                    showCancel: false
                })
            }
           
        },
        fail:function(err){
            wx.showModal({
                content: err,
                showCancel: false
            })
        }
    }); 
  },
  inputUsername:function(e){
      console.log(e.detail.value);
      this.setData({
          contacts: e.detail.value
      });
  },
  inputTel:function(e){
      console.log(e.detail.value);
      this.setData({
          phone:e.detail.value
      });
  },
  inputDetailAddress:function(e){
      console.log(e.detail.value);
      this.setData({
          streetName: e.detail.value
      });
  },
  inputDoorplate:function(e){
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
              userInfo: userInfo
          })
      });
      wx.getSystemInfo({

          success: function (res) {
              that.setData({
                  winWidth: res.windowWidth,
                  winHeight: res.windowHeight
              });
          }

      });
      wx.request({
          url: app.globalData.domain + "/wxLiteapp/getAddressTags",
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
              wx.setStorageSync('isRefeshAddress',1);
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
          fail:function(err){
              wx.showModal({
                  content: err,
                  showCancel: false
              })
          }
      }); 
      wx.request({
          url: app.globalData.domain + "/wxLiteapp/getUserCellphone",
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
                  that.setData({
                      phone: res.data.data,
                      streetDescribe: wx.getStorageSync('cityName') + wx.getStorageSync('districtName'),
                      location: wx.getStorageSync('wxLocation')
                  });
              } else {
                  wx.showModal({
                      content: res.data.message,
                      showCancel: false
                  })
              }
          },
          fail:function(err){
              wx.showModal({
                  content: err,
                  showCancel: false
              })
          }
      }); 

      
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
          title: app.globalData.productName,
          desc: '最具人气的订水平台!',
          path: 'pages/index/index'
      }
  }
})