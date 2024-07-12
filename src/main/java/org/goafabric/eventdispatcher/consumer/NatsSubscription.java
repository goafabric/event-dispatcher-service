package org.goafabric.eventdispatcher.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import io.nats.client.PushSubscribeOptions;
import io.nats.client.api.ConsumerConfiguration;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import org.goafabric.eventdispatcher.producer.EventData;
import org.goafabric.eventdispatcher.service.extensions.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;


@Component
public class NatsSubscription {
    private final Connection natsConnection;
    private final ObjectMapper objectMapper;
    private final ObservationRegistry observationRegistry;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public NatsSubscription(@Autowired(required = false) Connection natsConnection, ObservationRegistry observationRegistry) {
        this.natsConnection = natsConnection;
        this.observationRegistry = observationRegistry;
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

    private PushSubscribeOptions createDurableOptions(String consumerName, String subject) {
        var subscriberName = consumerName + "-" + subject.replaceAll("[*.]", ""); //unique consumer name per subject
        log.info("creating durable consumer {} with group {} ", subscriberName, subscriberName + "-group");
        return PushSubscribeOptions.builder()
                .configuration(ConsumerConfiguration.builder()
                        .durable(subscriberName)
                        //.deliverSubject(name + "-deliver") //must be set otherwise exception
                        .deliverGroup(subscriberName + "-group") //must be set to be deployable as replica
                        .build()
                ).build();
    }

    private MessageHandler createMessageHandler(EventMessageHandler eventHandler) {
        return msg -> withTenantInfos(() -> { //currently the spans are not connected on sender and receiver
            var observation = Observation.createNotStarted(msg.getSubject() + " receive", this.observationRegistry)
                    .lowCardinalityKeyValue("subject", msg.getSubject())
                    .lowCardinalityKeyValue("tenant.id", TenantContext.getTenantId());
            observation.observe(() -> eventHandler.onMessage(msg, getEvent(msg.getData())));
        });
    }

    private EventData getEvent(byte[] eventData) {
        try { return objectMapper.readValue(eventData, EventData.class); } catch (IOException e) { throw new IllegalStateException(e); }
    }

    public interface EventMessageHandler { // MessageHandler Proxy that takes care of deserializing the event
        void onMessage(Message msg, EventData eventData);
    }

    public static void withTenantInfos(Runnable runnable) {
        Span.fromContext(Context.current()).setAttribute("tenant.id", TenantContext.getTenantId());
        MDC.put("tenantId", TenantContext.getTenantId());
        try { runnable.run(); } finally { MDC.remove("tenantId"); }
    }

}
