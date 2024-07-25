package org.goafabric.eventdispatcher.service.consumer;

import org.goafabric.eventdispatcher.consumer.LatchConsumer;
import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@Import(KafkaAutoConfiguration.class)
public class ConsumerIT {
    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private List<LatchConsumer> consumers;

    record Patient(String id, String givenName, String lastName, String gender, String payload) {};

    private String EXTRA_PAYLOAD = "";

    @Test
    void consumer() {
        eventProducer.produce(createEvent(createPatient(), DbOperation.CREATE));

        consumers.forEach(consumer -> {
            try { assertThat(consumer.getLatch().await(10, TimeUnit.SECONDS)).isTrue();
            } catch (InterruptedException e) { throw new RuntimeException(e);}
        });
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
        return new Patient(UUID.randomUUID().toString(), "Homer", "Simpson", "m", EXTRA_PAYLOAD);
    }
}
