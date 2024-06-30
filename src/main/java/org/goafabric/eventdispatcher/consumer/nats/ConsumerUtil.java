package org.goafabric.eventdispatcher.consumer.nats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import io.nats.client.Connection;
import io.nats.client.MessageHandler;
import io.nats.client.PushSubscribeOptions;
import io.nats.client.api.ConsumerConfiguration;
import org.goafabric.eventdispatcher.producer.EventData;

import java.io.IOException;

public class ConsumerUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper(new CBORFactory()); //binary serializer for performance

    private ConsumerUtil() {
    }

    //be careful with the consumername here, if you change it, a new consumer will be created and the old will not be removeed
    public static void subscribe(Connection natsConnection, String consumerName, String subject, MessageHandler handler ) {
        try {
            natsConnection.jetStream().subscribe(subject, natsConnection.createDispatcher(), handler, true, createDurableOptions(consumerName));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    //creates a durable consumer which msg being delivered even if consumer is not running, autoAck above will automatically remove from queue when delivered
    public static PushSubscribeOptions createDurableOptions(String consumerName) {
        return PushSubscribeOptions.builder()
                .configuration(ConsumerConfiguration.builder()
                        .durable(consumerName).deliverSubject(consumerName+ "-deliver")
                        .build()
                ).build();
    }

    public static EventData getEvent(byte[] eventData) {
        try { return objectMapper.readValue(eventData, EventData.class); } catch (IOException e) { throw new IllegalStateException(e); }
    }

}
