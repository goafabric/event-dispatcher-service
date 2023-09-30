# rabbit
https://www.rabbitmq.com/tutorials/amqp-concepts.html

# kafka
https://developer.confluent.io/quickstart/kafka-docker/
https://www.baeldung.com/ops/kafka-docker-setup
https://docs.spring.io/spring-kafka/docs/current/reference/html/#quick-tour

# create topic
docker exec kafka \
kafka-topics --bootstrap-server host.docker.internal:9092 \
--create \
--topic quickstart
              
# listen to topics
docker exec --interactive --tty kafka \
kafka-console-consumer --bootstrap-server host.docker.internal:9092 \
--topic quickstart \
--from-beginning

# write to topics
docker exec --interactive --tty kafka \
kafka-console-producer --bootstrap-server host.docker.internal:9092 \
--topic quickstart

# kafkaui 
docker run --rm --name kafkaui -e 'KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=host.docker.internal:29092' -p8080:8080 provectuslabs/kafka-ui:latest
                  
# native
docker run --rm quay.io/ogunalp/kafka-native:0.4.0-kafka-3.5.1
