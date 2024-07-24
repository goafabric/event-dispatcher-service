package org.goafabric.eventdispatcher.service.controller;

import org.goafabric.eventdispatcher.service.controller.dto.ChangeEvent;
import org.goafabric.eventdispatcher.service.controller.dto.SocketMessage;
import org.goafabric.eventdispatcher.service.logic.EventDispatcherLogic;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "events", produces = MediaType.APPLICATION_JSON_VALUE)
@MessageMapping(value = "events")
public class EventDispatcherController {
    private final EventDispatcherLogic eventDispatcherLogic;
    private final SimpMessagingTemplate messagingTemplate;

    public EventDispatcherController(EventDispatcherLogic eventDispatcherLogic, SimpMessagingTemplate messagingTemplate) {
        this.eventDispatcherLogic = eventDispatcherLogic;
        this.messagingTemplate = messagingTemplate;
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
        final String message = eventDispatcherLogic.createPatient();
        broadcastToClients(message, "/patient");
    }

    private void broadcastToClients(String message, String topic) {
        messagingTemplate.convertAndSend(topic, new SocketMessage(message));
    }

    @GetMapping("updatepatient")
    @MessageMapping("updatepatient")
    public void updatePatient() {
        final String message = eventDispatcherLogic.updatePatient();
        broadcastToClients(message, "/patient");
    }

    @GetMapping("createpractitioner")
    @MessageMapping("createpractitioner")
    public void createPractitioner() {
        final String message = eventDispatcherLogic.createPractitioner();
        broadcastToClients(message, "/practitioner");
    }

    @GetMapping("updatepractitioner")
    @MessageMapping("updatepractitioner")
    public void updatePractitioner() {
        final String message = eventDispatcherLogic.updatePractitioner();
        broadcastToClients(message, "/practitioner");
    }
}

