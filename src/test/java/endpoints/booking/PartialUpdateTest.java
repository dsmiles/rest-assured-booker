package endpoints.booking;

import Base.BaseTest;
import builders.BookingBuilder;
import model.Booking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static helpers.Helpers.createBooking;
import static helpers.Helpers.getAuthorisation;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class PartialUpdateTest extends BaseTest {

    @Test
    @DisplayName("Updates a current booking with a partial payload")
    public void testPartialUpdateBooking() {

        // Generate data and create booking
        Booking originalBooking = new BookingBuilder().build();
        int bookingId = createBooking(originalBooking);

        // Generate new partial booking data
        Map<String, String> partialUpdate = new HashMap<>();
        partialUpdate.put("firstname", "Pepper");
        partialUpdate.put("lastname", "Potts");

        // Get authorisation token
        String token = getAuthorisation();

        // Update the booking and check the data
        given()
            .spec(requestSpec())
            .cookie("token", token)
            .pathParams("id", bookingId)
            .body(partialUpdate)
            .when()
            .patch("/booking/{id}")
            .then()
            .spec(responseSpec())
            .body("firstname", equalTo(partialUpdate.get("firstname")))
            .body("lastname", equalTo(partialUpdate.get("lastname")))
            .body("totalprice", equalTo(originalBooking.getTotalPrice()))
            .body("depositpaid", equalTo(originalBooking.isDepositPaid()))
            .body("bookingdates.checkin", equalTo(originalBooking.getBookingDates().getCheckin()))
            .body("bookingdates.checkout", equalTo(originalBooking.getBookingDates().getCheckout()))
            .body("additionalneeds", equalTo(originalBooking.getAdditionalNeeds()))
            .body(matchesJsonSchemaInClasspath("BookingSchema.json"));;
    }
}
