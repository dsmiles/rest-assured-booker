package endpoints.booking;

import base.BaseTest;
import builders.BookingBuilder;
import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.AuthenticationHelper.getAuthenticationToken;
import static helpers.BookingHelper.createBooking;
import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;

public class DeleteBookingTest extends BaseTest {

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
            .statusCode(HttpStatus.SC_CREATED);

        // This should really be 200 or 204 on deletion success
    }

    @Disabled("Disabled because API incorrectly returns 201")
    @Test
    @DisplayName("Responds with 200 when deleting an existing booking using auth")
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
            .statusCode(HttpStatus.SC_OK);

        // Currently returns 201. This should really be 200 with a body or 204 without body on deletion success
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

    @Disabled("Disabled because API incorrectly returns 405")
    @Test
    @DisplayName("Responds with 404 when deleting a non-existent booking")
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
            .statusCode(HttpStatus.SC_NOT_FOUND);

        // Currently returns 405 Method Not Supported
    }

    @Disabled("Disabled because API incorrectly returns 404")
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
            .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);

        // This should really be 405 Method Not Allowed
    }
}
