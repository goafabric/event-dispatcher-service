package org.goafabric.eventdispatcher.service.extensions;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.messaging.access.intercept.MessageAuthorizationContext;

public class MyTenantAuthorizationManager implements AuthorizationManager<MessageAuthorizationContext<?>> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, MessageAuthorizationContext<?> messageContext) {
        String destination = SimpMessageHeaderAccessor.getDestination(messageContext.getMessage().getHeaders());
        if (destination == null) {
            return new AuthorizationDecision(false);
        }
        /* here we assume that all websocket topics comply to the following naming schema: /tenant/<tenantId>/... */
        Pattern pattern = Pattern.compile("/tenant/(\\w+)/.+");
        Matcher matcher = pattern.matcher(destination);
        if (!matcher.matches()) {
            return new AuthorizationDecision(false);
        }
        String tenantOfDestination = matcher.group(1);

        boolean granted = tenantOfDestination.equals(TenantContext.getTenantId());
        return new AuthorizationDecision(granted);
    }
}
