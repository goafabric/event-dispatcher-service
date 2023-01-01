package org.goafabric.eventdispatcher.producer;

import lombok.NonNull;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Profile("rabbit")
@Import(RabbitAutoConfiguration.class)
@Component
public class EventProducerRabbit implements EventProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HashMap<String, String> eventTypeMapping;

    public void produce(ChangeEvent changeEvent) {
        final String type = eventTypeMapping.get(changeEvent.getType());
        send(type + "."  + changeEvent.getOperation().toString().toLowerCase(), changeEvent.getReferenceId());
    }

    private void send(@NonNull String key, @NonNull String referenceId) {
        rabbitTemplate.convertAndSend("main.topic", key, referenceId);
    }

}
