package praktikum.testing;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import java.util.HashMap;
import java.util.UUID;

import praktikum.testing.api.Auth;
import praktikum.testing.api.Orders;

import static org.hamcrest.Matchers.*;

public class GetUserOrdersTests {

    @Test
    @DisplayName("Получить заказы пользователя (с авторизацией)")
    public void getUserOrdersBeingAuthorized() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        Response getOrderesRes = Orders.getUserOrders(res.jsonPath().get("accessToken"));
        getOrderesRes.then().statusCode(200);

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);
    }

    @Test
    @DisplayName("Получить заказы пользователя (без авторизации)")
    public void getUserOrdersNotBeingAuthorized() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        Response getOrderesRes = Orders.getUserOrders("");
        getOrderesRes.then().statusCode(401).and().body("message", equalTo("You should be authorised"));

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);
    }
}
