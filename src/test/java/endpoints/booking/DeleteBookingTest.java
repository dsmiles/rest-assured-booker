package endpoints.booking;

import base.BaseTest;
import builders.BookingBuilder;
import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.AuthenticationHelper.getAuthenticationToken;
import static helpers.BookingHelper.createBooking;
import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;

public class DeleteBookingTest extends BaseTest {

    public static final int INVALID_BOOKING_ID = 999999;

    @Test
    @DisplayName("Responds with 201 when deleting an existing booking")
    void testDeleteBooking() {
        Booking booking = new BookingBuilder().build();
        int bookingId = createBooking(booking);

        // Get authorisation token
        String token = getAuthenticationToken();

        // Delete the booking
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .pathParams("id", bookingId)
            .when()
            .delete("/booking/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_CREATED);     // wrong status code

        // This should really be 200 or 204 on deletion success
    }

    @Test
    @DisplayName("Responds with 201 when deleting an existing booking using auth")
    void testDeleteBookingWithAuthHeader() {
        Booking booking = new BookingBuilder().build();
        int bookingId = createBooking(booking);

        // Delete the booking
        given()
            .spec(requestSpec())
            .auth().preemptive().basic("admin", "password123")
            .pathParams("id", bookingId)
            .when()
            .delete("/booking/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_CREATED);     // wrong status code

        // This should really be 200 or 204 on deletion success
    }

    @Test
    @DisplayName("Responds with 403 deleting booking when not authorized")
    void testDeleteBookingsWithoutAuthorization() {
        given()
            .spec(requestSpec())
            .when()
            .delete("/booking/1")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Responds with 403 deleting an existing booking when not authorised")
    void testDeleteBookingWithInvalidLogin() {
        Booking booking = new BookingBuilder().build();
        int bookingId = createBooking(booking);

        // Delete the booking
        given()
            .spec(requestSpec())
            .auth().preemptive().basic("admin", "badpassword")
            .pathParams("id", bookingId)
            .when()
            .delete("/booking/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Responds with error when deleting an non-existent booking")
    void testDeleteNonExistentBooking() {
        String token = getAuthenticationToken();

        // Delete the booking
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .pathParams("id", INVALID_BOOKING_ID)
            .when()
            .delete("/booking/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);     // wrong status code

        // This should really be 404 Not Found
    }

    @Test
    @DisplayName("Responds with 405 when deleting entire booking collection")
    void testDeleteAllBookings() {
        // Get authorisation token
        String token = getAuthenticationToken();

        // Delete the booking
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .when()
            .delete("/booking")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NOT_FOUND);   // wrong status code

        // This should really be 405 Method Not Supported
    }
}
