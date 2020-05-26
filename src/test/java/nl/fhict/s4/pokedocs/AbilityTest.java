package nl.fhict.s4.pokedocs;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import nl.fhict.s4.pokedocs.dal.Ability;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Transactional
public class AbilityTest {

    Ability ability;
    
    @BeforeEach
    public void initDB() {   
        ability = Ability.addAbility("sturdy", "pokemon does not go down after one hit");
    }

    @AfterEach
    public void resetDB() {
        Ability.deleteAll();
    }

    @Test
    public void addAbility() {
        Ability result = given()
        .when()
        .urlEncodingEnabled(true)
        .param("name", "Adaptability")
        .param("description", "Powers up moves of the same type as the Pokémon.")
        .post("/abilities/").then()
        .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getObject("$", Ability.class);

            assertEquals(result.name, "Adaptability");
    }

    @Test
    public void addAbilityNameExists() {
        given()
            .when()
            .urlEncodingEnabled(true)
            .param("name", "sturdy")
            .param("description", "pokemon does not go down after one hit")
            .post("/abilities/").then()
            .statusCode(409);
    }

    @Test
    public void getAllAbilities() {
        int abilityCount = given()
        .when()
            .get("/abilities/")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getList("$")
            .size();

        assertEquals(abilityCount, 1);
    }

    @Test
    public void deleteAbility() {
        given().delete("/abilities/" + ability.id)
        .then()
            .statusCode(204);
    }

    @Test
    public void getAbility() {
        Ability result = given()
        .when()
        .get("/abilities/" + ability.id).then()
        .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getObject("$", Ability.class);
    
        assertEquals(result.name, "sturdy");
    }

    @Test
    public void getAbilityNotFound() {
        given()
            .when()
            .get("/abilities/" + -1).then()
            .statusCode(404);

    }
}