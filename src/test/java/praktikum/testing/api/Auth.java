package praktikum.testing.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import praktikum.testing.paths.Paths;

public class Auth {
    public static Response register(JSONObject body) {
        return RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body.toJSONString())
                .when().post(Paths.AUTH_REGISTER);
    }
}
