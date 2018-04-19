//app.js  小程序的项目入口文件
App({
  onLaunch: function (options) {
    console.log('onLaunch' + 1);
    wx.removeStorageSync('cacheKey');
    var that = this;
    console.log(options);
    //通过微信群分享进入小程序
    if (options.scene == 1008) {
      console.log('场景值');
      console.log(options.scene);
      that.globalData.scene = options.scene;
      // 分享到群里的水店id
      that.globalData.shareShopId = options.query.shopId;
      // 分享人id
      that.globalData.shareClientId = options.query.clientId;
      console.log(options.query.clientId);
    }
    // 获取第三方平台信息
    if (wx.getExtConfig) {
      wx.getExtConfig({
        success: function (res) {
          console.log('获取第三方平台数据成功');
          console.log(res);
          if (res.extConfig.c) {
            // 企业id
            that.globalData.c = res.extConfig.c;
            // 页面标题
            that.globalData.title = res.extConfig.title;

            // 接口域名
            that.globalData.domain = res.extConfig.domain;
            // 小程序版本号
            that.globalData.version = res.extConfig.version;
            // 企业名称
            that.globalData.ename = res.extConfig.ename;
            // 主题色
            // that.globalData.theme = res.extConfig.styleCode;
          } else {
            console.log("使用默认参数");
            // 默认参数
            // 企业id   1：水趣 36：水趣防伪  46：水宜家
            that.globalData.c = 1;
            // 接口域名  天津大力水手  https://sappa.tjdlss.cn
            that.globalData.domain = "https://x1.shuiqoo.cn";
            // 页面标题
            that.globalData.title = "水趣";
            // 主题色
            //  that.globalData.theme = 1;
          }

          console.log(that.globalData.productName);
        //  that.login();

        },
        fail: function (err) {
          console.log('获取第三方数据失败');
          console.log(err);
        }
      })
    }
    console.log('onLaunch' + 2);
  },
  login: function (callback) {
    var that = this;
    return new Promise(function (resolve, reject) {
      if (that.loginData != null) { resolve(that.loginData);return }
      if (that.globalData.userInfo != null) { return resolve() }
      // 获取 临时登录凭证code
      wx.login({
  
        success: function (res) {
          if (res.code) {
            //用code换取 用户唯一标识 cacheKey
            wx.request({
              url: that.globalData.domain + '/wxLiteapp/login',
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                code: res.code,
                c: that.globalData.c
              },
              success: function (res) {
                resolve(res.data);
          
                that.loginData = res.data;
                var code = res.data.code;
                if (code == 'E00000') {
                  if (res.data.data.shopId) {
                    // 用户最后一次订水的水店id或扫描进入的水店id
                    that.globalData.shopId = res.data.data.shopId;


                  }
                  // 企业未配置好支付数据
                  if (!res.data.data.isValid) {
                    console.log('小程序支付正在完善中...');
                    wx.showModal({
                      content: '小程序支付正在完善中...',
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
                  //分享内容
                  that.globalData.liteappShearPic = res.data.data.liteappShearDesc;

                  // 主题色
                  that.globalData.theme = res.data.data.styleCode||1;
                  that.globalData.cacheKey = res.data.data.cacheKey;

                  that.globalData.hasBind = res.data.data.hasBind;
                  //是否验证支付参数
                  that.globalData.isValid = res.data.data.isValid;

                  that.globalData.cityId = res.data.data.cityId;

                  that.globalData.clientId = res.data.data.clientId;

                  //是否有消费习惯
                  that.globalData.hasHabit = res.data.data.hasHabit;
                  that.globalData.isOnLoad = 0;

                  typeof callback == "function" && callback();
                  //如果没有保存用户信息
                  if (!res.data.data.isDownload && that.globalData.userInfo) {
                    console.log('保存用户信息');
                    var userInfo = that.globalData.userInfo;
                    //保存用户信息
                    wx.request({
                      url: that.globalData.domain + "/wxLiteapp/saveSubscribeInfo",
                      method: 'POST',
                      header: {
                        'content-type': 'application/x-www-form-urlencoded'
                      },
                      data: {
                        cacheKey: that.globalData.cacheKey,
                        c: that.globalData.c,
                        nickName: userInfo.nickName,
                        gender: userInfo.gender,
                        language: userInfo.language,
                        city: userInfo.city,
                        province: userInfo.province,
                        country: userInfo.country,
                        avatarUrl: userInfo.avatarUrl
                      },
                      success: function (res) {
                        console.log(res.data.data);
                      },
                      fail: function (err) {
                        wx.showModal({
                          title: '',
                          showCancel: false,
                          content: "网络异常！"
                        })
                      }
                    })
                  }
                } else {
                  console.log('/wxLiteapp/login errcode ');
                  wx.showModal({
                    content: res.data.message,
                    showCancel: false
                  })
                }

              },
              fail: function (err) {
                // clearInterval(timer);
                console.log('/wxLiteapp/login err');
                console.log(err);
                wx.showModal({
                  content: err,
                  showCancel: false
                })
              }
            })
          } else {
            wx.showModal({
              content: '获取用户登录态失败！' + res.errMsg,
              showCancel: false
            })
            console.log('获取用户登录态失败！' + res.errMsg)
          }

        },
        fail: function (err) {
          console.log('login fail');
          console.log(err);
        }
      });
    })

  },
  getSetting() {
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo

              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        }
      }
    })
  },
  // 获取用户信息
  getUserInfo: function (cb) {
    var that = this;

    if (this.globalData.userInfo) {
      typeof cb == "function" && cb(this.globalData.userInfo);
      // console.log('授权成功');
    } else {

      wx.getUserInfo({
        success: function (res) {

          that.globalData.userInfo = res.userInfo;

          typeof cb == "function" && cb(that.globalData.userInfo);

        },
        fail: function () {
          that.globalData.userInfo = null;
          wx.hideLoading();

          wx.navigateTo({
            url: '../unauthorized/unauthorized',
          })

        }
      })
    }
  },
  loginData:null,
  globalData: {
    userInfo: null,
    domain: "",
    // 1:水趣 44:绿源 50:四梦泉
    productName: "",
    c: null,
    theme: 1
  }
})

