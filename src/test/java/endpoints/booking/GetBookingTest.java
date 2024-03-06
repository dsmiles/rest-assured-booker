package endpoints.booking;

import base.BaseTest;
import builders.BookingBuilder;
import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.BookingHelper.createBooking;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static specs.BaseSpec.*;

public class GetBookingTest extends BaseTest {

    @Test
    @DisplayName("Responds with payload when retrieving booking ID")
    public void testGetBookingById() {
        Booking booking = new BookingBuilder().build();
        int bookingId = createBooking(booking);

        given()
            .spec(requestSpec())
            .pathParams("id", bookingId)
            .when()
            .get("/booking/{id}")
            .then()
            .spec(responseSpec())
            .body("firstname", equalTo(booking.getFirstname()))
            .body("lastname", equalTo(booking.getLastname()))
            .body("totalprice", isA(Integer.class))
            .body("depositpaid", isA(Boolean.class))
            .body("bookingdates.checkin", equalTo(booking.getBookingDates().getCheckin()))
            .body("bookingdates.checkout", equalTo(booking.getBookingDates().getCheckout()))
            .body("additionalneeds", equalTo(booking.getAdditionalNeeds()));
    }

    @Test
    @DisplayName("Responds with payload matching expected schema when retrieving booking ID")
    public void testGetBookingSchema() {
        given()
            .spec(requestSpec())
            .pathParams("id", 1)
            .when()
            .get("/booking/{id}")
            .then()
            .spec(responseSpec())
            .body(matchesJsonSchemaInClasspath("BookingSchema.json"));
    }

    @Test
    @DisplayName("Responds with XML payload when retrieving booking ID with accept application/xml")
    public void testGetBookingByIdWithAcceptXml() {
        Booking booking = new BookingBuilder().build();
        int bookingId = createBooking(booking);

        given()
            .spec(requestSpecXml())
            .pathParams("id", bookingId)
            .when()
            .get("/booking/{id}")
            .then()
            .spec(responseSpecXml())        // Should be Content-Type: application/xml
            .body("booking.firstname", equalTo(booking.getFirstname()))
            .body("booking.lastname", equalTo(booking.getLastname()))
            .body("booking.totalprice", equalTo(String.valueOf(booking.getTotalPrice())))
            .body("booking.depositpaid", equalTo(String.valueOf(booking.isDepositPaid())))
            .body("booking.bookingdates.checkin", equalTo(booking.getBookingDates().getCheckin()))
            .body("booking.bookingdates.checkout", equalTo(booking.getBookingDates().getCheckout()))
            .body("booking.additionalneeds", equalTo(booking.getAdditionalNeeds()));

        // Error: Value returned is Content-Type: text/html; charset=utf-8 should be application/xml
    }

    @Test
    @DisplayName("Responds with 404 when retrieving non-existing booking ID")
    public void testGetNonExistentBooking() {
        given()
            .spec(requestSpec())
            .pathParams("id", INVALID_BOOKING_ID)
            .when()
            .get("/booking/{id}")
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body(equalTo("Not Found"));
    }
}
