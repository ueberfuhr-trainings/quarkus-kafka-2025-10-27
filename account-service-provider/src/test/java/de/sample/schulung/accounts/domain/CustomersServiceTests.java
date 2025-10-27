package de.sample.schulung.accounts.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
class CustomersServiceTests {

  @Inject
  CustomersService service;

  @Test
  void shouldNotCreateInvalidCustomer() {
    var customer = new Customer();
    customer.setName("T");
    customer.setState(Customer.CustomerState.ACTIVE);
    customer.setDateOfBirth(LocalDate.of(2000, Month.DECEMBER, 20));

    assertThatThrownBy(() -> service.createCustomer(customer))
      .isNotNull();

  }


}
