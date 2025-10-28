package de.sample.schulung.statistics.kafka;

import de.sample.schulung.statistics.domain.CustomerStatistics;
import de.sample.schulung.statistics.domain.CustomerStatisticsService;
import io.quarkus.arc.Arc;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@QuarkusTest
class CustomerEventsConsumerTests {

  @Inject
  CustomerStatisticsService customerStatisticsService;

  @Inject
  @Connector("smallrye-in-memory")
  InMemoryConnector connector;

  private void checkForStatisticsAsync(Consumer<CustomerStatistics> statistics) {
    await()
      .atMost(5, TimeUnit.SECONDS)
      .untilAsserted(
        () -> {
          final var requestContext = Arc.container().requestContext();
          requestContext.activate();
          try {
            statistics.accept(customerStatisticsService.getStatistics());
          } finally {
            requestContext.deactivate();
          }
        }
      );
  }

  @Test
  void shouldIncreaseStatisticsOnCustomerCreatedEventAndDecreaseOnCustomerDeletedEvent() {
    var uuid = UUID.randomUUID();

    var initialStatistics = customerStatisticsService.getStatistics();

    connector
      .source("customers")
      .send(
        new CustomerEventRecord(
          "created",
          uuid,
          new CustomerRecord(
            "Tom Mayer",
            LocalDate.of(2000, Month.DECEMBER, 1),
            "active"
          )
        )
      );

    checkForStatisticsAsync(
      customerStatistics ->
        assertThat(customerStatistics)
          .extracting(CustomerStatistics::getCount)
          .isEqualTo(initialStatistics.getCount() + 1)
    );

    await()
      .atMost(5, TimeUnit.SECONDS)
      .untilAsserted(
        () -> {
          Arc.container().requestContext().activate();
          assertThat(customerStatisticsService.getStatistics())
            .extracting(CustomerStatistics::getCount)
            .isEqualTo(initialStatistics.getCount() + 1);
        }
      );

    connector
      .source("customers")
      .send(
        new CustomerEventRecord(
          "deleted",
          uuid,
          null)
      );

    checkForStatisticsAsync(
      customerStatistics ->
        assertThat(customerStatistics)
          .extracting(CustomerStatistics::getCount)
          .isEqualTo(initialStatistics.getCount())
    );

  }


}
