package org.goafabric.eventdispatcher.consumer.nats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import io.nats.client.Connection;
import io.nats.client.PushSubscribeOptions;
import io.nats.client.api.ConsumerConfiguration;
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

    public NatsLoggerConsumer(Connection natsConnection) throws Exception {
        this.objectMapper = new ObjectMapper(new CBORFactory());

        var options = PushSubscribeOptions.builder()
                .configuration(ConsumerConfiguration.builder()
                        .durable(CONSUMER_NAME).deliverSubject("my-subject")
                        .build()
                ).build();


        var dispatcher = natsConnection.createDispatcher();

        natsConnection.jetStream().subscribe("*.*", dispatcher,
                msg -> process(msg.getSubject(), getEvent(msg.getData())), true, options);
    }

    private void process(String key, EventData eventData) {
        log.info("logging event: {}; id = {}", key, eventData.referenceId());
        //msg.ack();
    }

    private EventData getEvent(byte[] eventData) {
        try { return objectMapper.readValue(eventData, EventData.class); } catch (IOException e) { throw new IllegalStateException(e); }
    }
}
