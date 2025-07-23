package org.goafabric.eventdispatcher.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.service.controller.dto.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class PatientConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String CONSUMER_NAME = "Patient";
    private final CountDownLatch latch = new CountDownLatch(1);

    private final ObjectMapper objectMapper;

    public PatientConsumer() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @KafkaListener(groupId = CONSUMER_NAME, topics = {"patient.root"}) //only topics listed here will be autocreated
    public void process(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, EventData eventData) {
        if ("patient".equals(eventData.type())) {
            var patient = getPayLoad(eventData, Patient.class);
            switch (eventData.operation()) {
                case "create" -> createPatient(patient.id());
                case "update" -> updatePatient(patient.id());
                default -> throw new IllegalStateException("event operation not found");
            }
        }
        latch.countDown();
    }

    private void createPatient(String id) {
        log.info("create patient; id = {}", id);
    }

    private void updatePatient(String id) {
        log.info("update patient; id = {}", id);
    }

    public CountDownLatch getLatch() { return latch; }

    private <T> T getPayLoad(EventData eventData, Class<T> clazz) {
        return objectMapper.convertValue(eventData.payload(), clazz);
    }
}
