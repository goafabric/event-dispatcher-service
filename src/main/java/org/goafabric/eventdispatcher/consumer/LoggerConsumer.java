package org.goafabric.eventdispatcher.consumer;

import org.goafabric.eventdispatcher.producer.EventData;
import org.goafabric.eventdispatcher.service.extensions.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static org.goafabric.eventdispatcher.consumer.NatsSubscription.withTenantInfos;

@Component
public class LoggerConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    static final String CONSUMER_NAME = "Logger";
    public static Long CONSUMER_COUNT = 0L;


    public LoggerConsumer(NatsSubscription natsSubscription) {
        natsSubscription.create(CONSUMER_NAME, "*", (msg, eventData) -> process(msg.getSubject(), eventData));
    }

    @KafkaListener(groupId = CONSUMER_NAME, topicPattern = ".*")
    public void processKafka(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, EventData eventData) {
        withTenantInfos(() -> process(topic, eventData));
    }

    private void process(String topic, EventData eventData) {
        log.info("logger event: {}; id = {}, payload = {}", topic + " " + eventData.operation(), eventData.referenceId(), eventData.payload() != null ? eventData.payload().toString() : "<none>");
        log.debug("tenantinfo: {}", TenantContext.getAdapterHeaderMap());
        CONSUMER_COUNT++;
    }

}
