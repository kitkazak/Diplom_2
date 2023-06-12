package praktikum.testing;

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

public class AuthLoginTests {

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
    @DisplayName("Логин под существующим пользователем")
    public void authLoginExistingUser() {
        // Login user
        Response loginResponse = Auth.login(registerBody);
        loginResponse.then().statusCode(200);
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void authLoginInvalidCredentials() {
        // Login user
        HashMap<String, Object> loginBody = new HashMap<>();
        loginBody.put("name", "kitkazak_name" + uuid.toString());
        loginBody.put("password", "iNvAlId PaSsWoRd");
        loginBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        Response loginRes = Auth.login(new JSONObject(loginBody));
        loginRes.then().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }
}
