var stompClient = null;


function connectSocket() {
    var websocketPath = "/websocket";
    if (window.location.pathname.startsWith('/event')) {
        websocketPath = "/event/websocket";
    }
    var socket = new SockJS(websocketPath);    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/public', function (socketMessage) {
            console.log("Got Socket Message : " + JSON.parse(socketMessage.body).message);
        });
    });
}

function disconnectSocket() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function createPatient() {
    stompClient.send("/events/createpatient", {}, "");
}

function updatePatient() {
    stompClient.send("/events/updatepatient", {}, "");
}

function createPractitioner() {
    stompClient.send("/events/createpractitioner", {}, "");
}

function updatePractitioner() {
    stompClient.send("/events/updatepractitioner", {}, "");
}

