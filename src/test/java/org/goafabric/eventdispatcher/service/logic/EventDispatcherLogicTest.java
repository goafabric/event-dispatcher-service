//package org.goafabric.eventdispatcher.service.logic;
//
//import org.goafabric.eventdispatcher.producer.EventProducerKafka;
//import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
//import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.util.UUID;
//
//class EventDispatcherLogicTest {
//
//    private static class Patient {
//    }
//
//    private static class Employee {
//    }
//
//    private static class Organization {
//    }
//
//    @Test
//    void dispatch() {
//        var eventProducer = Mockito.mock(EventProducerKafka.class);
//        var eventProducerLogic = new EventDispatcherLogic(eventProducer);
//
//        eventProducerLogic.dispatch(createEvent(new Patient(), DbOperation.CREATE));
//        eventProducerLogic.dispatch(createEvent(new Patient(), DbOperation.UPDATE));
//        eventProducerLogic.dispatch(createEvent(new Patient(), DbOperation.DELETE));
//
//        eventProducerLogic.dispatch(createEvent(new Employee(), DbOperation.CREATE));
//        eventProducerLogic.dispatch(createEvent(new Employee(), DbOperation.UPDATE));
//        eventProducerLogic.dispatch(createEvent(new Employee(), DbOperation.DELETE));
//
//        eventProducerLogic.dispatch(createEvent(new Organization(), DbOperation.CREATE));
//        eventProducerLogic.dispatch(createEvent(new Organization(), DbOperation.UPDATE));
//        eventProducerLogic.dispatch(createEvent(new Organization(), DbOperation.DELETE));
//
//        Mockito.verify(eventProducer, Mockito.times(9)).produce(Mockito.any(ChangeEvent.class));
//    }
//
//
//    private ChangeEvent createEvent(Object object, DbOperation operation) {
//        return new ChangeEvent(
//                UUID.randomUUID().toString(),
//                UUID.randomUUID().toString(),
//                object.getClass().getSimpleName(),
//                operation,
//                "secret-service",
//                null
//        );
//    }
//}