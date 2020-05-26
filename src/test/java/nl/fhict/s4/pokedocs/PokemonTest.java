package nl.fhict.s4.pokedocs;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import nl.fhict.s4.pokedocs.dal.Pokemon;
import nl.fhict.s4.pokedocs.dal.Type;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


@QuarkusTest
@Transactional
public class PokemonTest {

    Type grassType;
    Type poisonType;

    Pokemon pokemon;


    @BeforeEach
    public void initPokemon() {

        grassType  = Type.addType("grass");
        poisonType = Type.addType("poison");

    
        pokemon = new Pokemon();
        pokemon.pokeDexEntry = 1;
        pokemon.name = "bulbasaur";
        pokemon.type = grassType;
        pokemon.secondType = poisonType;
        pokemon.persist();
    }


    @AfterEach
    public void resetDB() {
        Pokemon.deleteAll();
        Type.deleteAll();
    }


    @Test
    public void addPokemon() {
        Pokemon result = given()
        .when()
        .urlEncodingEnabled(true)
        .param("pokedexEntry", 2)
        .param("name", "Ivysaur")
        .param("typeId", grassType.id)
        .param("secondTypeId", poisonType.id)
        .post("/pokemon/").then()
        .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getObject("$", Pokemon.class);

        assertEquals(result.name, "Ivysaur");
    }



    @Test
    public void searchPokemonType() {
        Integer pokemonCount = given()
        .when()
        .queryParam("typeId", grassType.id)
            .get("/pokemon/")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getList("$")
            .size();

        assertEquals(pokemonCount, 1);
    }


    @Test
    public void searchPokemonTypeNoResult() {

        Type fireType = Type.addType("fire");

        Integer pokemonCount = given()
        .when()
        .queryParam("typeId", fireType.id)
            .get("/pokemon/")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getList("$")
            .size();

        assertEquals(pokemonCount, 0);
    }


    @Test
    public void searchPokemonSecondaryType() {
        Integer pokemonCount = given()
        .when()
        .queryParam("secondTypeId", poisonType.id)
            .get("/pokemon/")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getList("$")
            .size();

        assertEquals(pokemonCount, 1);
    }


    @Test
    public void searchPokemonSecondaryTypeNoResult() {

        Type fireType = Type.addType("fire");

        Integer pokemonCount = given()
        .when()
        .queryParam("secondTypeId", fireType.id)
            .get("/pokemon/")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getList("$")
            .size();

        assertEquals(pokemonCount, 0);
    }

    @Test
    public void searchPokemonName() {
        int pokemonCount = given()
        .when()
        .queryParam("name", "bulb")
            .get("/pokemon/")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getList("$") 
            .size();

        assertEquals(pokemonCount, 1);
    }

    @Test
    public void searchPokemonNameNoResults() {
        int pokemonCount = given()
        .when()
        .queryParam("name", "ivy")
            .get("/pokemon/")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getList("$") 
            .size();

        assertEquals(pokemonCount, 0);
    }

    @Test
    public void getPokemon() {
        Pokemon result = given()
        .when()
        .get("/pokemon/" + pokemon.pokeDexEntry).then()
        .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .response()
            .jsonPath()
            .getObject("$", Pokemon.class);
        
        assertEquals(result.name, "bulbasaur");
    }

}