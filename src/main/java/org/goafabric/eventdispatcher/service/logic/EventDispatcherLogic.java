package org.goafabric.eventdispatcher.service.logic;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.goafabric.eventdispatcher.service.service.ChangeEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventDispatcherLogic {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void dispatch(@RequestBody ChangeEvent changeEvent) {
        log.info(changeEvent.toString());
        //rabbitTemplate.convertAndSend(changeEvent.getType().toLowerCase(), changeEvent.getOperation().toString().toLowerCase(),changeEvent.getId()  );
    }
}