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
public class PatientAdapter {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "PatientQueue"),
            exchange = @Exchange(value = "patient", type = ExchangeTypes.TOPIC), key = {"CREATE"}
    ))
    public void create(String id) {
        log.info("patient create for id = {}", id);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "PatientQueue"),
            exchange = @Exchange(value = "patient", type = ExchangeTypes.TOPIC), key = {"UPDATE"}
    ))
    public void update(String id) {
        log.info("patient update for id = {}", id);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "PatientQueue"),
            exchange = @Exchange(value = "patient", type = ExchangeTypes.TOPIC), key = {"DELETE"}
    ))
    public void delete(String id) {
        log.info("patient delete for id = {}", id);
    }
}
