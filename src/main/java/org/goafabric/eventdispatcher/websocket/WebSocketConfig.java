package org.goafabric.eventdispatcher.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .addInterceptors(new CustomHandshakeInterceptor())
                .withSockJS();
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new TenantAuthorizationInterceptor());
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
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {}
    }

    //checks tenant authorization and also denies stomp send
    static class TenantAuthorizationInterceptor implements ChannelInterceptor {
        private final Logger log = LoggerFactory.getLogger(this.getClass());

        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            if ((accessor != null) && (StompCommand.SEND.equals(accessor.getCommand()))) {
                log.error("Sending via Websocket denied, due to multi tenancy limitations");
                throw new IllegalStateException("Sending via Websocket denied, due to multi tenancy limitations");
            }

            if (accessor != null && accessor.getDestination() != null & StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                String tenantId = (String) accessor.getSessionAttributes().get("tenantId");

                if (accessor.getDestination().contains("/tenant/")) {
                    if (!accessor.getDestination().equals("/tenant/" + tenantId)) {
                        log.error("Access to tenant denied: {}", tenantId);
                        throw new IllegalStateException("Access to tenant denied");
                    }
                }
            }

            return message;
        }
    }


}