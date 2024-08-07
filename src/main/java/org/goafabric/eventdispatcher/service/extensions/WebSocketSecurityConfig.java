package org.goafabric.eventdispatcher.service.extensions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    @Bean
    AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        MyTenantAuthorizationManager myTenantAuthorizationManager = new MyTenantAuthorizationManager();
        // CAUTION: these permissions need to be tightened before this code is used in production environment
        // order is relevant here!
        messages
            .simpSubscribeDestMatchers("/**").access(myTenantAuthorizationManager)
            .simpMessageDestMatchers("/events/**").permitAll()
            .nullDestMatcher().permitAll()
            .anyMessage().denyAll();
        return messages.build();
    }

    @Bean
    public ChannelInterceptor csrfChannelInterceptor() {
        //disabling csrf
        return new ChannelInterceptor() {};
    }
}