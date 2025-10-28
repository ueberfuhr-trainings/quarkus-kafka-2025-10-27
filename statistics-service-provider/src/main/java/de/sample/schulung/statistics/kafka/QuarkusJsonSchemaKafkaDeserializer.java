package de.sample.schulung.statistics.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.networknt.schema.JsonSchema;
import io.apicurio.registry.resolver.SchemaResolver;
import io.apicurio.registry.rest.client.RegistryClient;
import io.apicurio.registry.serde.jsonschema.JsonSchemaKafkaDeserializer;
import io.quarkus.arc.Arc;

import java.util.Map;

public class QuarkusJsonSchemaKafkaDeserializer
  extends JsonSchemaKafkaDeserializer<CustomerEventRecord> {

  public QuarkusJsonSchemaKafkaDeserializer() {
  }

  public QuarkusJsonSchemaKafkaDeserializer(RegistryClient client, SchemaResolver<JsonSchema, CustomerEventRecord> schemaResolver) {
    super(client, schemaResolver);
  }

  public QuarkusJsonSchemaKafkaDeserializer(RegistryClient client) {
    super(client);
  }

  public QuarkusJsonSchemaKafkaDeserializer(SchemaResolver<JsonSchema, CustomerEventRecord> schemaResolver) {
    super(schemaResolver);
  }

  public QuarkusJsonSchemaKafkaDeserializer(RegistryClient client, Boolean validationEnabled) {
    super(client, validationEnabled);
  }

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    super.configure(configs, isKey);
    try (final var mapper = Arc.container().instance(ObjectMapper.class)) {
      this.setObjectMapper(
        mapper
          .orElse(
            new ObjectMapper()
              .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
              .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
              .registerModule(new JavaTimeModule())
          )
      );
    }
  }

  @Override
  public void setObjectMapper(ObjectMapper objectMapper) {
    if (null != objectMapper) {
      objectMapper.registerModule(new JavaTimeModule());
    }
    super.setObjectMapper(objectMapper);
  }

}
