package nl.fhict.s4.pokedocs;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import nl.fhict.s4.pokedocs.dal.Type;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Transactional
public class TypeTest {

    Type type;

    @BeforeEach
    public void initDB() {  
        type = Type.addType("grass");
    }

    @AfterEach
    public void resetDB() {
        Type.deleteAll();
    }

    @Test
    public void addType() {
        Type result = given()
        .when()
        .urlEncodingEnabled(true)
        .param("name", "poison")
        .post("/types").then()
        .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getObject("$", Type.class);

            assertEquals(result.name, "poison");
    }


    @Test
    public void addTypeNameExists() {
        given()
            .when()
            .urlEncodingEnabled(true)
            .param("name", "grass")
            .post("/types/").then()
            .statusCode(409);
    }


    @Test
    public void getAllTypes() {
        int typeCount = given()
        .when()
            .get("/types")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getList("$")
            .size();

        assertEquals(typeCount, 1);    
    }

    @Test
    public void deleteType() {
        given().delete("/types/" + type.id)
        .then()
            .statusCode(204);
    }

    @Test
    public void getType() {
        Type result = given()
        .when()
        .get("/types/" + type.id).then()
        .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getObject("$", Type.class);
        
        assertEquals(result.name, "grass");
    }

    @Test
    public void getTypeNotFound() {
      given()
        .when()
        .get("/types/" + -1).then()
        .statusCode(404);

    }
}