package de.sample.schulung.accounts.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.networknt.schema.JsonSchema;
import io.apicurio.registry.resolver.SchemaResolver;
import io.apicurio.registry.resolver.strategy.ArtifactReferenceResolverStrategy;
import io.apicurio.registry.rest.client.RegistryClient;
import io.apicurio.registry.serde.jsonschema.JsonSchemaKafkaSerializer;
import io.quarkus.arc.Arc;

import java.util.Map;

public class QuarkusJsonSchemaKafkaSerializer<T>
  extends JsonSchemaKafkaSerializer<T> {

  public QuarkusJsonSchemaKafkaSerializer() {
  }

  public QuarkusJsonSchemaKafkaSerializer(RegistryClient client, ArtifactReferenceResolverStrategy<JsonSchema, T> artifactResolverStrategy, SchemaResolver<JsonSchema, T> schemaResolver) {
    super(client, artifactResolverStrategy, schemaResolver);
  }

  public QuarkusJsonSchemaKafkaSerializer(RegistryClient client) {
    super(client);
  }

  public QuarkusJsonSchemaKafkaSerializer(SchemaResolver<JsonSchema, T> schemaResolver) {
    super(schemaResolver);
  }

  public QuarkusJsonSchemaKafkaSerializer(RegistryClient client, Boolean validationEnabled) {
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
