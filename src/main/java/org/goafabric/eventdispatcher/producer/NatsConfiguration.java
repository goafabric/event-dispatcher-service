package org.goafabric.eventdispatcher.producer;

import io.nats.client.Connection;
import io.nats.client.JetStreamManagement;
import io.nats.client.Nats;
import io.nats.client.Options;
import io.nats.client.api.StorageType;
import io.nats.client.api.StreamConfiguration;
import io.nats.client.api.StreamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//https://gcore.com/learning/nats-rabbitmq-nsq-kafka-comparison/
@Configuration
@Profile("nats")
@RegisterReflectionForBinding(EventData.class)
public class NatsConfiguration {
    private static Logger log = LoggerFactory.getLogger(NatsConfiguration.class);

    @Bean
    public Connection connection(@Value("${nats.spring.server}") String serverUrl) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            try {
                return Nats.connect(new Options.Builder()
                        .server(serverUrl)
                        .connectionListener((connection, event) -> log.info("Connection Event: " + event))
                        .build());
            } catch (Exception e) {
                log.warn("cannot connect to nats server, will retry {}", e.getMessage());
                Thread.sleep(1000);
            }
        }
        System.exit(0);
        return null;
    }

    @Bean
    public JetStreamManagement jetStreamManagement(Connection connection) throws Exception {
        if (true) {
            return null;
        }
        JetStreamManagement jsm = connection.jetStreamManagement();

        // Define the stream configuration
        StreamConfiguration streamConfig = StreamConfiguration.builder()
                .name("all")
                .subjects("patient.*", "practitioner.*")
                .storageType(StorageType.File)
                .build();

        // Add or get the stream
        try {
            jsm.addStream(streamConfig);
        } catch (Exception e) {
            // Stream might already exist, handle exception
            StreamInfo info = jsm.getStreamInfo("all");
            System.out.println("Stream already exists: " + info);
        }
        return jsm;
    }
}
