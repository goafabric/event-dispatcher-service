# docker compose
go to /src/deploy/docker and do "./stack up"

# run jvm multi image
docker run --pull always --name event-dispatcher-service --rm -p50500:50500 goafabric/event-dispatcher-service:$(grep '^version=' gradle.properties | cut -d'=' -f2)

# run native image
docker run --pull always --name event-dispatcher-service-native --rm -p50500:50500 goafabric/event-dispatcher-service-native:$(grep '^version=' gradle.properties | cut -d'=' -f2) -Xmx32m

# run native image arm
docker run --pull always --name event-dispatcher-service-native --rm -p50500:50500 goafabric/event-dispatcher-service-native-arm64v8:$(grep '^version=' gradle.properties | cut -d'=' -f2) -Xmx32m
                                              
# kafka
docker run --rm --name kafka -p 9092:9092 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:9092 quay.io/ogunalp/kafka-native:0.10.0-kafka-3.7.1-arm64

