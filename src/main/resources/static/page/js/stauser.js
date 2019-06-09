/**
 * 样本点提交人员统计
 */
var pageCurr;
var form;
var active;
function test(th)
{
    var btid = parseInt(th);
    //var btid = document.getElementById("divId");
    endTime = new Date();
    startTime = new Date(endTime-btid*60*60*24*1000);
    active.chartLoad(startTime,endTime)
}
layui.use(['table','laydate','form'], function(){
    var table = layui.table
        ,form = layui.form
        ,laydate = layui.laydate
        ,admin = layui.admin;
    var dataArrayKey=[];
    var dataArrayValue=[];
    active = {
        init: function () {

            var date = new Date();
            var d = new Date(date-30*60*60*24*1000);


            //监听搜索
            form.on('submit(LAY-user-front-search)', function(data){
                var field = data.field;
                console.log(field);

                active.chartLoad(field.startTime,field.endTime);

            });

            //初始化日期框
            laydate.render({
                elem: '#startTime'
                ,type: 'date'
                ,value: new Date(d)
                ,max: 'date'
                ,done: function (value, date) {
                    var endtime = $('#endTime').val();
                    if (endtime != null || starttime != "") {
                        var numberEndtime = endtime.split("-").join('');
                        var numberDate = date.year * 10000 + date.month * 100 + date.date;
                        if (parseInt(numberEndtime) < numberDate) {
                            layer.msg('开始时间不能小于结束时间');
                        }
                    }
                }
            });
            laydate.render({
                elem: '#endTime'
                ,type: 'date'
                ,value: new Date
                ,max: 'date'
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

            $.ajax({
                url: '/fpoint/getdata',
                type: "post",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"starttime": starttime,"endtime": endtime,"type": "user"}),
                success: function (data) {
                    var dataArrayKey = [];
                    var dataArrayValue = [];
                    for (var key in data) {

                        dataArrayKey.push(key);
                        dataArrayValue.push(data[key]);
                    }

                    // var myChart = echarts.init(document.getElementById('chart'));
                    // myChart.dispose();
                    var option = {
                        color: ['#3398DB'],
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                                //     type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        title: {
                            text: '样本点提交人员统计'
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data :dataArrayKey,
                                axisTick: {
                                    alignWithLabel: true
                                }
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value',
                                axisLabel:{
                                    formatter:'{value} (个)'
                                }
                            }
                        ],
                        series : [
                            {
                                name:'提交样本点数：',
                                type:'bar',
                                barWidth: '60%',
                                itemStyle: {
                                    normal: {
                                        label: {
                                            show: true, //开启显示
                                            position: 'top', //在上方显示
                                            textStyle: { //数值样式
                                                color: 'black',
                                                fontSize: 16
                                            }
                                        }
                                    }
                                },
                                data:dataArrayValue
                            }
                        ]
                    };
                    //初始化echarts实例
                    var myChart = echarts.init(document.getElementById('chart'));
                    window.onresize = myChart.resize;
                    //使用制定的配置项和数据显示图表
                    myChart.setOption(option,true);
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