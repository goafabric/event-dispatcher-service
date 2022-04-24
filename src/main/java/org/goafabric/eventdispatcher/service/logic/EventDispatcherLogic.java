package org.goafabric.eventdispatcher.service.logic;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.service.dto.ChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventDispatcherLogic {
    @Autowired
    private EventProducer eventProducer;

    @Autowired
    public EventDispatcherLogic(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    public void dispatch(@RequestBody ChangeEvent changeEvent) {
        eventProducer.produce(changeEvent);
    }
}