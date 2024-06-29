package org.goafabric.eventdispatcher.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import io.nats.client.impl.Headers;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("nats")
public class EventProducerNats implements EventProducer {
    private final Connection natsConnection;
    private final ObjectMapper objectMapper;

    public EventProducerNats(Connection natsConnection, ObjectMapper objectMapper) {
        this.natsConnection = natsConnection;
        this.objectMapper = objectMapper;
    }


    public void produce(ChangeEvent changeEvent) {
        send(changeEvent.type().toLowerCase() + "."  + changeEvent.operation().toString().toLowerCase(),
                changeEvent.referenceId());
    }

    private void send(String key, String referenceId) {
        natsConnection.publish("main.topic", new Headers().put("key", key), createEvent(new EventData(referenceId)));
    }

    private byte[] createEvent(EventData eventData) {
        try { return objectMapper.writeValueAsBytes(eventData); } catch (JsonProcessingException e) { throw new IllegalStateException(e); }
    }

}
