package org.goafabric.eventdispatcher.consumer.nats;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import org.goafabric.eventdispatcher.producer.EventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("nats")
public class NatsCalendarConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    static final String CONSUMER_NAME = "Calendar";

    private final ObjectMapper objectMapper;

    public NatsCalendarConsumer(Connection natsConnection, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        natsConnection.createDispatcher().subscribe("main.topic", CONSUMER_NAME, //we would probably rather user separate topics for patient, practitioner
                msg -> process(msg.getHeaders().get("key").getFirst(), getEvent(msg.getData())));
    }

    private void process(String key, EventData eventData) {
        switch (key) {
            case "patient.create" : createPatient(eventData.referenceId()); break;
            case "patient.update" : updatePatient(eventData.referenceId()); break;
            case "practitioner.create" : createPractitioner(eventData.referenceId()); break;
            case "practitioner.update" : updatePractitioner(eventData.referenceId()); break;
        }
    }

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

    private EventData getEvent(byte[] eventData) {
        try { return objectMapper.readValue(eventData, EventData.class); } catch (IOException e) { throw new IllegalStateException(e); }
    }
}
