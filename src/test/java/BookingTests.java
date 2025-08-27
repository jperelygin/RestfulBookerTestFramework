import models.Booking;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.BookingDates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class BookingTests extends BaseTest {

    @Test
    @DisplayName("Create a booking and validate response schema")
    void createBookingAndValidateSchema() throws Exception {
        Booking booking = createDefaultBooking();
        Response response = given()
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract().response();

        Assertions.assertNotNull(response.path("bookingid"));
        int totalPrice = response.path("booking.totalprice");
        boolean isDepositPaid = response.path("booking.depositpaid");
        Assertions.assertEquals(TestData.getDefaultFirstName(), response.path("booking.firstname"));
        Assertions.assertEquals(TestData.getDefaultLastName(), response.path("booking.lastname"));
        Assertions.assertEquals(TestData.getDefaultTotalPrice(), totalPrice);
        Assertions.assertTrue(isDepositPaid);
        Assertions.assertEquals(TestData.getAdditionalNeeds(), response.path("booking.additionalneeds"));
        Assertions.assertEquals(TestData.getDefaultBookingDates().getCheckin(),
                response.path("booking.bookingdates.checkin"));
        Assertions.assertEquals(TestData.getDefaultBookingDates().getCheckout(),
                response.path("booking.bookingdates.checkout"));
    }

    @Test
    @DisplayName("Get list of all bookings without any filters")
    void getBookingsWithoutFiltersTest() {
        Response response = given()
                .get("/booking")
                .then()
                .statusCode(200)
                .extract().response();
        Assertions.assertFalse(response.body().jsonPath().getList("").isEmpty());
        Assertions.assertNotNull(response.body().jsonPath().getList("").getFirst());
    }

    @Test
    @DisplayName("Get list bookings with filtering")
    void getBookingWithFiltersTest() {
        createBookingAndGetId(createDefaultBooking());
        Response response = given()
                .queryParam("firstname", TestData.getDefaultFirstName())
                .queryParam("lastname", TestData.getDefaultLastName())
                .get("/booking")
                .then()
                .statusCode(200)
                .extract().response();
        Assertions.assertFalse(response.body().jsonPath().getList("").isEmpty());
    }

    @Test
    @DisplayName("Get booking by ID")
    void getBookingById() {
        int bookingId = createBookingAndGetId(createDefaultBooking());
        Response response = given()
                .get(String.format("/booking/%d", bookingId))
                .then()
                .statusCode(200)
                .extract().response();

        int totalPrice = response.path("totalprice");
        boolean isDepositPaid = response.path("depositpaid");
        Assertions.assertEquals(TestData.getDefaultFirstName(), response.path("firstname"));
        Assertions.assertEquals(TestData.getDefaultLastName(), response.path("lastname"));
        Assertions.assertEquals(TestData.getDefaultTotalPrice(), totalPrice);
        Assertions.assertTrue(isDepositPaid);
        Assertions.assertEquals(TestData.getAdditionalNeeds(), response.path("additionalneeds"));
        Assertions.assertEquals(TestData.getDefaultBookingDates().getCheckin(),
                response.path("bookingdates.checkin"));
        Assertions.assertEquals(TestData.getDefaultBookingDates().getCheckout(),
                response.path("bookingdates.checkout"));
    }

    @Test
    @DisplayName("Update all fields in existing booking")
    void updateBookingFullTest() {
        int bookingId = createBookingAndGetId(createDefaultBooking());

        String newFirstName = "Toost";
        String newLastName = "Toostovich";
        int newTotalPrice = 666;
        boolean newDepositPaid = false;
        BookingDates newBookingDates = new BookingDates("2025-01-01", "2025-01-10");
        String newAdditionalNeeds = "Balcony";
        Booking updatedBooking = new Booking(newFirstName, newLastName, newTotalPrice,
                newDepositPaid, newBookingDates, newAdditionalNeeds);
        String authToken = getAuthToken(authRequest(TestData.getAuthLogin(), TestData.getAuthPassword()));

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Cookie", String.format("token=%s", authToken))
                .body(updatedBooking)
                .put(String.format("/booking/%d", bookingId))
                .then()
                .statusCode(200)
                .extract().response();

        int totalPrice = response.path("totalprice");
        boolean isDepositPaid = response.path("depositpaid");
        Assertions.assertEquals(newFirstName, response.path("firstname"));
        Assertions.assertEquals(newLastName, response.path("lastname"));
        Assertions.assertEquals(newTotalPrice, totalPrice);
        Assertions.assertFalse(isDepositPaid);
        Assertions.assertEquals(newAdditionalNeeds, response.path("additionalneeds"));
        Assertions.assertEquals(newBookingDates.getCheckin(),
                response.path("bookingdates.checkin"));
        Assertions.assertEquals(newBookingDates.getCheckout(),
                response.path("bookingdates.checkout"));
    }

    @Test
    @DisplayName("Update some fields of existing booking")
    void updateBookingPartiallyTest() {
        int bookingId = createBookingAndGetId(createDefaultBooking());

        String newFirstName = "Toost";
        String newAdditionalNeeds = "Jacuzzi";
        String body = String.format("{\"firstname\": \"%s\", \"additionalneeds\": \"%s\"}", newFirstName, newAdditionalNeeds);
        String authToken = getAuthToken(authRequest(TestData.getAuthLogin(), TestData.getAuthPassword()));

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Cookie", String.format("token=%s", authToken))
                .body(body)
                .patch(String.format("/booking/%d", bookingId))
                .then()
                .statusCode(200)
                .extract().response();

        int totalPrice = response.body().path("totalprice");
        Assertions.assertEquals(newFirstName, response.body().path("firstname"));
        Assertions.assertEquals(newAdditionalNeeds, response.body().path("additionalneeds"));
        Assertions.assertEquals(TestData.getDefaultLastName(), response.body().path("lastname"));
        Assertions.assertEquals(TestData.getDefaultTotalPrice(), totalPrice);
    }

    @Test
    @DisplayName("Delete existing booking")
    void deleteBookingTest() {
        int bookingId = createBookingAndGetId(createDefaultBooking());
        String authToken = getAuthToken(authRequest(TestData.getAuthLogin(), TestData.getAuthPassword()));

        given()
                .header("Cookie", String.format("token=%s", authToken))
                .delete(String.format("/booking/%d", bookingId))
                .then()
                .statusCode(201);
        given()
                .get(String.format("/booking/%d", bookingId))
                .then()
                .statusCode(404);
    }

}
