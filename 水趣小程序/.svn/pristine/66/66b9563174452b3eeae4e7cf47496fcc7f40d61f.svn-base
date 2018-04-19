// pages/evaluate/evaluate.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
      feel:''
  },
//   修改评价星级
  changeStar:function(e){
      var index = e.currentTarget.dataset.index;
      var projectId = e.currentTarget.dataset.projectid;
      console.log(index, projectId);
      var evaluate = this.data.evaluate;
      for (var i = 0; i < evaluate.orderTags.length;i++){
          if (projectId == evaluate.orderTags[i].projectId){
              
              evaluate.orderTags[i].defaultName = evaluate.orderTags[i].projectGrades[index].gradeName;
              evaluate.orderTags[i].defaultGrades = evaluate.orderTags[i].projectGrades[index].grade;

              for (var j = 0; j < evaluate.orderTags[i].projectGrades.length;j++){
                  evaluate.orderTags[i].projectGrades[j].defaultGrades = evaluate.orderTags[i].projectGrades[index].grade;
              }
            
          }
      }
    
      this.setData({
          evaluate: evaluate,
      });
      console.log(evaluate);
  },
//   修改商品评价星级
  changeProductStar:function(e){
      var that = this;
      var index = e.currentTarget.dataset.index;
      var projectId = e.currentTarget.dataset.projectid;
      var pid = e.currentTarget.dataset.pid;
      console.log(index, projectId);
      var evaluate = this.data.evaluate;
      for (var i = 0; i < evaluate.goods.length; i++) {
          if (pid == evaluate.goods[i].pid) {

              evaluate.goods[i].defaultName = evaluate.productTags[0].projectGrades[index].gradeName;
              evaluate.goods[i].defaultGrades = evaluate.productTags[0].projectGrades[index].grade;
              

          }
      }
      that.setData({
          evaluate: evaluate
      });
      console.log(evaluate);
  },
  /*服务感受*/  
  serviceFeel:function(e){
      var feel = e.detail.value;
      var evaluate = this.data.evaluate;
      evaluate.orderTags.forEach(function(item){
        item.content = feel;
      });
      this.setData({ evaluate: evaluate});  
  },
  /*商品评价*/
  productEvaluate:function(e){
      var pid = e.currentTarget.dataset.pid;
      var content = e.detail.value;
      var evaluate = this.data.evaluate;
      evaluate.goods.forEach(function(item){
            if(pid==item.pid){
                item.content = content;
            }
      });
      this.setData({
          evaluate: evaluate
      })
      console.log(evaluate);
  },
  /**发表评价 */
  submitEvaluate:function(){
      var that = this;
      var evaluate = this.data.evaluate;
      var orderEvaluate = [];
      var productEvalutes = [];
      evaluate.orderTags.forEach(function(item){
          orderEvaluate.push({ projectId: item.projectId, projectName: item.projectName, grade: item.defaultGrades, gradeName: item.defaultName, content: item.content});
      });
      evaluate.goods.forEach(function(item){
          productEvalutes.push({ projectId: item.projectId, projectName: item.projectName, grade: item.defaultGrades, gradeName: item.defaultName, content: item.content, pics: item.pics, pid: item.pid});
      });
      console.log(orderEvaluate);
      console.log(productEvalutes);
    
      wx.request({
          url: app.globalData.domain + "/wxLiteapp/addOrderEvaluate",
          method: 'POST',
          header: {
              'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
              cacheKey: app.globalData.cacheKey,
              c: app.globalData.c,
              orderId: that.data.orderId,
              shopId: that.data.shopId,
              orderEvaluate: JSON.stringify(orderEvaluate),
              productEvalutes: JSON.stringify(productEvalutes)
          },
          success: function (res) {
              var code = res.data.code;
              
              if (code == 'E00000') {
                  wx.setStorageSync('evaluateDone', true);
                  console.log(res.data);
                  wx.showModal({
                      content: '评价成功',
                      showCancel:false,
                      success:function(){
                          wx.navigateBack({
                              delta: 1
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
                  title: '',
                  showCancel: false,
                  content: "网络异常！"
              })
          }
      })
      
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var shopId = options.shopId;
    var orderId = options.orderId;
    this.setData({ 
        shopId: shopId,
        orderId: orderId,
        theme:app.globalData.theme
    });
    wx.request({
        url: app.globalData.domain + "/wxLiteapp/toOrderEvaluate",
        method: 'POST',
        header: {
            'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
            cacheKey: app.globalData.cacheKey,
            c: app.globalData.c,
            orderId:orderId,
            shopId:shopId
        },
        success: function (res) {
            var code = res.data.code;
            if (code == 'E00000') {
                var evaluate = res.data.data;
                evaluate.orderTags.forEach(function(item){
                    item.projectGrades.forEach(function(grade){
                        grade.projectId = item.projectId;
                        grade.defaultGrades = 5;
                    });
                    item.defaultName = '非常满意';
                    item.defaultGrades = 5;
                    item.content = '';

                });
                evaluate.goods.forEach(function (item) {
                    item.defaultGrades =5;
                    item.defaultName = '非常满意';
                    item.projectId = evaluate.productTags[0].projectId;
                    item.projectName = evaluate.productTags[0].projectName;
                    item.pics = [];
                    item.content = '';
                });
                that.setData({
                    evaluate: evaluate
                });
                console.log(evaluate);
                // console.log(res.data.data);
            } else {
                wx.showModal({
                    content: res.data.message,
                    showCancel: false
                })
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