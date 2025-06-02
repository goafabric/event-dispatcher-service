package org.goafabric.eventdispatcher.service.producer;

import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class EventProducerLoadNRIT {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EventProducer eventProducer;

    record Patient(String id, String givenName, String lastName, String gender, String payload) {};

    @Test
    void testProduce() {
        long duration = 10;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < (duration * 1000)) {
            eventProducer.produce(createEvent(createPatient(), DbOperation.CREATE));
            eventProducer.produce(createEvent(createPatient(), DbOperation.CREATE));
        }
        long count = TestConsumer.getConsumerCount();
        log.info("iteration for {} s, events processed {}, events/s {}", duration, count, count / duration);
    }

    private ChangeEvent createEvent(Object object, DbOperation operation) {
        return new ChangeEvent(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "test-topic",
                operation,
                "secret-service",
                object
        );
    }

    private Patient createPatient() {
        return new Patient(UUID.randomUUID().toString(), "Homer", "Simpson", "m", "");
    }
}