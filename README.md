# docker compose
go to /src/deploy/docker and do "./stack up"

# run jvm multi image
docker run --pull always --name event-dispatcher-service --rm -p50500:50500 -e spring.rabbitmq.host=host.docker.internal goafabric/event-dispatcher-service:3.2.3-SNAPSHOT

# run native image
docker run --pull always --name event-dispatcher-service-native --rm -p50500:50500 -e spring.rabbitmq.host=host.docker.internal goafabric/event-dispatcher-service-native:3.2.3-SNAPSHOT -Xmx32m

# run native image arm
docker run --pull always --name event-dispatcher-service-native --rm -p50500:50500 -e spring.rabbitmq.host=host.docker.internal goafabric/event-dispatcher-service-native-arm64v8:3.2.3-SNAPSHOT -Xmx32m
                                              
# rabbitmq
docker run --rm -p15672:15672 -p5672:5672 --name rabbitmq -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin rabbitmq:3.11.5-management

# kafka
docker run --rm --name kafka -p 9092:9092 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:9092 quay.io/ogunalp/kafka-native:0.4.0-kafka-3.5.1

