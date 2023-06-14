package praktikum.testing.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import praktikum.testing.paths.Paths;

import java.util.Objects;

public class Orders {
    public static Response create(JSONObject body, String token) {
        if (Objects.equals(token, "")) {
            return RestAssured
                    .given()
                    .header("Content-Type", "application/json")
                    .body(body.toJSONString())
                    .when().post(Paths.ORDERS);
        } else {
            return RestAssured
                    .given()
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .body(body.toJSONString())
                    .when().post(Paths.ORDERS);
        }
    }

    public static Response getUserOrders(String token) {
        if (Objects.equals(token, "")) {
            return RestAssured
                    .when().get(Paths.ORDERS);
        } else {
            return RestAssured
                    .given()
                    .header("Authorization", token)
                    .when().get(Paths.ORDERS);
        }
    }
}
