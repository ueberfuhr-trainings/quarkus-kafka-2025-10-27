package de.sample.schulung.accounts.persistence.inmemory;

import de.sample.schulung.accounts.domain.Customer;
import de.sample.schulung.accounts.domain.CustomersSinkPort;
import de.sample.schulung.accounts.domain.NotFoundException;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@DefaultBean
@Typed(CustomersSinkPort.class)
public class CustomersSinkInMemoryAdapter implements CustomersSinkPort {

  private final Map<UUID, Customer> customers = new HashMap<>();

  public Stream<Customer> getCustomers() {
    return customers
      .values()
      .stream();
  }

  public Stream<Customer> getCustomersByState(Customer.CustomerState state) {
    return this.getCustomers()
      .filter(customer -> state.equals(customer.getState()));
  }

  public void createCustomer(Customer customer) {
    var uuid = UUID.randomUUID();
    customer.setUuid(uuid);
    this.customers.put(customer.getUuid(), customer);
  }

  public Optional<Customer> findCustomerById(UUID uuid) {
    return Optional.ofNullable(
      this.customers.get(uuid)
    );
  }

  public void replaceCustomer(Customer customer) {
    if (this.exists(customer.getUuid())) {
      this.customers.put(customer.getUuid(), customer);
    } else {
      throw new NotFoundException();
    }
  }

  public void deleteCustomer(UUID uuid) {
    if (this.exists(uuid)) {
      this.customers.remove(uuid);
    } else {
      throw new NotFoundException();
    }
  }

  public boolean exists(UUID uuid) {
    return this.customers.containsKey(uuid);
  }

  @Override
  public long count() {
    return this.customers.size();
  }
}
