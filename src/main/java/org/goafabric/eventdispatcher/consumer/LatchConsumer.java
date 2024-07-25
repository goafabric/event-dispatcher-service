package org.goafabric.eventdispatcher.consumer;

import java.util.concurrent.CountDownLatch;

public interface LatchConsumer {
    CountDownLatch getLatch();
}
