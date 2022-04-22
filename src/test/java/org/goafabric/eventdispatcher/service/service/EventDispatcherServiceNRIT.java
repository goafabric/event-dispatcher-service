package org.goafabric.eventdispatcher.service.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class EventDispatcherServiceNRIT {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String DISPATCH_URL = "http://localhost:50500/events/dispatch";

    private static class Patient {
        private final String id = "44";
    }

    private static class Employee {
        private final String id = "46";
    }

    private static class Organization {
        private final String id = "48";
    }

    @Test
    public void testDispatch() {
        dispatch(createEvent(new Patient(), DbOperation.CREATE));
        dispatch(createEvent(new Patient(), DbOperation.UPDATE));
        dispatch(createEvent(new Patient(), DbOperation.DELETE));

        dispatch(createEvent(new Employee(), DbOperation.CREATE));
        dispatch(createEvent(new Employee(), DbOperation.UPDATE));
        dispatch(createEvent(new Employee(), DbOperation.DELETE));

        dispatch(createEvent(new Organization(), DbOperation.CREATE));
        dispatch(createEvent(new Organization(), DbOperation.UPDATE));
        dispatch(createEvent(new Organization(), DbOperation.DELETE));
    }

    private void dispatch(ChangeEvent changeEvent)  {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.postForEntity(DISPATCH_URL, new HttpEntity<>(changeEvent, headers), Void.class);
    }

    private ChangeEvent createEvent(Object object, DbOperation operation) {
        return ChangeEvent.builder()
                .id(UUID.randomUUID().toString())
                .tenantId("0")
                .referenceId("1")
                .operation(operation)
                .type(object.getClass().getSimpleName())
                .origin("origin")
                .build();
    }


}