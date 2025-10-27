package de.sample.schulung.statistics.domain;

import java.util.UUID;

public interface CustomersSinkPort {

  void saveCustomer(Customer customer);

  void deleteCustomer(UUID uuid);

  long count();

}
