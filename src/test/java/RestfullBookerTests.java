import Base.BaseSpec;
import Base.BaseTest;
import io.restassured.response.Response;
import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import builders.BookingBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Base.BaseSpec.requestSpec;
import static Base.BaseSpec.responseSpec;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class RestfullBookerTests extends BaseTest {

    @Test
    @DisplayName("Responds with subset of booking ids when filtering by firstname")
    public void testFilterByFirstName() {
        Booking booking = new BookingBuilder().withFirstname("Xander").build();
        int bookingId = createBooking(booking);

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
    @DisplayName("Responds with subset of booking ids when filtering by lastname")
    public void testFilterByLastName() {
        Booking booking = new BookingBuilder().withLastname("Philpotts").build();
        int bookingId = createBooking(booking);

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
    @DisplayName("Responds with subset of booking ids when filtering by firstname & lastname")
    public void testFilterByFirstAndLastName() {
        Booking booking = new BookingBuilder().withFirstname("Kenyon").withLastname("Beltran").build();
        int bookingId1 = createBooking(booking);
        int bookingId2 = createBooking(booking);

        given()
            .spec(requestSpec())
            .queryParam("firstname", booking.getFirstname())
            .queryParam("lastname", booking.getLastname())
            .when()
            .get("/booking")
            .then()
            .spec(responseSpec())
            .body("bookingid", is(notNullValue()))
            .body(".", hasSize(2));
    }

    @Test
    @DisplayName("Responds with subset of booking ids when filtering by checkin date")
    public void testFilterByCheckinDate() {
        Booking booking = new BookingBuilder().withCheckin("2024-02-29").build();
        int bookingId = createBooking(booking);

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
    @DisplayName("Responds with subset of booking ids when filtering by checkin date")
    public void testFilterByCheckinDateNew() {
        Booking booking = new BookingBuilder().withCheckin("2024-01-29").build();
        int bookingId = createBooking(booking);

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
    @DisplayName("Responds with subset of booking ids when filtering by Checkout date")
    public void testFilterByCheckoutDate() {
        Booking booking = new BookingBuilder().withCheckout("2024-02-29").build();
        int bookingId = createBooking(booking);

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
    @DisplayName("Responds with subset of booking ids when filtering by checkin and checkout date")
    public void testFilterByCheckinAndCheckoutDate() {
        Booking booking = new BookingBuilder().withCheckin("2024-01-01").withCheckout("2024-01-02").build();
        int bookingId = createBooking(booking);

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
    @DisplayName("Responds with a booking payload when retrieving the specified booking id")
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
            .body("bookingdates.checkout", equalTo(booking.getBookingDates().getCheckout()));
    }

    @Test
    @DisplayName("Check booking response matches the expected schema when retrieving booking id")
    public void testGetBookingSchema() {
        Booking booking = new BookingBuilder().build();
        int bookingId = createBooking(booking);

        given()
            .spec(requestSpec())
            .pathParams("id", bookingId)
            .when()
            .get("/booking/{id}")
            .then()
            .spec(responseSpec())
            .body(matchesJsonSchemaInClasspath("BookingSchema.json"));
    }

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
            .body("additionalneeds", equalTo(updatedBooking.getAdditionalNeeds()))
            .body(matchesJsonSchemaInClasspath("BookingSchema.json"));;
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

    @Test
    @DisplayName("Updates a current booking with a partial payload")
    public void testPartialUpdateBooking() {

        // Generate data and create original booking
        Booking originalBooking = new BookingBuilder().build();
        int originalBookingId = createBooking(originalBooking);

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
            .pathParams("id", originalBookingId)
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
    @DisplayName("Responds with a 404 when deleting an non-existent booking")
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
            .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);     // This should really be 200 or 204 on deletion success
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
    @DisplayName("Responds with 200 and token when given valid credentials")
    public void testTokenGeneration() {
        Map<String, String> payload = new HashMap<>();
        payload.put("username", "admin");
        payload.put("password", "password123");

        given()
            .spec(requestSpec())
            .body(payload)
            .when()
            .post("/auth")
            .then()
            .spec(responseSpec())
            .assertThat()
            .body("token", is(notNullValue()));
    }

    /**
     * Create a new booking and return its ID
     * @param booking the booking to create
     * @return booking ID of the new booking
     */
    private int createBooking(Booking booking) {
        return given()
            .spec(requestSpec())
            .body(booking)
            .when()
            .post("/booking")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .path("bookingid");
    }

    /**
     * Get an authorisation token using the given credentials
     * @return an authorisation token
     */
    private String getAuthorisation() {
        Map<String, String> params = new HashMap<>();
        params.put("username", "admin");
        params.put("password", "password123");

        return given()
            .spec(requestSpec())
            .body(params)
            .when()
            .post("/auth")
            .then()
            .spec(responseSpec())
            .assertThat()
            .body("token", is(notNullValue()))
            .extract()
            .path("token");
    }
}
