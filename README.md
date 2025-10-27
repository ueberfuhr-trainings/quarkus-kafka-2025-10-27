# Quarkus - Kafka

## Contents:

In this repository, we can find the following projects:

- [Account Service Provider](account-service-provider)
- [Statistics Service Provider](statistics-service-provider)

## Build and run the project

We use Maven Wrapper to build the project.

```bash
# Build all
./mvnw clean package -Dquarkus.package.jar.type=uber-jar
# Run Account Service Provider
./mvnw -pl account-service-provider -am quarkus:dev
# Run Statistics Service Provider
./mvnw -pl statistics-service-provider -am quarkus:dev
```

# Documentation

We can find documentation in the [docs](docs) folder. Public documentation is available at

- Baeldung
  - [Kafka Basics](https://www.baeldung.com/apache-kafka)
- Quarkus (Guides)
  - [Quarkus & Kafka](https://quarkus.io/guides/kafka)
  - [Schema Registry](https://quarkus.io/guides/kafka-schema-registry-json-schema)
- MicroProfile
  - [Reactive Messaging](https://microprofile.io/specifications/reactive-messaging/)
  - [SmallRye (Reference Implementation)](https://smallrye.io/smallrye-reactive-messaging/smallrye-reactive-messaging/3.3/index.html)
