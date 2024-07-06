package org.goafabric.eventdispatcher.producer;

import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.extensions.TenantContext;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
@Profile("kafka")
@Import(KafkaAutoConfiguration.class)
@Component
public class EventProducerKafka implements EventProducer {

    private final KafkaTemplate kafkaTemplate;

    private final HashMap<String, String> eventTypeMapping;

    public EventProducerKafka(KafkaTemplate kafkaTemplate, HashMap<String, String> eventTypeMapping) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventTypeMapping = eventTypeMapping;
    }

    public void produce(ChangeEvent changeEvent) {
        send(changeEvent.type().toLowerCase(), changeEvent.type().toLowerCase() + "."  + changeEvent.operation().toString().toLowerCase(),
                changeEvent.referenceId());
    }

    private void send(String topic, String key, String referenceId) {
        kafkaTemplate.send(topic, key, new EventData(TenantContext.getAdapterHeaderMap(), referenceId, null));
    }

}
