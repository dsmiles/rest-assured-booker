package helpers;

import model.Booking;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;

public class BookingHelper {

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

}
