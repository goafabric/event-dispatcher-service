package org.goafabric.eventdispatcher.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import io.nats.client.Connection;
import io.opentelemetry.api.trace.Tracer;
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
        send(changeEvent.type().toLowerCase() + "."  + changeEvent.operation().toString().toLowerCase(), changeEvent.referenceId());
    }

    private void send(String subject, String referenceId) {
        var span = tracer.spanBuilder(subject + " send").startSpan()
                .setAttribute("subject",subject).setAttribute("tenant.id", TenantContext.getTenantId());
        try {
            natsConnection.publish(subject, createEvent(new EventData(TenantContext.getAdapterHeaderMap(), referenceId, null)));
        } finally {
            span.end();
        }
    }

    private byte[] createEvent(EventData eventData) {
        try { return objectMapper.writeValueAsBytes(eventData); } catch (JsonProcessingException e) { throw new IllegalStateException(e); }
    }

}
