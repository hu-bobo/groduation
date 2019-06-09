var message;
layui.config({
    base: 'src/js/',
    version: '1.0.1'
}).use(['app', 'message'], function() {
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    $('#pay').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: '<img src="/src/images/pay.png" />',
            area: ['500px', '250px'],
            shadeClose: true
        });
    });
    $('dl.skin > dd').on('click', function() {
        var $that = $(this);
        var skin = $that.children('a').data('skin');
        switchSkin(skin);
    });
    var setSkin = function(value) {
            layui.data('kit_skin', {
                key: 'skin',
                value: value
            });
        },
        getSkinName = function() {
            return layui.data('kit_skin').skin;
        },
        switchSkin = function(value) {
            var _target = $('link[kit-skin]')[0];
            _target.href = _target.href.substring(0, _target.href.lastIndexOf('/') + 1) + value + _target.href.substring(_target.href.lastIndexOf('.'));
            setSkin(value);

        },
        initSkin = function() {
            var skin = getSkinName();
            switchSkin(skin === undefined ? 'default' : skin);
        }();
});