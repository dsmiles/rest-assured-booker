package endpoints.ping;

import Base.BaseTest;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static specs.BaseSpec.requestSpec;

public class HealthCheckTest extends BaseTest {

    @Test
    @DisplayName("Health check endpoint to confirm whether the API is up and running")
    public void testHealthEndpoint() {
        RestAssured
            .given()
            .spec(requestSpec())
            .when()
            .get("/ping")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_CREATED);

        // This should really be SC_OK (200) since we didn't create any data - idempotent
    }
}
