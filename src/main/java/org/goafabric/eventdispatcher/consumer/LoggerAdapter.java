package org.goafabric.eventdispatcher.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggerAdapter {

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(durable = "false"),
        exchange = @Exchange(value = "patient", type = ExchangeTypes.TOPIC),
        key = { "#" }
    ))
    public void logExchangePatient(@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, String id) {
        log.info("logExchangePatient: routingKey={}; {}", routingKey, id);
    }


    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(durable = "false"),
        exchange = @Exchange(value = "practice", type = ExchangeTypes.TOPIC),
        key = { "#" }
    ))
    public void logExchangePractice(@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, String id) {
        log.info("logExchangePractice: routingKey={}; {}", routingKey, id);
    }
}
