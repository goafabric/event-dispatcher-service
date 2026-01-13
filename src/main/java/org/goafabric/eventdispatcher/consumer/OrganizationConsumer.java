package org.goafabric.eventdispatcher.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.goafabric.eventdispatcher.service.controller.dto.Practitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class OrganizationConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String CONSUMER_NAME = "Organization";

    private final CountDownLatch latch = new CountDownLatch(1);

    private final ObjectMapper objectMapper;

    public OrganizationConsumer() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @KafkaListener(groupId = CONSUMER_NAME, topics = {"organization.notification"}) //only topics listed here will be autocreated
    public void process(Practitioner practitioner, @Header("operation") String operation) {
        switch (operation) {
            case "CREATE" -> createPractitioner(practitioner.id());
            case "UPDATE" -> updatePractitioner(practitioner.id());
            default -> throw new IllegalStateException("event operation not found");
        }
        latch.countDown();
    }

    private void createPractitioner(String id) {
        log.info("create practitioner; id = {}", id);
    }

    private void updatePractitioner(String id) {
        log.info("update practitioner; id = {}", id);
    }


    public CountDownLatch getLatch() { return latch; }
}
