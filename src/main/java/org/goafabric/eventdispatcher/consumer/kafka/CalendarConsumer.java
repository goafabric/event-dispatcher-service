package org.goafabric.eventdispatcher.consumer.kafka;

import org.goafabric.eventdispatcher.producer.EventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class CalendarConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    static final String CONSUMER_NAME = "Calendar";

    @KafkaListener(groupId = CONSUMER_NAME, topics = {"patient.create", "patient.update"})
    @KafkaListener(groupId = CONSUMER_NAME, topics = {"practitioner.create", "practitioner.update"})
    public void processKafka(@Header(KafkaHeaders.RECEIVED_TOPIC) String key, EventData eventData) {
        process(key, eventData);
    }

    private void process(String key, EventData eventData) {
        switch (key) {
            case "patient.create" : createPatient(eventData.referenceId()); break;
            case "patient.update" : updatePatient(eventData.referenceId()); break;
            case "practitioner.create" : createPractitioner(eventData.referenceId()); break;
            case "practitioner.update" : updatePractitioner(eventData.referenceId()); break;
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
