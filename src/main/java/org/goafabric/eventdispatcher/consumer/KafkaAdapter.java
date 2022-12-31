package org.goafabric.eventdispatcher.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class KafkaAdapter {
    @KafkaListener(id = "myId", topics = "main.topic")
    public void listen(@Header(KafkaHeaders.RECEIVED_KEY) String key, String id) {
        System.out.println(key);
        System.out.println(id);
    }

}
