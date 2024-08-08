package org.goafabric.eventdispatcher.producer;

import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(("!kafka && !nats"))
public class EventProducerInMemory implements EventProducer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void produce(ChangeEvent changeEvent) {
        log.info("ChangeEvent received: {}", changeEvent);
    }
}
