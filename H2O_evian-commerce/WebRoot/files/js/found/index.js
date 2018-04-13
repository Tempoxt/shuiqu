var pageInfo = [{
    no: 1,
    canload: false
}, {
    no: 1,
    canload: false
}, {
    no: 1,
    canload: false
}, {
    no: 1,
    canload: false
}, {
    no: 1,
    canload: false
}];
var responseMsg = {
    '-3' : 'APP未登录',
    '-2' : '微信超时',
    '-1' : '微信未登录',
    '0' : '查询数据不存在',
    '2' : '数据库执行错误',
    '3' : '参数错误',
    '4' : '接口调用错误',
    '5' : '接口调用返回结果错误'
}
var tabIndex = 0;
var pageSize = 2;
var loading = false;
var userInfo = '';
var isApp = $('[name="isApp"]').val();

function AppMakeWebLoginJS(info){
    userInfo = info;
    // $.alert('AppMakeWebLoginJS run');
}
var updateIndex = function(){
    
    var id = $('.infinite-scroll.active').attr('id');
    switch (id) {
        case "tab1":
            tabIndex = 0;
            break;
        case "tab2":
            tabIndex = 1;
            break;
        case "tab3":
            tabIndex = 2;
            break;
        case "tab4":
            tabIndex = 3;
            break;
        case "tab5":
            tabIndex = 4;
            break;
    }
};
var getData = function(method) {
    var classId = $('.switch-wrap').attr('data-active-class');
    var tagId = $('.tabs').attr('data-active-tag');

    $.ajax({
        type: "post",
        url: "/found/ajax/articleList",
        data: {
            classId: classId,
            tagId: tagId,
            articleTitle: "",
            pageIndex: pageInfo[tabIndex].no,
            pageSize: pageSize
        },
        dataType: 'json',
        complete: function(json) {
            // 重置加载flag
            loading = false;
            var data = json.response;
                
            
            if ($.type(data) == 'string') {
                data = JSON.parse(json.response);
            }
            if(responseMsg[data.code]){
                $.alert(responseMsg[data.code]);
                return;
            }
            if (data.data.length == 0) {
                $('[data-role="null-data"]').html('没有更多内容了，请稍后刷新');
                // 加载完毕，则注销无限加载事件，以防不必要的加载:$.detachInfiniteScroll($('.infinite-scroll').eq(tabIndex));多个无线滚动请自行根据自己代码逻辑判断注销时机
                $.detachInfiniteScroll($('.tab').eq(tabIndex));
                pageInfo[tabIndex].canload = false;
                // 删除加载提示符
                $('.infinite-scroll-preloader').eq(tabIndex).hide();
                return;
            } else {
                pageInfo[tabIndex].canload = true;
                if(data.data.length >= pageSize){
                   $('.infinite-scroll-preloader').eq(tabIndex).show(); 
                }else{
                    $('.infinite-scroll-preloader').eq(tabIndex).hide();
                }
                var tpl = $("#tpl").html();
               
                var template = Handlebars.compile(tpl);
                var context = {
                    data: data.data
                };
                var html = template(context);
                if(method == 'pull'){
                    $('.infinite-scroll.active .list-container').append(html);
                }else{
                    $('.infinite-scroll.active .list-container').html(html);
                }
                
            }

            $.refreshScroller();
        }
    });
};

$(document).on("pageInit", function(e, id, page) {

    

    $(page).on('infinite', function() {

        // 如果正在加载，则退出
        if (loading) return;

        // 设置flag
        loading = true;
        
        //新增一页
        pageInfo[tabIndex].no = pageInfo[tabIndex].no + 1;
  
        getData('pull');
        
    });
});
$.init();

$(function() {
    getData();

    //点赞
    $('body').delegate('[data-role="support"]','click',  function(e) {
        e.preventDefault();
        
        var me = $(this),
            articleId = me.parents('li').data('articleId'),
            icon = me.children('[data-role="support-icon"]'),
            count = me.children('[data-role="support-count"]'),
            val = parseInt(count.data('val')),
            countVal = null,
            doSupport = function(){
                if (me.hasClass('active')) {
                    me.removeClass('active');
                    countVal = val - 1;
                    
                } else {
                    me.addClass('active');
                    countVal = val + 1;
                    
                }
                if (countVal == 0) {
                    count.html('赞');
                 
                } else {
                    count.html(countVal);
                  
                }

                count.attr('data-val', countVal);

                $.ajax({
                    type: "post",
                    url: "/found/ajax/articleLikeOperat",
                    data: {
                        articleId: articleId
                    },
                    dataType: 'json',
                    complete: function(json) {
                        
                        var data = json.response;
                        if ($.type(data) == 'string') {
                            data = JSON.parse(json.response);
                        }
                        if(responseMsg[data.code]){
                            $.alert(responseMsg[data.code]);
                            return;
                        }
                        
                    }
                });
            };
        //登录校验    
        if(isApp == '1'){
            //判断是否已登录
            $.ajax({
                type: "post",
                url: "/found/ajax/checkAppLogin",
                dataType: 'json',
                complete: function(json) {
                    
                    var data = json.response;
                    if ($.type(data) == 'string') {
                        data = JSON.parse(json.response);
                    }
                    //未登录
                    if(data.code == '-3'){
                        try{
                            userInfo = app.UserInfoFromApp();
                        }catch(err){
                            userInfo = UserInfoFromApp();
                        }
                        //重新登陆
                        $.ajax({
                            type: "post",
                            url: "/weixin/appLogin",
                            data: {
                                uinfo: userInfo
                            },
                            dataType: 'json',
                            success:function(result){
                                if(result.code == "E00000"){
                                    // $.alert("登陆成功");
                                    doSupport();
                                }else{
                                    $.alert(result.message);
                                }
                            },error:function(msg){
                                $.alert("点赞系统错误");
                            }
                        });
                    }else{
                        doSupport();
                    }
                }
            });
            
        }
        //else{
        //    alert("微信"); 
        //    doSupport(); 
        // }
        

        
    });
    //switch-tabs
    $('.switch-tabs').click(function(e) {
        e.preventDefault();
        var cont = $(this).data('tab');
        var classId = $(this).data('classId');
        
        $('.tabs').attr('data-active-tag', '');
        $('.switch-wrap').attr('data-active-class', classId);
        //切换状态
        $('.switch-wrap .switch-tabs').removeClass('active');
        $(this).addClass('active');
        $('.tab').removeClass('active').hide();
        $('#' + cont).show().addClass('active');
        updateIndex();
        $('[data-role="null-data"]').html('');
        $('.infinite-scroll-preloader').eq(tabIndex).show();
        //启用下拉请求
        $.attachInfiniteScroll($('.tab').eq(tabIndex));
        getData();

    });
    $('.tag-box span').click(function() {
        pageInfo[tabIndex].no = 1;
        $('[data-role="null-data"]').html(''); 
        //启用下拉请求
        $.attachInfiniteScroll($('.tab').eq(tabIndex));
        if ($(this).hasClass('cur')) {
            $(this).removeClass('cur');
            $('.tabs').attr('data-active-tag', '');
            getData();
        } else {
            $(this).siblings('span').removeClass('cur');
            $(this).addClass('cur');
            $('.tabs').attr('data-active-tag', $(this).data('tagId'));
            getData();
        }

    });
});