package org.goafabric.eventdispatcher.producer;

import org.goafabric.eventdispatcher.service.service.ChangeEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class EventProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HashMap<String, String> exchanges;

    public void produce(ChangeEvent changeEvent) {
        final String exchange = exchanges.get(changeEvent.getType());
        if (exchange != null) {
            rabbitTemplate.convertAndSend(
                    exchange,
                    changeEvent.getOperation().toString(),
                    changeEvent.getReferenceId());
        }
    }
}
