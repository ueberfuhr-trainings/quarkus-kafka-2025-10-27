# Quarkus Properties for the SmallRye Kafka Connector

We can find some properties in the examples of the [Quarkus Kafka Guide](https://quarkus.io/guides/kafka).
In this guide, we also can find a list of properties for the SmallRye Kafka Connector.

- [Incoming Channel Configuration](https://quarkus.io/guides/kafka#incoming-channel-configuration-polling-from-kafka)
- [Outgoing Channel Configuration](https://quarkus.io/guides/kafka#outgoing-channel-configuration-writing-to-kafka)

When we want to know the details, we need to take a look into the source code of the following classes:

- `io.smallrye.reactive.messaging.kafka.KafkaConnectorIncomingConfiguration`
- `io.smallrye.reactive.messaging.kafka.KafkaConnectorOutgingConfiguration`


For example, we can find the `mp.messaging.incoming.<channel>.group.id` property in the `KafkaConnectorIncomingConfiguration` class:

```java
/**
 * Extract the incoming configuration for the {@code smallrye-kafka} connector.
*/
public class KafkaConnectorIncomingConfiguration extends KafkaConnectorCommonConfiguration {
 
  // ...
  
  /**
  * Gets the group.id value from the configuration.
  * Attribute Name: group.id
  * Description: A unique string that identifies the consumer group the application belongs to.
  * If not set, a unique, generated id is used
  * @return the group.id
  */
  public Optional<String> getGroupId() {
    return config.getOptionalValue("group.id", String.class);
  }

    // ...

}
```

