package org.goafabric.eventdispatcher.service.consumer;

import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.consumer.OrganizationConsumer;
import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
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
class OrganizationConsumerIT {
    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private OrganizationConsumer consumer;

    record Patient(String id, String givenName, String lastName, String gender, String payload) {}

    @Test
    void consumer() throws InterruptedException {
        eventProducer.produce("organization", UUID.randomUUID().toString(),
                new EventData("practitioner", UUID.randomUUID().toString(), DbOperation.CREATE.toString().toLowerCase(), null, UserContext.getAdapterHeaderMap()));
        assertThat(consumer.getLatch().await(10, TimeUnit.SECONDS)).isTrue();
    }


}
