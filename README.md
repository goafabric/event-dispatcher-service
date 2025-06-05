# sonarqube
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=org.goafabric%3Aevent-dispatcher-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=org.goafabric%3Aevent-dispatcher-service)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.goafabric%3Aevent-dispatcher-service&metric=coverage)](https://sonarcloud.io/summary/new_code?id=org.goafabric%3Aevent-dispatcher-service)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=org.goafabric%3Aevent-dispatcher-service&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=org.goafabric%3Aevent-dispatcher-service)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=org.goafabric%3Aevent-dispatcher-service&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=org.goafabric%3Aevent-dispatcher-service)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=org.goafabric%3Aevent-dispatcher-service&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=org.goafabric%3Aevent-dispatcher-service)

# docker compose
go to /src/deploy/docker and do "./stack up"

# run jvm multi image
docker run --pull always --name event-dispatcher-service --rm -p50500:50500 goafabric/event-dispatcher-service:$(grep '^version=' gradle.properties | cut -d'=' -f2)

# run native image
docker run --pull always --name event-dispatcher-service-native --rm -p50500:50500 goafabric/event-dispatcher-service-native:$(grep '^version=' gradle.properties | cut -d'=' -f2) -Xmx32m

