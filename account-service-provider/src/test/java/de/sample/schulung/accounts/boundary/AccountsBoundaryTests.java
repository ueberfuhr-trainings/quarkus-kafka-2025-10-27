package de.sample.schulung.accounts.boundary;

import de.sample.schulung.accounts.domain.CustomersService;
import de.sample.schulung.accounts.domain.NotFoundException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AccountsBoundaryTests {

  @InjectMock
  CustomersService service;

  @Test
  void shouldReturnEmptyArrayOnGetCustomersWhenNoCustomersExist() {
    when(service.getCustomers()).thenReturn(Stream.empty());

    given()
      .accept(MediaType.APPLICATION_JSON)
      .when()
      .get("/customers")
      .then()
      .statusCode(200)
      .contentType(MediaType.APPLICATION_JSON)
      .body(is("[]"));
  }

  @Test
  void shouldReturn404OnFindByIdIfCustomerNotFound() {
    UUID customerId = UUID.randomUUID();
    when(service.findCustomerById(customerId)).thenReturn(Optional.empty());

    given()
      .accept(MediaType.APPLICATION_JSON)
      .when()
      .get("/customers/{id}", customerId)
      .then()
      .statusCode(404);
  }

  @Test
  void shouldReturn404OnDeleteIfCustomerNotFound() {
    UUID customerId = UUID.randomUUID();
    doThrow(NotFoundException.class).when(service).deleteCustomer(customerId);

    given()
      .when()
      .delete("/customers/{id}", customerId)
      .then()
      .statusCode(404);
  }

  @Test
  void shouldReturn204OnDeleteIfCustomerFound() {
    UUID customerId = UUID.randomUUID();

    given()
      .when()
      .delete("/customers/{id}", customerId)
      .then()
      .statusCode(204);

    verify(service).deleteCustomer(customerId);
  }

  @Test
  void shouldReturn400OnCreateInvalidCustomer() {
    given()
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .body("""
        {
          "name": "T",
          "birthdate": "1985-07-30",
          "state": "active"
        }
        """)
      .when()
      .post("/customers")
      .then()
      .statusCode(400);

    verifyNoInteractions(service);
  }
}
