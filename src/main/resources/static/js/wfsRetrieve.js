var container = document.getElementById("popup");
var content = document.getElementById("popup-content");
var closer = document.getElementById("popup-closer");
var overlay = new ol.Overlay({
    //设置弹出框的容器
    element: container,
    //是否自动平移，即假如标记在屏幕边缘，弹出时自动平移地图使弹出框完全可见
    autoPan: true
});
closer.onclick = function() {
    overlay.setPosition(undefined);
    closer.blur();
    return false;
};
// map.on('click',function(e){
//     //在点击时获取像素区域
//     var pixel = map.getEventPixel(e.originalEvent);
//     map.forEachFeatureAtPixel(pixel,function(feature){
//         //coodinate存放了点击时的坐标信息
//         var coodinate = e.coordinate;
//         //设置弹出框内容，可以HTML自定义
//         content.innerHTML = "<p>你点击的坐标为：" + coodinate + "</p>";
//         //设置overlay的显示位置
//         overlay.setPosition(coodinate);
//         //显示overlay
//         map.addOverlay(overlay);
//     });
// });
var select = new ol.interaction.Select();
map.addInteraction(select);
select.on('select', function (e) {
    if(select.getFeatures().getArray().length == 0) {
        console.log('null');
    } else {
        var geomType = e.target.getFeatures().getArray()[0].getGeometry().getType().toLowerCase();
        //transact('delete', e.target.getFeatures().getArray()[0], geomType);
        var f;
        f = pointSource.getFeatureById(e.target.getFeatures().getArray()[0].getId());
        pro = f.getProperties();
        console.log(pro);

        //     //coodinate存放了点击时的坐标信息
        var coodinate = e.target.getFeatures().getArray()[0].getGeometry().getCoordinates();
        e.target.getFeatures().clear();

        //设置弹出框内容，可以HTML自定义
        // content.innerHTML = "<p>lat : " + pro.lat + "</p>" +
        //     "<p>id : " + f.getId() + "</p>";


        content.innerHTML ='<ul>' +
            '<li>编    号 : ' + f.getId() +'</li>' +
            '<li>名    称 : ' + pro.name + '</li>' +
            //'<li>gid : ' + pro.gid + '</li>' +
            '<li>提 交 人 : ' + pro.optuser + '</li>' +
            '<li>土地种类 : ' + pro.category + '</li>' +
            '<li>详细地址 : ' + pro.address + '</li>' +
            '</ul>' +
            // "<div id='jklh'  style='color: #0000FF;cursor: pointer'>附件</div>" +
            '<img id ="pic" src='+pro.url+' alt = "土地种类示意图" height="100" width="200" style="display:none;" />';
        var ui =document.getElementById("pic");
        //var uii =document.getElementById("popup-content");
        content.style.height = '90px';
        if(pro.url != null){
            // var ui =document.getElementById("pic");
            ui.style.display="block";
            content.style.height = '190px';

        }//style="display:none;"
        //设置overlay的显示位置
        overlay.setPosition(coodinate);
        //显示overlay
        map.addOverlay(overlay);


    }
});
// document.getElementById("jklh").onclick = function () {
//     layui.use(['carousel', 'form', 'layer'], function () { //独立版的layer无需执行这一句
//         var $ = layui.jquery, layer = layui.layer;
//         var carousel = layui.carousel
//             , form = layui.form;
//         layer.open({
//             type: 1
//             , offset: 'auto' //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
//             , id: 'layerDemo2'//防止重复弹出
//             , area: ["300px", "300px"]
//             , content:
//
//                 "<div style='height: 100%;width: 100%'>" +
//                 "<fieldset class='layui-elem-field layui-field-title' style='margin: 0;'>"
//
//                 + "</fieldset>"
//
//                 + "<div  class='layui-carousel' id='test1'style='margin-left: 1%;margin-right: 1%;margin-top: 2%;margin-bottom: 2%'>"
//                 + "<div id='detail' carousel-item='' >"
//                 + "</div>"
//                 + "</div>"
//                 + "</div>"
//
//             , btnAlign: 'c' //按钮居中
//             , shade: 0 //不显示遮罩
//             , title: '详细信息'
//             , yes: function () {
//                 layer.closeAll();
//             }
//
//         });
//         var imagediv = document.createElement("img");
//         //var myurl = rooturl +response[i].path;
//         imagediv.src = pro.url;
//         var detaildiv = document.getElementById('detail');
//         detaildiv.appendChild(imagediv);
//         //常规轮播
//         carousel.render({
//             elem: '#test1'
//             , arrow: 'always'
//             , height: '94%'
//             , width: '98%'
//             , full: false
//         });
//     });
// };
