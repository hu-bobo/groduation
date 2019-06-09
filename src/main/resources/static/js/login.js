layui.use(['layer', 'form'], function() {
    var form = layui.form,
        $ = layui.jquery;

    $('#forgot').on('click', function() {
        layer.msg('请联系管理员.');
    });
    var active = {
        init: function() {
            //监听提交
            form.on('submit(login_hash)', function (data) {
                //layer.msg(JSON.stringify(data.field));
                active.check(data);


                return false;
            });
        }
        ,check: function (data) {
            var field = data.field; //获取提交的字段
            $.ajax({
                type: "POST",
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify(field),
                url: "/user/check",
                success: function (data) {
                    console.log(data);
                    if (data.code == 0) {
                        // layer.closeAll('loading');
                        // active.tableLoad();
                        // layer.closeAll();
                        //layer.msg(data.msg);
                        layer.alert(data.msg, {icon: 2});
                    } else if (data.code == 1) {
                        // layer.closeAll('loading');
                         layer.alert(data.msg, {icon: 2});
                    }else if (data.code == 2){
                        setTimeout(function () {
                            location.href = '/home';
                        }, 1000);
                    }else{

                    }
                },
                error: function () {
                    layer.alert("操作请求错误，请您稍后再试",function(){
                        layer.closeAll();
                    });
                }
            });
        }
    };
    active.init()
});