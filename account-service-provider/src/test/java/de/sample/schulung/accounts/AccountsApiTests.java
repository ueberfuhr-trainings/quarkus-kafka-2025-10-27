package de.sample.schulung.accounts;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@QuarkusTest
class AccountsApiTests {

  @Test
  void shouldReturnCustomers() {
    given()
      .accept(ContentType.JSON)
      .when()
      .get("/customers")
      .then()
      .statusCode(200)
      .contentType(ContentType.JSON);
  }

  @Test
  void shouldReturnBadRequestOnInvalidStateFilter() {
    given()
      .accept(ContentType.JSON)
      .queryParam("state", "gelbekatze")
      .when()
      .get("/customers")
      .then()
      .statusCode(400);
  }

  @Test
  void shouldNotReturnCustomersWithXml() {
    given()
      .accept(ContentType.XML)
      .when()
      .get("/customers")
      .then()
      .statusCode(406); // Not Acceptable
  }

  @Test
  void shouldCreateCustomer() {
    String location =
      given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
            {
              "name": "Tom Mayer",
              "birthdate": "1985-07-30",
              "state": "active"
            }
          """)
        .when()
        .post("/customers")
        .then()
        .statusCode(201)
        .header("Location", not(is(emptyOrNullString())))
        .extract()
        .header("Location");

    assertThat(location).isNotBlank();

    given()
      .accept(ContentType.JSON)
      .when()
      .get(location)
      .then()
      .statusCode(200)
      .body("name", equalTo("Tom Mayer"));
  }

  @Test
  void shouldNotCreateCustomerWithInvalidName() {
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
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
  }

  @Test
  void shouldNotCreateCustomerWithInvalidState() {
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body("""
          {
            "name": "Tom Mayer",
            "birthdate": "1985-07-30",
            "state": "gelbekatze"
          }
        """)
      .when()
      .post("/customers")
      .then()
      .statusCode(400);
  }

  @Nested
  class ExistingCustomerTests {

    String customerUrl;

    @BeforeEach
    void setup() {
      customerUrl =
        given()
          .contentType(ContentType.JSON)
          .accept(ContentType.JSON)
          .body("""
              {
                "name": "Tom Mayer",
                "birthdate": "1985-07-30",
                "state": "active"
              }
            """)
          .when()
          .post("/customers")
          .then()
          .statusCode(201)
          .extract()
          .header("Location");

      assertThat(customerUrl).isNotBlank();
    }

    @Test
    void shouldReplaceCustomer() {
      given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "name": "Tom Schmidt",
              "birthdate": "1985-07-30",
              "state": "active"
            }
          """)
        .when()
        .put(customerUrl)
        .then()
        .statusCode(204);

      given()
        .accept(ContentType.JSON)
        .when()
        .get(customerUrl)
        .then()
        .statusCode(200)
        .body("name", equalTo("Tom Schmidt"));
    }

    @Test
    void shouldDeleteCustomer() {
      // delete existing
      when()
        .delete(customerUrl)
        .then()
        .statusCode(204);

      // verify it’s gone
      given()
        .accept(ContentType.JSON)
        .when()
        .get(customerUrl)
        .then()
        .statusCode(404);
    }

  }

}
