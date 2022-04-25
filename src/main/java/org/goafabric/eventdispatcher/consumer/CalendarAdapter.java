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

import static org.goafabric.eventdispatcher.listener.ListenerConstants.*;

@Component
@Slf4j
public class CalendarAdapter {
    private static final String QUEUE_NAME = "CalendarQueue";

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = QUEUE_NAME),
            exchange = @Exchange(value = MAIN_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC), key = {"patient.create"}))
    public void createPatient(@Header(AmqpHeaders.RECEIVED_EXCHANGE) String exchange, String id) {
        log.info("patient create for id = {}; exchange {}", id, exchange);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = QUEUE_NAME),
            exchange = @Exchange(value = MAIN_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC), key = {"patient.update"}))
    public void updatePatient(String id) {
        log.info("patient update for id = {}", id);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = QUEUE_NAME),
            exchange = @Exchange(value = MAIN_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC), key = {"patient.delete"}))
    public void deletePatient(String id) {
        log.info("patient delete for id = {}", id);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = QUEUE_NAME),
            exchange = @Exchange(value = MAIN_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC), key = {"practitioner.create"}))
    public void createPractitioner(String id) {
        log.info("practitioner create for id = {}", id);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = QUEUE_NAME),
            exchange = @Exchange(value = MAIN_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC), key = {"practitioner.update"}))
    public void updatePractitioner(String id) {
        log.info("practitioner update for id = {}", id);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = QUEUE_NAME),
            exchange = @Exchange(value = MAIN_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC), key = {"practitioner.delete"}))
    public void deletePractitioner(String id) {
        log.info("practitioner delete for id = {}", id);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = QUEUE_NAME),
            exchange = @Exchange(value = MAIN_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC), key = {"organization.create"}))
    public void createOrganization(String id) {
        log.info("organization create for id = {}", id);
    }
}
