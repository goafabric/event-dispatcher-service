//package org.goafabric.eventdispatcher.service.producer;
//
//import org.goafabric.event.EventData;
//import org.goafabric.eventdispatcher.consumer.LatchConsumer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.CountDownLatch;
//
//@Component
//public class TestConsumer implements LatchConsumer {
//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//
//    static final String CONSUMER_NAME = "Calendar";
//    private static Long consumerCount = 0L;
//
//    private final CountDownLatch latch = new CountDownLatch(1);
//
//
//    @KafkaListener(groupId = CONSUMER_NAME, topics = {"test-topic"}) //only topics listed here will be autocreated
//    public void processKafka(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, EventData eventData) {
//        log.info("processing test event {} {}", topic, eventData);
//        consumerCount++;
//        latch.countDown();
//    }
//
//    @Override
//    public CountDownLatch getLatch() { return latch; }
//
//    public static Long getConsumerCount() {
//        return consumerCount;
//    }
//}
