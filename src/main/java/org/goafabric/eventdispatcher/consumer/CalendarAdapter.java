package org.goafabric.eventdispatcher.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CalendarAdapter {
    private static final String QUEUE_NAME = "CalendarQueue";

    /*
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = QUEUE_NAME),
            exchange = @Exchange(value = "main.topic", type = ExchangeTypes.TOPIC), key = {"patient.*", "practitioner.*"}))
    public void process(@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key, String id) {
        switch (key) {
            case "patient.create" : createPatient(id); break;
            case "patient.update" : updatePatient(id); break;
            case "practitioner.create" : createPractitioner(id); break;
            case "practitioner.update" : updatePractitioner(id); break;
        }
    }

     */

    private void createPatient(String id) {
        log.info("create patient; id = {}", id);
    }

    private void updatePatient(String id) {
        log.info("update patient; id = {}", id);
    }

    private void createPractitioner(String id) {
        log.info("create practitioner; id = {}", id);
    }

    private void updatePractitioner(String id) {
        log.info("update practitioner; id = {}", id);
    }

}
