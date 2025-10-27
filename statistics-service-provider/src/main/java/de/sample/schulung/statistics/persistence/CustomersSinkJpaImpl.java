package de.sample.schulung.statistics.persistence;

import de.sample.schulung.statistics.domain.Customer;
import de.sample.schulung.statistics.domain.CustomersSinkPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@ApplicationScoped
@Typed(CustomersSinkPort.class)
@RequiredArgsConstructor
public class CustomersSinkJpaImpl implements CustomersSinkPort {

  private final CustomerEntityRepository repo;
  private final CustomerEntityMapper mapper;

  @Transactional
  @Override
  public void saveCustomer(Customer customer) {
    var customerEntity = mapper.map(customer);
    if (this.repo.count("uuid", customerEntity.getUuid()) == 0) {
      this.repo.persist(customerEntity);
    } else {
      this.repo.getEntityManager().merge(customerEntity);
    }
  }

  @Transactional
  @Override
  public void deleteCustomer(UUID uuid) {
    repo.deleteById(uuid);
  }

  @Override
  public long count() {
    return repo.count();
  }
}
