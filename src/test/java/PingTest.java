import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class PingTest extends BaseTest {

    @Test
    @DisplayName("Health check")
    void healthCheckTest() {
        Response response = given()
                .get("/ping")
                .then().statusCode(201).extract().response();
        Assertions.assertEquals("Created", response.body().asString());
    }
}
