package praktikum.testing;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.UUID;

import praktikum.testing.api.Auth;

import static org.hamcrest.Matchers.*;

public class AuthRegisterTests {

    UUID uuid;
    String accessToken;
    JSONObject registerBody;
    Response registerResponse;

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
    }

    @After
    public void tearDown() {
        Response deleteResponse = Auth.delete(accessToken);
        deleteResponse.then().statusCode(202);
    }

    @Test
    @DisplayName("Cоздать пользователя, который уже зарегистрирован")
    public void authRegisterUserAlreadyExists() {
        Response secondRes = Auth.register(new JSONObject(registerBody));
        secondRes.then().statusCode(403).and().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить одно из обязательных полей")
    public void authRegisterNotAllRequiredFields() {
        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "notallfields_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("name", "notallfields_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
