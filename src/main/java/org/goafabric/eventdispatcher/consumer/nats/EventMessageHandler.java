package org.goafabric.eventdispatcher.consumer.nats;

import io.nats.client.Message;
import org.goafabric.eventdispatcher.producer.EventData;


public interface EventMessageHandler {
    void onMessage(Message msg, EventData eventData) throws InterruptedException;
}
