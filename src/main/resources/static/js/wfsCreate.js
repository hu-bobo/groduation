var newId = 1000;
// var wfsVectorLayer = null;
// var drawedFeature = null;
//
//
//
// // 添加绘制新图形的interaction，用于添加新的点
// var drawInteraction = new ol.interaction.Draw({
//     type: 'Point', // 设定为点
//     source: drawLayer.getSource()
// });
// drawInteraction.on('drawend', function(e) {
//     // 绘制结束时暂存绘制的feature
//     drawedFeature = e.feature;//featqueryWfsure
// });
//
// function queryWfs() {
//     if (wfsVectorLayer) {
//         map.removeLayer(wfsVectorLayer);
//     }
//
//
//     wfsVectorLayer = new ol.layer.Vector({
//         source: new ol.source.Vector({
//             format: new ol.format.GeoJSON({
//                 geometryName: 'geom'
//             }),
//             url: 'http://localhost:8080/geoserver/wfs?service=wfs&version=1.1.0&request=GetFeature&typeNames=projectshp:fpoint_bl&outputFormat=application/json&srsname=EPSG:4326'
//         })
//     });
//     map.addLayer(wfsVectorLayer);
// }
//
// $('#add').change(function() {
//     if (this.checked) {
//         // 勾选新增复选框时，添加绘制的Interaction
//         map.removeInteraction(drawInteraction);
//         map.addInteraction(drawInteraction);
//     } else {
//         // 取消勾选新增复选框时，移出绘制的Interaction，删除已经绘制的feature
//         map.removeInteraction(drawInteraction);
//         if (drawedFeature) {
//             drawLayer.getSource().removeFeature(drawedFeature);
//         }
//         drawedFeature = null;
//     }
// });
//
// // 保存新绘制的feature
// function onSaveNew() {
//     // 转换坐标
//     var point = drawedFeature.getGeometry().getCoordinates();
//     var newFeature = new ol.Feature({
//         geom: new ol.geom.Point([point[1],point[0]])
//     });
//
//     addWfs([newFeature]);
//     // 更新id
//     newId = newId + 1;
//     // 3秒后，自动刷新页面上的feature
//     setTimeout(function() {
//         drawLayer.getSource().clear();
//         queryWfs();
//     }, 1000);
// }
//
// // 添加到服务器端
// function addWfs(features) {
//     var WFSTSerializer = new ol.format.WFS();
//     var featObject = WFSTSerializer.writeTransaction(features,
//         null, null, {
//             featureType: 'fpoint_bl',
//             featureNS: 'http://localhost:8080/projectshp',
//             srsName: 'EPSG:4326'
//         });
//     var serializer = new XMLSerializer();
//     var featString = serializer.serializeToString(featObject);
//     var request = new XMLHttpRequest();
//     request.open('POST', 'http://localhost:8080/geoserver/wfs?service=wfs');
//     request.setRequestHeader('Content-Type', 'text/xml');
//     request.send(featString);
// }



$('#saveNew').click(function () {
    var type = 'Point';
    var source = pointSource;
    map.getInteractions().clear();
    var draw = new ol.interaction.Draw({
        source: source,
        type: type,
        geometryName: 'geometry'
    });
    map.addInteraction(draw);

    draw.on('drawend', function (e) {
        var geomType = e.feature.getGeometry().getType().toString().toLowerCase();
        var drawedFeature = e.feature;
        var point = drawedFeature.getGeometry().getCoordinates();
    var newFeature = new ol.Feature({
        geom: new ol.geom.Point([point[1],point[0]])
    });
        newId = newId + 1;
        transact('insert', newFeature, geomType);
        map.removeInteraction(draw);
        map.getInteractions().clear();
    });

});

function transact(transType, feat, geomType) {
    if (geomType === 'linestring') {
        geomType = 'line';
    }
    var formatWFS = new ol.format.WFS();
    var formatGML = new ol.format.GML({
        featureNS: 'http://localhost:8080/projectshp', // Your namespace
        featureType: 'fpoint_bl',
        srsName: 'EPSG:4326'
    });
    switch (transType) {
        case 'insert':
            node = formatWFS.writeTransaction([feat], null, null, formatGML);
            break;
        case 'update':
            node = formatWFS.writeTransaction(null, [feat], null, formatGML);
            break;
        case 'delete':
            node = formatWFS.writeTransaction(null, null, [feat], formatGML);
            break;
    }

    // s = new XMLSerializer();
    // str = s.serializeToString(node);
    // console.log(str);
    // $.ajax('http://localhost:8080/geoserver/wfs',{
    //     type: 'POST',
    //     dataType: 'xml',
    //     processData: false,
    //     contentType: 'text/xml',
    //     data: str
    // }).done();

    var serializer = new XMLSerializer();
    var featString = serializer.serializeToString(node);
    var request = new XMLHttpRequest();
    request.open('POST', 'http://localhost:8080/geoserver/wfs?service=wfs');
    request.setRequestHeader('Content-Type', 'text/xml');
    request.send(featString);

}

$('#modify').click(function () {
    map.getInteractions().clear();
    var select = new ol.interaction.Select(

    );
    var modify = new ol.interaction.Modify({
        features: select.getFeatures()
    });
    map.addInteraction(select);
    map.addInteraction(modify);

    modify.on('modifyend', function (e) {
        var geomType = e.features.getArray()[0].getGeometry().getType().toLowerCase();
        //var geomType = e.feature.getGeometry().getType().toString().toLowerCase();
        // var drawedFeature = e.feature;
        // var point = drawedFeature.getGeometry().getCoordinates();
        // var newFeature = new ol.Feature({
        //     geom: new ol.geom.Point([point[1],point[0]])
        // });
        transact('update',e.features.getArray()[0], geomType);
        map.removeInteraction(select);
        map.removeInteraction(modify);
    });

});