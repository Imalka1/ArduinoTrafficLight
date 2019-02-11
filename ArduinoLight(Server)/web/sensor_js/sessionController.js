var webSoc = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/light-end-point");
var segments = ["Segment - 1", "Segment - 2"];
var spotsCount = [2, 4];

webSoc.onopen = function (ev) {
    console.log("logged");
}

webSoc.onclose = function (ev) {
    console.log("closed");
}

webSoc.onmessage = function processMessage(message) {
    // console.log(message)
    // $("#txtId").html("Ultrasonic Sensor :- Reading (" + message.data.split('&')[1] + ")  =>  " + message.data.split('&')[0] + "cm");
    // if (message.data.split('&')[2] == "true") {
    //     $("#lightId").html("Lights are switched ON");
    //     $("#swCol").css("background-color", "green");
    // } else if (message.data.split('&')[2] == "false") {
    //     $("#lightId").html("Lights are switched OFF");
    //     $("#swCol").css("background-color", "red");
    // }
    setVehicleCount(message.data.split('&')[0], message.data.split('&')[1], message.data.split('&')[2], message.data.split('&')[3])//Sensor & Segment & Cur_Value & Pre_Value
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
            '<div class="col-4" style="text-align: center">Spot - ' + (i + 1) + '</div>' +
            '<div class="col-4" style="text-align: center">Light</div>' +
            '<div class="col-4" id="seg' + segmentCount + 'vCount' + (i + 1) + '" style="text-align: center">Vehicle Count - 0</div>' +
            '</div>';console.log('seg' + segmentCount + 'vCount' + (i + 1))
    }
    return spots;
}

function setVehicleCount(val1, val2, val3, val4) {//Sensor & Segment & Cur_Value & Pre_Value
    if (val1 == 1) {
        $("#seg" + val2 + "vCount" + val1).html('Vehicle Count - ' + val3);
    } else {
        $("#seg" + val2 + "vCount" + (val1 - 1)).html('Vehicle Count - ' + val4);
        $("#seg" + val2 + "vCount" + val1).html('Vehicle Count - ' + val3);
    }
}

$(window).ready(function () {
    for (var i = 0; i < segments.length; i++) {
        setSegments((i + 1), segments[i], spotsCount[i]);
    }
});