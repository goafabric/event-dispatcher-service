package org.goafabric.eventdispatcher.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.nats.client.Connection;
import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.extensions.TenantContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("nats")
@Component
public class EventProducerNats implements EventProducer {
    private final Connection natsConnection;
    private final ObjectMapper objectMapper;
    private final ObservationRegistry observationRegistry;
    
    public EventProducerNats(Connection natsConnection, ObservationRegistry observationRegistry) {
        this.natsConnection = natsConnection;
        this.observationRegistry = observationRegistry;
        this.objectMapper = new ObjectMapper();
    }

    public void produce(ChangeEvent changeEvent) {
        send(changeEvent.type().toLowerCase(), changeEvent.operation().toString().toLowerCase(), changeEvent.referenceId(), changeEvent.payload());
    }

    private void send(String subject, String operation, String referenceId, Object payload) {
        var observation = Observation.createNotStarted(subject + " send", this.observationRegistry)
                        .lowCardinalityKeyValue("subject", subject)
                        .lowCardinalityKeyValue("tenant.id", TenantContext.getTenantId());
        observation.observe(() -> natsConnection.publish(subject, createEvent(new EventData(TenantContext.getAdapterHeaderMap(), referenceId, operation, payload))));
    }

    private byte[] createEvent(EventData eventData) {
        try { return objectMapper.writeValueAsBytes(eventData); } catch (JsonProcessingException e) { throw new IllegalStateException(e); }
    }

}
