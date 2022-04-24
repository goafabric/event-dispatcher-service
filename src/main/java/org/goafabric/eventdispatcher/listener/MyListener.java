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

@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@RabbitListener(bindings = @QueueBinding(value = @Queue(name = "MyQueue"),
        exchange = @Exchange(value = "patient", type = ExchangeTypes.TOPIC), key = {CREATE}))
public @interface MyListener {
    /*
    @AliasFor(
            annotation = Exchange.class,
            attribute = "value"
    )
    String exchangeName() default "";

     */
}
