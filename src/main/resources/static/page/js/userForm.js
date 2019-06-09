/**
 * 用户管理 添加更新页面
 */
var form;
layui.use(['table','form', 'laydate'], function(){
    var table = layui.table
        ,form = layui.form
        ,tincher = layui.tincher
        ,laydate = layui.laydate
        ,admin = layui.admin;
    var active = {
        formListener:function () {
            form.on('select(item)', function(data){
            });
            // //日期范围
            // laydate.render({
            //     elem: '#birth',
            //     type: 'date' //默认，可不填
            // });
        },
        getUrlParm: function (parm) {
            var search = document.location.search;
            var parms = search.substring(1);
            var attr = parms.split("&");
            for (i = 0; i < attr.length; i++) {
                var v = attr[i];
                var obj = v.split("=");
                if (parm == obj[0]) {
                    return obj[1];
                }
            }
        }
    };

    active.formListener();
    var id = active.getUrlParm("id");
    if(id){
        $.ajax({
            type: "POST",
            contentType: "application/json;charset=UTF-8",
            url: "/user/getId",
            data: JSON.stringify({userid: id}),
            success: function (res) {
                console.log(res);
                if (res.code == 1) {
                    var obj = res.data;
                    form.val("layuiadmin-form-useradmin", obj);
                    if(obj.sex == 1){
                        $("input[name='sex'][value='1']").attr("checked", true);
                    } else {
                        $("input[name='sex'][value='2']").attr("checked", true);
                    }
                    var type = active.getUrlParm("type");
                    if(type){
                        $("#username").attr("disabled","disabled");
                        $("#name").attr("disabled","disabled");
                        //$("#birth").attr("disabled","disabled");
                        $("#phone").attr("disabled","disabled");
                        $("#remark").attr("disabled","disabled");

                        var sex = $("input[name='sex']:checked").val();
                        if(sex == 1){
                            $("input[name='sex'][value='2']").attr("disabled","disabled");
                        } else {
                            $("input[name='sex'][value='1']").attr("disabled","disabled");
                        }
                    }
                } else {
                    layer.closeAll('loading');
                    layer.alert(data.msg,function(){
                        layer.close(); //关闭弹层
                    });
                }
            },
            error: function () {
                layer.alert("操作请求错误，请您稍后再试",function(){
                    layer.closeAll();
                });
            }
        });
    } else {
        $("input[name='sex'][value='1']").attr("checked", true);
    }
    form.render();
});