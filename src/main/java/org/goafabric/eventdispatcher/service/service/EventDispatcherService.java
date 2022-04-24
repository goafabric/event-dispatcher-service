package org.goafabric.eventdispatcher.service.service;

import org.goafabric.eventdispatcher.service.logic.EventDispatcherLogic;
import org.goafabric.eventdispatcher.service.service.dto.ChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "events", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventDispatcherService {
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
}

