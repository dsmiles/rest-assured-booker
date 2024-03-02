package endpoints.booking;

import Base.BaseTest;
import builders.BookingBuilder;
import model.Booking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.Helpers.createBooking;
import static helpers.Helpers.getAuthorisation;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class UpdateBookingTest extends BaseTest {

    @Test
    @DisplayName("Updates a current booking")
    public void testUpdateBooking() {
        // Generate data and create original booking
        Booking originalBooking = new BookingBuilder().build();
        int originalBookingId = createBooking(originalBooking);

        // Generate new booking data
        Booking updatedBooking = new BookingBuilder().build();

        // Get token
        String token = getAuthorisation();

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
    @DisplayName("Check updated booking response matches expected schema")
    public void testUpdateBookingSchema() {
        // Generate data and create original booking
        Booking originalBooking = new BookingBuilder().build();
        int originalBookingId = createBooking(originalBooking);

        // Generate new booking data
        Booking updatedBooking = new BookingBuilder().build();

        // Get token
        String token = getAuthorisation();

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
            .body(matchesJsonSchemaInClasspath("BookingSchema.json"));;
    }
}
