package org.goafabric.eventdispatcher.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import static org.goafabric.eventdispatcher.listener.ListenerConstants.*;

//@Component
@Slf4j
public class LoggerAdapter {
    private static final String QUEUE_NAME = "LoggingQueue";

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = QUEUE_NAME, durable = "false"),
        exchange = @Exchange(value = PATIENT, type = ExchangeTypes.TOPIC),
        key = { "#" }
    ))
    public void logExchangePatient(@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, String id) {
        log.info("type = patient; operation = {}; id = {}", routingKey, id);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = QUEUE_NAME, durable = "false"),
            exchange = @Exchange(value = PRACTITIONER, type = ExchangeTypes.TOPIC),
            key = { "#" }
    ))
    public void logExchangePractice(@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, String id) {
        log.info("type = practice; operation = {}; id = {}", routingKey, id);
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = QUEUE_NAME, durable = "false"),
        exchange = @Exchange(value = ORGANIZATION, type = ExchangeTypes.TOPIC),
        key = { "#" }
    ))
    public void logExchangeOrganization(@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, String id) {
        log.info("type = organization; operation = {}; id = {}", routingKey, id);
    }
}
