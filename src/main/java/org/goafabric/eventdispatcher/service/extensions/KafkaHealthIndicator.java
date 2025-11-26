package org.goafabric.eventdispatcher.service.extensions;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;


@Component
public class KafkaHealthIndicator implements HealthIndicator {

    private final KafkaAdmin kafkaAdmin;

    public KafkaHealthIndicator(KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
    }

    @Override
    public Health health() {
        try (AdminClient client = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            client.describeCluster().clusterId().get(); // needs proper timeouts configured in application.yaml / configmap to not hang forever
            //kafkaTemplate.send("health-check", "ping").get(); //would not do that, because it will send msgs in health check and topic also needs to exist
            return Health.up().withDetail("kafka", "Available").build();
        } catch (Exception e) {
            return Health.down(e).withDetail("kafka", "Unavailable").build();
        }
    }
}