package de.sample.schulung.accounts.persistence.jpa;

import de.sample.schulung.accounts.domain.Customer;
import de.sample.schulung.accounts.domain.CustomersSinkPort;
import de.sample.schulung.accounts.domain.NotFoundException;
import io.quarkus.arc.properties.UnlessBuildProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@Typed(CustomersSinkPort.class)
@UnlessBuildProperty(
  name = "customers.jpa.enabled",
  stringValue = "false",
  enableIfMissing = true
)
public class CustomersSinkJpaAdapter
  implements CustomersSinkPort {

  @Inject
  CustomerEntityRepository repository;
  @Inject
  CustomerEntityMapper mapper;

  @Override
  public Stream<Customer> getCustomers() {
    return this
      .repository
      .findAll()
      .stream()
      .map(this.mapper::map);
  }

  @Override
  public Stream<Customer> getCustomersByState(Customer.CustomerState state) {
    return this
      .repository
      .findAllByState(state)
      .stream()
      .map(this.mapper::map);
  }

  @Override
  public long count() {
    return this
      .repository
      .count();
  }

  @Override
  public boolean exists(UUID uuid) {
    return this
      .repository
      .count("uuid", uuid) > 0;
  }

  @Override
  public Optional<Customer> findCustomerById(UUID uuid) {
    return this
      .repository
      .findByIdOptional(uuid)
      .map(this.mapper::map);
  }

  @Transactional
  @Override
  public void createCustomer(Customer customer) {
    var entity = this.mapper.map(customer);
    this.repository.persist(entity);
    //customer.setUuid(entity.getUuid());
    this.mapper.copy(entity, customer);
  }

  @Transactional
  @Override
  public void replaceCustomer(Customer customer) throws NotFoundException {
    if (!this.exists(customer.getUuid())) {
      throw new NotFoundException();
    }
    final var entity = mapper.map(customer);
    this.repository.getEntityManager().merge(entity);
    this.mapper.copy(entity, customer);
  }

  @Transactional
  @Override
  public void deleteCustomer(UUID uuid) throws NotFoundException {
    if (!this.repository.deleteById(uuid)) {
      throw new NotFoundException();
    }
  }

}
