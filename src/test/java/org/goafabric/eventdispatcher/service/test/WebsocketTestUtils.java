package org.goafabric.eventdispatcher.service.test;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.goafabric.eventdispatcher.service.controller.dto.SocketMessage;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class WebsocketTestUtils {

    private final StompSession session;

    private final Map<String, List<SocketMessage>> subscribedTopics = new HashMap<>();

    public WebsocketTestUtils(int port) throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketStompClient webSocketStompClient = new WebSocketStompClient(new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        session = webSocketStompClient
                .connectAsync(getWsPath(port), new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);
    }

    public void subscribeToTopic(String topic) {
        session.subscribe(
            topic,
            new StompFrameHandler() {

                @Override
                public @NonNull Type getPayloadType(@NonNull StompHeaders headers) {
                    return SocketMessage.class;
                }

                @Override
                public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                    subscribedTopics.computeIfAbsent(topic, k -> new ArrayList<>()).add((SocketMessage) payload);
                }
            });
    }

    public void sendMessageToTopic(String topic, String message) {
        session.send(topic, message);
    }

    public List<SocketMessage> getReceivedMessagesForTopic(String topic) {
        assert hasSubscription(topic) : "Please first subscribe to the topic " + topic;

        return subscribedTopics.get(topic);
    }

    private boolean hasSubscription(String topic) {
        return subscribedTopics.containsKey(topic);
    }

    private String getWsPath(int port) {
        return String.format("ws://localhost:%d/websocket", port);
    }
}
