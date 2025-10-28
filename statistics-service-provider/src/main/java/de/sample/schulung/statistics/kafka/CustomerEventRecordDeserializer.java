package de.sample.schulung.statistics.kafka;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class CustomerEventRecordDeserializer
  extends ObjectMapperDeserializer<CustomerEventRecord> {

  public CustomerEventRecordDeserializer() {
    super(CustomerEventRecord.class);
  }

}
