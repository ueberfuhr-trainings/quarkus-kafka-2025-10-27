package de.sample.schulung.statistics.persistence;

import de.sample.schulung.statistics.domain.CustomerStatistics;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.UUID;

@ApplicationScoped
public class CustomerEntityRepository
  implements PanacheRepositoryBase<CustomerEntity, UUID> {

  private Object[] getStatisticsAsArray() {
    return (Object[]) getEntityManager()
      .createQuery(
        "SELECT count(c), min(c.dateOfBirth), max(c.dateOfBirth) FROM Customer c"
      )
      .getSingleResult();
  }

  public CustomerStatistics getStatistics() {
    var statistics = this.getStatisticsAsArray();
    var countObject = (Long) statistics[0];
    var count = countObject != null ? countObject : 0L;
    final var result = CustomerStatistics
      .builder()
      .count(count);
    if (count > 0) {
      result
        .earliestBirthdate((LocalDate) statistics[1])
        .latestBirthdate((LocalDate) statistics[2]);
    }
    return result.build();
  }

}
