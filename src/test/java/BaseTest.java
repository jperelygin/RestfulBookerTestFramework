import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Booking;

import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class BaseTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = TestData.getBaseUrl();
    }

    protected Response authRequest(String login, String password) {
        String body = String.format("{ \"username\": \"%s\", \"password\": \"%s\" }", login, password);
        return given().contentType(ContentType.JSON).body(body).post("/auth").then().extract().response();
    }

    protected String getAuthToken(Response response) {
        return response.path("token");
    }

    protected int createBookingAndGetId(Booking booking) {
        return given().contentType(ContentType.JSON).body(booking)
                .when().post("/booking")
                .then().statusCode(200).extract().path("bookingid");
    }

    protected Booking createDefaultBooking() {
        return new Booking(
                TestData.getDefaultFirstName(),
                TestData.getDefaultLastName(),
                TestData.getDefaultTotalPrice(),
                true,
                TestData.getDefaultBookingDates(),
                TestData.getAdditionalNeeds()
        );
    }
}
