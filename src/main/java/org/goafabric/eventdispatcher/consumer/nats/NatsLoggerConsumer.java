package org.goafabric.eventdispatcher.consumer.nats;

import org.goafabric.eventdispatcher.producer.EventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("nats")
public class NatsLoggerConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String CONSUMER_NAME = "Logger";

    public NatsLoggerConsumer(ConsumerUtil consumerUtil) {
        consumerUtil.subscribe(CONSUMER_NAME, "*.*",
                (msg, eventData) -> process(msg.getSubject(), eventData));
    }

    private void process(String key, EventData eventData) {
        log.info("logging event: {}; id = {}", key, eventData.referenceId());
        //msg.ack();
    }

}
