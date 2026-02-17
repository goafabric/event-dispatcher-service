package org.goafabric.eventdispatcher.service.logic;

import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class EventDispatcherLogicTest {

    private static class Patient {
    }

    private static class Employee {
    }

    private static class Organization {
    }

    @Test
    void dispatch() {
        var eventProducer = Mockito.mock(EventProducer.class);
        var eventProducerLogic = new EventDispatcherLogic(eventProducer);

        eventProducerLogic.createPatient();
        eventProducerLogic.updatePatient();

        eventProducerLogic.createPractitioner();
        eventProducerLogic.updatePractitioner();

        verify(eventProducer, times(2))
                .produce(anyString(), anyString(), eq(DbOperation.CREATE), any(Object.class));
        verify(eventProducer, times(2))
                .produce(anyString(), anyString(), eq(DbOperation.UPDATE), any(Object.class));
    }



}