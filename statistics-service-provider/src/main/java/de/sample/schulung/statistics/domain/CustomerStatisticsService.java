package de.sample.schulung.statistics.domain;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class CustomerStatisticsService {

  private final CustomerStatisticsSinkPort customerStatisticsSink;

  public CustomerStatistics getStatistics() {
    return customerStatisticsSink.getStatistics();
  }

}
