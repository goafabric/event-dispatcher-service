package org.goafabric.eventdispatcher.service.controller;

import org.goafabric.eventdispatcher.service.controller.dto.SocketMessage;
import org.goafabric.eventdispatcher.service.logic.EventDispatcherLogic;
import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "events", produces = MediaType.APPLICATION_JSON_VALUE)
@MessageMapping(value = "events")
public class EventDispatcherController {
    @Autowired
    EventDispatcherLogic eventDispatcherLogic;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    //dispatch event to be called from external rest clients
    @PostMapping(value = "dispatch", consumes = MediaType.APPLICATION_JSON_VALUE)
    @MessageMapping("dispatch")
    public void dispatch(@RequestBody ChangeEvent changeEvent) {
        eventDispatcherLogic.dispatch(changeEvent);
    }

    //events to be called from html page
    @GetMapping("createpatient")
    @MessageMapping("createpatient")
    public String createPatient() {
        final String message = eventDispatcherLogic.createPatient();
        messagingTemplate.convertAndSend("/public", SocketMessage.builder().message(message).build());
        return message;
    }

    @GetMapping("updatepatient")
    @MessageMapping("updatepatient")
    public String updatePatient() {
        final String message = eventDispatcherLogic.updatePatient();
        messagingTemplate.convertAndSend("/public", SocketMessage.builder().message(message).build());
        return message;
    }

    @GetMapping("createpractitioner")
    @MessageMapping("createpractitioner")
    public String createPractitioner() {
        final String message = eventDispatcherLogic.createPractitioner();
        messagingTemplate.convertAndSend("/public", SocketMessage.builder().message(message).build());
        return message;
    }

    @GetMapping("updatepractitioner")
    @MessageMapping("updatepractitioner")
    public String updatePractitioner() {
        final String message = eventDispatcherLogic.updatePractitioner();
        messagingTemplate.convertAndSend("/public", SocketMessage.builder().message(message).build());
        return message;
    }
}

