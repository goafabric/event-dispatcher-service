package org.goafabric.eventdispatcher.service.logic;

import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Component
public class EventDispatcherLogic {
    private final EventProducer eventProducer;

    public EventDispatcherLogic(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    public void dispatch(@RequestBody ChangeEvent changeEvent) {
        eventProducer.produce(changeEvent);
    }

    public String createPatient() {
        eventProducer.produce(createEvent("Patient", DbOperation.CREATE));
        return "patient created";
    }

    public String updatePatient() {
        eventProducer.produce(createEvent("Patient", DbOperation.UPDATE));
        return "patient updated";
    }

    public String createPractitioner() {
        eventProducer.produce(createEvent("Practitioner", DbOperation.CREATE));
        return "practitioner created";
    }

    public String updatePractitioner() {
        eventProducer.produce(createEvent("Practitioner", DbOperation.UPDATE));
        return "practitioner updated";
    }

    private ChangeEvent createEvent(String typeName, DbOperation operation) {
        return new ChangeEvent(
                UUID.randomUUID().toString(),
                "0",
                UUID.randomUUID().toString(),
                typeName,
                operation,
                "secret-service"
        );
    }

}