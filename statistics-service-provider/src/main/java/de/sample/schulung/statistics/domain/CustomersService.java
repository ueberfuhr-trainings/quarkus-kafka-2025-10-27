package de.sample.schulung.statistics.domain;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class CustomersService {

  private final CustomersSinkPort customersSink;

  public void saveCustomer(Customer customer) {
    customersSink.saveCustomer(customer);
  }

  public void deleteCustomer(UUID uuid) {
    customersSink.deleteCustomer(uuid);
  }

  public long count() {
    return customersSink.count();
  }

}
