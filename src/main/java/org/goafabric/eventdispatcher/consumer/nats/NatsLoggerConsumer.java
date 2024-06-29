package org.goafabric.eventdispatcher.consumer.nats;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import org.goafabric.eventdispatcher.producer.EventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("nats")
public class NatsLoggerConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    static final String CONSUMER_NAME = "Logger";

    private final ObjectMapper objectMapper;

    public NatsLoggerConsumer(Connection natsConnection, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        natsConnection.createDispatcher().subscribe("main.topic", CONSUMER_NAME,
                msg -> process(msg.getHeaders().get("key").getFirst(), getEvent(msg.getData())));
    }

    private void process(String key, EventData eventData) {
        log.info("logging event: {}; id = {}", key, eventData.referenceId());
    }

    private EventData getEvent(byte[] eventData) {
        try { return objectMapper.readValue(eventData, EventData.class); } catch (IOException e) { throw new IllegalStateException(e); }
    }
}
