# docker compose
go to /src/deploy/docker and do "./stack up"

# run jvm multi image
docker run --pull always --name event-dispatcher-service --rm -p50500:50500 -e spring.rabbitmq.host=host.docker.internal goafabric/event-dispatcher-service:3.0.3

# run native image
docker run --pull always --name event-dispatcher-service-native --rm -p50500:50500 -e spring.rabbitmq.host=host.docker.internal goafabric/event-dispatcher-service-native:3.0.3 -Xmx32m

# run native image arm
docker run --pull always --name event-dispatcher-service-native --rm -p50500:50500 -e spring.rabbitmq.host=host.docker.internal goafabric/event-dispatcher-service-native-arm64v8:3.0.3 -Xmx32m
                                              
# rabbitmq
docker run --rm -p15672:15672 -p5672:5672 --name rabbitmq -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin rabbitmq:3.11.5-management

# kafka
docker run --rm --name zookeeper -p 2181:2181 -e 'ZOOKEEPER_CLIENT_PORT=2181' -e'ZOOKEEPER_TICK_TIME=2000' confluentinc/cp-zookeeper:7.3.1 &;\
docker run --rm --name kafka -p 9092:9092 \
-e 'KAFKA_BROKER_ID=1'  -e 'KAFKA_ZOOKEEPER_CONNECT=host.docker.internal:2181' -e 'KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,PLAINTEXT_INTERNAL:PLAINTEXT' \
-e 'KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092,PLAINTEXT_INTERNAL://broker:29092' \
-e 'KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1' -e 'KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1' -e 'KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1' \
confluentinc/cp-kafka:7.3.1; docker stop zookeeper
