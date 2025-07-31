package org.goafabric.eventdispatcher.service.extensions;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;


@Component
public class KafkaAdminHealthIndicator implements HealthIndicator {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    //@Autowired
    //private KafkaAdminClient kafkaAdminClient;

    @Override
    public Health health() {
        try (AdminClient client = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            DescribeClusterResult result = client.describeCluster();
            result.clusterId().get(); // wait for result
            return Health.up().withDetail("kafka", "Available").build();
        } catch (Exception e) {
            return Health.down(e).withDetail("kafka", "Unavailable").build();
        }
    }
}