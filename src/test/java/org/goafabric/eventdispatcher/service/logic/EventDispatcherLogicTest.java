package org.goafabric.eventdispatcher.service.logic;

import org.goafabric.eventdispatcher.producer.EventProducerKafka;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

class EventDispatcherLogicTest {
    private EventDispatcherLogic eventDispatcherLogic =
            new EventDispatcherLogic(Mockito.mock(EventProducerKafka.class));

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
    public void dispatch() {
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
        eventDispatcherLogic.dispatch(changeEvent);
    }


    private ChangeEvent createEvent(Object object, DbOperation operation) {
        return new ChangeEvent(
                UUID.randomUUID().toString(),
                "0",
                UUID.randomUUID().toString(),
                object.getClass().getSimpleName(),
                operation,
                "secret-service"
        );
    }
}