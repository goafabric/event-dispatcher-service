package org.goafabric.eventdispatcher.producer;

import org.goafabric.event.EventData;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {
    private final KafkaTemplate<String, EventData> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, EventData> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce(String topic, String key, EventData eventData) {
        kafkaTemplate.send(topic, key, eventData);
    }
}
