var pageNo  = 1;
var pageSize = 5;
var classId = 1;//默认取热度排行的分类
var loading = false;
var preloader = $('.infinite-scroll-preloader');
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
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
//获取数据
var getData = function(method, articleTitle) {
    $.ajax({
        type: "post",
        url: "/found/ajax/articleList",
        data: {
            classId: classId,
            articleTitle:articleTitle,
            pageIndex: pageNo,
            pageSize: pageSize
        },
        dataType: 'json',
        complete: function(json) {
          
            // 重置加载flag
            loading = false;
            var data = json.response;
            var isApp = getQueryString('fromApp');
            var tpl = '';
            if ($.type(data) == 'string') {
                data = JSON.parse(json.response);
            }
            if(responseMsg[data.code]){
                $.alert(responseMsg[data.code]);
                return;
            }
            
            if (data.data.length == 0) {
                $('[data-role="null-data"]').html('没有查找到您搜索的内容');
                // 加载完毕，则注销无限加载事件，以防不必要的加载:$.detachInfiniteScroll($('.infinite-scroll').eq(tabIndex));多个无线滚动请自行根据自己代码逻辑判断注销时机
                $.detachInfiniteScroll($('.content'));
               
                // 删除加载提示符
                preloader.hide();
                return;
            } else {
                
                if(data.data.length >= pageSize){
                   preloader.show(); 
                }else{
                    preloader.hide();
                }
                if(isApp == '1'){
                    tpl = $("#appTpl").html();
                }else{
                    tpl = $("#tpl").html();
                }
                var template = Handlebars.compile(tpl);
                var context = {
                    data: data.data
                };
                var html = template(context);
                if(method == 'pull'){
                    $('[data-role="list-container"]').append(html);
                }else{
                    $('[data-role="list-container"]').html(html);
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
    
    $('[data-role="search"]').click(function(e){
        e.preventDefault();
        var searchval = $.trim($('#search').val());
        if(searchval){
            getData('get', searchval);
        }
    });
});
