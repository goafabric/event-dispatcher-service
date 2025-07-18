package org.goafabric.eventdispatcher.websocket;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.annotation.RegisterReflection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@RegisterReflection(classes = java.util.Map.class, memberCategories = {MemberCategory.DECLARED_CLASSES, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS})
public class WebsocketRelayConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> latestKafkaListenerContainerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(latestConsumerConfigs(bootstrapServers)));
        return factory;
    }

    @Bean
    public Map<String, Object> latestConsumerConfigs(String bootstrapServers) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "WebsocketRelayConsumer" + UUID.randomUUID()); //the important part here is for the groupId to be unique, because otherwise only one cusomer will get the partitions / meesssages
        return props;
    }

}
