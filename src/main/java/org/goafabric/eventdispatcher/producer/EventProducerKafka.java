package org.goafabric.eventdispatcher.producer;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
@Profile("org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration")
@Component
@Slf4j
public class EventProducerKafka implements EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private HashMap<String, String> eventTypeMapping;

    public void produce(ChangeEvent changeEvent) {
        final String type = eventTypeMapping.get(changeEvent.getType());
        send(type, changeEvent.getOperation().toString().toLowerCase(), changeEvent.getReferenceId());
    }

    private void send(@NonNull String type, @NonNull String operation, @NonNull String referenceId) {
        kafkaTemplate.send("main.topic", type + "." + operation, referenceId);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("main.topic").partitions(10).replicas(1).build();
    }
}
