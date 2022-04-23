package org.goafabric.eventdispatcher.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

import static org.goafabric.eventdispatcher.listener.ListenerConstants.*;

@Configuration
public class ListenerConfiguration {
    @Bean
    public HashMap<String, String> exchanges() {
        HashMap<String, String> eventTypes = new HashMap<>();

        eventTypes.put("Patient", PATIENT);
        eventTypes.put("Employee", PRACTITIONER);
        eventTypes.put("Organization", ORGANIZATION);

        return eventTypes;
    }
}
