package org.goafabric.eventdispatcher.service.logic;

import org.goafabric.eventdispatcher.producer.EventProducer;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.goafabric.eventdispatcher.service.controller.dto.Patient;
import org.goafabric.eventdispatcher.service.controller.dto.Practitioner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class EventDispatcherLogic {
    private final EventProducer eventProducer;

    public EventDispatcherLogic(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    public String createPatient() {
        producePatient(DbOperation.CREATE);
        return "patient created";
    }

    public String updatePatient() {
        producePatient(DbOperation.UPDATE);
        return "patient updated";
    }

    public String createPractitioner() {
        producePractitioner(DbOperation.CREATE);
        return "practitioner created";
    }

    public String updatePractitioner() {
        producePractitioner(DbOperation.UPDATE);
        return "practitioner updated";
    }

    private void producePatient(DbOperation operation) {
        var patient = new Patient(UUID.randomUUID().toString(), "Homer", "Simpson", "Male", LocalDate.of(1970, 01, 01));
        eventProducer.produce("patient.notification", patient.id(), operation, patient);
    }

    private void producePractitioner(DbOperation operation) {
        var practitioner = new Practitioner(UUID.randomUUID().toString(), UUID.randomUUID().toString(),"Homer", "Simpson", "Male", LocalDate.of(1970, 01, 01));
        eventProducer.produce("organization.notification", practitioner.organizationId(), operation, practitioner);
    }

}