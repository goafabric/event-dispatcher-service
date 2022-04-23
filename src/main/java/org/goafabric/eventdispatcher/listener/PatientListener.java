package org.goafabric.eventdispatcher.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;

public interface PatientListener {
    String QUEUE_NAME = "CalendarQueue";

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = QUEUE_NAME),
            exchange = @Exchange(value = "patient", type = ExchangeTypes.TOPIC), key = {"CREATE"}
    ))
    void createPatient(String id);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "yo"),
            exchange = @Exchange(value = "patient", type = ExchangeTypes.TOPIC), key = {"UPDATE"}
    ))
    void updatePatient(String id);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "yo"),
            exchange = @Exchange(value = "patient", type = ExchangeTypes.TOPIC), key = {"DELETE"}
    ))
    void deletePatient(String id);

}
