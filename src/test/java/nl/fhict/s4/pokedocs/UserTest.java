package nl.fhict.s4.pokedocs;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import io.restassured.http.Cookies;
import nl.fhict.s4.pokedocs.dal.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@QuarkusTest
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
public class UserTest {


    @BeforeEach
    public void initDB() {
        User.add("user", "password", "User");
    }

    @AfterEach
    public void resetDB() {
        User.deleteAll();
    }


    @Test
    public void checkRoles() {

        Cookies cookies = given()
        .when()
        .urlEncodingEnabled(true)
        .param("username", "user")
        .param("password", "password")
        .post("/users/login").then()
        .statusCode(204)
            .extract()
            .response()
            .getDetailedCookies();

       List<String> result = given()
            .cookies(cookies)
            .when()
                .get("users/roles")
        .then()
        .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getList("$", String.class);
                
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), "User");
    }



    @Test
    public void RegisterUser() {
        User result = given()
            .when()
            .urlEncodingEnabled(true)
            .param("username", "user2")
            .param("password", "test")
            .post("/users/register").then()
            .statusCode(201)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getObject("$", User.class);

        assertEquals(result.role, "User");
        assertEquals(result.username, "user2");
    }


    @Test
    public void RegisterUserUsernameTaken() {
       given()
            .when()
            .urlEncodingEnabled(true)
            .param("username", "user")
            .param("password", "test")
            .post("/users/register").then()
            .statusCode(409);
                
    }

    @Test
    public void loginUser() {  
        given()
            .when()
            .urlEncodingEnabled(true)
            .param("username", "user")
            .param("password", "password")
            .post("/users/login").then()
            .statusCode(204);
    }

    @Test
    public void loginUserIncorrectCredentials() {
        given()
            .when()
            .urlEncodingEnabled(true)
            .param("username", "user")
            .param("password", "")
            .post("/users/login").then()
            .statusCode(401);
    }


}