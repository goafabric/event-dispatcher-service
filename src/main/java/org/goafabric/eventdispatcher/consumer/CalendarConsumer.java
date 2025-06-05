package org.goafabric.eventdispatcher.consumer;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import org.goafabric.event.EventData;
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
public class CalendarConsumer implements LatchConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String CONSUMER_NAME = "Calendar";
    private final CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(groupId = CONSUMER_NAME, topics = {"patient", "practitioner"}) //only topics listed here will be autocreated
    public void processKafka(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, EventData eventData) {
        withTenantInfos(() -> process(topic, eventData));
    }

    private void process(String topic, EventData eventData) {
        switch (topic) {
            case "patient" -> {
                switch (eventData.operation()) {
                    case "create" -> createPatient(eventData.referenceId());
                    case "update" -> updatePatient(eventData.referenceId());
                    default -> throw new IllegalStateException("event operation not found");
                }
            }
            case "practitioner" -> {
                switch (eventData.operation()) {
                    case "create" -> createPractitioner(eventData.referenceId());
                    case "update" -> updatePractitioner(eventData.referenceId());
                    default -> throw new IllegalStateException("event operation not found");
                }
            }
            default -> throw new IllegalStateException("unknown topic");
        }
        latch.countDown();
    }

    private void createPatient(String id) {
        log.info("calendar create patient; id = {}", id);
    }

    private void updatePatient(String id) {
        log.info("calendar update patient; id = {}", id);
    }

    private void createPractitioner(String id) {
        log.info("calendar create practitioner; id = {}", id);
    }

    private void updatePractitioner(String id) {
        log.info("calendar update practitioner; id = {}", id);
    }

    private static void withTenantInfos(Runnable runnable) {
        Span.fromContext(Context.current()).setAttribute("tenant.id", UserContext.getTenantId());
        MDC.put("tenantId", UserContext.getTenantId());
        try { runnable.run(); } finally { MDC.remove("tenantId"); }
    }

    @Override
    public CountDownLatch getLatch() { return latch; }
}
