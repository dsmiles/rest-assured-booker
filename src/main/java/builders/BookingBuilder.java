package utils;

import lombok.Builder;
import model.Booking;
import model.BookingDates;
import net.datafaker.Faker;


@Builder
public class TestBooking {
    private static final Faker faker = new Faker();
    private static String firstname = faker.name().firstName();
    private static String lastname = faker.name().lastName();
    private static int totalPrice = faker.number().numberBetween(100, 1000);
    private static boolean depositPaid = faker.random().nextBoolean();
    private static String checkin = faker.expression("#{date.past '30', 'DAYS', 'YYYY-MM-DD'}");
    private static String checkout = faker.expression("#{date.future '30', 'DAYS', 'YYYY-MM-DD'}");
    private static String additionalNeeds = faker.restaurant().description();

    public static Booking aBooking() {
        BookingDates bookingDates = new BookingDates(checkin, checkout);

        return Booking.builder()
            .firstname(firstname)
            .lastname(lastname)
            .totalPrice(totalPrice)
            .depositPaid(depositPaid)
            .bookingDates(bookingDates)
            .additionalNeeds(additionalNeeds)
            .build();
    }

    public Booking build() {
        BookingDates bookingDates = BookingDates.builder().checkin(checkin).checkout(checkout).build();
        return new Booking(firstname,lastname,totalPrice,depositPaid,bookingDates,additionalNeeds);
    }
}
