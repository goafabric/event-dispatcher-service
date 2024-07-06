package org.goafabric.eventdispatcher.consumer.nats;

import org.goafabric.eventdispatcher.producer.EventData;
import org.goafabric.eventdispatcher.service.extensions.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("nats")
public class NatsLoggerConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String CONSUMER_NAME = "Logger";

    public NatsLoggerConsumer(NatsSubscription natsSubscription) {
        natsSubscription.create(CONSUMER_NAME, "*.*", (msg, eventData) -> process(msg.getSubject(), eventData));
    }

    private void process(String key, EventData eventData) {
        log.info("tenantinfo: {}", TenantContext.getAdapterHeaderMap());
        log.info("logging event: {}; id = {}", key, eventData.referenceId());
        //msg.ack();
    }

}
