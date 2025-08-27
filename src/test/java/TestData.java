import lombok.Getter;
import models.BookingDates;

public class TestData {
    @Getter
    private static final String baseUrl = "https://restful-booker.herokuapp.com";

    @Getter
    private static final String defaultFirstName = "John";

    @Getter
    private static final String defaultLastName = "Testovich";

    @Getter
    private static final int defaultTotalPrice = 300;

    @Getter
    private static final BookingDates defaultBookingDates = new BookingDates("2025-09-01", "2025-09-05");

    @Getter
    private static final String additionalNeeds = "Breakfast";

    @Getter
    private static final String authLogin = "admin";

    @Getter
    private static final String authPassword = "password123";
}
