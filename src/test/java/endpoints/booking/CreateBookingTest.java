package endpoints.booking;

import base.BaseTest;
import builders.BookingBuilder;
import io.restassured.http.ContentType;
import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.ClassToJsonConverter.convertClassToJsonWithExtraField;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class CreateBookingTest extends BaseTest {

    @Test
    @DisplayName("Responds with newly created booking and assigned booking ID")
    public void testCreateNewBooking() {
        Booking booking = new BookingBuilder().build();

        given()
            .spec(requestSpec())
            .body(booking)
            .when()
            .post("/booking")
            .then()
            .spec(responseSpec())
            .body("bookingid", is(notNullValue()))
            .body("booking.firstname", equalTo(booking.getFirstname()))
            .body("booking.lastname", equalTo(booking.getLastname()))
            .body("booking.totalprice", equalTo(booking.getTotalPrice()))
            .body("booking.depositpaid", equalTo(booking.isDepositPaid()))
            .body("booking.bookingdates.checkin", equalTo(booking.getBookingDates().getCheckin()))
            .body("booking.bookingdates.checkout", equalTo(booking.getBookingDates().getCheckout()))
            .body("booking.additionalneeds", equalTo(booking.getAdditionalNeeds()));
    }

    @Test
    @DisplayName("Responds with payload matching expected schema when creating booking")
    public void testCreateNewBookingSchema() {
        Booking booking = new BookingBuilder().build();

        given()
            .spec(requestSpec())
            .body(booking)
            .when()
            .post("/booking")
            .then()
            .spec(responseSpec())
            .body(matchesJsonSchemaInClasspath("CreatedBookingSchema.json"));
    }

    // TODO XML payload

    @Disabled("Disabled because API incorrectly returns 500")
    @Test
    @DisplayName("Responds with 400 when a bad payload is sent")
    public void testCreateNewBookingWithBadPayload() {
        Booking booking = new BookingBuilder()
            .withFirstname(null)
            .withLastname(null)
            .build();

        given()
            .spec(requestSpec())
            .body(booking)
            .when()
            .post("/booking")
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);

        // Server side validation SHOULD stop this - Return 400 Bad Request
    }

    @Test
    @DisplayName("Responds with newly created booking and assigned booking ID ignoring extra fields sent in payload")
    public void testCreateNewBookingWithExtraFields() {
        Booking booking = new BookingBuilder().build();
        String payload = convertClassToJsonWithExtraField(booking, "extraField", "extraValue");

        given()
            .spec(requestSpec())
            .body(payload)
            .when()
            .post("/booking")
            .then()
            .spec(responseSpec())
            .body("bookingid", is(notNullValue()));
    }

    @Test
    @DisplayName("Responds with error when accept header value invalid")
    public void testCreateNewBookingWithInvalidAcceptHeader() {
        Booking booking = new BookingBuilder().build();

        given()
            .spec(requestSpec())
            .header("Accept", "application/invalid")
            .body(booking)
            .when()
            .post("/booking")
            .then()
            .statusCode(418)
            .contentType(ContentType.TEXT)
            .body(equalTo("I'm a teapot"));

        // Error: This should be a 406 Not Acceptable
    }

    // TODO Payload contains XML Content-Type: application/xml
    // TODO Response payload Accept: application/xml
}
