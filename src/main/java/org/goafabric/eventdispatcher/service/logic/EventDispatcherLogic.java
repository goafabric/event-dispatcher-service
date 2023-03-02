package org.goafabric.eventdispatcher.service.logic;

import lombok.extern.slf4j.Slf4j;
import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Component
@Slf4j
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
        eventProducer.produce(createEvent("Employee", DbOperation.CREATE));
        return "practitioner created";
    }

    public String updatePractitioner() {
        eventProducer.produce(createEvent("Employee", DbOperation.UPDATE));
        return "practitioner updated";
    }

    private ChangeEvent createEvent(String typeName, DbOperation operation) {
        return ChangeEvent.builder()
                .id(UUID.randomUUID().toString())
                .tenantId("0")
                .referenceId(UUID.randomUUID().toString())
                .operation(operation)
                .type(typeName)
                .origin("secret-service")
                .build();
    }

}