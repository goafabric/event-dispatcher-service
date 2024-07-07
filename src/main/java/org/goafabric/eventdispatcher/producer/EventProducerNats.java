package org.goafabric.eventdispatcher.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.nats.client.Connection;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.extensions.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("nats")
@Component
public class EventProducerNats implements EventProducer {
    private final Connection natsConnection;
    private final ObjectMapper objectMapper;

    @Autowired
    Tracer tracer;
    
    public EventProducerNats(Connection natsConnection) {
        this.natsConnection = natsConnection;
        this.objectMapper = new ObjectMapper(new CBORFactory());
    }

    public void produce(ChangeEvent changeEvent) {
        Span newSpan = tracer. nextSpan().name("manualTraceExample").start();
        try (io.micrometer.tracing.Tracer.SpanInScope ws = tracer.withSpan(newSpan.start())) {
            // Add custom tags or logs if needed
            newSpan.tag("customTag", "customValue");
            send(changeEvent.type().toLowerCase() + "."  + changeEvent.operation().toString().toLowerCase(),
                    changeEvent.referenceId());
        } finally {
            // End the span
            newSpan.end();
        }
    }

    private void send(String subject, String referenceId) {
        natsConnection.publish(subject, createEvent(new EventData(TenantContext.getAdapterHeaderMap(), referenceId, null)));
    }

    private byte[] createEvent(EventData eventData) {
        try { return objectMapper.writeValueAsBytes(eventData); } catch (JsonProcessingException e) { throw new IllegalStateException(e); }
    }

}
