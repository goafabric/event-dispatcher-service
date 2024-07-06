package org.goafabric.eventdispatcher.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import io.nats.client.Connection;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("nats")
public class EventProducerNats implements EventProducer {
    private final Connection natsConnection;
    private final ObjectMapper objectMapper;

    public EventProducerNats(Connection natsConnection) {
        this.natsConnection = natsConnection;
        this.objectMapper = new ObjectMapper(new CBORFactory());
    }


    public void produce(ChangeEvent changeEvent) {
        send(changeEvent.type().toLowerCase() + "."  + changeEvent.operation().toString().toLowerCase(),
                changeEvent.referenceId());
    }

    private void send(String subject, String referenceId) {
        natsConnection.publish(subject, createEvent(new EventData(referenceId)));
    }

    private byte[] createEvent(EventData eventData) {
        try { return objectMapper.writeValueAsBytes(eventData); } catch (JsonProcessingException e) { throw new IllegalStateException(e); }
    }

}