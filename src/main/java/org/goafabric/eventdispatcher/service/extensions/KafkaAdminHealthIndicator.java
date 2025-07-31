package org.goafabric.eventdispatcher.service.extensions;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;


@Component
public class KafkaAdminHealthIndicator implements HealthIndicator {

    private final KafkaAdmin kafkaAdmin;

    public KafkaAdminHealthIndicator(KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
    }

    @Override
    public Health health() {
        try (AdminClient client = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            client.describeCluster().clusterId().get(); // needs proper timeouts configured in application.yaml / configmap to not hang forever
            return Health.up().withDetail("kafka", "Available").build();
        } catch (Exception e) {
            return Health.down(e).withDetail("kafka", "Unavailable").build();
        }
    }
}