#docker compose
go to /src/deploy/docker and do "./stack up"

#run jvm multi image
docker pull goafabric/event-dispatcher-service:3.0.0-RC2 && docker run --name event-dispatcher-service --rm -p50900:50900 goafabric/event-dispatcher-service:3.0.0-RC2

#run native image
docker pull goafabric/event-dispatcher-service-native:3.0.0-RC2 && docker run --name event-dispatcher-service-native --rm -p50900:50900 goafabric/event-dispatcher-service-native:3.0.0-RC2 -Xmx32m

#run native image arm
docker pull goafabric/event-dispatcher-service-native-arm64v8:3.0.0-RC2 && docker run --name event-dispatcher-service-native-arm64v8 --rm -p50900:50900 goafabric/event-dispatcher-service-native-arm64v8:3.0.0-RC2 -Xmx32m
