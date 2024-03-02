package helpers;

import model.Booking;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class Helpers {

    /**
     * Create a new booking and return its ID
     * @param booking the booking to create
     * @return booking ID of the new booking
     */
    public static int createBooking(Booking booking) {
        return given()
            .spec(requestSpec())
            .body(booking)
            .when()
            .post("/booking")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .path("bookingid");
    }

    /**
     * Get an authorisation token using the given credentials
     * @return an authorisation token
     */
    public static String getAuthorisation() {
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
