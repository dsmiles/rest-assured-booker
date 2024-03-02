package endpoints.auth;

import Base.BaseTest;
import model.Credentials;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class AuthorisationTest extends BaseTest {

    @Test
    @DisplayName("Responds with 200 OK and token when given valid credentials")
    public void testTokenGeneration() {
        Map<String, String> payload = new HashMap<>();
        payload.put("username", "admin");
        payload.put("password", "password123");

        given()
            .spec(requestSpec())
            .body(payload)
            .when()
            .post("/auth")
            .then()
            .spec(responseSpec())
            .assertThat()
            .body("token", is(notNullValue()));
    }

    @Test
    @DisplayName("Responds with 401 Unauthorized when given invalid credentials")
    public void testTokenGenerationWithInvalidCredentials() {
        Map<String, String> payload = new HashMap<>();
        payload.put("username", "badusername");
        payload.put("password", "password123");

        Credentials credentials = Credentials.builder().username("badusername").password("password123").build();

        given()
            .spec(requestSpec())
            .body(credentials)
            .when()
            .post("/auth")
            .then()
            .spec(responseSpec())
            .assertThat()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)       // Warning - returns 200 OK
            .body("reason", equalTo("Bad credentials"));
    }

    // TODO add authentication header tests

}
