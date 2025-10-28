package de.sample.schulung.accounts.kafka;

import de.sample.schulung.accounts.domain.events.CustomerCreatedEvent;
import de.sample.schulung.accounts.domain.events.CustomerDeletedEvent;
import de.sample.schulung.accounts.domain.events.CustomerReplacedEvent;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class CustomerEventsProducer {

  @Inject
  @Channel("customers")
  Emitter<Record<UUID, CustomerEventRecord>> emitter;

  @Inject
  CustomerEventRecordMapper mapper;

  // Lombok:
  // @SneakyThrows
  private void send(CustomerEventRecord eventRecord) throws Exception {
    emitter
      .send(
        Record.of(
          eventRecord.customerUuid(), // partition = message order
          eventRecord
        )
      )
      // synchronous - wait for ack/failure
      .toCompletableFuture()
      .get(2, TimeUnit.SECONDS);
  }

  public void onCustomerCreated(@Observes CustomerCreatedEvent event) throws Exception {
    final var eventRecord = mapper.map(event);
    send(eventRecord);
  }

  public void onCustomerReplaced(@Observes CustomerReplacedEvent event) throws Exception {
    final var eventRecord = mapper.map(event);
    send(eventRecord);
  }

  public void onCustomerDeleted(@Observes CustomerDeletedEvent event) throws Exception {
    final var eventRecord = mapper.map(event);
    send(eventRecord);
  }

}
