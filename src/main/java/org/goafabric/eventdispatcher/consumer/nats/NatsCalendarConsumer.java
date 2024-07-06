package org.goafabric.eventdispatcher.consumer.nats;

import org.goafabric.eventdispatcher.producer.EventData;
import org.goafabric.eventdispatcher.service.extensions.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("nats")
public class NatsCalendarConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String CONSUMER_NAME = "Calendar";

    public NatsCalendarConsumer(NatsSubscription natsSubscription) {
        natsSubscription.create(CONSUMER_NAME, "patient.*", (msg, eventData) -> process(msg.getSubject(), eventData));
        natsSubscription.create(CONSUMER_NAME, "practitioner.*", (msg, eventData) -> process(msg.getSubject(), eventData));
    }

    private void process(String key, EventData eventData) {
        switch (key) {
            case "patient.create" -> createPatient(eventData.referenceId());
            case "patient.update" -> updatePatient(eventData.referenceId());
            case "practitioner.create" -> createPractitioner(eventData.referenceId());
            case "practitioner.update" -> updatePractitioner(eventData.referenceId());
        }
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
}
