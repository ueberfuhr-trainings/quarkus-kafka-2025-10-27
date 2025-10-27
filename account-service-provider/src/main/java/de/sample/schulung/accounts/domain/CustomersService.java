package de.sample.schulung.accounts.domain;

import de.sample.schulung.accounts.domain.Customer.CustomerState;
import de.sample.schulung.accounts.domain.events.CustomerCreatedEvent;
import de.sample.schulung.accounts.domain.events.CustomerDeletedEvent;
import de.sample.schulung.accounts.domain.events.CustomerReplacedEvent;
import de.sample.schulung.accounts.shared.interceptors.FireEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@RequiredArgsConstructor
public class CustomersService {

  private final CustomersSinkPort sink;

  public Stream<Customer> getCustomers() {
    return sink.getCustomers();
  }

  public Stream<Customer> getCustomersByState(@NotNull CustomerState state) {
    return sink.getCustomersByState(state);
  }

  @FireEvent(CustomerCreatedEvent.class)
  public void createCustomer(@Valid Customer customer) {
    sink.createCustomer(customer);
  }

  public Optional<Customer> findCustomerById(@NotNull UUID uuid) {
    return sink.findCustomerById(uuid);
  }

  @FireEvent(CustomerReplacedEvent.class)
  public void replaceCustomer(@Valid Customer customer) throws NotFoundException {
    sink.replaceCustomer(customer);
  }

  @FireEvent(CustomerDeletedEvent.class)
  public void deleteCustomer(@NotNull UUID uuid) throws NotFoundException {
    sink.deleteCustomer(uuid);
  }

  public boolean exists(UUID uuid) {
    return sink.exists(uuid);
  }

  public long count() {
    return sink.count();
  }

}
