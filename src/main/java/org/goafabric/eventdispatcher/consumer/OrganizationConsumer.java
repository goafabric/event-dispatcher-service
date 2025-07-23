package org.goafabric.eventdispatcher.consumer;

import org.goafabric.event.EventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class OrganizationConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String CONSUMER_NAME = "Organization";
    private final CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(groupId = CONSUMER_NAME, topics = {"organization"}) //only topics listed here will be autocreated
    public void process(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, EventData eventData) {
        if ("practitioner".equals(eventData.type())) {
            switch (eventData.operation()) {
                case "create" -> createPractitioner(eventData.referenceId());
                case "update" -> updatePractitioner(eventData.referenceId());
                default -> throw new IllegalStateException("event operation not found");
            }
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
