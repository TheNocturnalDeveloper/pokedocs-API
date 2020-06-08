package nl.fhict.s4.pokedocs;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import io.restassured.http.Cookies;
import nl.fhict.s4.pokedocs.dal.User;
import nl.fhict.s4.pokedocs.presentation.services.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@QuarkusTest
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
public class UserTest {

    @Inject UserService userService;

    User user;

    @BeforeEach
    public void initDB() {
        user = User.add("user", "password", "User");
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
    public void registerUser() {
        Response result = userService.Register("user2", "test");
        User ResultValue = (User)result.getEntity();
        
        assertEquals(201, result.getStatus());
        assertEquals(ResultValue.role, "User");
        assertEquals(ResultValue.username, "user2");
    }


    @Test
    public void registerUserUsernameTaken() {
        Response result = userService.Register(user.username, "test");
        
        assertEquals(409, result.getStatus());           
    }

    @Test
    public void loginUser() throws Exception {
        Response result = userService.login(user.username, "password");
        
        assertEquals(204, result.getStatus());      
    }

    @Test
    public void loginUserIncorrectCredentials() throws Exception {
        Response result = userService.login("", "");
        
        assertEquals(401, result.getStatus()); 
    }


}