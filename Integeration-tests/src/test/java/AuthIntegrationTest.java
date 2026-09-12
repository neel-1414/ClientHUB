import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {
    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://localhost:4004";
    }
    @Test
    public void ShouldReturnOkWithValidToken() {
        //1. Arrange the required data
        //2. Perform the function required
        //3. Analyze the result
        String LoginPayload = """
                {
                    "email": "testuser@test.com",
                    "password": "password123"
                }
                """; // defining the properties to send in test run

        Response response = given()
                .contentType("application/json")
                .body(LoginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("jwtToken", notNullValue())
                .extract()
                .response();

        System.out.println("Generated Token: "+response.jsonPath().getString("token"));
    }

    @Test
    public void ShouldReturnUnauthorizedWithInvalidLogin() {
        //1. Arrange the required data
        //2. Perform the function required
        //3. Analyze the result
        String LoginPayload = """
                {
                    "email": "invalid_user@test.com",
                    "password": "wrong_pass"
                }
                """; // defining the properties to send in test run

             given()
                .contentType("application/json")
                .body(LoginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);

    }
}
