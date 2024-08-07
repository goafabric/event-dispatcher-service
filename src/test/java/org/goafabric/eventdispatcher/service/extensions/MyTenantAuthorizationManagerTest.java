package org.goafabric.eventdispatcher.service.extensions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.messaging.access.intercept.MessageAuthorizationContext;

class MyTenantAuthorizationManagerTest {
    
    private final String tenantId = "myTenantId";
    private final String defaultDestination = "/tenant/" + tenantId + "/destination";
    
    private final MyTenantAuthorizationManager unitUnderTest = new MyTenantAuthorizationManager();
    
    @BeforeEach
    public void setUp() {
        TenantContext.setContext(tenantId, null, null, null);
    }

    @Test
    public void testCheck_TenantOfContextMatchesTenantOfDestination_AccessGranted() {
        MessageAuthorizationContext<String> messageContext = createMessageAuthorizationContextForDestination(defaultDestination);

        AuthorizationDecision authorizationDecision = unitUnderTest.check(null, messageContext);

        assertThat(authorizationDecision).isNotNull();
        assertThat(authorizationDecision.isGranted()).isTrue();
    }

    @ValueSource(strings = {
        "/tenant/someOtherTenant/destination",
        "/foo",
        "/tenant/destination",
        "/tenant/destination/some-destination-id",
    })
    @ParameterizedTest
    public void testCheck_TenantOfContextDoesNotMatchTenantOfDestination_AccessDenied(
        String destination
    ) {
        MessageAuthorizationContext<String> messageContext = createMessageAuthorizationContextForDestination(destination);

        AuthorizationDecision authorizationDecision = unitUnderTest.check(null, messageContext);

        assertThat(authorizationDecision).isNotNull();
        assertThat(authorizationDecision.isGranted()).isFalse();
    }

    @Test
    public void testCheck_NoDestination_AccessDenied() {
        MessageAuthorizationContext<String> messageContext = createMessageAuthorizationContextForDestination(null);

        AuthorizationDecision authorizationDecision = unitUnderTest.check(null, messageContext);

        assertThat(authorizationDecision).isNotNull();
        assertThat(authorizationDecision.isGranted()).isFalse();
    }

    @Test
    public void testCheck_NoTenantInContext_AccessDenied() {
        TenantContext.setTenantId(null);
        MessageAuthorizationContext<String> messageContext = createMessageAuthorizationContextForDestination(defaultDestination);

        AuthorizationDecision authorizationDecision = unitUnderTest.check(null, messageContext);

        assertThat(authorizationDecision).isNotNull();
        assertThat(authorizationDecision.isGranted()).isFalse();
    }

    private static MessageAuthorizationContext<String> createMessageAuthorizationContextForDestination(String destination) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(SimpMessageHeaderAccessor.DESTINATION_HEADER, destination);
        return new MessageAuthorizationContext<>(new GenericMessage<>("testMessage", headers));
    }
}