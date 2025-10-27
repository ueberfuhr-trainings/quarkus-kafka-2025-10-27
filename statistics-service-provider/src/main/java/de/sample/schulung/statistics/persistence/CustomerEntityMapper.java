package de.sample.schulung.statistics.persistence;

import de.sample.schulung.statistics.domain.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface CustomerEntityMapper {

  CustomerEntity map(Customer customer);

}
