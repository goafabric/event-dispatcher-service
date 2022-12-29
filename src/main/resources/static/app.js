var stompClient = null;


function connectSocket() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat/public', function (socketMessage) {
            console.log(JSON.parse(socketMessage.body).message);
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
    stompClient.send("/createpatient", {}, "");
}

function updatePatient() {
    stompClient.send("/updatepatient", {}, "");
}

function createPractitioner() {
    stompClient.send("/createpractitioner", {}, "");
}

function updatePractitioner() {
    stompClient.send("/updatepractitioner", {}, "");
}

