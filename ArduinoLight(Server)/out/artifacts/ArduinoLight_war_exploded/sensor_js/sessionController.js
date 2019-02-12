var webSoc = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/light-end-point");
var segments = [["Galle - Baddegama", 2]];

webSoc.onopen = function (ev) {
    console.log("logged");
}

webSoc.onclose = function (ev) {
    console.log("closed");
}

webSoc.onmessage = function processMessage(message) {
    var dataSet = JSON.parse(message.data);
    setVehicleCount(dataSet.sensor, dataSet.segment, dataSet.curCount, dataSet.preCount)//Sensor & Segment & Cur_Value & Pre_Value
}

function setSegments(i, segment, spots) {
    $("#screenBody").append('' +
        '<div class="row" style="margin-bottom: 20px">' +
        '<div class="col-12">' +
        '<div style="text-align:center;width: 70%;margin: auto;background-color: #f9b94e;color: #635120;padding: 15px;font-size: 25px">' +
        segment +
        '</div>' +
        '</div>' +
        '<div class="col-12">' +
        '<div style="width: 70%;margin: auto;border: 2px solid #f9b94e;color: #635120;font-size: 18px;padding: 13px">' +
        setSpots(i, spots) +
        '</div>' +
        '</div>' +
        '</div>');
}

function setSpots(segmentCount, spotsCount) {
    var spots = "";
    for (var i = 0; i < spotsCount; i++) {
        spots += '' +
            '<div class="row" style="border: 1px solid black;padding: 5px;margin: 10px">' +
            '<div class="col-4" style="text-align: center">Area - ' + (i + 1) + '</div>' +
            '<div class="col-4" id="seg' + segmentCount + 'led' + (i + 1) + '" style="text-align: center;font-weight: bold;color: red">Lights OFF</div>' +
            '<div class="col-4" id="seg' + segmentCount + 'vCount' + (i + 1) + '" style="text-align: center">Vehicle Count - 0</div>' +
            '</div>';
    }
    return spots;
}

function setVehicleCount(val1, val2, val3, val4) {//Sensor & Segment & Cur_Value & Pre_Value
    if (val1 == 1) {
        $("#seg" + val2 + "vCount" + val1).html('Vehicle Count - ' + val3);
        if (val3 > 0) {
            $("#seg" + val2 + "led" + val1).html('Lights ON');
            $("#seg" + val2 + "led" + val1).css("color", "#3EA317");
        } else {
            $("#seg" + val2 + "led" + val1).html('Lights OFF');
            $("#seg" + val2 + "led" + val1).css("color", "red");
        }
    } else {
        $("#seg" + val2 + "vCount" + (val1 - 1)).html('Vehicle Count - ' + val4);
        $("#seg" + val2 + "vCount" + val1).html('Vehicle Count - ' + val3);
        if (val3 > 0) {
            $("#seg" + val2 + "led" + val1).html('Lights ON');
            $("#seg" + val2 + "led" + val1).css("color", "#3EA317");
        } else {
            $("#seg" + val2 + "led" + val1).html('Lights OFF');
            $("#seg" + val2 + "led" + val1).css("color", "red");
        }
        if (val4 > 0) {
            $("#seg" + val2 + "led" + (val1 - 1)).html('Lights ON');
            $("#seg" + val2 + "led" + (val1 - 1)).css("color", "#3EA317");
        } else {
            $("#seg" + val2 + "led" + (val1 - 1)).html('Lights OFF');
            $("#seg" + val2 + "led" + (val1 - 1)).css("color", "red");
        }
    }
}

$(window).ready(function () {
    for (var i = 0; i < segments.length; i++) {
        setSegments((i + 1), segments[i][0], segments[i][1]);
    }
    $.ajax(
        {
            type: "post",
            url: "http://" + window.location.hostname + ":8080/getProperties",
            success: function (response) {
                var jsonData  = JSON.parse(response);
                for (var i = 0; i < jsonData.sensorData.length; i++) {
                    console.log(jsonData.sensorData[i])
                    setVehicleCount(jsonData.sensorData[i].sensor, jsonData.sensorData[i].segment, jsonData.sensorData[i].curCount, jsonData.sensorData[i].preCount);//Sensor & Segment & Cur_Value & Pre_Value
                }
            },
            error: function () {

            }
        }
    );
});