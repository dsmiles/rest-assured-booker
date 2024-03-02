package endpoints.booking;

import Base.BaseTest;
import builders.BookingBuilder;
import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.Helpers.createBooking;
import static helpers.Helpers.getAuthorisation;
import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;

public class DeleteBookingTest extends BaseTest {

    @Test
    @DisplayName("Responds with a 201 when deleting an existing booking")
    void testDeleteBooking() {
        Booking booking = new BookingBuilder().build();
        int bookingId = createBooking(booking);

        // Get authorisation token
        String token = getAuthorisation();

        // Delete the booking
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .pathParams("id", bookingId)
            .when()
            .delete("/booking/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_CREATED);     // This should really be 200 or 204 on deletion success
    }

    @Test
    @DisplayName("Responds with an error when deleting an non-existing booking")
    void testDeleteNonExistentBooking() {
        // Get authorisation token
        String token = getAuthorisation();

        // Delete the booking
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .pathParams("id", 999999)
            .when()
            .delete("/booking/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);     // This should really be 404 Not Found
    }

    @Test
    @DisplayName("Responds with a 404 when deleting an non-existent booking")
    void testDeletedBooking() {
        Booking booking = new BookingBuilder().build();
        int bookingId = createBooking(booking);

        // Get authorisation token
        String token = getAuthorisation();

        // Delete the booking
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .pathParams("id", bookingId)
            .when()
            .delete("/booking/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_CREATED);     // This should really be 200 or 204 on deletion success

        // Attempt to retrieve the booking id
        given()
            .spec(requestSpec())
            .pathParams("id", bookingId)
            .when()
            .get("/booking/{id}")
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Responds with 405 when attempting to delete entire booking collection")
    void testDeleteAllBookings() {
        // Get authorisation token
        String token = getAuthorisation();

        // Delete the booking
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .when()
            .delete("/booking")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NOT_FOUND);     // This should really be 405 Method Not Supported
    }
}
