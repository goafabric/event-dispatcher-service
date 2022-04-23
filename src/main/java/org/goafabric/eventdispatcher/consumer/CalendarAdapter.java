package org.goafabric.eventdispatcher.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CalendarAdapter {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "CalendarQueue"),
            exchange = @Exchange(value = "patient", type = ExchangeTypes.TOPIC), key = {"CREATE"}
    ))
    public void createPatient(String id) {
        log.info("patient create for id = {}", id);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "CalendarQueue"),
            exchange = @Exchange(value = "patient", type = ExchangeTypes.TOPIC), key = {"UPDATE"}
    ))
    public void updatePatient(String id) {
        log.info("patient update for id = {}", id);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "CalendarQueue"),
            exchange = @Exchange(value = "patient", type = ExchangeTypes.TOPIC), key = {"DELETE"}
    ))
    public void deletePatient(String id) {
        log.info("patient delete for id = {}", id);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "CalendarQueue"),
            exchange = @Exchange(value = "organization", type = ExchangeTypes.TOPIC), key = {"CREATE"}
    ))
    public void createOrganization(String id) {
        log.info("organization create for id = {}", id);
    }
}
