// 引用百度地图微信小程序JSAPI模块 
const app = getApp();
var bmap = require('../../utils/bmap-wx.min.js');
var wxMarkerData = []; 
Page({

  /**
   * 页面的初始数据
   */
  data: {
      markers: [],
      latitude: '',
      longitude: '',
      rgcData: {}
  },
  makertap: function (e) {
    //   console.log(e);
      var that = this;
      var id = e.markerId;
    //   that.showSearchInfo(wxMarkerData, id);
    //   console.log(id,111);
  },
  addMaker:function(e){
      this.moveToLocation();
    // console.log(e);
  },
  getLngLat: function () {
        var that = this;
        this.mapCtx = wx.createMapContext("map");
        this.mapCtx.getCenterLocation({
            success: function (res) {
                // console.log(res);
                that.showSearchInfo(wxMarkerData,0);
                that.setData({
                    longitude: res.longitude
                    , latitude: res.latitude
                    , markers: [
                        {
                            id: 0
                            , iconPath: "../../images/marker_red.png"
                            , longitude: res.longitude
                            , latitude: res.latitude
                        }
                    ]
                })

            }
        })
  },
  regionchange(e) {
        // console.log(e);
        
        // 地图发生变化的时候，获取中间点，也就是用户选择的位置
        if(e.type == 'end') {
            this.getLngLat();
        }
  },  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var that = this;
      // 新建百度地图对象 
      var BMap = new bmap.BMapWX({
          ak: 'CFecNbZbabHRfdW8QCz40xqkvrNrB3Bu'
      });
      var fail = function (data) {
          console.log(data);
      };
      var success = function (data) {
          //返回数据内，已经包含经纬度  
          console.log(data);
          //使用wxMarkerData获取数据  
          wxMarkerData = data.wxMarkerData;
          //把所有数据放在初始化data内  
          that.setData({
              markers: wxMarkerData,
              latitude: wxMarkerData[0].latitude,
              longitude: wxMarkerData[0].longitude,
              address: wxMarkerData[0].address,
              cityInfo: data.originalData.result.addressComponent
          });
      }    
    //   that.locationAuto();
    //   var fail = function (data) {
    //       console.log(data)
    //   };
    //   var success = function (data) {
    //     //   console.log(data);
    //       wxMarkerData = data.wxMarkerData;
    //       that.setData({
    //           markers: wxMarkerData,
    //           latitude: wxMarkerData[0].latitude,
    //           longitude: wxMarkerData[0].longitude,
    //           rgcData: {
    //               address: '地址：' + data.originalData.result.formatted_address
    //           }  
    //       });
        
    //   }
      // 动态设置map的宽和高
    //   wx.getSystemInfo({
    //       success: function (res) {
    //           console.log('getSystemInfo');
    //           console.log(res.windowWidth);
    //           that.setData({
    //               map_width: res.windowWidth
    //               , map_height: res.windowWidth
    //               , controls: [{
    //                   id: 1,
    //                   iconPath: '../../images/marker_red.png',
    //                   position: {
    //                       left: res.windowWidth / 2 - 8,
    //                       top: res.windowHeight / 2
    //                   },
    //                   clickable: true
    //               }]
    //           })
    //       }
    //   });
      // 发起regeocoding检索请求 
      BMap.regeocoding({
          fail: fail,
          success: success,
          iconPath: '../../images/marker_red.png',
          iconTapPath: '../../images/marker_red.png'
      });
  },
  getCenterLocation: function () {
      this.mapCtx.getCenterLocation({
          success: function (res) {
              console.log(res.longitude)
              console.log(res.latitude)
          }
      })
  },
  moveToLocation: function () {
      this.mapCtx.moveToLocation()
  },
  locationAuto:function(){
      var fail = function (data) {
          console.log(data)
      };
      var success = function (data) {
          console.log(data);
          wxMarkerData = data.wxMarkerData;
          that.setData({
              markers: wxMarkerData
          });
          that.setData({
              latitude: wxMarkerData[0].latitude
          });
          that.setData({
              longitude: wxMarkerData[0].longitude
          });
      }
  },
  showSearchInfo: function (data, i) {
      var that = this;
      that.setData({
          rgcData: {
              address: '地址：' + data[i].address + '\n'
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