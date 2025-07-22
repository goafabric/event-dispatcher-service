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
        produce("patient.root", "patient", DbOperation.CREATE);
        return "patient created";
    }

    public String updatePatient() {
        produce("patient.root", "patient", DbOperation.UPDATE);
        return "patient updated";
    }

    public String createPractitioner() {
        produce("organization", "practitioner", DbOperation.CREATE);
        return "practitioner created";
    }

    public String updatePractitioner() {
        produce("organization", "practitioner", DbOperation.UPDATE);
        return "practitioner updated";
    }

    private void produce(String topic, String key, DbOperation operation) {
        eventProducer.produce(topic, UUID.randomUUID().toString(),
                new EventData(key, UUID.randomUUID().toString(), operation.toString().toLowerCase(), null, UserContext.getAdapterHeaderMap()));
    }


}