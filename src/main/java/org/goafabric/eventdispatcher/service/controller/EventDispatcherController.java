package org.goafabric.eventdispatcher.service.controller;

import org.goafabric.eventdispatcher.service.logic.EventDispatcherLogic;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "events", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventDispatcherController {
    @Autowired
    EventDispatcherLogic eventDispatcherLogic;

    @PostMapping(value = "dispatch", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void dispatch(@RequestBody ChangeEvent changeEvent) {
        eventDispatcherLogic.dispatch(changeEvent);
    }

    @GetMapping(value = "createpatient")
    public String createPatient() {
        eventDispatcherLogic.createPatient();
        return "patient created";
    }

    @GetMapping(value = "updatepatient")
    public String updatePatient() {
        eventDispatcherLogic.updatePatient();
        return "patient updated";
    }

    @GetMapping(value = "createpractitioner")
    public String createPractitioner() {
        eventDispatcherLogic.createPractitioner();
        return "practitioner created";
    }

    @GetMapping(value = "updatepractitioner")
    public String updatePractitioner() {
        eventDispatcherLogic.updatePractitioner();
        return "practitioner updated";
    }
}

