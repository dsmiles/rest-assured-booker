package helpers;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

/**
 * Helper class for authentication-related tasks.
 */
public class AuthenticationHelper {

    /**
     * Get an authentication token using default credentials.
     * @return An authentication token.
     */
    public static String getAuthenticationToken() {
        return getAuthenticationToken("admin", "password123");
    }

    /**
     * Get an authentication token using the given credentials.
     * @param username The username for authentication.
     * @param password The password for authentication.
     * @return An authentication token.
     */
    public static String getAuthenticationToken(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        return given()
            .spec(requestSpec())
            .body(params)
            .when()
            .post("/auth")
            .then()
            .spec(responseSpec())
            .body("token", is(notNullValue()))
            .extract()
            .path("token");
    }
}
