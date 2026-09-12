import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class PatientIntegrationTest {

    @BeforeAll
    static void Setup() {
        RestAssured.baseURI="http://localhost:4004";
    }

    @Test
    public void ShouldReturnPatientsWithValidToken() {
        String LoginPayload = """
                {
                    "email": "testuser@test.com",
                    "password": "password123"
                }
                """; // defining the properties to send in test run

        String token = given().contentType("application/json").body(LoginPayload).when().post("/auth/login")
                .then().statusCode(200).extract().jsonPath().get("jwtToken");

        given().header("Authorization", "Bearer " + token).when().get("/api/patients/getAllPatients").then()
                .statusCode(200).body("patients", notNullValue());

    }
}
