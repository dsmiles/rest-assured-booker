package endpoints.booking;

import Base.BaseTest;
import builders.BookingBuilder;
import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static helpers.AuthenticationHelper.getAuthenticationToken;
import static helpers.BookingHelper.createBooking;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class PartialUpdateTest extends BaseTest {

    public static final int INVALID_BOOKING_ID = 999999;

    @Test
    @DisplayName("Responds with updated booking when attempting partial payload update")
    public void testPartialUpdateBooking() {
        Booking originalBooking = new BookingBuilder().build();
        int bookingId = createBooking(originalBooking);

        // Generate new partial booking data
        Map<String, String> payload = new HashMap<>();
        payload.put("firstname", "Pepper");
        payload.put("lastname", "Potts");

        // Get authorisation token
        String token = getAuthenticationToken();

        // Update the booking and check the data
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .pathParams("id", bookingId)
            .body(payload)
            .when()
            .patch("/booking/{id}")
            .then()
            .spec(responseSpec())
            .body("firstname", equalTo(payload.get("firstname")))
            .body("lastname", equalTo(payload.get("lastname")))
            .body("totalprice", equalTo(originalBooking.getTotalPrice()))
            .body("depositpaid", equalTo(originalBooking.isDepositPaid()))
            .body("bookingdates.checkin", equalTo(originalBooking.getBookingDates().getCheckin()))
            .body("bookingdates.checkout", equalTo(originalBooking.getBookingDates().getCheckout()))
            .body("additionalneeds", equalTo(originalBooking.getAdditionalNeeds()))
            .body(matchesJsonSchemaInClasspath("BookingSchema.json"));
    }

    @Test
    @DisplayName("Responds with updated booking when attempting partial payload update using auth")
    public void testPartialUpdateBookingWithAuth() {
        Booking originalBooking = new BookingBuilder().build();
        int bookingId = createBooking(originalBooking);

        // Generate new partial booking data
        Map<String, String> payload = new HashMap<>();
        payload.put("firstname", "Pepper");
        payload.put("lastname", "Potts");

        // Update the booking and check the data
        given()
            .spec(requestSpec())
            .auth().preemptive().basic("admin", "password123")
            .pathParams("id", bookingId)
            .body(payload)
            .when()
            .patch("/booking/{id}")
            .then()
            .spec(responseSpec())
            .body("firstname", equalTo(payload.get("firstname")))
            .body("lastname", equalTo(payload.get("lastname")));
    }

    @Test
    @DisplayName("Responds with 403 when attempting partial payload update without authentication")
    public void testPartialUpdateBookingWithoutAuthorization() {
        Booking originalBooking = new BookingBuilder().build();
        int bookingId = createBooking(originalBooking);

        // Generate new partial booking data
        Map<String, String> payload = new HashMap<>();
        payload.put("firstname", "Pepper");
        payload.put("lastname", "Potts");

        // Update the booking and check the data
        given()
            .spec(requestSpec())
            .pathParams("id", bookingId)
            .body(payload)
            .when()
            .patch("/booking/{id}")
            .then()
            .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Responds with 403 when attempting partial payload update when not authorised")
    public void testPartialUpdateBookingWhenNotAuthorised() {
        Booking originalBooking = new BookingBuilder().build();
        int bookingId = createBooking(originalBooking);

        // Generate new partial booking data
        Map<String, String> payload = new HashMap<>();
        payload.put("firstname", "Pepper");
        payload.put("lastname", "Potts");

        // Update the booking and check the data
        given()
            .spec(requestSpec())
            .auth().preemptive().basic("admin", "badpassword")
            .pathParams("id", bookingId)
            .body(payload)
            .when()
            .patch("/booking/{id}")
            .then()
            .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Responds with 404 when attempting partial payload update of non-existent booking")
    public void testPartialUpdateOfNonExistentBooking() {
        Map<String, String> payload = new HashMap<>();
        payload.put("firstname", "Pepper");
        payload.put("lastname", "Potts");

        // Get authorisation token
        String token = getAuthenticationToken();

        // Update the booking and check the data
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .pathParams("id", INVALID_BOOKING_ID)
            .body(payload)
            .when()
            .patch("/booking/{id}")
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);

        // This should be 404 (Not Found), if ID not found or invalid.
    }

    // TODO Payload contains XML Content-Type: application/xml
    // TODO Response payload contains XML Accept: application/xml
}
