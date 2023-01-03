package org.goafabric.eventdispatcher.producer;

import lombok.NonNull;
import org.apache.kafka.clients.admin.NewTopic;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
@Profile("kafka")
@Import(KafkaAutoConfiguration.class)
@Component
public class EventProducerKafka implements EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private HashMap<String, String> eventTypeMapping;

    public void produce(ChangeEvent changeEvent) {
        send(eventTypeMapping.get(changeEvent.getType()) + "."  + changeEvent.getOperation().toString().toLowerCase(),
                changeEvent.getReferenceId());
    }

    private void send(@NonNull String key, @NonNull String referenceId) {
        kafkaTemplate.send("main.topic", key, EventData.builder().referenceId(referenceId).build());
    }

    @Bean //creates topic inside kafka broker
    public NewTopic topic() {
        return TopicBuilder.name("main.topic").partitions(10).replicas(1).build();
    }

}
