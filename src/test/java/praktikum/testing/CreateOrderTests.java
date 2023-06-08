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

public class CreateOrderTests {
    @Test
    @DisplayName("Создание заказа с ингридентами и авторизацией")
    public void createOrderBeingAuthorized() {
        UUID uuid = UUID.randomUUID();

        // Create user
        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        // Create order
        HashMap<String, Object> createOrderBody = new HashMap<>();
        JSONArray ings = new JSONArray();

        // Времени очень мало осталось, поэтому решил захаркодить значения
        ings.add("61c0c5a71d1f82001bdaaa6d");
        ings.add("61c0c5a71d1f82001bdaaa6f");
        createOrderBody.put("ingredients", ings);

        Response createOrderRes = Orders.create(new JSONObject(createOrderBody), res.jsonPath().get("accessToken"));
        createOrderRes.then().statusCode(200);

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);
    }

    // Я долго боролся с этим тестом, не понимаю почему он не проходит
    // Выполняю запрос на создание заказа без accessToken в хедере
    // Итог: 200, хотя должен быть 401
    @Test
    @DisplayName("Создание заказа с ингридентами и без авторизации")
    public void createOrderNotBeingAuthorized() {
        UUID uuid = UUID.randomUUID();

        // Create user
        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        // Create order
        HashMap<String, Object> createOrderBody = new HashMap<>();
        JSONArray ings = new JSONArray();

        // Времени очень мало осталось, поэтому решил захаркодить значения
        ings.add("61c0c5a71d1f82001bdaaa6d");
        ings.add("61c0c5a71d1f82001bdaaa6f");
        createOrderBody.put("ingredients", ings);

        Response createOrderRes = Orders.create(new JSONObject(createOrderBody), "");
        createOrderRes.then().statusCode(401).and().body("message", equalTo("You should be authorised"));

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        UUID uuid = UUID.randomUUID();

        // Create user
        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        // Create order
        HashMap<String, Object> createOrderBody = new HashMap<>();
        JSONArray ings = new JSONArray();
        createOrderBody.put("ingredients", ings);

        Response createOrderRes = Orders.create(new JSONObject(createOrderBody), res.jsonPath().get("accessToken"));
        createOrderRes.then().statusCode(400).and().body("message", equalTo("Ingredient ids must be provided"));

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);
    }

    @Test
    @DisplayName("Создание заказа c неверным хэшем ингредиентов")
    public void createOrderInvalidIngredientsHash() {
        UUID uuid = UUID.randomUUID();

        // Create user
        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        // Create order
        HashMap<String, Object> createOrderBody = new HashMap<>();
        JSONArray ings = new JSONArray();
        ings.add("someDummyText");
        createOrderBody.put("ingredients", ings);

        Response createOrderRes = Orders.create(new JSONObject(createOrderBody), res.jsonPath().get("accessToken"));
        createOrderRes.then().statusCode(500);

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);
    }
}
