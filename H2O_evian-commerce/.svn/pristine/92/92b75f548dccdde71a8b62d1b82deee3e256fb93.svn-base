var pageNo  = 1;
var pageSize = 5;
var loading = false;
var articleId = $('[name=articleId]').val();
var send = $('[data-role="send"]');
var msgBox = $('[data-role="msg-box"]');
var isApp = $('[name="isApp"]').val();
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

var userInfo = '';
function AppMakeWebLoginJS(info){
    userInfo = info;
}
var preloader = $('.infinite-scroll-preloader');
//获取数据
var getData = function(method) {
    $.ajax({
        type: "post",
        url: "/found/ajax/articleCommentList",
        data: {
            articleId: articleId,
            pageIndex: pageNo,
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
                $('[data-role="null-data"]').html('暂无更多内容哦');
                // 加载完毕，则注销无限加载事件，以防不必要的加载:$.detachInfiniteScroll($('.infinite-scroll').eq(tabIndex));多个无线滚动请自行根据自己代码逻辑判断注销时机
                $.detachInfiniteScroll($('.message-box'));
               
                // 删除加载提示符
                preloader.hide();
                return;
            } else {
                
                if(data.data.length >= pageSize){
                   preloader.show(); 
               }else{
                    preloader.hide();
               }
                var tpl = $("#tpl").html();
                var template = Handlebars.compile(tpl);
                var context = {
                    data: data.data
                };
                var html = template(context);
                
                $('[data-role="list-container"]').append(html);
                
                
            }

            $.refreshScroller();
        }
    });
};
$(document).on("pageInit", function(e, id, page) {

    $(page).on('infinite', function() {

        // 如果正在加载，则退出
        if (loading) return;
        preloader.show();
        // 设置flag
        loading = true;
        
        //新增一页
        pageNo = pageNo + 1;
  
        getData('pull');
        
    });
});
$.init();
$(function() {
    getData();
    
    /*$('.content').delegate('.open-services','click', function () {
      
    });*/
    /*$('header,.content').on('click', function(e){
        e.stopPropagation(); 

        var target = $(e.target);
        //console.log(target[0].tagName);
        if(target.data('role') == 'comment' || target[0].tagName == 'A' ||　target[0].tagName == 'SPAN'){
            return;
        }
        msgBox.attr('placeholder', '评论文章');
        send.attr('data-client-id', 0);
        send.attr('data-parent-id', 0);
    });*/
    
    $('.content').delegate('[data-role="support"], [data-role="comment"], [data-role="like"]','click',  function(e) {
        e.preventDefault();
        var me = $(this),
            action = me.data('role'),
            doIt = function(){
                if(action == "support"){//评论点赞

                    var icon = me.children('[data-role="support-icon"]'),
                        count = me.children('[data-role="support-count"]'),
                        val = parseInt(count.data('val')),
                        commentId = me.data('commentId'),
                        countVal = null;

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
                    // debugger;
                    count.attr('data-val', countVal);

                    $.ajax({
                        type: "post",
                        url: "/found/ajax/articleCommentLikeOperat",
                        data: {
                            commentId: commentId
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
                }else if(action == "like"){//文章点赞
                    if (me.hasClass('active')) {
                        me.removeClass('active');

                    } else {
                        me.addClass('active');
                    }

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
                }else if(action == "comment"){//回复文章
                    var clientId = me.data('clientId'),
                        parentId = me.data('parentId'),
                        name = me.data('name');

                    msgBox.focus();
                    send.attr('data-client-id', clientId);
                    send.attr('data-parent-id', parentId);
                    msgBox.attr('placeholder', '回复' + name);
                    $.popup('.popup-services');
                }
            };
            
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
                    if(data.code == '-3'){
                        try{
                            userInfo = app.UserInfoFromApp();
                        }catch(err){
                            try{
                                userInfo = UserInfoFromApp();
                             }catch(err){
                             }
                        }
                        // $.alert('userInfo:' +　userInfo);
                        $.ajax({
                            type: "post",
                            url: "/weixin/ajax/appLogin",
                            data: {
                                uinfo: userInfo
                            },
                            dataType: 'json',
                            complete: function(result) {
                                // $.alert(JSON.stringify(result));
                                
                                var rs = result.response;
                                if ($.type(rs) == 'string') {
                                    rs = JSON.parse(result.response);
                                }
                                // $.alert('appLogin:' + rs.code);
                                if(responseMsg[rs.code]){
                                    $.alert(responseMsg[rs.code]);
                                    return;
                                }
                                doIt();
                            }
                        });
                    }else{
                       
                        doIt();   
                    }
                }
            });
        }
        
        
    });
    
    send.click(function(){
        var comment = $.trim(msgBox.val());
        var clientId = $(this).data('clientId');
        var parentId = $(this).data('parentId');
        var doSend = function(){
            if(comment == ''){
                $.alert('请输入留言内容！');
                return;
            }
            if(comment.length > 200){
                $.alert('留言字数不能超过200！');
                return;
            }
            $.ajax({
                type: "post",
                url: "/found/ajax/articleCommentOperat",
                data: {
                    articleId: articleId,
                    parentId: parentId,
                    clientId: clientId,
                    commentContent: comment
                },
                dataType: 'json',
                complete: function(json) {
                    
                    
                    var data = json.response;
                    if ($.type(data) == 'string') {
                        data = JSON.parse(json.response);
                    }
                    $.alert('articleCommentOperat:' + data.code);
                    if(responseMsg[data.code]){
                        $.alert(responseMsg[data.code]);
                        return;
                    }
                    
                    window.location.reload();
                    
                }
            });
        };
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
                    if(data.code == '-3'){
                        try{
                            userInfo = app.UserInfoFromApp();
                        }catch(err){
                            userInfo = UserInfoFromApp();
                        }
                        $.ajax({
                            type: "post",
                            url: "/weixin/ajax/appLogin",
                            data: {
                                uinfo: userInfo
                            },
                            dataType: 'json',
                            complete: function(result) {
                                
                                var rs = result.response;
                                if ($.type(rs) == 'string') {
                                    rs = JSON.parse(result.response);
                                }
                                // $.alert('appLogin:' + rs.code);
                                if(responseMsg[rs.code]){
                                    $.alert(responseMsg[rs.code]);
                                    return;
                                }
                                doSend();
                            }
                        });
                    }else{
                        doSend();
                    }
                }
            });
        }
        
    });
    //调用app方法
    $('[data-role="company"]').on('click',function(e){
        
        e.preventDefault();
        var eid = $(this).data('eid');
        
        try{
            //安卓
            app.showEnterprise(eid);
        }catch(error){
            try{
                //苹果
                showEnterprise(eid);
            }catch(error){
                //微信
                var url = $(this).attr('href');
                window.location.href = url;
            }
        }
        //window.location.href = '/found/brand?eid=' + eid + '&from=' + articleId;
    });
});