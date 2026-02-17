package org.goafabric.eventdispatcher.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class LoggerConsumer  {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String CONSUMER_NAME = "Logger";
    private final CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(groupId = CONSUMER_NAME, topicPattern = ".*")
    public void process(ConsumerRecord consumerRecord, @Header("operation") String operation) {
        String type = consumerRecord.value().getClass().getSimpleName().toLowerCase();
        log.info("logger event: {} {}; payload = {}", type, operation, consumerRecord.value() != null ? consumerRecord.value().toString() : "<none>");
        log.debug("tenantinfo: {}", UserContext.getAdapterHeaderMap());
        latch.countDown();
    }


    public CountDownLatch getLatch() { return latch; }
}
