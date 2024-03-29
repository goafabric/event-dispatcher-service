package org.goafabric.eventdispatcher.consumer;

import org.goafabric.eventdispatcher.producer.EventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static org.goafabric.eventdispatcher.consumer.LoggerConsumer.CONSUMER_NAME;

@Component
@RabbitListener(bindings = @QueueBinding(value = @Queue(name = CONSUMER_NAME),
        exchange = @Exchange(value = "main.topic", type = ExchangeTypes.TOPIC), key = {"*"}))
public class LoggerConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    static final String CONSUMER_NAME = "Logger";

    @RabbitHandler
    public void processRabbit(@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key, EventData eventData) {
        process(key, eventData);
    }

    @KafkaListener(groupId = CONSUMER_NAME, topics = "main.topic")
    public void processKafka(@Header(KafkaHeaders.RECEIVED_KEY) String key, EventData eventData) {
        process(key, eventData);
    }

    private void process(String key, EventData eventData) {
        log.info("logging event: {}; id = {}", key, eventData.referenceId());
    }

}
