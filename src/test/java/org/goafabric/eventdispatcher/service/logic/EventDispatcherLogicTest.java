package org.goafabric.eventdispatcher.service.logic;

import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.producer.EventProducer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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

        Mockito.verify(eventProducer, Mockito.times(4)).produce(anyString(), anyString(), any(EventData.class));
    }



}