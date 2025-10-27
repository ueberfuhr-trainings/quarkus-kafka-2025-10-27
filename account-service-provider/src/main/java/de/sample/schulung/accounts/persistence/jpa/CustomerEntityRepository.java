package de.sample.schulung.accounts.persistence.jpa;

import de.sample.schulung.accounts.domain.Customer.CustomerState;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CustomerEntityRepository
  implements PanacheRepositoryBase<CustomerEntity, UUID> {

  public List<CustomerEntity> findAllByState(CustomerState state) {
    return list("state", state);
  }

}
