package org.goafabric.eventdispatcher.consumer.nats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.PushSubscribeOptions;
import io.nats.client.api.ConsumerConfiguration;
import org.goafabric.eventdispatcher.producer.EventData;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class ConsumerUtil {
    private final Connection natsConnection;
    private final ObjectMapper objectMapper;

    public ConsumerUtil(Connection natsConnection) {
        this.natsConnection = natsConnection;
        this.objectMapper = new ObjectMapper(new CBORFactory()); //binary serializer for performance
    }

    //creates a durable consumer which msg being delivered even if consumer is not running,
    //autoAck will automatically remove from queue when delivered, when renaming the consumer, it will just create a new one !
    public void subscribe(String consumerName, String subject, EventMessageHandler handler ) {
        try {
            natsConnection.jetStream().subscribe(subject, natsConnection.createDispatcher(),
                    msg -> handler.onMessage(msg, getEvent(msg.getData())),
                    true, createDurableOptions(consumerName, subject));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static PushSubscribeOptions createDurableOptions(String consumerName, String subject) {
        var name = consumerName + "-" + subject.replaceAll("[*.]", ""); //unique consumer name per subject
        return PushSubscribeOptions.builder()
                .configuration(ConsumerConfiguration.builder()
                        .durable(name)
                        .deliverSubject(name + "-deliver") //must be set otherwise exception
                        .deliverGroup(name + "-group") //must be set to be deployable as replica
                        .build()
                ).build();
    }

    private EventData getEvent(byte[] eventData) {
        try { return objectMapper.readValue(eventData, EventData.class); } catch (IOException e) { throw new IllegalStateException(e); }
    }

    public interface EventMessageHandler { // MessageHandler Proxy that takes care of deserializing the event
        void onMessage(Message msg, EventData eventData) throws InterruptedException;
    }

}
