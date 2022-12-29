var stompClient = null;


function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat/public', function (chatMessage) {
            showGreeting(JSON.parse(chatMessage.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}


function sendName() {
    connect();
    stompClient.send("/app/chat/dispatcher", {}, JSON.stringify({'name': $("#name").val()}));
    disconnect();
}


