package org.goafabric.eventdispatcher.producer;

import org.goafabric.eventdispatcher.service.service.ChangeEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void produce(ChangeEvent changeEvent) {
        rabbitTemplate.convertAndSend(
                changeEvent.getType().toLowerCase(),
                changeEvent.getOperation().toString(),
                changeEvent.getReferenceId());
    }
}
