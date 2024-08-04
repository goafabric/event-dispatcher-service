package org.goafabric.eventdispatcher.consumer;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.service.extensions.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class InvoiceConsumer implements LatchConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String CONSUMER_NAME = "Invoice";
    private final CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(groupId = CONSUMER_NAME, topicPattern = ".*", topics = {"condition", "chargeitem"})
    public void processKafka(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, EventData eventData) {
        withTenantInfos(() -> process(topic, eventData));
    }

    private void process(String topic, EventData eventData) {
        log.info("logger event: {}; id = {}, payload = {}", topic + " " + eventData.operation(), eventData.referenceId(), eventData.payload() != null ? eventData.payload().toString() : "<none>");
        log.debug("tenantinfo: {}", TenantContext.getAdapterHeaderMap());
        latch.countDown();
    }

    private static void withTenantInfos(Runnable runnable) {
        Span.fromContext(Context.current()).setAttribute("tenant.id", TenantContext.getTenantId());
        MDC.put("tenantId", TenantContext.getTenantId());
        try { runnable.run(); } finally { MDC.remove("tenantId"); }
    }

    @Override
    public CountDownLatch getLatch() { return latch; }
}
