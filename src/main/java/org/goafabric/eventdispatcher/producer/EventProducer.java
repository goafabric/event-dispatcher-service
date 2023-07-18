package org.goafabric.eventdispatcher.producer;

import org.goafabric.eventdispatcher.service.controller.vo.ChangeEvent;

public interface EventProducer {
    void produce(ChangeEvent changeEvent);
}
