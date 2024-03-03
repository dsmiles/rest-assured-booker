package endpoints.booking;

import Base.BaseTest;
import builders.BookingBuilder;
import io.restassured.http.ContentType;
import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static helpers.ClassToJsonConverter.convertClassToJsonWithExtraField;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateBookingTest extends BaseTest {

    @Test
    @DisplayName("Responds with the created booking and assigned booking id")
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
    @DisplayName("Check created booking response matches the expected schema")
    public void testCreateNewBookingSchema() {
        Booking booking = new BookingBuilder().build();

        given()
            .spec(requestSpec())
            .body(booking)
            .when()
            .post("/booking")
            .then()
            .spec(responseSpec())
            .body(matchesJsonSchemaInClasspath("CreatedBookingSchema.json"));;
    }

    // TODO XML payload

    @Test
    @DisplayName("Responds with error when bad payload is sent")
    public void testCreateNewBookingWithBadPayload() {
        Booking booking = new BookingBuilder()
            .withFirstname(null)
            .build();

        given()
            .spec(requestSpec())
            .body(booking)
            .when()
            .post("/booking")
            .then()
            .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);  // Should be 400 Bad Request

        // Error: This should be a client error 400
    }

    @Test
    @DisplayName("Responds with the created booking and assigned booking id when extra fields are sent in the payload")
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
    @DisplayName("Responds with an error when accept header value invalid")
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
            .body(equalTo("I'm a teapot"));;  // This should be 406 Not Acceptable

        // Error: This should be a 406
    }

}
