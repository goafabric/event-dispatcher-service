package org.goafabric.eventdispatcher.consumer.kafka;

import org.goafabric.eventdispatcher.producer.EventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(bindings = @QueueBinding(value = @Queue(name = CalendarConsumer.CONSUMER_NAME),
        exchange = @Exchange(value = "main.topic", type = ExchangeTypes.TOPIC), key = {"patient.*", "practitioner.*"}))
public class CalendarConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

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
            case "patient.create" : createPatient(eventData.referenceId()); break;
            case "patient.update" : updatePatient(eventData.referenceId()); break;
            case "practitioner.create" : createPractitioner(eventData.referenceId()); break;
            case "practitioner.update" : updatePractitioner(eventData.referenceId()); break;
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
