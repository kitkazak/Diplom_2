package praktikum.testing;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Test;
import java.util.HashMap;
import java.util.UUID;

import praktikum.testing.api.Auth;

import static org.hamcrest.Matchers.*;

public class AuthLoginTests {
    @Test
    @DisplayName("Логин под существующим пользователем")
    public void authLoginExistingUser() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        // Login user
        HashMap<String, Object> loginBody = new HashMap<>();
        loginBody.put("name", "kitkazak_name" + uuid.toString());
        loginBody.put("password", "kitkazak_password" + uuid.toString());
        loginBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        Response loginRes = Auth.login(new JSONObject(loginBody));
        loginRes.then().statusCode(200);

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);

    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void authLoginInvalidCredentials() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        // Login user
        HashMap<String, Object> loginBody = new HashMap<>();
        loginBody.put("name", "kitkazak_name" + uuid.toString());
        loginBody.put("password", "iNvAlId PaSsWoRd");
        loginBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        Response loginRes = Auth.login(new JSONObject(loginBody));
        loginRes.then().statusCode(401).and().body("message", equalTo("email or password are incorrect"));

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);

    }
}
