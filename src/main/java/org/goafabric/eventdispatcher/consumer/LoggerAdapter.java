package org.goafabric.eventdispatcher.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggerAdapter {
    private static final String QUEUE_NAME = "LoggerQueue";

    /*
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = QUEUE_NAME),
            exchange = @Exchange(value = "main.topic", type = ExchangeTypes.TOPIC), key = {"patient.*", "practitioner.*"}))
    public void process(@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key, String id) {
     */
    @KafkaListener(id = "logger", topics = "main.topic")
    public void process(@Header(KafkaHeaders.RECEIVED_KEY) String key, String id) {
        switch (key) {
            case "patient.create" : createPatient(id); break;
            case "patient.update" : updatePatient(id); break;
            case "practitioner.create" : createPractitioner(id); break;
            case "practitioner.update" : updatePractitioner(id); break;
        }
    }

    private void createPatient(String id) {
        log.info("create patient; id = {}", id);
    }

    private void updatePatient(String id) {
        log.info("update patient; id = {}", id);
    }

    private void updatePractitioner(String id) {
        log.info("update practitioner; id = {}", id);
    }

    private void createPractitioner(String id) {
        log.info("create practitioner; id = {}", id);
    }
}
