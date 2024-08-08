package org.goafabric.eventdispatcher.consumer;

import org.goafabric.eventdispatcher.producer.EventData;
import org.goafabric.eventdispatcher.service.controller.dto.SocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebsocketBridgeConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SimpMessagingTemplate websocketMessagingTemplate;

    public WebsocketBridgeConsumer(SimpMessagingTemplate websocketMessagingTemplate) {
        this.websocketMessagingTemplate = websocketMessagingTemplate;
    }

    @KafkaListener(
        topics = {"patient", "practitioner"},
        topicPartitions = {
            @TopicPartition(topic = "patient", partitions = "0"),
            @TopicPartition(topic = "practitioner", partitions = "0")
        },
        containerFactory = "latestKafkaListenerContainerFactory"
    )
    public void listen(@Header(KafkaHeaders.RECEIVED_TOPIC) String kafkaTopic, EventData eventData) {

        String message = eventData.operation() + " on topic " + kafkaTopic;
        String topic = "/tenant/0/" + kafkaTopic;
        log.info("Received Kafka message - publishing to websocket topic {}: {}", topic, message );
        websocketMessagingTemplate.convertAndSend( topic, new SocketMessage(message));
    }

}
