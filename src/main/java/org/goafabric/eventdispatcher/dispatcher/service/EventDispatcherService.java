package org.goafabric.eventdispatcher.dispatcher.service;

import org.goafabric.eventdispatcher.dispatcher.logic.EventDispatcherLogic;
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
}

