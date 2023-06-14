package praktikum.testing.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import praktikum.testing.paths.Paths;

import java.util.Objects;

public class Auth {
    public static Response register(JSONObject body) {
        return RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body.toJSONString())
                .when().post(Paths.AUTH_REGISTER);
    }

    public static Response login(JSONObject body) {
        return RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body.toJSONString())
                .when().post(Paths.AUTH_LOGIN);
    }

    public static Response delete(String token) {
        return RestAssured
                .given()
                .header("Authorization", token)
                .when().delete(Paths.AUTH_USER);
    }

    public static Response update(JSONObject body, String token) {
        if (Objects.equals(token, "")) {
            return RestAssured
                    .given()
                    .header("Content-Type", "application/json")
                    .body(body.toJSONString())
                    .when().patch(Paths.AUTH_USER);
        } else {
            return RestAssured
                    .given()
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .body(body.toJSONString())
                    .when().patch(Paths.AUTH_USER);
        }

    }
}
