https://developer.confluent.io/quickstart/kafka-docker/
https://www.baeldung.com/ops/kafka-docker-setup
https://docs.spring.io/spring-kafka/docs/current/reference/html/#quick-tour

# create topic
docker exec broker \
kafka-topics --bootstrap-server broker:9092 \
--create \
--topic quickstart
              
# listen to topics
docker exec --interactive --tty broker \
kafka-console-consumer --bootstrap-server broker:9092 \
--topic quickstart \
--from-beginning

# write to topics
docker exec --interactive --tty broker \
kafka-console-producer --bootstrap-server broker:9092 \
--topic quickstart

# kafkaui 
docker run --rm --name kafkaui -e 'KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=host.docker.internal:29092' -p8080:8080 provectuslabs/kafka-ui:latest
