package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;


@Data
@Builder
@Jacksonized
public class BookingDetail {
    @JsonProperty("bookingid")
    private int bookingId;              // ID for newly created booking

    private String firstname;           // Firstname for the guest who made the booking
    private String lastname;            // Lastname for the guest who made the booking

    @JsonProperty("totalprice")
    private int totalPrice;             // The total price for the booking

    @JsonProperty("depositpaid")
    private boolean depositPaid;        // Whether the deposit has been paid or not

    @JsonProperty("bookingdates")
    private BookingDates bookingDates;  // sub-object that contains the checkin and checkout dates

    @JsonProperty("additionalneeds")
    private String additionalNeeds;     // Any other needs the guest has

}
