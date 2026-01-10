package org.goafabric.eventdispatcher.websocket;


import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.service.controller.dto.SocketMessage;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

//Relay consumer listen to all kafka messages and sends them via the internal broker to the correct websocket tenant channel
//So the trigger is always a kafka message for websockets to receive
@Component
public class WebsocketRelayConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final SimpMessagingTemplate msgTemplate;

    public WebsocketRelayConsumer(SimpMessagingTemplate msgTemplate) {
        this.msgTemplate = msgTemplate;
    }

    @KafkaListener(groupId = "WebsocketRelayConsumer-#{T(java.util.UUID).randomUUID().toString()}", topicPattern = ".*") //, containerFactory = "latestKafkaListenerContainerFactory")
    public void process(EventData eventData) {
        log.info("inside relay consumer");
        msgTemplate.convertAndSend("/patient/tenant/" + UserContext.getTenantId(), //this works as long as the TenantContext is set by TenantAspect
                new SocketMessage(eventData.type() + " " + eventData.operation() + " for Tenant " + UserContext.getTenantId()));
    }

}
