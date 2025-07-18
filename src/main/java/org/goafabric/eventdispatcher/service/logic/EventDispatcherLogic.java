package org.goafabric.eventdispatcher.service.logic;

import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventDispatcherLogic {
    private final EventProducer eventProducer;

    public EventDispatcherLogic(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    public String createPatient() {
        extracted("patient", DbOperation.CREATE);
        return "patient created";
    }

    public String updatePatient() {
        extracted("patient", DbOperation.UPDATE);
        return "patient updated";
    }

    public String createPractitioner() {
        extracted("practitioner", DbOperation.CREATE);
        return "practitioner created";
    }

    public String updatePractitioner() {
        extracted("practitioner", DbOperation.UPDATE);
        return "practitioner updated";
    }

    private void extracted(String topic, DbOperation operation) {
        eventProducer.produce(topic, UUID.randomUUID().toString(),
                new EventData(topic, UUID.randomUUID().toString(), operation.toString().toLowerCase(), null, UserContext.getAdapterHeaderMap()));
    }


}