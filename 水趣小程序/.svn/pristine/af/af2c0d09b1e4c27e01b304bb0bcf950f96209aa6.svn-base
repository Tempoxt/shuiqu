// pages/productEvaluate/productEvaluate.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
      grade: -1,
      isPic:false
  },
  /*加载更多*/
  loadMore:function(){
      wx.showLoading({
          title: '加载更多',
      })
      var that = this;
      var pid = this.data.pid;
      var grade = this.data.grade;
      var pageIndex = this.data.pageIndex+1;
      var isPic = this.data.isPic;
      setTimeout(function(){
          wx.request({
              url: app.globalData.domain + "/wxLiteapp/getProductEvaluates",
              method: 'POST',
              header: {
                  'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  pid: pid,
                  isPic: isPic,
                  pageIndex: pageIndex,
                  grade: grade
              },
              success: function (res) {
                  var code = res.data.code;
                  if (code == 'E00000') {
                      console.log(res.data.data);
                      if (res.data.data.evaluates.length > 0) {
                          var result = that.data.result;
                          for (var i = 0; i < res.data.data.evaluates.length; i++) {
                              result.evaluates.push(res.data.data.evaluates[i]);
                          }
                          that.setData({ result: result, pageIndex: pageIndex });
                          wx.hideLoading();
                      } else {
                          wx.hideLoading();
                          wx.showToast({
                              title: '没有更多了',
                              image: '../../images/error_popover.png'
                          })
                      }


                  } else if (code == '-3') {

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
      },1000);
      
  },
  /*查看图片*/
  showImg:function(e){
      console.log(e);
      wx.previewImage({
          current: e.currentTarget.dataset.current, // 当前显示图片的http链接
          urls: e.currentTarget.dataset.arr // 需要预览的图片http链接列表
      })
  },
  /*切换评价类型*/
  toggleEvaluate:function(e){
      var that = this;
      var grade = e.currentTarget.dataset.grade;
      this.setData({ grade: grade, pageIndex:1});
      console.log(e.currentTarget.dataset.grade);
      wx.request({
          url: app.globalData.domain + "/wxLiteapp/getProductEvaluates",
          method: 'POST',
          header: {
              'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
              cacheKey: app.globalData.cacheKey,
              c: app.globalData.c,
              pid: that.data.pid,
              isPic: that.data.isPic,
              pageIndex: 1,
              grade: grade
          },
          success: function (res) {
              var code = res.data.code;
              if (code == 'E00000') {
                  console.log(res.data.data);
                  that.setData({ result: res.data.data});
                  wx.hideLoading();
              } else if (code == '-3') {

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
  /*是否显示图片*/
  toggleImage:function(){
      var that = this;
    var isPic = this.data.isPic;
    this.setData({ isPic: !isPic, pageIndex:1});
    wx.request({
        url: app.globalData.domain + "/wxLiteapp/getProductEvaluates",
        method: 'POST',
        header: {
            'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
            cacheKey: app.globalData.cacheKey,
            c: app.globalData.c,
            pid: that.data.pid,
            isPic: that.data.isPic,
            pageIndex: 1,
            grade: that.data.grade
        },
        success: function (res) {
            var code = res.data.code;
            if (code == 'E00000') {
                console.log(res.data.data);
                that.setData({ result: res.data.data});
                wx.hideLoading();
            } else if (code == '-3') {

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
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.showLoading({
        title: '加载中...',
    })
    this.setData({
        theme:app.globalData.theme
    });
    var that = this;
    var pid = options.pid;
    console.log(pid);
    // 获取商品评价
    wx.request({
        url: app.globalData.domain + "/wxLiteapp/getProductEvaluates",
        method: 'POST',
        header: {
            'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
            cacheKey: app.globalData.cacheKey,
            c: app.globalData.c,
            pid: pid,
            isPic: that.data.isPic,
            pageIndex:1,
            grade:-1
        },
        success: function (res) {
            var code = res.data.code;
            if (code == 'E00000') {
                console.log(res.data.data);
                that.setData({ 
                    result: res.data.data, 
                    pid: pid, 
                    count: res.data.data.count,
                    goodCount: res.data.data.goodCount,
                    centerCount: res.data.data.centerCount,
                    badCount: res.data.data.badCount,
                    pageIndex:1
                });
                wx.hideLoading();
            } else if (code == '-3') {
                
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
  
  }
})