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

import static org.goafabric.eventdispatcher.listener.ListenerConstants.MAIN_TOPIC_EXCHANGE;

@Slf4j
@Component
public class CalendarAdapterNew {
    private static final String QUEUE_NAME = "CalendarQueue";

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = QUEUE_NAME),
            exchange = @Exchange(value = MAIN_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC), key = {"patient.*", "practitioner.*"}))
    public void process(@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key, String id) {
        if (key.equals("patient.create")) {
            createPatient(id);
        }

        if (key.equals("patient.update")) {
            updatePatient(id);
        }

        if (key.equals("practitioner.create")) {
            createPractitioner(id);
        }

        if (key.equals("practitioner.update")) {
            updatePractitioner(id);
        }
    }

    private void createPatient(String id) {
        log.info("calendar; create patient");
    }

    private void updatePatient(String id) {
        log.info("calendar; update patient");
    }

    private void updatePractitioner(String id) {
        log.info("calendar; update practitioner");
    }

    private void createPractitioner(String id) {
        log.info("calendar; create practitioner");
    }
}
