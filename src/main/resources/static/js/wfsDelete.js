// // 选择器
// var selectInteraction = new ol.interaction.Select({
//     style: new ol.style.Style({
//         stroke: new ol.style.Stroke({
//             color: 'red',
//             width: 10
//         })
//     })
// });
//
//
//
//
//
// $('#select').change(function() {
//     if (this.checked) {
//         map.removeInteraction(selectInteraction);
//         map.addInteraction(selectInteraction);
//     } else {
//         map.removeInteraction(selectInteraction);
//     }
// });
//
// function onDeleteFeature() {
//     // 删选择器选中的feature
//     if (selectInteraction.getFeatures().getLength() > 0) {
//         deleteWfs([selectInteraction.getFeatures().item(0)]);
//         // 3秒后自动更新features
//         setTimeout(function() {
//             selectInteraction.getFeatures().clear();
//             queryWfs();
//         }, 3000);
//     }
// }
//
// // 在服务器端删除feature
// function deleteWfs(features) {
//     var WFSTSerializer = new ol.format.WFS();
//     var featObject = WFSTSerializer.writeTransaction(null,
//         null, features, {
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






//Delete
$('#delete').click(function () {
    map.getInteractions().clear();
    var select = new ol.interaction.Select();
    map.addInteraction(select);
    select.on('select', function (e) {
        if(select.getFeatures().getArray().length == 0) {
            console.log('null');
        } else {
            var geomType = e.target.getFeatures().getArray()[0].getGeometry().getType().toLowerCase();
            transact('delete', e.target.getFeatures().getArray()[0], geomType);
            map.removeInteraction(select);
            var f;
            switch(geomType) {
                case 'point':
                    f = pointSource.getFeatureById(e.target.getFeatures().getArray()[0].getId());
                    pointSource.removeFeature(f);
                    e.target.getFeatures().clear();
                    break;
                default:
                    console.log('Type of feature unknown!!!');
            }

        }
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
    // });//.done()

    var serializer = new XMLSerializer();
    var featString = serializer.serializeToString(node);
    var request = new XMLHttpRequest();
    request.open('POST', 'http://localhost:8080/geoserver/wfs?service=wfs');
    request.setRequestHeader('Content-Type', 'text/xml');
    request.send(featString);

}