package org.goafabric.eventdispatcher.service.consumer;

import org.goafabric.eventdispatcher.consumer.CalendarConsumer;
import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@Import(KafkaAutoConfiguration.class)
class CalendarConsumerIT {
    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private CalendarConsumer consumer;

    record Patient(String id, String givenName, String lastName, String gender, String payload) {}


    @BeforeEach
    void setup() throws Exception {
        // Give Kafka a moment to fully start up and create the topic
        Thread.sleep(1000);
    }

    @Test
    void consumer() throws InterruptedException {
        eventProducer.produce(createEvent(createPatient(), DbOperation.CREATE));
        assertThat(consumer.getLatch().await(10, TimeUnit.SECONDS)).isTrue();
    }

    private ChangeEvent createEvent(Object object, DbOperation operation) {
        return new ChangeEvent(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "patient",
                operation,
                "secret-service",
                object
        );
    }

    private Patient createPatient() {
        return new Patient(UUID.randomUUID().toString(), "Homer", "Simpson", "m", "");
    }

}
