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

public class UpdateUserInfoTests {

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
    @DisplayName("Обновить имя пользователя (с авторизацией)")
    public void updateNameBeingAuthorized() {
        HashMap<String, Object> updateBody = new HashMap<>();
        updateBody.put("name", "kitkazak_newName" + uuid.toString());
        Response updateRes = Auth.update(new JSONObject(updateBody), accessToken);
        updateRes.then().statusCode(200);
    }

    @Test
    @DisplayName("Обновить имя пользователя (без авторизации)")
    public void updateNameNotBeingAuthorized() {
        HashMap<String, Object> updateBody = new HashMap<>();
        updateBody.put("name", "kitkazak_newName" + uuid.toString());
        Response updateRes = Auth.update(new JSONObject(updateBody), "");
        updateRes.then().statusCode(401).and().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Обновить пароль пользователя (с авторизацией)")
    public void updatePasswordBeingAuthorized() {
        HashMap<String, Object> updateBody = new HashMap<>();
        updateBody.put("password", "kitkazak_newPassword" + uuid.toString());
        Response updateRes = Auth.update(new JSONObject(updateBody), accessToken);
        updateRes.then().statusCode(200);
    }

    @Test
    @DisplayName("Обновить пароль пользователя (без авторизации)")
    public void updatePasswordNotBeingAuthorized() {
        HashMap<String, Object> updateBody = new HashMap<>();
        updateBody.put("password", "kitkazak_newPassword" + uuid.toString());
        Response updateRes = Auth.update(new JSONObject(updateBody), "");
        updateRes.then().statusCode(401).and().body("message", equalTo("You should be authorised"));;
    }

    @Test
    @DisplayName("Обновить email пользователя (с авторизацией)")
    public void updateEmailBeingAuthorized() {
        HashMap<String, Object> updateBody = new HashMap<>();
        updateBody.put("email", "kitkazak_newEmail" + uuid.toString());
        Response updateRes = Auth.update(new JSONObject(updateBody), accessToken);
        updateRes.then().statusCode(200);
    }

    @Test
    @DisplayName("Обновить email пользователя (без авторизации)")
    public void updateEmailNotBeingAuthorized() {
        HashMap<String, Object> updateBody = new HashMap<>();
        updateBody.put("email", "kitkazak_newEmail" + uuid.toString()  + "@yandex.ru");
        Response updateRes = Auth.update(new JSONObject(updateBody), "");
        updateRes.then().statusCode(401).and().body("message", equalTo("You should be authorised"));
    }
}
