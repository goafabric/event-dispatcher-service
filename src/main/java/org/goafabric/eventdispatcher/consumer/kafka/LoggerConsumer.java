package org.goafabric.eventdispatcher.consumer.kafka;

import org.goafabric.eventdispatcher.producer.EventData;
import org.goafabric.eventdispatcher.service.extensions.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class LoggerConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    static final String CONSUMER_NAME = "Logger";

    @KafkaListener(groupId = CONSUMER_NAME, topicPattern = ".*")
    public void processKafka(@Header(KafkaHeaders.RECEIVED_KEY) String key, EventData eventData) {
        TenantContext.setContext(eventData.tenantInfos());
        process(key, eventData);
    }

    private void process(String key, EventData eventData) {
        log.info("logging event: {}; id = {}", key, eventData.referenceId());
    }

}
