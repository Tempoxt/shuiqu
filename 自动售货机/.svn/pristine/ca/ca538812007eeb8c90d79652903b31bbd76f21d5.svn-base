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
//索引加1
Handlebars.registerHelper('addOne',function(index){  
        return index + 1;  
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