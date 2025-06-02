package org.goafabric.eventdispatcher.service.producer;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.consumer.LatchConsumer;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class TestConsumer implements LatchConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    static final String CONSUMER_NAME = "Calendar";
    public static Long CONSUMER_COUNT = 0L;

    private final CountDownLatch latch = new CountDownLatch(1);


    @KafkaListener(groupId = CONSUMER_NAME, topics = {"test-topic"}) //only topics listed here will be autocreated
    public void processKafka(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, EventData eventData) {
        withTenantInfos(() -> process(topic, eventData));
    }

    private void process(String topic, EventData eventData) {
        log.info("processing test event {} {}", topic, eventData.toString());
        CONSUMER_COUNT++;
        latch.countDown();
    }

    private static void withTenantInfos(Runnable runnable) {
        Span.fromContext(Context.current()).setAttribute("tenant.id", UserContext.getTenantId());
        MDC.put("tenantId", UserContext.getTenantId());
        try { runnable.run(); } finally { MDC.remove("tenantId"); }
    }

    @Override
    public CountDownLatch getLatch() { return latch; }
}
