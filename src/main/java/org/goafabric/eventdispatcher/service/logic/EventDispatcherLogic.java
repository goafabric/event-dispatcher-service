package org.goafabric.eventdispatcher.service.logic;

import lombok.extern.slf4j.Slf4j;
import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.service.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.service.dto.DbOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Component
@Slf4j
public class EventDispatcherLogic {
    @Autowired
    private EventProducer eventProducer;

    @Autowired
    public EventDispatcherLogic(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    public void dispatch(@RequestBody ChangeEvent changeEvent) {
        eventProducer.produce(changeEvent);
    }

    public void createPatient() {
        eventProducer.produce(createEvent("Patient", DbOperation.CREATE));
    }

    public void updatePatient() {
        eventProducer.produce(createEvent("Patient", DbOperation.UPDATE));
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