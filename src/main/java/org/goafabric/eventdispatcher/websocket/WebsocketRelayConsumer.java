package org.goafabric.eventdispatcher.websocket;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

//Relay consumer listen to all ".notification" kafka messages and sends them via the internal broker to the correct websocket tenant channel, which will match the rewritten one inside WebSocketConfig.TenantDestinationInterceptor
//So the trigger is always a kafka message for websockets to receive
@Component
@RegisterReflectionForBinding(WebsocketRelayConsumer.SocketMessage.class)
public class WebsocketRelayConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SimpMessagingTemplate msgTemplate;
    record SocketMessage (String message) {}

    public WebsocketRelayConsumer(SimpMessagingTemplate msgTemplate) {
        this.msgTemplate = msgTemplate;
    }


    @KafkaListener(topicPattern = ".*.notification", containerFactory = "relayKafkaListenerContainerFactory")
    public void processPatient(ConsumerRecord consumerRecord, @Header("operation") String operation) {
        log.info("inside relay consumer");
        String type = consumerRecord.value().getClass().getSimpleName().toLowerCase();
        send(type, operation);
    }

    private void send(String type, String operation) {
        msgTemplate.convertAndSend("/" + type + "/tenant/" + UserContext.getTenantId(), //this works as long as the TenantContext is set by TenantAspect
                new SocketMessage(type + " " + operation + " for Tenant " + UserContext.getTenantId()));
    }

    @Configuration
    static class WebsocketRelayConsumerConfig {
        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, Object> relayKafkaListenerContainerFactory(KafkaProperties kafkaProperties, RecordInterceptor<String, Object> recordInterceptor) {
            var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
            var props = kafkaProperties.buildConsumerProperties();
            factory.setRecordInterceptor(recordInterceptor);
            props.putAll(Map.of(ConsumerConfig.GROUP_ID_CONFIG, "WebsocketRelayConsumer" + UUID.randomUUID(),
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest",
                    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false));
            factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));
            return factory;
        }
    }

}
