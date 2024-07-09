package org.goafabric.eventdispatcher.consumer;

import org.goafabric.eventdispatcher.producer.EventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static org.goafabric.eventdispatcher.consumer.NatsSubscription.withTenantInfos;

@Component
public class CalendarConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    static final String CONSUMER_NAME = "Calendar";

    public CalendarConsumer(NatsSubscription natsSubscription) {
        natsSubscription.create(CONSUMER_NAME, new String[]{"patient", "practitioner"},
                (msg, eventData) -> process(msg.getSubject(), eventData));
    }

    @KafkaListener(groupId = CONSUMER_NAME, topics = {"patient", "practitioner"}) //only topics listed here will be autocreated
    public void processKafka(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, EventData eventData) {
        withTenantInfos(() -> process(topic, eventData));
    }

    private void process(String topic, EventData eventData) {
        if (topic.equals("patient")) {
            switch (eventData.operation()) {
                case "create" -> createPatient(eventData.referenceId());
                case "update" -> updatePatient(eventData.referenceId());
            }
        }
        if (topic.equals("practitioner")) {
            switch (eventData.operation()) {
                case "create" -> createPractitioner(eventData.referenceId());
                case "update" -> updatePractitioner(eventData.referenceId());
            }
        }
    }

    private void createPatient(String id) {
        log.info("calendar create patient; id = {}", id);
    }

    private void updatePatient(String id) {
        log.info("calendar update patient; id = {}", id);
    }

    private void createPractitioner(String id) {
        log.info("calendar create practitioner; id = {}", id);
    }

    private void updatePractitioner(String id) {
        log.info("calendar update practitioner; id = {}", id);
    }

}
