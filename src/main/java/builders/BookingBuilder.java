package builders;

import model.Booking;
import model.BookingDates;
import net.datafaker.Faker;

/**
 * A builder class for creating Booking objects with random or customized data.
 */
public class BookingBuilder {

    /**
     * Initialise the DataFaker
     */
    private final Faker faker = new Faker();

    /**
     * Default values initialized using Faker
     */
    private String firstname = faker.name().firstName();
    private String lastname = faker.name().lastName();
    private int totalPrice = faker.number().numberBetween(100, 1000);
    private boolean depositPaid = faker.random().nextBoolean();
    private String checkin = faker.expression("#{date.past '10', 'DAYS', 'YYYY-MM-dd'}");
    private String checkout = faker.expression("#{date.future '30', 'DAYS', 'YYYY-MM-dd'}");
    private String additionalNeeds = faker.expression("#{options.option 'Breakfast', 'Lunch', 'Dinner', 'Accessible Room', ''}");

    /**
     * Sets the first name for the booking.
     *
     * @param firstname The first name of the customer.
     * @return This BookingBuilder object.
     */
    public BookingBuilder withFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    /**
     * Sets the last name for the booking.
     *
     * @param lastname The last name of the customer.
     * @return This BookingBuilder object.
     */
    public BookingBuilder withLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    /**
     * Sets the check-in date for the booking.
     *
     * @param checkin The check-in date in the format 'YYYY-MM-dd'.
     * @return This BookingBuilder object.
     */
    public BookingBuilder withCheckin(String checkin) {
        this.checkin = checkin;
        return this;
    }

    /**
     * Sets the check-out date for the booking.
     *
     * @param checkout The check-out date in the format 'YYYY-MM-dd'.
     * @return This BookingBuilder object.
     */
    public BookingBuilder withCheckout(String checkout) {
        this.checkout = checkout;
        return this;
    }

    /**
     * Builds a Booking object using the provided or default values.
     *
     * @return A Booking object.
     */
    public Booking build() {
        BookingDates bookingDates = new BookingDates(checkin, checkout);
        return new Booking(firstname, lastname, totalPrice, depositPaid, bookingDates, additionalNeeds);
    }
}
