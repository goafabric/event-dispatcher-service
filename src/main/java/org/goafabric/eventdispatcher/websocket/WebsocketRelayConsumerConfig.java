package org.goafabric.eventdispatcher.websocket;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;
import java.util.UUID;

@Configuration
public class WebsocketRelayConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> latestKafkaListenerContainerFactory(KafkaProperties kafkaProperties) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "WebsocketRelayConsumer" + UUID.randomUUID()); //the important part here is for the groupId to be unique, because otherwise only one cusomer will get the partitions / meesssages
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));
        return factory;
    }

}
