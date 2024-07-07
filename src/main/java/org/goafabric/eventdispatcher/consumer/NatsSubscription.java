package org.goafabric.eventdispatcher.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import io.nats.client.PushSubscribeOptions;
import io.nats.client.api.ConsumerConfiguration;
import io.opentelemetry.api.trace.Tracer;
import org.goafabric.eventdispatcher.producer.EventData;
import org.goafabric.eventdispatcher.service.extensions.TenantContext;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;


@Component
public class NatsSubscription {
    private final Connection natsConnection;
    private final ObjectMapper objectMapper;
    private final Tracer tracer;

    public NatsSubscription(@Autowired(required = false) Connection natsConnection, Tracer tracer) {
        this.natsConnection = natsConnection;
        this.tracer = tracer;
        this.objectMapper = new ObjectMapper(new CBORFactory()); //binary serializer for performance
    }

    public void create(String consumerName, String subject, EventMessageHandler eventHandler) {
        create(consumerName, new String[]{subject}, eventHandler);
    }

    public void create(String consumerName, String[] subjects, EventMessageHandler eventHandler) {
        Arrays.stream(subjects).forEach(subject -> {
            try {
                if (natsConnection != null) {
                    natsConnection.jetStream().subscribe(subject, natsConnection.createDispatcher(),
                            createMessageHandler(eventHandler),
                            true, createDurableOptions(consumerName, subject));
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private MessageHandler createMessageHandler(EventMessageHandler eventHandler) {
        return msg -> {
            MDC.put("tenantId", TenantContext.getTenantId());
            var span = tracer.spanBuilder(msg.getSubject() + " receive").startSpan();
            try {
                span.setAttribute("subject", msg.getSubject());
                span.setAttribute("tenant.id", TenantContext.getTenantId());
                eventHandler.onMessage(msg, getEvent(msg.getData()));
            } finally {
                span.end();
            }
            MDC.remove("tenantId");
        };
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