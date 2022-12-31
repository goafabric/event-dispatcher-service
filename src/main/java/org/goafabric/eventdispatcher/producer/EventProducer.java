package org.goafabric.eventdispatcher.producer;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class EventProducer {
    //@Autowired private RabbitTemplate rabbitTemplate;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private HashMap<String, String> eventTypeMapping;

    public void produce(ChangeEvent changeEvent) {
        log.debug(changeEvent.toString());
        final String type = eventTypeMapping.get(changeEvent.getType());
        send(type, changeEvent.getOperation().toString().toLowerCase(), changeEvent.getReferenceId());
    }

    private void send(@NonNull String type, @NonNull String operation, @NonNull String referenceId) {
        kafkaTemplate.send("main.topic", "##id " + referenceId);
        //rabbitTemplate.convertAndSend("main.topic", type + "."  + operation, referenceId);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("main.topic").partitions(10).replicas(1).build();
    }
}
