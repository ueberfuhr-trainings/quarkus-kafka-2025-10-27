package de.sample.schulung.accounts.kafka;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.reactive.messaging.kafka.Record;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

@QuarkusTest
class CustomerEventsProducerTests {

  @Inject
  @Connector("smallrye-in-memory")
  InMemoryConnector connector;

  @BeforeEach
  void resetConnector() {
    connector.sink("customers").clear();
  }

  @Test
  void shouldSendMessageOnCreateCustomer() {
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body("""
          {
            "name": "Tom Mayer",
            "birthdate": "1985-07-30",
            "state": "active"
          }
        """)
      .when()
      .post("/customers")
      .then()
      .statusCode(201);

    // ✅ Check sent Kafka message
    var messages = connector.sink("customers").received();
    assertThat(messages).hasSize(1);

    var message = messages.get(0);
    var payload = message.getPayload();
    assertThat(payload).isInstanceOf(Record.class);
    var record = (Record<?, ?>) payload;

    assertThat(record.key())
      .isInstanceOf(UUID.class);

    assertThat(record.value())
      .isInstanceOf(CustomerEventRecord.class)
      .asInstanceOf(type(CustomerEventRecord.class))
      .extracting(CustomerEventRecord::customer)
      .extracting(CustomerRecord::name)
      .isEqualTo("Tom Mayer");

  }

}
