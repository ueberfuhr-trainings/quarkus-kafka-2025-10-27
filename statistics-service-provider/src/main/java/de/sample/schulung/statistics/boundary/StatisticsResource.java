package de.sample.schulung.statistics.boundary;

import de.sample.schulung.statistics.domain.CustomerStatisticsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/statistics")
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsResource {

  @Inject
  CustomerStatisticsService customerStatisticsService;

  @Inject
  CustomerStatisticsDtoMapper customerStatisticsDtoMapper;

  @GET
  @Path("/customers")
  public CustomerStatisticsDto getStatistics() {
    return customerStatisticsDtoMapper
      .map(customerStatisticsService.getStatistics());
  }
}
