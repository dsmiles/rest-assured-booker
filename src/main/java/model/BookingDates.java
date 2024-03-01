package model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class BookingDates {
    @JsonFormat(pattern="yyyy-MM-dd")
    private String checkin;     // date the guest is checking in - YYYY-MM-DD

    @JsonFormat(pattern="yyyy-MM-dd")
    private String checkout;    // date the guest is checking out - YYYY-MM-DD
}
