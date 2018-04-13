//注册is操作符
    var isArray = function(value) {
        return Object.prototype.toString.call(value) === '[object Array]';
    }

    var ExpressionRegistry = function() {
        this.expressions = [];
    };

    ExpressionRegistry.prototype.add = function (operator, method) {
        this.expressions[operator] = method;
    };

    ExpressionRegistry.prototype.call = function (operator, left, right) {
        if ( ! this.expressions.hasOwnProperty(operator)) {
            throw new Error('Unknown operator "'+operator+'"');
        }

        return this.expressions[operator](left, right);
    };
    var eR = new ExpressionRegistry;
    eR.add('not', function(left, right) {
        return left != right;
    });
    eR.add('>', function(left, right) {
        return left > right;
    });
    eR.add('<', function(left, right) {
        return left < right;
    });
    eR.add('>=', function(left, right) {
        return left >= right;
    });
    eR.add('<=', function(left, right) {
        return left <= right;
    });
    eR.add('===', function(left, right) {
        return left === right;
    });
    eR.add('!==', function(left, right) {
        return left !== right;
    });
    eR.add('in', function(left, right) {
        if ( ! isArray(right)) {
            right = right.split(',');
        }
        return right.indexOf(left) !== -1;
    });
    var isHelper = function() {
        var args = arguments
        ,   left = args[0]
        ,   operator = args[1]
        ,   right = args[2]
        ,   options = args[3]
        ;

        if (args.length == 2) {
            options = args[1];
            if (left) return options.fn(this);
            return options.inverse(this);
        }

        if (args.length == 3) {
            right = args[1];
            options = args[2];
            if (left == right) return options.fn(this);
            return options.inverse(this);
        }

        if (eR.call(operator, left, right)) {
            return options.fn(this);
        }
        return options.inverse(this);
    };

    Handlebars.registerHelper('is', isHelper);
    
//注册is操作符 end
var sumHelper = function(){
    var arr = arguments[0],
        key = arguments[1];
    var args = arr;
    var i = args.length, sum = 0;
 
    for(var j=0; j<i; j++){
        sum = arr[j][key] + sum;
        //console.log(arr[j][key]);
    }
    return sum;
};
Handlebars.registerHelper('sum', sumHelper);
Handlebars.registerHelper('ceil', function(value){
    var num = value/1000;
    num = num.toFixed(2);
    return num;
});
//去除小数点，取整数
Handlebars.registerHelper('parseInt', function(value){
    var num = parseInt(value);
    return num;
});
Handlebars.registerHelper('getArrayLength', function(){
    var arr = arguments[0];
    var arrl = arr.length;
    return arrl;
});
Handlebars.registerHelper('showstars', function(value){
    if(value>=5){
      var value=5;
    }
    var activeNum = Math.floor(value);
    var halfNum = 0;
    var defaultNum = 5 - activeNum - halfNum;

    var Astars = '<span class="icon icon-86star active"></span>';
    var Hstars = '<span class="icon icon-star02"></span>';
    var Dstars = '<span class="icon icon-86star"></span>';

    var allStars = '';
    for(var i=0; i<activeNum; i++){
        allStars = allStars + Astars;
    }
    for(var i=0; i<halfNum; i++){
        allStars = allStars + Hstars;
    }
    for(var i=0; i<defaultNum; i++){
        allStars = allStars + Dstars;
    }

    return allStars;
});
Handlebars.registerHelper('showShopStars', function(value){
    if(value>=10){
      var value=10;
    }
    var activeNum = Math.floor(value/2);
    var halfNum = value%2;
    var defaultNum = 5 - activeNum - halfNum;

    var Astars = '<span class="icon icon-86star active"></span>';
    var Hstars = '<span class="icon icon-star02"></span>';
    var Dstars = '<span class="icon icon-86star"></span>';

    var allStars = '';
    for(var i=0; i<activeNum; i++){
        allStars = allStars + Astars;
    }
    for(var i=0; i<halfNum; i++){
        allStars = allStars + Hstars;
    }
    for(var i=0; i<defaultNum; i++){
        allStars = allStars + Dstars;
    }

    return allStars;
});
//获取URL中参数
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

// 倒计时类
var CountDown = {
    // 动态秒数
    wait:0,
    // 初始化秒数  初始化设置从多少开始倒数
    initWait:0,
    // 需要倒数元素的内容 html()
    oHtml:'',
    // 需要倒数元素的文字颜色
    oColor:'',
    // 需要倒数元素的背景颜色
    oBackgroundColor:'',
    // 需要倒数的元素
    oInput:'',
    // 倒数完成后的回调函数 如果没有 可以不传
    action:'',
    // 倒数时的回调函数 如果没有 可以不传
    out:'',
    // 运行倒数函数
    time:function(){
        if (CountDown.wait == 0) {  
            CountDown.oInput.html(CountDown.oHtml);
            CountDown.wait = CountDown.initWait;
            CountDown.oInput.css({'background-color':CountDown.oBackgroundColor,'color':CountDown.oColor});
            if(typeof CountDown.action === 'function'){
                CountDown.action();
            }
        } else {  
            CountDown.oInput.css({'background-color':'#7E8699','color':'#FFF'});
            if(typeof CountDown.out === 'function'){
                CountDown.out();
            }
            CountDown.oInput.html(""+CountDown.wait+"s");  
            CountDown.wait--;  
            setTimeout(function() {
                CountDown.time()
            },  
            1000)  
        }  
    },

    // 初始化 参数分别是 o=需要倒数的元素 wait=初始化秒数 action=倒数完成后的回调函数 out=倒数时的回调函数
    init:function(o,wait,action,out){
        CountDown.wait=parseInt(wait);
        CountDown.initWait=parseInt(wait);
        CountDown.oInput=o;
        CountDown.oColor=o.css('color');
        CountDown.oBackgroundColor=o.css('background-color');
        CountDown.oHtml=o.html();
        CountDown.action=action;
        CountDown.out=out;
    }
}