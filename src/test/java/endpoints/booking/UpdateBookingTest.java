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
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class UpdateBookingTest extends BaseTest {

    public static final int INVALID_BOOKING_ID = 999999;

    @Test
    @DisplayName("Responds with updated booking when updating existing booking")
    public void testUpdateBooking() {
        // Generate original and updated booking
        Booking originalBooking = new BookingBuilder().build();
        Booking updatedBooking = new BookingBuilder().build();
        int originalBookingId = createBooking(originalBooking);

        // Get token
        String token = getAuthenticationToken();

        // Update the original booking and check the data
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .pathParams("id", originalBookingId)
            .body(updatedBooking)
            .when()
            .put("/booking/{id}")
            .then()
            .spec(responseSpec())
            .body("firstname", equalTo(updatedBooking.getFirstname()))
            .body("lastname", equalTo(updatedBooking.getLastname()))
            .body("totalprice", equalTo(updatedBooking.getTotalPrice()))
            .body("depositpaid", equalTo(updatedBooking.isDepositPaid()))
            .body("bookingdates.checkin", equalTo(updatedBooking.getBookingDates().getCheckin()))
            .body("bookingdates.checkout", equalTo(updatedBooking.getBookingDates().getCheckout()))
            .body("additionalneeds", equalTo(updatedBooking.getAdditionalNeeds()));
    }

    @Test
    @DisplayName("Responds with payload matching expected schema when updating existing booking")
    public void testUpdateBookingSchema() {
        Booking originalBooking = new BookingBuilder().build();
        Booking updatedBooking = new BookingBuilder().build();
        int originalBookingId = createBooking(originalBooking);

        // Get token
        String token = getAuthenticationToken();

        // Update the original booking and check the data
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .pathParams("id", originalBookingId)
            .body(updatedBooking)
            .when()
            .put("/booking/{id}")
            .then()
            .spec(responseSpec())
            .body(matchesJsonSchemaInClasspath("BookingSchema.json"));
    }

    @Test
    @DisplayName("Responds with updated booking when updating existing booking using authorization header")
    public void testUpdateBookingWithAuthorizationHeader() {
        Booking originalBooking = new BookingBuilder().build();
        Booking updatedBooking = new BookingBuilder().build();
        int originalBookingId = createBooking(originalBooking);

        // Update the original booking and check the data
        given()
            .spec(requestSpec())
            .auth().preemptive().basic("admin", "password123")
            .pathParams("id", originalBookingId)
            .body(updatedBooking)
            .when()
            .put("/booking/{id}")
            .then()
            .spec(responseSpec());
    }

    @Test
    @DisplayName("Responds with 403 when attempting to update booking without authentication")
    public void testUpdateBookingWithoutAuthentication() {
        Booking originalBooking = new BookingBuilder().build();
        Booking updatedBooking = new BookingBuilder().build();
        int originalBookingId = createBooking(originalBooking);

        // Update the original booking and check the data
        given()
            .spec(requestSpec())
            .pathParams("id", originalBookingId)
            .body(updatedBooking)
            .when()
            .put("/booking/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN);

        // Some might argue this should be 401 Not Authorised
    }

    @Test
    @DisplayName("Responds with 403 when to update booking without authorization")
    public void testUpdateBookingWithInvalidAuthorization() {
        Booking originalBooking = new BookingBuilder().build();
        Booking updatedBooking = new BookingBuilder().build();
        int originalBookingId = createBooking(originalBooking);

        // Update the original booking and check the data
        given()
            .spec(requestSpec())
            .auth().preemptive().basic("admin", "badpassword")
            .pathParams("id", originalBookingId)
            .body(updatedBooking)
            .when()
            .put("/booking/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Responds with 405 when attempting to update non-existent booking")
    public void testUpdateOfNonExistentBooking() {
        Booking updatedBooking = new BookingBuilder().build();
        String token = getAuthenticationToken();

        // Update the original booking and check the data
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .pathParams("id", INVALID_BOOKING_ID)
            .body(updatedBooking)
            .when()
            .put("/booking/{id}")
            .then()
            .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    // TODO Payload contains XML Content-Type: application/xml
    // TODO Response payload Accept: application/xml
}
