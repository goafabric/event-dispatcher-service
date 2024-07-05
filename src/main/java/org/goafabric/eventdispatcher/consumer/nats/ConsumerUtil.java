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

    //creates a durable consumer which msg being delivered even if consumer is not running,
    //autoAck will automatically remove from queue when delivered, when renaming the consumer, it will just create a new one !
    public static void subscribe(Connection natsConnection, String consumerName, String subject, MessageHandler handler ) {
        try {
            natsConnection.jetStream().subscribe(subject, natsConnection.createDispatcher(), handler, true,
                    createDurableOptions(consumerName, subject));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static PushSubscribeOptions createDurableOptions(String consumerName, String subject) {
        var name = consumerName + "-" + subject.replaceAll("[*.]", "");
        return PushSubscribeOptions.builder()
                .configuration(ConsumerConfiguration.builder()
                        .durable(name)
                        .deliverSubject(name + "-deliver") //must be set otherwise exception
                        .deliverGroup(name + "-group") //must be set to be deployable as replica
                        .build()
                ).build();
    }

    public static EventData getEvent(byte[] eventData) {
        try { return objectMapper.readValue(eventData, EventData.class); } catch (IOException e) { throw new IllegalStateException(e); }
    }

}
