package praktikum.testing;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Test;
import java.util.HashMap;
import java.util.UUID;

import praktikum.testing.api.Auth;

import static org.hamcrest.Matchers.*;

public class AuthRegisterTests {

    @Test
    @DisplayName("Создать уникального пользователя")
    public void authRegisterUnique() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);

    }

    @Test
    @DisplayName("Cоздать пользователя, который уже зарегистрирован")
    public void authRegisterUserAlreadyExists() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        Response secondRes = Auth.register(new JSONObject(authRegisterBody));
        secondRes.then().statusCode(403).and().body("message", equalTo("User already exists"));

        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить одно из обязательных полей")
    public void authRegisterNotAllRequiredFields() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
