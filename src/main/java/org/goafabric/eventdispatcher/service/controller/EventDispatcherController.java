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
public class EventDispatcherController {
    @Autowired
    EventDispatcherLogic eventDispatcherLogic;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @PostMapping(value = "dispatch", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void dispatch(@RequestBody ChangeEvent changeEvent) {
        eventDispatcherLogic.dispatch(changeEvent);
    }

    @GetMapping("createpatient")
    @MessageMapping("createpatient")
    public String createPatient() {
        eventDispatcherLogic.createPatient();
        messagingTemplate.convertAndSend("/topic/chat/public",
                SocketMessage.builder().message("patient.created").build());
        return "patient created";
    }

    @GetMapping("updatepatient")
    @MessageMapping("updatepatient")
    public String updatePatient() {
        eventDispatcherLogic.updatePatient();
        messagingTemplate.convertAndSend("/topic/chat/public", SocketMessage.builder().message("patient.created").build());
        return "patient updated";
    }

    @GetMapping("createpractitioner")
    @MessageMapping("createpractitioner")
    public String createPractitioner() {
        eventDispatcherLogic.createPractitioner();
        messagingTemplate.convertAndSend("/topic/chat/public", SocketMessage.builder().message("patient.created").build());
        return "practitioner created";
    }

    @GetMapping("updatepractitioner")
    @MessageMapping("updatepractitioner")
    public String updatePractitioner() {
        eventDispatcherLogic.updatePractitioner();
        messagingTemplate.convertAndSend("/topic/chat/public", SocketMessage.builder().message("patient.created").build());
        return "practitioner updated";
    }
}

