package utils;

import model.BookingDates;
import model.Booking;
import net.datafaker.Faker;

import java.util.HashMap;
import java.util.Map;

public class TestBooking {

    private static final Faker faker = new Faker();

    private Booking booking =

    public enum Type {
        SALLY,
        JOHN,
        JANE,
        MARK
        // Add more booking types as needed
    }

    private static final Map<Type, Booking> bookingMap = new HashMap<>();

    // Static block to populate the map with booking data
    static {
        bookingMap.put(Type.SALLY, setBooking("Sally", "Smith", 612, false, "2021-02-24", "2023-09-04", ""));
        bookingMap.put(Type.JOHN, setBooking("John", "Smith", 101, true, "2024-02-23", "2024-02-25", "Breakfast required"));
        bookingMap.put(Type.JANE, setBooking("Jane", "Doe", 230, false, "2024-04-23", "2024-04-25", "New towels"));
        bookingMap.put(Type.MARK, setBooking("Mark", "Twain", 200, false, "2024-06-01", "2024-06-10", "Extra fruit"));
        // Add more bookings as needed
    }


    /**
     * Creates a {@link Booking} object with the provided data
     *
     * @param firstname       First name of the guest
     * @param lastname        Last name of the guest
     * @param totalPrice      Total price for the booking
     * @param depositPaid     Whether the deposit has been paid or not
     * @param checkin         Check-in date
     * @param checkout        Check-out date
     * @param additionalNeeds Any additional needs the guest has
     * @return a {@link Booking} object
     */
    private static Booking setBooking(String firstname, String lastname, int totalPrice, boolean depositPaid,
                                      String checkin, String checkout, String additionalNeeds) {
        BookingDates bookingDates = BookingDates.builder()
            .checkin(checkin)
            .checkout(checkout)
            .build();

        return Booking.builder()
            .firstname(firstname)
            .lastname(lastname)
            .totalPrice(totalPrice)
            .depositPaid(depositPaid)
            .bookingDates(bookingDates)
            .additionalNeeds(additionalNeeds)
            .build();
    }

    /**
     * Creates a {@link Booking} object for the specified booking type
     *
     * @param bookingType The type of booking to create
     * @return a {@link Booking} object
     * @throws IllegalArgumentException if an invalid booking type is provided
     */
    public static Booking get(Type bookingType) {
        Booking booking = bookingMap.get(bookingType);
        if (booking == null) {
            throw new IllegalArgumentException("Invalid booking type");
        }
        return booking;
    }


    public

}
