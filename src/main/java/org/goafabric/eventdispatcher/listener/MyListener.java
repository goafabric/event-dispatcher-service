package org.goafabric.eventdispatcher.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.goafabric.eventdispatcher.listener.ListenerConstants.CREATE;
import static org.goafabric.eventdispatcher.listener.ListenerConstants.PATIENT;

@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@RabbitListener(bindings = @QueueBinding(value = @Queue(name = "MyQueue"),
        exchange = @Exchange(value = PATIENT, type = ExchangeTypes.TOPIC), key = {CREATE}))
public @interface MyListener {

}
