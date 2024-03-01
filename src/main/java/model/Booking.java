package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

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
