package org.goafabric.eventdispatcher.producer;

import lombok.extern.slf4j.Slf4j;
import org.goafabric.eventdispatcher.listener.ListenerConstants;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class EventProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HashMap<String, String> exchanges;

    public void produce(ChangeEvent changeEvent) {
        log.debug(changeEvent.toString());
        final String type = exchanges.get(changeEvent.getType());
        if (type != null) {
            rabbitTemplate.convertAndSend(
                    ListenerConstants.MAIN_TOPIC_EXCHANGE,
                    type + "." + changeEvent.getOperation().toString().toLowerCase(),
                    changeEvent.getReferenceId());
        }
    }
}
