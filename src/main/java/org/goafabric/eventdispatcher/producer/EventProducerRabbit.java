package org.goafabric.eventdispatcher.producer;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Profile("org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@Component
@Slf4j
public class EventProducerRabbit implements EventProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HashMap<String, String> eventTypeMapping;

    public void produce(ChangeEvent changeEvent) {
        final String type = eventTypeMapping.get(changeEvent.getType());
        send(type, changeEvent.getOperation().toString().toLowerCase(), changeEvent.getReferenceId());
    }

    private void send(@NonNull String type, @NonNull String operation, @NonNull String referenceId) {
        rabbitTemplate.convertAndSend("main.topic", type + "."  + operation, referenceId);
    }

}
