package org.goafabric.eventdispatcher.consumer.nats;

import io.nats.client.Connection;
import org.goafabric.eventdispatcher.producer.EventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static org.goafabric.eventdispatcher.consumer.nats.ConsumerUtil.getEvent;
import static org.goafabric.eventdispatcher.consumer.nats.ConsumerUtil.subscribe;

@Component
@Profile("nats")
public class NatsLoggerConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String CONSUMER_NAME = "Logger";

    public NatsLoggerConsumer(Connection natsConnection) {
        subscribe(natsConnection, CONSUMER_NAME, "*.*",
                msg -> process(msg.getSubject(), getEvent(msg.getData())));
    }

    private void process(String key, EventData eventData) {
        log.info("logging event: {}; id = {}", key, eventData.referenceId());
        //msg.ack();
    }


}
