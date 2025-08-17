package org.goafabric.eventdispatcher.service.consumer;

import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.consumer.OrganizationConsumer;
import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.goafabric.eventdispatcher.service.controller.dto.Practitioner;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@Import(KafkaAutoConfiguration.class)
class OrganizationConsumerIT {
    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private OrganizationConsumer consumer;

    record Patient(String id, String givenName, String lastName, String gender, String payload) {}

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @BeforeEach
    void setUp() {
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(container -> {
            if (container.getContainerProperties().getTopics() != null && "patient.root".equals(container.getContainerProperties().getTopics()[0])) {
                ContainerTestUtils.waitForAssignment(container, 1);
            }
        });
    }

    @Test
    void consumer() throws InterruptedException {
        producePractitioner(DbOperation.CREATE);
        assertThat(consumer.getLatch().await(10, TimeUnit.SECONDS)).isTrue();
    }

    private void producePractitioner(DbOperation operation) {
        var practitioner = new Practitioner(UUID.randomUUID().toString(), UUID.randomUUID().toString(),"Homer", "Simpson", "Male", LocalDate.of(1970, 01, 01));
        eventProducer.produce("organization", practitioner.organizationId(),
                new EventData("practitioner", operation.toString().toLowerCase(), practitioner, UserContext.getAdapterHeaderMap()));
    }

}
