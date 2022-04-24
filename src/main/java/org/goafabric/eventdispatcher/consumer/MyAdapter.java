package org.goafabric.eventdispatcher.consumer;

import lombok.extern.slf4j.Slf4j;
import org.goafabric.eventdispatcher.listener.MyListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyAdapter {
    @MyListener
    public void createPatient() {
        log.info("inside my listener") ;
    }
}
