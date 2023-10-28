package org.goafabric.eventdispatcher.service.controller;

import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.controller.dto.SocketMessage;
import org.goafabric.eventdispatcher.service.logic.EventDispatcherLogic;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

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
    @SendTo("/public")
    public SocketMessage createPatient() {
        final String message = eventDispatcherLogic.createPatient();
        return new SocketMessage(message);
    }

    @GetMapping("updatepatient")
    @MessageMapping("updatepatient")
    @SendTo("/public")
    public SocketMessage updatePatient() {
        final String message = eventDispatcherLogic.updatePatient();
        return new SocketMessage(message);
    }

    @GetMapping("createpractitioner")
    @MessageMapping("createpractitioner")
    @SendTo("/public")
    public SocketMessage createPractitioner() {
        final String message = eventDispatcherLogic.createPractitioner();
        return new SocketMessage(message);
    }

    @GetMapping("updatepractitioner")
    @MessageMapping("updatepractitioner")
    @SendTo("/public")
    public SocketMessage updatePractitioner() {
        final String message = eventDispatcherLogic.updatePractitioner();
        return new SocketMessage(message);
    }
}

