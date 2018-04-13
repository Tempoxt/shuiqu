// 初始化放到script最前面 免得不能$.alert();
var pwdBox = PwdBox = {
    template: '<style>.flexable{display: -webkit-box;height:25%;} .flexable>div{-webkit-box-flex: 1;} .flexable.password>div{opacity: 0};.flexable.password>div.active{opacity: 1 !important;}.input-box{position: absolute;bottom: 0;width: 104%;height: 57%;left: -2%;}.password{position: absolute;width: 90%;top: 60%;left: 5%;height: 19%;}.password>div{height:46px;line-height:46px;text-align:center}.password>div:first-child{border-top-left-radius:5px;border-bottom-left-radius:5px}.password>div:last-child{border-top-right-radius:5px;border-bottom-right-radius:5px}.input-box .flexable>div{height:53px}.input-box .flexable>div:active{background:rgba(142,142,142,.5)}.list-block .item-title0{font-weight:normal!important;font-size:14px}ul li{position:relative}.close{position:absolute;left:16%;font-size:20px;width:22px;text-align:center;margin-top: -12.5%;} h1.title0{height:50px;font-size:18px;line-height:50px;text-align:center;margin: 0;position: relative;left: 1%;margin-top: -20%;} .notice{height:30px;line-height:30px;font-size:12px;text-align:center;margin-bottom:15px;color: #00a9dd;display:none;}</style>' + '<div class="password-box" style="position: fixed;top:0;left:0;z-index: 10000;width: 100%;height: 100%;background: rgba(0,0,0,.7);display:none;">' + '<div class="inner-box" style="position: fixed;bottom: 0;left: 0;width: 100%;height: 380px;background: url(/files/images/pwd_keyboard_b.png) center bottom / 100% 57% no-repeat;">' + '<div style="position: absolute;width: 100%;height: 100%;text-align: right;top: -25%;" >' + '<div style="width: 70%;height: 50%;position:  absolute;margin-left: 15%;margin-top: -20%;background: #F5F5F5 url(/files/images/pwd_keyboard_t.png) center bottom / 100% 57% no-repeat;border-radius: 8px;">' +'<div class="flexable password">' + '<div >●</div>' + '<div >●</div>' + '<div >●</div>' + '<div >●</div>' + '<div >●</div>' + '<div >●</div>' + '<!--●-->' + '</div>' + '<h2 class="setZFMone" style="text-align: center;margin-top: 20%;"></h2><a href="/weixin/tuiKeRetrievePwd?page=forget" style="color: #f9d600;font-size: 14px;width: 40%;position:  absolute;right: 0%;bottom: 4%;">忘记密码？</a></div>' + '<h1 class="title0">支付密码</h1>' + '<span class="iconfont icon-guanbi close">×</span>' +  '<div class="notice color-lightblue">请输入支付密码！</div>' + '</div>' + '<div class="input-box">' + '<div class="flexable">' + '<div class="input-key" data-label="1" ></div><div class="input-key" data-label="2"></div><div class="input-key"  data-label="3"></div>' + '</div>' + '<div class="flexable">' + '<div class="input-key" data-label="4" ></div><div class="input-key" data-label="5"></div><div class="input-key" data-label="6"></div>' + '</div>' + '<div class="flexable">' + '<div class="input-key" data-label="7" ></div><div class="input-key" data-label="8"></div><div class="input-key" data-label="9"></div>' + '</div>' + '<div class="flexable">' + '<div></div><div class="input-key" data-label="0" ></div><div class="input-key" data-label="del"  ></div>' + '</div>' + '</div>' + '</div>' + '</div>',
    passwordOrg: '',
    password: '',
    inited: false,
    money: 0,
    callback: function(res) {
        if (res) {
            // alert('密码正确');
            document.querySelector('.password-box').style.display = 'none'
        } else {
            // alert('密码错误')
        }
    },
    setMoney: function(res){
        pwdBox.money=res;
        $('.setZFMone').html("￥"+res);
    },
    init: function(password, keyboard, title0, notice) {
        if (pwdBox.inited) {
            return
        }
        console.log(document.getElementsByTagName('body'));
        document.getElementsByTagName('body')[0].innerHTML += pwdBox.template;
        if (keyboard) {
            document.querySelector('.password-box .inner-box').style.backgroundImage = keyboard
        }
        title0 && (document.querySelector('h1.title0').innerText = title0);
        notice && (document.querySelector('.password-box .notice').innerText = notice);
        password && (pwdBox.passwordOrg = password);
        document.querySelector('.close').addEventListener('click', function() {
            document.querySelector('.password-box').style.display = 'none';
            pwdBox.reset()
        });
        var inputs = document.querySelectorAll('.input-key');
        for (var i = 0; i < inputs.length; i++) {
            inputs[i].addEventListener('touchstart', function(e) {
                onTouch(this.getAttribute('data-label'))
            }, true)
        }
        var onTouch = function(label) {
            if (label == 'del') {
                pwdBox.password = pwdBox.password.substr(0, pwdBox.password.length - 1);
                pwdBox.onChange()
            } else {
                pwdBox.password += label;
                pwdBox.onChange();
                if (pwdBox.password.length == 6) {

                    $.ajax({
                        type: "post",
                        url: "/weixin/txToWxWallet",
                        dataType: 'json',
                        async:false,
                        data:{
                            money:pwdBox.money,
                            dealPass:pwdBox.password
                        },
                        complete: function(json) {

                            var data = json.response;
                            var status = json.status;
                            if (status != 200) {
                                $.alert('网络异常，状态为22.1' + status);
                                // isSwitch = true;
                                return;
                            }
                            if ($.type(data) == 'string') {
                                data = JSON.parse(data);
                            }
                            if (data.code != 'E00000') {
                                $.alert(data.message);
                                // isSwitch = true;
                                pwdBox.callback({
                                    status: false,
                                    password: pwdBox.password
                                })
                                // return;
                            } else {
                                $.alert("您的提现申请已提交成功！")
                                pwdBox.callback({
                                    status: true,
                                    password: pwdBox.password
                                })
                            }
                        }
                    });
                    /*if (pwdBox.passwordOrg) {
                        if (pwdBox.password == pwdBox.passwordOrg) {
                            pwdBox.callback({
                                status: true,
                                password: pwdBox.password
                            })
                        } else {
                            pwdBox.callback({
                                status: false,
                                password: pwdBox.password
                            })
                        }
                    } else {
                        pwdBox.callback({
                            status: true,
                            password: pwdBox.password
                        })
                    }*/
                }
            }
        };
        pwdBox.inited = true
    },
    onChange: function() {
        var texts = document.querySelectorAll('.password>div');
        for (var i = 0; i < texts.length; i++) {
            texts[i].style.opacity = 0
        }
        for (i = 0; i < pwdBox.password.length; i++) {
            texts[i].style.opacity = 1
        }
    },
    reset: function() {
        pwdBox.password = '';
        pwdBox.onChange();
        document.querySelector('.password-box').style.display = 'none'
    },
    show: function(callback) {
        if (callback) {
            pwdBox.callback = callback;
        }
        document.querySelector('.password-box').style.display = 'block'
    }
};