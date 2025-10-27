# Testing

To test a Quarkus application that is a Kafka client, we need to be aware of different concepts.

## Black Box Testing

This will test the application with its external, observable behavior. It will connect to a running Kafka broker
(single or cluster) and send and receive messages.

### Requirements

In this case, we are dependent of a running Kafka broker, e.g.
 - as an external resource
 - as a locally running container, e.g. by using
   [Quarkus Dev Services for Kafka](https://quarkus.io/guides/kafka-dev-services) or
   [Test Containers (Kafka Module)](https://java.testcontainers.org/modules/kafka/)

### Test Strategy

For a consumer, the test will send messages to a topic and verify that the messages are received and processed correctly.
For a producer, the test will trigger the application to send messages to a topic and verify that the messages can be
consumed from the topic with the correct content.

We could use the [Kafka Companion Library]https://quarkus.io/guides/kafka#testing-using-a-kafka-broker) or [Citrus](https://citrusframework.org/) to easily interact with the Kafka topic.

### Possibilities and Limitations

This will test...
- ✅ ... connecting to the Kafka broker using the configuration properties, incl. acknowledgment and exception handling.
- ✅ ... (de)serializing keys and payloads.
- ✅ ... performing schema validation.

This will NOT test...
- ❌ ... internal code, e.g. transaction management
- ❌ ... some exception scenarios


## Using the SmallRye In-Memory Connector
 
To avoid a local running Kafka broker, we could use the 
[SmallRye In-Memory Connector](https://smallrye.io/smallrye-reactive-messaging/4.21.0/concepts/testing/). 
We can find details about using it in a Quarkus test in the 
[Quarkus Guide](https://quarkus.io/guides/kafka#testing-without-a-broker). 

### Possibilities and Limitations

This will test...
- ✅ ... correct message emission.
- ✅ ... correct channel selection.
- ✅ ... correct key/value/structure.
- ✅ ... correct message metadata.

This will NOT test...
- ❌ ... whether Jackson (or Avro, Protobuf, etc.) correctly (de)serializes the keys and payloads.
- ❌ ... whether the Kafka connector and channel configuration is working.
- ❌ ... whether Kafka actually accepts the message.

## Mocking

We could also mock the CDI beans that send and receive Kafka events. With this, we could completely disable the Kafka
connector, but this will have the most limited testing capabilities and is prone to maintainability issues.
