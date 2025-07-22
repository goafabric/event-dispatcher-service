package org.goafabric.eventdispatcher.service.consumer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {
    @Bean
    public NewTopic patient() {
        return TopicBuilder.name("patient.root").build();
    }

    @Bean
    public NewTopic organization() {
        return TopicBuilder.name("organization").build();
    }

}
