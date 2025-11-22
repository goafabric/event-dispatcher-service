package org.goafabric.eventdispatcher.producer;

import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.service.controller.dto.Patient;
import org.goafabric.eventdispatcher.service.controller.dto.Practitioner;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.annotation.RegisterReflection;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RegisterReflection(classes = {Patient.class, Practitioner.class}
        , memberCategories = { MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS})
public class EventProducer {
    private final KafkaTemplate<String, EventData> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, EventData> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce(String topic, String key, EventData eventData) {
        kafkaTemplate.send(topic, key, eventData);
    }
}
