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
    public void createPatient() {
        eventDispatcherLogic.createPatient();
    }

    @GetMapping(value = "updatepatient")
    public void updatePatient() {
        eventDispatcherLogic.updatePatient();
    }

    @GetMapping(value = "createpractitioner")
    public void createPractitioner() {
        eventDispatcherLogic.createPractitioner();
    }

    @GetMapping(value = "updatepractitioner")
    public void updatePractitioner() {
        eventDispatcherLogic.updatePractitioner();
    }
}

