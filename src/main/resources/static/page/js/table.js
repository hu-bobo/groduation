
layui.use(['table','form'], function(){
    var pageCurr;
    //var form;
    var table = layui.table
        ,form = layui.form
        ,admin = layui.admin;
    var active = {
        tableLoad: function () {
            table.render({
                elem: '#pointList',
                url:'/fpoint/getList',
                method: 'post', //默认：get请求
                cellMinWidth: 80,
                page: true,
                limit: 10,
                title: '用户管理',
                toolbar: '#toolbar-user',
                contentType: "application/json;charset=UTF-8",
                where: {},
                response:{
                    statusName: 'code', //数据状态的字段名称，默认：code
                    statusCode: 200, //成功的状态码，默认：0
                    countName: 'totals', //数据总数的字段名称，默认：count
                    dataName: 'list' //数据列表的字段名称，默认：data,
                    ,limit:10,
                    page: 1
                },
                cols: [[
                    {type:'numbers',fixed:'left'}
                    ,{type: 'checkbox',fixed:'left'}
                    ,{field:'gid', title:'编号',align:'center', sort: true,fixed:'left'}
                    ,{field:'name', title:'名称',align:'center', sort: true}
                    ,{field:'optuser', title:'提交人',align:'center', sort: true}
                    ,{field:'category', title: '土地类型',align:'center', sort: true}
                    ,{field:'address', title: '详细地址',align:'center', sort: true}
                    ,{field:'wgs84_lng', title: '经度',align:'center', sort: true}
                    ,{field:'wgs84_lat', title: '纬度',align:'center', sort: true}
                    ,{
                        field: 'status',
                        title: '状态',
                        align: 'center', sort: true,
                        templet: function (d) {
                            if (d.status == 1) {
                                return '<span style="color: #1E9FFF">启用</span>';
                            } else if (d.status == 0) {
                                return '<span style="color: #ff654b">禁用</span>';
                            } else {
                                return '';
                            }
                        }
                    }
                    ,{field:'remark', title: '备注',align:'center',
                        templet:'<div><span title="{{d.remark}}">{{d.remark}}</span></div>'}
                    ,{title:'操作',align:'center', width: 165, toolbar:'#optBar',fixed: 'right'}
                ]],
                height: 'full-80',
                done: function(res, curr, count){
                    //如果是异步请求数据方式，res即为你接口返回的信息。
                    //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                    //console.log(res);
                    //得到当前页码
                    console.log(curr);
                    //得到数据总量
                    //console.log(count);
                    pageCurr=curr;
                }
            });
        }
        ,init: function () {
            //监听工具条
            table.on('toolbar(pointTable)', function (obj) {
                var type = $(this).data('type');
                active[type] ? active[type].call(this) : '';
            });

            table.on('tool(pointTable)', function(obj){
                var data = obj.data;

                if(obj.event == 'edit'){
                    //编辑
                    if(data.status != '1'){
                        layer.alert("状态已禁用，无法进行编辑操作！", {icon: 2});
                    } else {
                        active.editItems(data.gid);
                    }
                } else if (obj.event == 'del') {
                    if (!data.status) {
                        layer.alert("已被禁用，无法再次禁用！", {icon: 2});
                        return;
                    } else {
                        data.status = 0;
                        active.updateUser(data);
                    }
                } else if (obj.event == 'recover') {
                    if (!data.status) {
                        layer.alert("已被启用，无法再次启用！", {icon: 2});
                        return;
                    } else {
                        data.status = 1;
                        active.updateUser(data);
                    }
                } else if(obj.event === 'view') {
                    // 详情
                    active.viewItems(data.gid);
                }
            });
            //监听搜索
            form.on('submit(LAY-user-front-search)', function(data){
                var field = data.field;
                console.log(field);
                table.reload('pointList', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    },
                    where: field
                });
            });
            //监听行双击事件
            table.on('rowDouble(pointTable)', function (obj) {
                active.viewItems(obj.data.gid);
            });
        }
        // ,add: function (){
        //     var index = layer.open({
        //         type: 2     //类型，解析url
        //         , title: '添加[人员信息]'
        //         , maxmin: true //开启最大化最小化按钮
        //         , content: '/tincher-inspection/page/user/userForm'
        //         , btn: ['保存', '取消']
        //         , area: ['450px', '500px']
        //         , yes: function (index, layero) {
        //             var iframeWindow = window['layui-layer-iframe' + index]
        //                 , submitID = 'LAY-user-front-submit'
        //                 , submit = layero.find('iframe').contents().find('#' + submitID);
        //             //监听提交
        //             iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
        //                 layer.load();
        //                 var field = data.field; //获取提交的字段
        //                 $.ajax({
        //                     type: "POST",
        //                     contentType: "application/json;charset=UTF-8",
        //                     data: JSON.stringify(field),
        //                     url: "/tincher-inspection/user/addOrUpdate",
        //                     success: function (data) {
        //                         console.log(data);
        //                         if (data.code == 1) {
        //                             layer.closeAll('loading');
        //                             active.tableLoad();
        //                             layer.closeAll();
        //                         } else {
        //                             layer.closeAll('loading');
        //                             layer.alert(data.msg);
        //                         }
        //                     },
        //                     error: function () {
        //                         layer.alert("操作请求错误，请您稍后再试",function(){
        //                             layer.closeAll();
        //                         });
        //                     }
        //                 });
        //
        //                 layer.close(index); //关闭弹层
        //             });
        //             submit.trigger('click');
        //         }
        //     });
        //     // layer.full(index);
        // }
        ,editItems: function (gid){
            var index = layer.open({
                type: 2     //类型，解析url
                , title: '编辑[样本点信息]'
                , maxmin: true //开启最大化最小化按钮
                , content: '/page/pointForm.html?id=' + gid
                , btn: ['保存', '取消']
                , area: ['450px', '500px']
                , yes: function (index, layero) {
                    var iframeWindow = window['layui-layer-iframe' + index]
                        , submitID = 'LAY-user-front-submit'
                        , submit = layero.find('iframe').contents().find('#' + submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                        layer.load();
                        var field = data.field; //获取提交的字段
                        field.gid = gid;
                        $.ajax({
                            type: "POST",
                            contentType: "application/json;charset=UTF-8",
                            data: JSON.stringify(field),
                            url: "/fpoint/update",
                            success: function (data) {
                                console.log(data);
                                if (data.code == 1) {
                                    layer.closeAll('loading');
                                    active.tableLoad();
                                    layer.closeAll();
                                } else {
                                    layer.closeAll('loading');
                                    layer.alert(data.msg);
                                }
                            },
                            error: function () {
                                layer.alert("操作请求错误，请您稍后再试",function(){
                                    layer.closeAll();
                                });
                            }
                        });

                        layer.close(index); //关闭弹层
                    });
                    submit.trigger('click');
                }
            });
            // layer.full(index);
        }
        ,viewItems: function (gid){
            var index = layer.open({
                type: 2     //类型，解析url
                , title: '详情[样本点信息]'
                , maxmin: true //开启最大化最小化按钮
                , content: '/page/pointForm.html?id=' + gid + "&type=1"
                , btn: ['取消']
                , area: ['450px', '500px']
                , yes: function (index) {
                    layer.close(index);
                }
            });
            // layer.full(index);
        }
        ,updateUser: function(obj) {
            var info = '';
            if(obj.status==1){
                info = '您确定要启用'+obj.name+'样本点吗？';
            } else {
                info = '您确定要禁用'+obj.name+'样本点吗？';
            }
            layer.confirm(info, {
                btn: ['确认','返回'] //按钮
            }, function(){
                $.ajax({
                    type: "POST",
                    contentType: "application/json;charset=UTF-8",
                    url: "/fpoint/update",
                    data: JSON.stringify(obj),
                    success: function (res) {
                        console.log(res);
                        if (res.code == 1) {
                            table.reload('pointList');
                            layer.closeAll(); //关闭弹层
                            // layer.close(index); //关闭弹层
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
            }, function(){
                layer.closeAll();
            });
        }, batchEnable: function () {
            active.batch(1);
        },
        batchDisable: function () {
            active.batch(0);
        },
        batch: function (status) {
            var checkStatus = table.checkStatus('pointList')
                , checkData = checkStatus.data; //得到选中的数据

            if (checkData.length === 0) {
                return layer.msg('请选择数据');
            } else if (checkData.length === 1) {
                if(status==1 && checkData[0].status=='1'){
                    return layer.alert("状态已启用，无法进行编辑操作！", {icon: 2});
                } else if(status==0 && checkData[0].status!='1'){
                    return layer.alert("状态已禁用，无法进行编辑操作！", {icon: 2});
                }
            }
            var info = '';
            if(status == 1){
                info = '确定进行启用操作吗？';
            } else {
                info = '确定进行禁用操作吗？';
            }
            layer.confirm(info, function (index) {
                $.each(checkData, function (index, value) {
                    value.status = status;
                });
                layer.close(index);
                $.ajax({
                    type: "POST",
                    contentType: "application/json;charset=UTF-8",
                    url: "/fpoint/batchUpdateStatus",
                    data: JSON.stringify(checkData),
                    success: function (res) {
                        console.log(res);
                        if (res.code == 1) {
                            table.reload('pointList');
                            layer.close(index); //关闭弹层
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
            });
        }
        // ,load: function (obj) {
        //     //重新加载table
        //     table.tableLoad({
        //         where: obj.field
        //         , page: {
        //             curr: pageCurr //从当前页码开始
        //         }
        //     });
        // }
    };

    active.tableLoad();
    active.init();
});