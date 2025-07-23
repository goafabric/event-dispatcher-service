package org.goafabric.eventdispatcher.consumer;

import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class LoggerConsumer  {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String CONSUMER_NAME = "Logger";
    private final CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(groupId = CONSUMER_NAME, topicPattern = ".*")
    public void processKafka(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, EventData eventData) {
        log.info("logger event: {} {}; id = {}, payload = {}", eventData.type(), eventData.operation(), eventData.referenceId(), eventData.payload() != null ? eventData.payload().toString() : "<none>");
        log.debug("tenantinfo: {}", UserContext.getAdapterHeaderMap());
        latch.countDown();
    }

    public CountDownLatch getLatch() { return latch; }
}
