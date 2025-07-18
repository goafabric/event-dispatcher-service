package org.goafabric.eventdispatcher.websocket;


import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebsocketRelayConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final SimpMessagingTemplate msgTemplate;

    public WebsocketRelayConsumer(SimpMessagingTemplate msgTemplate) {
        this.msgTemplate = msgTemplate;
    }

    @KafkaListener(topics = {"patient", "practitioner"}, containerFactory = "latestKafkaListenerContainerFactory")
    public void process(EventData eventData, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) { //todo use eventData.Type instead of topic
        log.info("inside relay consumer");
        msgTemplate.convertAndSend("/tenant/" + UserContext.getTenantId(), //this works as long as the TenantContext is set by TenantAspect
                new MySocketMessage(topic + " " + eventData.operation() + " for Tenant " + UserContext.getTenantId()));
                //eventData.type(
    }

}
