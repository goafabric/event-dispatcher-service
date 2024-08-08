package org.goafabric.eventdispatcher.service.controller;

import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.logic.EventDispatcherLogic;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "events", produces = MediaType.APPLICATION_JSON_VALUE)
@MessageMapping(value = "events")
public class EventDispatcherController {
    private final EventDispatcherLogic eventDispatcherLogic;


    public EventDispatcherController(EventDispatcherLogic eventDispatcherLogic) {
        this.eventDispatcherLogic = eventDispatcherLogic;
    }

    //dispatch event to be called from external rest clients
    @PostMapping(value = "dispatch", consumes = MediaType.APPLICATION_JSON_VALUE)
    @MessageMapping("dispatch")
    public void dispatch(@RequestBody ChangeEvent changeEvent) {
        eventDispatcherLogic.dispatch(changeEvent);
    }

    //events to be called from html page
    @GetMapping("createpatient")
    @MessageMapping("createpatient")
    public void createPatient() {
        eventDispatcherLogic.createPatient();
    }

    @GetMapping("updatepatient")
    @MessageMapping("updatepatient")
    public void updatePatient() {
        eventDispatcherLogic.updatePatient();
    }

    @GetMapping("createpractitioner")
    @MessageMapping("createpractitioner")
    public void createPractitioner() {
        eventDispatcherLogic.createPractitioner();
    }

    @GetMapping("updatepractitioner")
    @MessageMapping("updatepractitioner")
    public void updatePractitioner() {
        eventDispatcherLogic.updatePractitioner();
    }
}

