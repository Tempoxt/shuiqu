// pages/tuihuo/tuihuo.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    index:0,
    th_remark:''
  },
  /**改变申请类型 */
  toggleType:function(e){
      var type = e.currentTarget.dataset.type;
      console.log(e.currentTarget.dataset.type);
      this.setData({ tuihuoType:type});
  },
  /**改变申请原因 */
  changeReason:function(e){
      var index = e.detail.value;
      var th_reason = this.data.tuihuo.th_reasons[index];
      console.log(e.detail.value);
      this.setData({
          index:index,
          th_reason:th_reason
      });
  },
  /**留言 */
  inputMessage:function(e){
      var th_remark = e.detail.value;
      this.setData({
          th_remark: th_remark
      });
      console.log(e.detail.value);
  },
  /**提交申请 */
  submitApply:function(e){
      var orderId = this.data.orderId;
      var th_reason = this.data.th_reason;
      var th_remark = this.data.th_remark;
      var th_type = this.data.tuihuoType;
    //   console.log(this.data.orderId);
    //   console.log(this.data.th_reason);
    //   console.log(this.data.th_remark);
    //   console.log(this.data.tuihuoType);
      if (th_remark == ''){
        wx.showToast({
            title: '留言不能为空',
            image: '../../images/error_popover.png'
        })
      }else{
          var orderJson = {
              orderId: orderId,
              th_type: th_type,
              th_reason: th_reason,
              th_remark: th_remark
          }
        //   console.log(orderJson);
        //   return;
        // 保存退货订单
          wx.request({
              url: app.globalData.domain + "/wxLiteapp/saveOrderTuiHuo",
              method: 'POST',
              header: {
                  'content-type': 'application/x-www-form-urlencoded'
              },
              data: {
                  cacheKey: app.globalData.cacheKey,
                  c: app.globalData.c,
                  orderJson: JSON.stringify(orderJson)
              },
              success: function (res) {
                console.log(res.data.data);
                wx.showModal({
                    content: '申请成功',
                    showCancel:false,
                    success:function(){
                        wx.setStorageSync('orderFlag', true);
                        wx.navigateBack({
                            delta:1
                        })
                    }
                })
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
      
  },  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var that = this;
      var orderId = options.orderId;
      that.setData({
          orderId:orderId,
          theme:app.globalData.theme
      });
    //   获取退货参数
      wx.request({
          url: app.globalData.domain + "/wxLiteapp/orderTuihuoParam",
          method: 'POST',
          header: {
              'content-type': 'application/x-www-form-urlencoded'
          },
          data: {
              cacheKey: app.globalData.cacheKey,
              c: app.globalData.c,
              orderId: orderId
          },
          success: function (res) {
              console.log(res.data.data);
              var th_reason = res.data.data.th_reasons[0];
              wx.hideLoading();
              that.setData({ 
                tuihuo:res.data.data,
                th_reason:th_reason,
                tuihuoType: '我要退款（无需退货）'
              });  
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
    console.log('见底了！');
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  }
})