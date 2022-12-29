package org.goafabric.eventdispatcher.producer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

//mapping external event types to internal ones
@Configuration
public class EventTypeMapping {
    @Bean
    public HashMap<String, String> exchanges() {
        HashMap<String, String> eventTypes = new HashMap<>();

        eventTypes.put("Patient", "patient");
        eventTypes.put("Employee", "practitioner");
        eventTypes.put("Organization", "organization");

        return eventTypes;
    }
}
