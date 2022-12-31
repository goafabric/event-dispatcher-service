package org.goafabric.eventdispatcher.producer;

import lombok.extern.slf4j.Slf4j;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class EventProducer {
    //@Autowired private RabbitTemplate rabbitTemplate;

    @Autowired
    private HashMap<String, String> eventTypeMapping;

    public void produce(ChangeEvent changeEvent) {
        log.debug(changeEvent.toString());
        final String type = eventTypeMapping.get(changeEvent.getType());
        if (type != null) {
            /*
            rabbitTemplate.convertAndSend(
                    "main.topic",
                    type + "." + changeEvent.getOperation().toString().toLowerCase(),
                    changeEvent.getReferenceId());

             */
        }
    }
}
