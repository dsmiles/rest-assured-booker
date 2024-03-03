package helpers;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class AuthorizationHelper {

    /**
     * Get an authorization token using the given credentials
     * @return an authorization token
     */
    public static String getAuthorization() {
        Map<String, String> params = new HashMap<>();
        params.put("username", "admin");
        params.put("password", "password123");

        return given()
            .spec(requestSpec())
            .body(params)
            .when()
            .post("/auth")
            .then()
            .spec(responseSpec())
            .assertThat()
            .body("token", is(notNullValue()))
            .extract()
            .path("token");
    }
}
