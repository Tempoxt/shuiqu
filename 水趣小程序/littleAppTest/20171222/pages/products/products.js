// pages/products/products.js
const app = getApp();
Page({
    data: {
        hasBind: -3,
        currentCity: "选城市",
        modalWrap: "",
        modal: "",
        getCodeText: "获取验证码",
        getCodeColor: "#EECE1F",
        selectStatus: true,
        userInfo: {},
        shopProducts: '',//购物车数据
        toView: '0',
        offsetTop: [],
        totalPrice: 0,// 总价格
        totalCount: 0, // 总商品数
        carArray: [],
        minPrice: 20,//起送價格
        payDesc: '',
        deliveryPrice: 4,//配送費
        cartShow: 'none',
        status: 0,
        shopId: '',
        cats: 0,
        shopMap:'',
        evaluateList:''
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
        this.setData({
            modalWrap: true,
            modal: true
        });
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
            });
        }else if(!regPhone.test(this.data.phoneNum)) {
            wx.showToast({
                title: '手机号不正确',
            })
        } else {
            var that = this;
            function timeOver() {
                if (that.data.getCodeText == 0) {
                    clearInterval(Countdown);
                    that.setData({
                        getCodeText: "获取验证码",
                        getCodeColor: "#EECE1F",
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
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    cellphone: that.data.phoneNum
                },
                success: function (result) {

                    console.log(result);
                }
            })
            //验证码倒计时，改变状态，不可点击
            this.setData({
                getCodeColor: "#ddd",
                getCodeText: "60",
                getCodeStatus: true
            });
            var Countdown = setInterval(function () {
                that.setData({
                    getCodeText: that.data.getCodeText - 1
                });
                timeOver();
            }, 1000)
        }
    },
    /**
     * 输入手机号时
     */
    inputPhoneNum: function (e) {
        var that = this;
        this.setData({
            phoneNum: e.detail.value
        });
        console.log(this.data.phoneNum);
    },
    /**
     * 输入验证码时
     */
    inputValidateCode: function (e) {
        this.setData({
            validateCode: e.detail.value
        });
        if (this.data.phoneNum != "" && this.data.validateCode != "" && this.data.selectStatus) {
            this.setData({
                commitColor: "#EECE1F",
                bindStatus: false
            });
        } else {
            this.setData({
                commitColor: "#ddd",
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
        if (this.data.phoneNum != "" && this.data.validateCode != "" && this.data.selectStatus) {
            this.setData({
                commitColor: "#EECE1F",
                bindStatus: false
            });
        } else {
            this.setData({
                commitColor: "#ddd",
                bindStatus: true
            });
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
                        cacheKey: wx.getStorageSync('cacheKey'),
                        c: app.globalData.c,
                        cellphone: that.data.phoneNum,
                        code: that.data.validateCode
                    },
                    success: function (result) {
                        if (result.data.code != "E00000") {
                            wx.showModal({
                                title: '',
                                showCancel: false,
                                content: result.data.message
                            })
                        } else {
                            wx.showModal({
                                title: '',
                                showCancel: false,
                                content: "注册成功"
                            })
                            that.setData({
                                hasBind: 1
                            });
                            wx.setStorageSync('hasBind', 1);
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
                    title: '请选择城市'
                })
            }


        }

    },
    /**
     * 选择城市
     */
    getCity: function () {
        var that = this;
        wx.request({
            url: app.globalData.domain + "/wxLiteapp/cityJson",
            method: 'POST',
            header: {
                'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
                cacheKey: that.data.cacheKey,
                c: app.globalData.c
            },
            success: function (res) {
                var citys = {};
                var allCitys = [];
                citys.hotCitys = res.data.hotCitys;
                //循环所有城市
                for (var j = 0; j < res.data.allCitys.length; j++) {
                    var flag = false
                    for (var i = 0; i < allCitys.length; i++) {
                        //如果新数组里有这个首字母
                        // console.log(res.data.allCitys[j].firstLetter);
                        if (allCitys[i].firstLetter == res.data.allCitys[j].firstLetter) {
                            flag = true;
                            allCitys[i]['cityInfo'].push({ "baiduCode": res.data.allCitys[j].baiduCode, "cityId": res.data.allCitys[j].cityId, "cityName": res.data.allCitys[j].cityName, "location": res.data.allCitys[j].location });
                        }
                    }
                    //没有这个首字母
                    if (!flag) {
                        allCitys.push({ "firstLetter": res.data.allCitys[j].firstLetter, "cityInfo": [{ "baiduCode": res.data.allCitys[j].baiduCode, "cityId": res.data.allCitys[j].cityId, "cityName": res.data.allCitys[j].cityName, "location": res.data.allCitys[j].location }] })

                    }




                }
                citys.allCitys = allCitys;
                console.log(citys);
                that.setData({
                    cityList: citys,
                    shopList: null,
                    sugData: ''
                });
            }
        })
    },
    closeCityList: function () {
        this.setData({
            cityList: false
        });
    },
    selectCity: function (e) {
        var that = this;
        // console.log(e);
        var cityname = e.currentTarget.dataset.cityname;
        var cityId = e.currentTarget.dataset.id;
        var location = e.currentTarget.dataset.location;
        //获取积分和电子票数据
        wx.request({
            url: app.globalData.domain + "/wxLiteapp/saveCity",
            method: 'POST',
            header: {
                'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
                cacheKey: wx.getStorageSync('cacheKey'),
                c: app.globalData.c,
                cityId: cityId,
                location: location

            },
            success: function (result) {
                if (result.data.message == "成功") {
                    that.closeCityList();
                    that.setData({
                        currentCity: cityname,
                        location: location
                    });
                } else {
                    console.log('网络异常');
                }

            }, fail: function (err) {
                wx.showToast({
                    title: '网络异常！',
                })
            }
        });
        console.log(cityname, location);

    },
    /**
     * 选择产品分类菜单
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
        var hasBind = wx.getStorageSync('hasBind');
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
            number: that.data.shopProducts[parentIndex].products[itemIndex].number-1,
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
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    tag: 'DEL',
                    products: JSON.stringify(shopCarData)
                },
                success: function (res) {
                    var code = res.data.code;
                    if (code = 'E00000') {
                        console.log(res);
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
        } else {
            
            wx.request({
                url: app.globalData.domain + "/wxLiteapp/editShopCart",
                method: 'POST',
                header: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                    cacheKey: wx.getStorageSync('cacheKey'),
                    c: app.globalData.c,
                    products: JSON.stringify(shopCarData)
                },
                success: function (res) {
                    var code = res.data.code;
                    if (code = 'E00000') {
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
        };
        //商品总数
        that.data.totalCount--;
        //获取缓存数据
        // var shopCarData = wx.getStorageSync('shopCarData');
        
        //先判断data中是否有相同的商品
        // for (var i = 0; i < shopCarData.length; i++) {
        //     var decreaseFlag = false;
        //     // debugger;
        //     //如果商品已经存在data中，则数量-1
        //     // debugger;
        //     if (shopCarData[i].pid === that.data.shopProducts[parentIndex].products[itemIndex].pid) {
        //         //   debugger;
        //         decreaseFlag = true;
        //         shopCarData[i].number--;
        //         if (shopCarData[i].number == 0) {
        //             shopCarData.splice(i, 1)
        //         }
        //         break;
        //     }

        // }
        // if (decreaseFlag == false) {
        //     shopCarData.push({
        //         shopId:that.data.shopId,
        //         pid: that.data.shopProducts[parentIndex].products[itemIndex].pid,
        //         number: that.data.shopProducts[parentIndex].products[itemIndex].number,
        //         settleStyle:"现金"
        //     });

        // }
        this.setData({
            shopProducts: this.data.shopProducts,
            totalCount: this.data.totalCount--
        })
        // wx.setStorageSync('shopCarData', shopCarData);

        // this.setData({
        //     shopCarData: shopCarData,
        //     shopProducts: that.data.shopProducts,
        //     totalCount: that.data.totalCount
        // });
        // if (that.data.shopCarData.number == 1) {
        //     wx.request({
        //         url: app.globalData.domain + "/wxLiteapp/editShopCart",
        //         method: 'POST',
        //         header: {
        //             'content-type': 'application/x-www-form-urlencoded'
        //         },
        //         data: {
        //             cacheKey: wx.getStorageSync('cacheKey'),
        //             tag: 'TAG',
        //             products: JSON.stringify(that.data.shopCarData)
        //         },
        //         success: function (result) {
        //             console.log(result);

        //         }
        //     });
        // } else {
        //     wx.request({
        //         url: app.globalData.domain + "/wxLiteapp/editShopCart",
        //         method: 'POST',
        //         header: {
        //             'content-type': 'application/x-www-form-urlencoded'
        //         },
        //         data: {
        //             cacheKey: wx.getStorageSync('cacheKey'),
        //             products: JSON.stringify(that.data.shopCarData)
        //         },
        //         success: function (result) {
        //             console.log(result);

        //         }
        //     });
        // }
        // console.log(that.data.shopProducts[parentIndex].products[itemIndex].number);
    },
    //添加商品
    addCart(e) {
        
        var that = this;
        var parentIndex = e.currentTarget.dataset.parentindex;
        var itemIndex = e.currentTarget.dataset.itemIndex;
        //判断是否登录
        var hasBind = wx.getStorageSync('hasBind');
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
        // var shopCarData = wx.getStorageSync('shopCarData');
        // console.log(this.data.shopProducts);
        //如果没有缓存数据则直接添加
        // if (shopCarData.length == 0) {
        //     shopCarData = [];
        //     shopCarData.push({
        //         shopId: that.data.shopId,
        //         pid: that.data.shopProducts[parentIndex].products[itemIndex].pid,
        //         number: that.data.shopProducts[parentIndex].products[itemIndex].number,
        //         settleStyle: "现金"
        //     });
        // } else {
        //     //先判断data中是否有相同的商品
        //     for (var i = 0; i < shopCarData.length; i++) {
        //         //如果商品已经存在data中，则数量+1
        //         var flag = false;
        //         if (shopCarData[i].pid === that.data.shopProducts[parentIndex].products[itemIndex].pid) {
        //             flag = true;
        //             shopCarData[i].number++;
        //             break;
        //         }

        //     }
        //     if (flag == false) {

        //         shopCarData.push({
        //             shopId: that.data.shopId,
        //             pid: that.data.shopProducts[parentIndex].products[itemIndex].pid,
        //             number: that.data.shopProducts[parentIndex].products[itemIndex].number,
        //             settleStyle: "现金"
        //         });

        //     }

        // }
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
                cacheKey: wx.getStorageSync('cacheKey'),
                c: app.globalData.c,
                products: JSON.stringify(shopCarData)
            },
            success: function (res) {
                var code = res.data.code;
                if (code = 'E00000') {
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
       
        // wx.removeStorageSync('shopCarData');
    },
    decreaseShopCart: function (e) {
        
        console.log(e);
    },
    addShopCart: function (e) {
        this.addCart(e);
    },
    //计算总价
    calTotalPrice: function () {
        // var carArray = this.data.carArray;
        // var totalPrice = 0;
        // var totalCount = 0;
        // for (var i = 0; i < carArray.length; i++) {
        //     totalPrice += carArray[i].price * carArray[i].num;
        //     totalCount += carArray[i].num
        // }
        // this.setData({
        //     totalPrice: totalPrice,
        //     totalCount: totalCount,
        //     //payDesc: this.payDesc()
        // });
    },
    //差几元起送
    // payDesc() {
    //     if (this.data.totalPrice === 0) {
    //         return `￥${this.data.minPrice}元起送`;
    //     } else if (this.data.totalPrice < this.data.minPrice) {
    //         let diff = this.data.minPrice - this.data.totalPrice;
    //         return '还差' + diff + '元起送';
    //     } else {
    //         return '去结算';
    //     }
    // },
    //結算
    pay(){
        var that = this;
        
        if (this.data.totalCount > 0) {
            
            //判断是否登录
            var hasBind = wx.getStorageSync('hasBind');
            if (hasBind != 1) {
                that.openModal();
                return;
            }else{
                // wx.navigateTo({
                //     url: '../shopCart/shopCart?shopId='+that.data.shopId
                // });
                // wx.navigateTo({
                //     url: '../shopCart/shopCart?shopId=' + that.data.shopId
                // });
                wx.navigateTo({
                    url: '../shopCart/shopCart?shopId=' + that.data.shopId
                });
            }
            
        } else {
            return;
        }

    },
    //去购物车结算按钮控制
    toggleList: function () {
        if (!this.data.totalCount) {
            return;
        }
        this.setData({
            fold: !this.data.fold,
        })
        var fold = this.data.fold
        //console.log(this.data.fold);
        this.cartShow(fold)
    },
    cartShow: function (fold) {
        console.log(fold);
        if (fold == false) {
            this.setData({
                cartShow: 'block',
            })
        } else {
            this.setData({
                cartShow: 'none',
            })
        }
        console.log(this.data.cartShow);
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
        if (showtype == 1){
            var shopId = that.data.shopId;
            var evaluateId = 0;
            if (that.data.evaluateList){
                return;
            }else{
                that.getEvaluate(shopId, evaluateId);
            }
        //商家    
        }else if(showtype==2){
            if (that.data.shopMap){
                return;
            }else{
                wx.showLoading({
                    title: '加载中',
                });
                that.getShopInfo();
            }
            
        }
    },
    /**
     * 拨打水店电话
     */
    callShop:function(e){
        var phoneNumber = e.currentTarget.dataset.tel;
        console.log(e.currentTarget.dataset.tel);
        wx.makePhoneCall({
            phoneNumber: phoneNumber //仅为示例，并非真实的电话号码
        })
    },
    /***
     * 查看资质证照
     */
    previewlmg:function(e){
        var that = this;
        var index = e.currentTarget.dataset.index;
        console.log(e,e.currentTarget.dataset.index);
        wx.previewImage({
            current: that.data.shopMap.certificate[index], // 当前显示图片的http链接
            urls: that.data.shopMap.certificate // 需要预览的图片http链接列表
        })
    },  
    onLoad: function (options) {
        
        
        wx.showLoading({
            title: '加载中',
        });
        var that = this;
        //调用应用实例的方法获取全局数据  
        app.getUserInfo(function (userInfo) {
            //获取二维码携带的参数
            // console.log(app);
            // console.log(app.globalData.isLogin);
            if (options.scene) {
                var shopCode = decodeURIComponent(options.scene);
                wx.request({
                    url: that.globalData.domain + "/wxLiteapp/saveLastScanShopId",
                    method: 'POST',
                    header: {
                        'content-type': 'application/x-www-form-urlencoded'
                    },
                    data: {
                        cacheKey: wx.getStorageSync('cacheKey'),
                        c: that.globalData.c,
                        shopCode: shopCode
                    },
                    success: function (res) {
                        wx.setStorageSync('shopId', res.data.data.shopId);
                        // debugger;
                        // console.log(shopId+11111);
                        
                    }
                })
            }else{
                var shopId = options.shopId;
            }
            
        
            //更新数据
            var cacheKey = wx.getStorageSync('cacheKey');
            var hasBind = wx.getStorageSync('hasBind');
            // var shopId = wx.getStorageSync('shopId');
            // console.log(shopId);
            that.setData({
                userInfo: userInfo,
                cacheKey: cacheKey,
                hasBind: hasBind,
                shopId: shopId
            })
            
            that.getshopList(shopId, cacheKey);

            wx.getSystemInfo({
                success: function (res) {
                    that.setData({
                        winWidth: res.windowWidth,
                        winHeight: res.windowHeight
                    });
                }

            });
            
            
           
        }); 
        
    },
    refresh:function(){
        wx.showLoading({
            title: '加载中',
        });
        var that = this;
        app.getUserInfo(function (userInfo) {
            var shopId = wx.getStorageSync('shopId');
            //更新数据
            var cacheKey = wx.getStorageSync('cacheKey');
            var hasBind = wx.getStorageSync('hasBind');
            that.setData({
                userInfo: userInfo,
                cacheKey: cacheKey,
                hasBind: hasBind,
                shopId: shopId
            })
            that.getshopList(shopId, cacheKey);


            wx.getSystemInfo({
                success: function (res) {
                    that.setData({
                        winWidth: res.windowWidth,
                        winHeight: res.windowHeight
                    });
                }

            });
        });
    },
    /**
     * 获取水店列表
     */
    getshopList: function (shopId,cacheKey){
        var that = this;
        
        wx.request({
            url: app.globalData.domain + '/wxLiteapp/getShopProductJson_v2?random=' + Math.random(),
            method: 'POST',
            header: {
                'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
                cacheKey: cacheKey,
                c: app.globalData.c,
                shopId:shopId
            },
            success: function (res) {
                wx.hideLoading();
                var code = res.data.code;
                
                if (code = 'E00000') {
                    wx.setStorageSync('wxLocation', res.data.data.location);
                    wx.setStorageSync('cityName', res.data.data.cityName);
                    wx.setStorageSync('districtName', res.data.data.districtName);
                    // console.log(res);
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
                        shopProducts: res.data.data.shopProducts,
                        shopId: shopId,
                        totalCount: totalCount
                    });
                    var offsetTopArr = []
                    for (var i = 0; i < that.data.shopProducts.length; i++) {

                        offsetTopArr.push("order" + i);

                    }
                    wx.setStorageSync('isOnLoad', 1);
                } else if(code == '-3'){
                    app.login(function(){
                        wx.request({
                            url: app.globalData.domain + '/wxLiteapp/getShopProductJson_v2?random=' + Math.random(),
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
                                wx.hideLoading();
                                var code = res.data.code;

                                if (code = 'E00000') {
                                    wx.setStorageSync('wxLocation', res.data.data.location);
                                    wx.setStorageSync('cityName', res.data.data.cityName);
                                    wx.setStorageSync('districtName', res.data.data.districtName);
                                    // console.log(res);
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
                                        shopProducts: res.data.data.shopProducts,
                                        shopId: shopId,
                                        totalCount: totalCount
                                    });
                                    var offsetTopArr = []
                                    for (var i = 0; i < that.data.shopProducts.length; i++) {

                                        offsetTopArr.push("order" + i);

                                    }
                                    wx.setStorageSync('isOnLoad', 1);
                                } else if (code == '-3') {
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
     * 获取评价
     */
    getEvaluate: function (shopId,evaluateId){
        var that = this;
        wx.showLoading({
            title: '加载中',
        });
        wx.request({
            url: app.globalData.domain + "/wxLiteapp/getShopEvaluate",
            method: 'POST',
            header: {
                'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
                cacheKey: that.data.cacheKey,
                c: app.globalData.c,
                evaluateId:evaluateId,
                shopId: shopId
            },
            success: function (res) {
                var code = res.data.code;
                wx.hideLoading();
                if (code = 'E00000') {
                    that.setData({
                        evaluateList: res.data.data
                    });
                    console.log(res.data.data);
                } else {
                    wx.showModal({
                        content: res.data.message,
                        showCancel: false
                    })
                }
                
            },
            fail:function(err){
                wx.hideLoading();
                wx.showModal({
                    showCancel: false,
                    content: err
                })
            }
        })
    },
    /**
     * 获取商家信息
     */
    getShopInfo:function(){
        var that = this;
        wx.request({
            url: app.globalData.domain + "/wxLiteapp/getShopById",
            method: 'POST',
            header: {
                'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
                cacheKey: wx.getStorageSync('cacheKey'),
                c: app.globalData.c,
                shopId: that.data.shopId
            },
            success: function (res) {
                var code = res.data.code;
                wx.hideLoading();
                if (code = 'E00000') {
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
    },
    scroll: function (e) {
        console.log(e);
    },
    onReady: function () {

    },
    onShow: function () {
        var that = this;
        // console.log(options);
        // this.onLoad();
        // 页面显示
        // console.log(getCurrentPages());
        var isOnLoad = wx.getStorageSync('isOnLoad')
        console.log(isOnLoad);
        if (isOnLoad==1){
            that.refresh();
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
        return {
            title: app.globalData.productName,
            desc: '最具人气的订水平台!',
            path: 'pages/products/products'
        }
    }
})
