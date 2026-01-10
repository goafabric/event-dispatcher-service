package org.goafabric.eventdispatcher.websocket;

import org.goafabric.eventdispatcher.service.controller.dto.SocketMessage;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@RegisterReflectionForBinding(SocketMessage.class)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .addInterceptors(new CustomHandshakeInterceptor())
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new TenantDestinationInterceptor());
    }

    //store Http Headers from HTTP Request (via lua) inside session,  to be used for Websocket later => yuck ... hope this works with replicasets
    static class CustomHandshakeInterceptor implements HandshakeInterceptor {

        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            String tenantId = request.getHeaders().getFirst("X-TenantId");
            attributes.put("tenantId", tenantId == null ? "0" : tenantId);
            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
            //not required to implement
        }
    }

    static class TenantDestinationInterceptor implements ChannelInterceptor {

        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

            switch (accessor.getCommand()) {
                case StompCommand.SEND : throw new IllegalStateException("SEND is not allowed");
                case StompCommand.SUBSCRIBE : rewriteDestination(message, accessor);
                default: return message;
            }

        }

        //rewrite destination based on the tenant, this will match the tenant from the kafka publisher, frontend can subscribe to non specific tenant endpoints
        private Message<?> rewriteDestination(Message<?> message, StompHeaderAccessor accessor) {
            String tenantId = (String) accessor.getSessionAttributes().get("tenantId");

            if (tenantId == null) { throw new IllegalStateException("No tenant bound to WebSocket session");}
            if (accessor.getDestination() == null) { throw new IllegalStateException("No Websocket distnation");};

            accessor.setDestination(accessor.getDestination() + "/tenant/" + tenantId);
            return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());

        }
    }



}