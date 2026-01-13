package org.goafabric.eventdispatcher.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.goafabric.eventdispatcher.service.controller.dto.DbOperation;
import org.goafabric.eventdispatcher.service.controller.dto.Patient;
import org.goafabric.eventdispatcher.service.controller.dto.Practitioner;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.annotation.RegisterReflection;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RegisterReflection(classes = {Patient.class, Practitioner.class} //every type we publish needs to be registered
        , memberCategories = { MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS})
public class EventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce(String topic, String key, DbOperation operation, Object payload) {
        publish(topic, key, operation, payload);
    }

    private void publish(String topic, String key, DbOperation operation, Object payload) {
        var producerRecord = new ProducerRecord<>(topic, key, payload);
        producerRecord.headers().add("operation", operation.toString().getBytes(StandardCharsets.UTF_8));
        UserContext.getAdapterHeaderMap().forEach((key1, value) -> producerRecord.headers().add(key1, value.getBytes(StandardCharsets.UTF_8)));
        kafkaTemplate.send(producerRecord);
    }
}
