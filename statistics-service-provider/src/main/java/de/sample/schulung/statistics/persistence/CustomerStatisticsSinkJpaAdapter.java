package de.sample.schulung.statistics.persistence;

import de.sample.schulung.statistics.domain.CustomerStatistics;
import de.sample.schulung.statistics.domain.CustomerStatisticsSinkPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@Typed(CustomerStatisticsSinkPort.class)
@RequiredArgsConstructor
public class CustomerStatisticsSinkJpaAdapter implements CustomerStatisticsSinkPort {

  private final CustomerEntityRepository repo;

  @Override
  public CustomerStatistics getStatistics() {
    return repo.getStatistics();
  }

}
