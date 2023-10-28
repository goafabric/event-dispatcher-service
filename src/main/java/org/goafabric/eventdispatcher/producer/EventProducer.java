package org.goafabric.eventdispatcher.producer;

import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;

public interface EventProducer {
    void produce(ChangeEvent changeEvent);
}
