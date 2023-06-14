package praktikum.testing;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.UUID;

import praktikum.testing.api.Auth;
import praktikum.testing.api.Orders;

import static org.hamcrest.Matchers.*;

public class GetUserOrdersTests {

    UUID uuid;
    String accessToken;
    JSONObject registerBody;
    Response registerResponse;

    @Before
    public void setUp() {
        uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());
        registerBody = new JSONObject(authRegisterBody);

        registerResponse = Auth.register(registerBody);
        accessToken = registerResponse.jsonPath().get("accessToken");
        registerResponse.then().statusCode(200);
    }

    @After
    public void tearDown() {
        Response deleteResponse = Auth.delete(accessToken);
        deleteResponse.then().statusCode(202);
    }

    @Test
    @DisplayName("Получить заказы пользователя (с авторизацией)")
    public void getUserOrdersBeingAuthorized() {
        Response getOrderesRes = Orders.getUserOrders(accessToken);
        getOrderesRes.then().statusCode(200);
    }

    @Test
    @DisplayName("Получить заказы пользователя (без авторизации)")
    public void getUserOrdersNotBeingAuthorized() {
        Response getOrderesRes = Orders.getUserOrders("");
        getOrderesRes.then().statusCode(401).and().body("message", equalTo("You should be authorised"));

    }
}
