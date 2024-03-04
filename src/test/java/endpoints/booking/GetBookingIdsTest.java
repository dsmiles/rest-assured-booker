package endpoints.booking;

import Base.BaseTest;
import builders.BookingBuilder;
import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.BookingHelper.createBooking;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class GetBookingIdsTest extends BaseTest {

    @Test
    @DisplayName("Responds with all booking IDs when getting collection")
    public void testGetAllBookingIDs() {
        given()
            .spec(requestSpec())
            .when()
            .get("/booking")
            .then()
            .spec(responseSpec())
            .body("bookingid", is(notNullValue()));
    }

    @Test
    @DisplayName("Responds with subset of booking IDs when filtering by firstname")
    public void testFilterByFirstName() {
        Booking booking = new BookingBuilder().withFirstname("Xander").build();
        createBooking(booking);

        given()
            .spec(requestSpec())
            .queryParam("firstname", booking.getFirstname())
            .when()
            .get("/booking")
            .then()
            .spec(responseSpec())
            .body("bookingid", is(notNullValue()))
            .body(".", hasSize(1));
    }

    @Test
    @DisplayName("Responds with subset of booking IDs when filtering by lastname")
    public void testFilterByLastName() {
        Booking booking = new BookingBuilder().withLastname("Philpotts").build();
        createBooking(booking);

        given()
            .spec(requestSpec())
            .queryParam("lastname", booking.getLastname())
            .when()
            .get("/booking")
            .then()
            .spec(responseSpec())
            .body("bookingid", is(notNullValue()))
            .body(".", hasSize(1));
    }

    @Test
    @DisplayName("Responds with subset of booking IDs when filtering by firstname & lastname")
    public void testFilterByFirstAndLastName() {
        Booking booking = new BookingBuilder()
            .withFirstname("Kenyon")
            .withLastname("Beltran")
            .build();
        createBooking(booking);

        given()
            .spec(requestSpec())
            .queryParam("firstname", booking.getFirstname())
            .queryParam("lastname", booking.getLastname())
            .when()
            .get("/booking")
            .then()
            .spec(responseSpec())
            .body("bookingid", is(notNullValue()))
            .body(".", hasSize(1));
    }

    @Test
    @DisplayName("Responds with subset of booking IDs when filtering by checkin date")
    public void testFilterByCheckinDate() {
        Booking booking = new BookingBuilder().withCheckin("2024-02-29").build();
        createBooking(booking);

        given()
            .spec(requestSpec())
            .queryParam("checkin", booking.getBookingDates().getCheckin())
            .when()
            .get("/booking")
            .then()
            .spec(responseSpec())
            .body("bookingid", is(notNullValue()))
            .body(".", hasSize(1));
    }

    @Test
    @DisplayName("Responds with subset of booking IDs when filtering by Checkout date")
    public void testFilterByCheckoutDate() {
        Booking booking = new BookingBuilder().withCheckout("2024-02-29").build();
        createBooking(booking);

        given()
            .spec(requestSpec())
            .queryParam("checkout", booking.getBookingDates().getCheckout())
            .when()
            .get("/booking")
            .then()
            .spec(responseSpec())
            .body("bookingid", is(notNullValue()))
            .body(".", hasSize(1));
    }

    @Test
    @DisplayName("Responds with subset of booking IDs when filtering by checkin and checkout date")
    public void testFilterByCheckinAndCheckoutDate() {
        Booking booking = new BookingBuilder()
            .withCheckin("2024-01-01")
            .withCheckout("2024-01-02")
            .build();
        createBooking(booking);

        given()
            .spec(requestSpec())
            .queryParam("checkin", booking.getBookingDates().getCheckin())
            .queryParam("checkout", booking.getBookingDates().getCheckout())
            .when()
            .get("/booking")
            .then()
            .spec(responseSpec())
            .body("bookingid", is(notNullValue()))
            .body(".", hasSize(1));
    }

    @Test
    @DisplayName("Responds with subset of booking IDs when filtering by name, checkin and checkout date")
    public void testFilterByNameWithCheckinAndCheckoutDate() {
        Booking booking = new BookingBuilder()
            .withFirstname("Donald")
            .withCheckin("2024-01-01")
            .withCheckout("2024-01-02")
            .build();
        createBooking(booking);

        given()
            .spec(requestSpec())
            .queryParam("firstname", booking.getFirstname())
            .queryParam("checkin", booking.getBookingDates().getCheckin())
            .queryParam("checkout", booking.getBookingDates().getCheckout())
            .when()
            .get("/booking")
            .then()
            .spec(responseSpec())
            .body("bookingid", is(notNullValue()))
            .body(".", hasSize(1));
    }

    @Test
    @DisplayName("Responds with error when filtering by checkin with a invalid date value")
    public void testFilterByCheckinDateInvalid() {
        given()
            .spec(requestSpec())
            .queryParam("checkin", "2024-00-00")
            .when()
            .get("/booking")
            .then()
            .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);   // This should be 400 Bad Request

        // Server side validation SHOULD stop this - Return 400 Bad Request
    }

    @Test
    @DisplayName("Responds with all booking IDs when filtering by unknown parameter")
    public void testFilterByInvalidParameter() {
        given()
            .spec(requestSpec())
            .queryParam("unknown", "value")
            .when()
            .get("/booking")
            .then()
            .spec(responseSpec())
            .body("bookingid", is(notNullValue()));
    }
}
