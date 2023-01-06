package org.goafabric.eventdispatcher.consumer;

import lombok.extern.slf4j.Slf4j;
import org.goafabric.eventdispatcher.producer.EventData;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(bindings = @QueueBinding(value = @Queue(name = CalendarConsumer.CONSUMER_NAME),
        exchange = @Exchange(value = "main.topic", type = ExchangeTypes.TOPIC), key = {"patient.*", "practitioner.*"}))
public class CalendarConsumer {
    static final String CONSUMER_NAME = "Calendar";

    @RabbitHandler
    public void processRabbit(@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key, EventData eventData) {
        process(key, eventData);
    }

    @KafkaListener(groupId = CONSUMER_NAME, topics = "main.topic")
    public void processKafka(@Header(KafkaHeaders.RECEIVED_KEY) String key, EventData eventData) {
        process(key, eventData);
    }

    private void process(String key, EventData eventData) {
        switch (key) {
            case "patient.create" : createPatient(eventData.getReferenceId()); break;
            case "patient.update" : updatePatient(eventData.getReferenceId()); break;
            case "practitioner.create" : createPractitioner(eventData.getReferenceId()); break;
            case "practitioner.update" : updatePractitioner(eventData.getReferenceId()); break;
        }
    }

    private void createPatient(String id) {
        log.info("create patient; id = {}", id);
    }

    private void updatePatient(String id) {
        log.info("update patient; id = {}", id);
    }

    private void createPractitioner(String id) {
        log.info("create practitioner; id = {}", id);
    }

    private void updatePractitioner(String id) {
        log.info("update practitioner; id = {}", id);
    }

}
