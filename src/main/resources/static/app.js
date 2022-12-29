var stompClient = null;


function connectSocket() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        //setConnected(true);
        console.log('Connected: ' + frame);
        /*
        stompClient.subscribe('/topic/chat/public', function (chatMessage) {
            showGreeting(JSON.parse(chatMessage.body).content);
        });
        */
    });
}

function disconnectSocket() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function createPatient() {
    stompClient.send("/app/createpatient", {}, "");
}

function updatePatient() {
    stompClient.send("/app/updatepatient", {}, "");
}

function createPractitioner() {
    stompClient.send("/app/createpractitioner", {}, "");
}

function updatePractitioner() {
    stompClient.send("/app/updatepractitioner", {}, "");
}

