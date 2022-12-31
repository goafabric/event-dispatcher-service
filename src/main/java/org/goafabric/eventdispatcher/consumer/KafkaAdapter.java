package org.goafabric.eventdispatcher.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaAdapter {
    @KafkaListener(id = "myId", topics = "main.topic")
    public void listen(String referenceId) {
        System.out.println(referenceId);
    }

}
