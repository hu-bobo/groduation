/**
 * 土地种类统计
 */
var pageCurr;
var form;
var active;
function test(th)
{
    var btid = parseInt(th);
    endTime = new Date();
    startTime = new Date(endTime-btid*60*60*24*1000);
    active.chartLoad(startTime,endTime)
}
layui.use(['table','laydate','form'], function(){
    var table = layui.table
        ,form = layui.form
        ,laydate = layui.laydate
        ,admin = layui.admin;
    active = {
        init: function () {
            var date = new Date();
            //初始化日期框
            laydate.render({
                elem: '#startTime'
                ,type: 'date'
                ,value: new Date(new Date()-30000*60*60*24)
                ,max:'date'
                ,done: function (value, date) {
                    var starttime = $('#startTime').val();
                    if (starttime != null || starttime != "") {
                        var numberStarttime = starttime.split("-").join('');
                        var numberDate = date.year * 10000 + date.month * 100 + date.date;
                        if (parseInt(numberStarttime) > numberDate) {
                            layer.msg('开始时间不能小于结束时间');
                        }
                    }
                }
            });
            laydate.render({
                elem: '#endTime'
                ,type: 'date'
                ,value: new Date
                ,max:'date'
                ,done: function (value, date) {
                    var starttime = $('#startTime').val();
                    if (starttime != null || starttime != "") {
                        var numberStarttime = starttime.split("-").join('');
                        var numberDate = date.year * 10000 + date.month * 100 + date.date;
                        if (parseInt(numberStarttime) > numberDate) {
                            layer.msg('开始时间不能小于结束时间');
                        }
                    }
                }
            });

            //监听搜索
            form.on('submit(LAY-user-front-search)', function(data){
                var field = data.field;
                console.log(field);

                active.chartLoad(field.startTime,field.endTime);


            });

        }
        , defaultSize: function () {
            var htHeight = $('#ht').height();
            var titHeight = $('#tit').height();
            var resultheight =htHeight-titHeight-20;
            $('#chart').height(resultheight);
            //console.log(tableHeight);
        }
        , autoSize: function () {
            $(window).resize(function () {
                active.defaultSize();
            });
        }


        ,chartLoad: function (starttime,endtime) {
            var option = {
                toolbox: {
                    right: '10',
                    show: true,
                    feature: {
                        saveAsImage: {show: true}
                    }
                },
                title: {
                    text: '土地类型统计'
                },
                //图例
                legend: {
                    orient: 'vertical',
                    x: 'right',
                    y: 'bottom',
                    data: []
                },
                series: [{
                    name: '土地类型',
                    type: 'pie',
                    radius: '60%',
                    center: ['50%', '50%'],
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                formatter: '{b}: {c} ({d}%)'
                            },
                            labelLine: {show: true}
                        }
                    },
                    data: []
                }]
            };
            $.ajax({
                url: '/fpoint/getdata',
                type: "post",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"starttime": starttime,"endtime": endtime,"type": "category"}),
                success: function (data) {
                    for (var key in data) {
                        option.series[0].data.push({'name': key, 'value': data[key]});
                        option.legend.data.push(key);
                    }

                    //初始化echarts实例
                    var myChart = echarts.init(document.getElementById('chart'));
                    window.onresize = myChart.resize;
                    //使用制定的配置项和数据显示图表
                    myChart.setOption(option);
                }
            });
        }
    };


    var endtimee = null;
    var starttimee = null;
    active.chartLoad(starttimee, endtimee);
    active.init();
    active.defaultSize();
    active.autoSize();
});
