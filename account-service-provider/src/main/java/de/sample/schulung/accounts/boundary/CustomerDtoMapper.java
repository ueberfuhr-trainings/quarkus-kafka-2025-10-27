package de.sample.schulung.accounts.boundary;

import de.sample.schulung.accounts.domain.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface CustomerDtoMapper {

  CustomerDto map(Customer source);

  Customer map(CustomerDto source);

  default CustomerDto.CustomerStateDto mapState(Customer.CustomerState source) {
    return switch (source) {
      case ACTIVE -> CustomerDto.CustomerStateDto.ACTIVE;
      case LOCKED -> CustomerDto.CustomerStateDto.LOCKED;
      case DISABLED -> CustomerDto.CustomerStateDto.DISABLED;
    };
  }

  default Customer.CustomerState mapState(CustomerDto.CustomerStateDto source) {
    return switch (source) {
      case ACTIVE -> Customer.CustomerState.ACTIVE;
      case LOCKED -> Customer.CustomerState.LOCKED;
      case DISABLED -> Customer.CustomerState.DISABLED;
    };
  }

}
