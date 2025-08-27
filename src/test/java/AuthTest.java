import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthTest extends BaseTest {

    @Test
    @DisplayName("Get authorization token")
    void createTokenTest() {
        Response response = authRequest(TestData.getAuthLogin(), TestData.getAuthPassword());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(response.path("token"));
    }
}
