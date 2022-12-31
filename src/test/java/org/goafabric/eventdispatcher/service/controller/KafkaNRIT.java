package org.goafabric.eventdispatcher.service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
public class KafkaNRIT {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Test
    public void test() throws InterruptedException {
        kafkaTemplate.send("topic1", "#hit me baby one more time");
        Thread.sleep(1000);
    }
}