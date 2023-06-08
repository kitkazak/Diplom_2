package praktikum.testing;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Test;
import java.util.HashMap;
import java.util.UUID;

import praktikum.testing.api.Auth;

import static org.hamcrest.Matchers.*;

public class UpdateUserInfoTests {

    @Test
    @DisplayName("Обновить имя пользователя (с авторизацией)")
    public void updateNameBeingAuthorized() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        HashMap<String, Object> updateBody = new HashMap<>();
        updateBody.put("name", "kitkazak_newName" + uuid.toString());
        Response updateRes = Auth.update(new JSONObject(updateBody), res.jsonPath().get("accessToken"));
        updateRes.then().statusCode(200);

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);
    }

    @Test
    @DisplayName("Обновить имя пользователя (без авторизации)")
    public void updateNameNotBeingAuthorized() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        HashMap<String, Object> updateBody = new HashMap<>();
        updateBody.put("name", "kitkazak_newName" + uuid.toString());
        Response updateRes = Auth.update(new JSONObject(updateBody), "");
        updateRes.then().statusCode(401).and().body("message", equalTo("You should be authorised"));

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);
    }

    @Test
    @DisplayName("Обновить пароль пользователя (с авторизацией)")
    public void updatePasswordBeingAuthorized() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        HashMap<String, Object> updateBody = new HashMap<>();
        updateBody.put("password", "kitkazak_newPassword" + uuid.toString());
        Response updateRes = Auth.update(new JSONObject(updateBody), res.jsonPath().get("accessToken"));
        updateRes.then().statusCode(200);

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);
    }

    @Test
    @DisplayName("Обновить пароль пользователя (без авторизации)")
    public void updatePasswordNotBeingAuthorized() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        HashMap<String, Object> updateBody = new HashMap<>();
        updateBody.put("password", "kitkazak_newPassword" + uuid.toString());
        Response updateRes = Auth.update(new JSONObject(updateBody), "");
        updateRes.then().statusCode(401).and().body("message", equalTo("You should be authorised"));

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);

    }

    @Test
    @DisplayName("Обновить email пользователя (с авторизацией)")
    public void updateEmailBeingAuthorized() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        HashMap<String, Object> updateBody = new HashMap<>();
        updateBody.put("email", "kitkazak_newEmail" + uuid.toString());
        Response updateRes = Auth.update(new JSONObject(updateBody), res.jsonPath().get("accessToken"));
        updateRes.then().statusCode(200);

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);
    }

    @Test
    @DisplayName("Обновить email пользователя (без авторизации)")
    public void updateEmailNotBeingAuthorized() {
        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> authRegisterBody = new HashMap<>();
        authRegisterBody.put("email", "kitkazak_email" + uuid.toString() + "@yandex.ru");
        authRegisterBody.put("password", "kitkazak_password" + uuid.toString());
        authRegisterBody.put("name", "kitkazak_name" + uuid.toString());

        Response res = Auth.register(new JSONObject(authRegisterBody));
        res.then().statusCode(200);

        HashMap<String, Object> updateBody = new HashMap<>();
        updateBody.put("email", "kitkazak_newEmail" + uuid.toString()  + "@yandex.ru");
        Response updateRes = Auth.update(new JSONObject(updateBody), "");
        updateRes.then().statusCode(401).and().body("message", equalTo("You should be authorised"));

        // Delete user
        Response deleteRes = Auth.delete(res.jsonPath().get("accessToken"));
        deleteRes.then().statusCode(202);
    }
}
