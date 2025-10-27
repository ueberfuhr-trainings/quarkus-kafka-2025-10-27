package de.sample.schulung.accounts.persistence.jpa;

import de.sample.schulung.accounts.domain.Customer;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface CustomerEntityMapper {

  @Mapping(target = "birthdate", source = "dateOfBirth")
  CustomerEntity map(Customer source);

  @InheritInverseConfiguration
  Customer map(CustomerEntity source);

  @Mapping(target = "dateOfBirth", source = "birthdate")
  void copy(CustomerEntity source, @MappingTarget Customer target);

}
