var webSoc = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/light-end-point");

webSoc.onopen = function (ev) {
    console.log("logged");
}

webSoc.onclose = function (ev) {
    console.log("closed");
}

webSoc.onmessage = function processMessage(message) {
    console.log(message)
    $("#txtId").html("Ultrasonic Sensor :- Reading (" + message.data.split('&')[1] + ")  =>  " + message.data.split('&')[0]+"cm");
    if (message.data.split('&')[2] == "true") {
        $("#lightId").html("Lights are switched ON");
        $("#swCol").css("background-color","green");
    } else if (message.data.split('&')[2] == "false") {
        $("#lightId").html("Lights are switched OFF");
        $("#swCol").css("background-color","red");
    }
}