package org.goafabric.eventdispatcher.producer;

import lombok.NonNull;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
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
        send(eventTypeMapping.get(changeEvent.getType()) + "."  + changeEvent.getOperation().toString().toLowerCase(),
                changeEvent.getReferenceId());
    }

    private void send(@NonNull String key, @NonNull String referenceId) {
        kafkaTemplate.send("main.topic", key, EventData.builder().referenceId(referenceId).build());
    }

}
