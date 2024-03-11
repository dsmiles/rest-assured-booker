package endpoints.auth;

import base.BaseTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class AuthenticationTest extends BaseTest {

    @Test
    @DisplayName("Responds with 200 and token when given valid credentials")
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
            .body("token", is(notNullValue()))
            .body("token", matchesPattern("[a-zA-Z0-9]{15,}"));
    }

    @Disabled("Disabled because API incorrectly returns 200")
    @Test
    @DisplayName("Responds with 401 when given invalid credentials")
    public void testTokenGenerationWithInvalidCredentials() {
        Map<String, String> payload = new HashMap<>();
        payload.put("username", "badusername");
        payload.put("password", "password123");

        given()
            .spec(requestSpec())
            .body(payload)
            .when()
            .post("/auth")
            .then()
            .spec(responseSpec())
            .assertThat()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .body("reason", equalTo("Bad credentials"));

        // Should return a 401 Unauthorized status code and MAY return a payload with a reason
    }
}
