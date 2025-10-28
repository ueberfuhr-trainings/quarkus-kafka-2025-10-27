package de.sample.schulung.statistics.kafka;

import de.sample.schulung.statistics.domain.Customer;
import de.sample.schulung.statistics.domain.CustomersService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class CustomerEventsConsumer {

  private final CustomersService customersService;

  /*
   * We only consume the payload here.
   * The key is only needed for partitioning.
   */
  @Incoming("customers")
  public void consume(CustomerEventRecord eventRecord) {
    log.info(
      "Received customer event: {} (id: {})",
      eventRecord.eventType(),
      eventRecord.customerUuid()
    );

    switch (eventRecord.eventType()) {
      case "created":
      case "replaced":
        if ("active".equals(eventRecord.customer().state())) {
          var customer = Customer
            .builder()
            .uuid(eventRecord.customerUuid())
            .dateOfBirth(eventRecord.customer().birthdate())
            .build();
          customersService.saveCustomer(customer);
        } else {
          customersService.deleteCustomer(eventRecord.customerUuid());
        }
        break;
      case "deleted":
        customersService.deleteCustomer(eventRecord.customerUuid());
        break;
      default:
        throw new ValidationException();
    }

  }

}
