package de.sample.schulung.accounts;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class IndexPageTests {

  @Test
  void shouldRedirectIndexPage() {
    given()
      .when()
      .get("/")
      .then()
      .statusCode(200)
      .contentType("text/html");
  }
}
