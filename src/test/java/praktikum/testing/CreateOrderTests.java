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

public class CreateOrderTests {

    UUID uuid;
    String accessToken;
    JSONObject registerBody;
    Response registerResponse;

    HashMap<String, Object> createOrderBody;
    JSONArray ings;

    // В before проверяется создание уникального пользователя
    // Поэтому тестов стало на один меньше
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

        // Create order
        createOrderBody = new HashMap<>();
        ings = new JSONArray();
    }

    @After
    public void tearDown() {
        Response deleteResponse = Auth.delete(accessToken);
        deleteResponse.then().statusCode(202);
    }

    @Test
    @DisplayName("Создание заказа с ингридентами и авторизацией")
    public void createOrderBeingAuthorized() {
        // Времени очень мало осталось, поэтому решил захаркодить значения
        ings.add("61c0c5a71d1f82001bdaaa6d");
        ings.add("61c0c5a71d1f82001bdaaa6f");
        createOrderBody.put("ingredients", ings);

        Response createOrderRes = Orders.create(new JSONObject(createOrderBody), accessToken);
        createOrderRes.then().statusCode(200);
    }

    // Выполняю запрос на создание заказа без accessToken в хедере
    // Итог: 200, хотя должен быть 401
    @Test
    @DisplayName("Создание заказа с ингридентами и без авторизации")
    public void createOrderNotBeingAuthorized() {
        // Времени очень мало осталось, поэтому решил захаркодить значения
        ings.add("61c0c5a71d1f82001bdaaa6d");
        ings.add("61c0c5a71d1f82001bdaaa6f");
        createOrderBody.put("ingredients", ings);

        Response createOrderRes = Orders.create(new JSONObject(createOrderBody), "");
        createOrderRes.then().statusCode(401).and().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        createOrderBody.put("ingredients", ings);

        Response createOrderRes = Orders.create(new JSONObject(createOrderBody), accessToken);
        createOrderRes.then().statusCode(400).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа c неверным хэшем ингредиентов")
    public void createOrderInvalidIngredientsHash() {
        ings.add("someDummyText");
        createOrderBody.put("ingredients", ings);

        Response createOrderRes = Orders.create(new JSONObject(createOrderBody), accessToken);
        createOrderRes.then().statusCode(500);
    }
}
