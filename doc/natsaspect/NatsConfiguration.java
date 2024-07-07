package org.goafabric.eventdispatcher.configuration;

import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import io.nats.client.api.StorageType;
import io.nats.client.api.StreamConfiguration;
import org.goafabric.eventdispatcher.producer.EventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//https://gcore.com/learning/nats-rabbitmq-nsq-kafka-comparison/
@Profile("nats")
@Configuration
@RegisterReflectionForBinding(EventData.class)
public class NatsConfiguration {
    private static Logger log = LoggerFactory.getLogger(NatsConfiguration.class);

    @Bean
    public Connection connection(
            @Value("${nats.spring.server}") String serverUrl, @Value("${nats.spring.username:}") String userName, @Value("${nats.spring.password:}") String password) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            try {
                var con = Nats.connect(new Options.Builder()
                        .server(serverUrl)
                        .userInfo(userName, password)
                        .connectionListener((connection, event) -> log.info("Connection Event: " + event))
                        .build());
                createStreams(con);
                return con;
            } catch (Exception e) {
                log.warn("cannot connect to nats server, will retry {}", e.getMessage());
                Thread.sleep(1000);
            }
        }
        System.exit(0);
        return null;
    }

    private void createStreams(Connection connection) throws Exception {
        //only for testing one stream for all, for production it could be one stream per subject
        var streamConfig = StreamConfiguration.builder()
                .name("all")
                .subjects("*.*") //"patient.*", "practitioner.*")
                .storageType(StorageType.File)
                .build();

        connection.jetStreamManagement().addStream(streamConfig);
        connection.jetStreamManagement().getStreams().forEach(stream -> log.info("Stream found {} {}", stream.getConfiguration().getName() , stream.getConfiguration().getSubjects().getFirst()));
    }
}