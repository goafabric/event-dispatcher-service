package org.goafabric.eventdispatcher.service.consumer;

import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.consumer.PatientConsumer;
import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
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
class PatientConsumerIT {
    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private PatientConsumer consumer;

    record Patient(String id, String givenName, String lastName, String gender, String payload) {}

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private KafkaListenerContainerFactory<?> kafkaListenerContainerFactory;

    @BeforeEach
    void setUp() {
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(container -> {
            if (container.getContainerProperties().getTopics() != null && "practitioner".equals(container.getContainerProperties().getTopics()[0])) {
                ContainerTestUtils.waitForAssignment(container, 1);
            }
        });
    }

    @Test
    @Disabled("Because of Kafka Embedded Timing Problems")
    void consumer() throws InterruptedException {
        producePatient(DbOperation.CREATE);
        assertThat(consumer.getLatch().await(10, TimeUnit.SECONDS)).isTrue();
    }

    private void producePatient(DbOperation operation) {
        var patient = new org.goafabric.eventdispatcher.service.controller.dto.Patient(UUID.randomUUID().toString(), "Homer", "Simpson", "Male", LocalDate.of(1970, 01, 01));
        eventProducer.produce("patient.root", patient.id(),
                new EventData("patient", operation.toString().toLowerCase(), patient, UserContext.getAdapterHeaderMap()));
    }



}
