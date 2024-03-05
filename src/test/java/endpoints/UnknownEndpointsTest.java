package endpoints;

import Base.BaseTest;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static specs.BaseSpec.requestSpec;

/**
 * Test class to verify the behavior of unknown endpoints.
 */
public class UnknownEndpointsTest extends BaseTest {

    /**
     * Test to verify that calls to unknown endpoints are rejected.
     */
    @Test
    @DisplayName("Reject calls to unknown endpoints")
    public void testUnknownEndpoint() {
        RestAssured
            .given()
            .spec(requestSpec())
            .when()
            .get("/unknown")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
