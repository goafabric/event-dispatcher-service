package org.goafabric.eventdispatcher.producer;

import lombok.NonNull;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Profile("rabbitmq")
@Import(RabbitAutoConfiguration.class)
@Component
public class EventProducerRabbit implements EventProducer {
    private final RabbitTemplate rabbitTemplate;

    private final HashMap<String, String> eventTypeMapping;

    public EventProducerRabbit(RabbitTemplate rabbitTemplate, HashMap<String, String> eventTypeMapping) {
        this.rabbitTemplate = rabbitTemplate;
        this.eventTypeMapping = eventTypeMapping;
    }

    public void produce(ChangeEvent changeEvent) {
        send(eventTypeMapping.get(changeEvent.getType()) + "."  + changeEvent.getOperation().toString().toLowerCase(),
                changeEvent.getReferenceId());
    }

    private void send(@NonNull String key, @NonNull String referenceId) {
        rabbitTemplate.convertAndSend("main.topic", key, EventData.builder().referenceId(referenceId).build());
    }

}
