package nl.fhict.s4.pokedocs;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import nl.fhict.s4.pokedocs.dal.Move;
import nl.fhict.s4.pokedocs.dal.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Transactional
public class MoveTest {

    Type grassType;
    Move move;

    @BeforeEach
    public void initDB() {
        grassType = Type.addType("grass");
        move = Move.addMove("razor leaf", "cuts the opponent using a sharp leaf", grassType);
    }

    @AfterEach
    public void resetDB() {
        Move.deleteAll();
        Type.deleteAll();
    }

    @Test
    public void addMove() {
        Move result = given()
            .when()
            .urlEncodingEnabled(true)
            .param("name", "leafstorm")
            .param("description", "shoots a storm of leaves at the opponent")
            .param("type", grassType.id)
            .post("/moves").then()
            .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getObject("$", Move.class);

        assertEquals(result.name, "leafstorm");
    }

    @Test
    public void addMoveNameExists() {
        given()
            .when()
            .urlEncodingEnabled(true)
            .param("name", "razor leaf")
            .param("description", "cuts the opponent using a sharp leaf")
            .param("type", grassType.id)
            .post("/moves").then()
            .statusCode(409);
    }
    

    @Test
    public void deleteMove() {
        given()
            .delete("/moves/" + move.id)
            .then()
                .statusCode(204);
    }

    @Test
    public void getAllMoves() {
        int moveCount = given()
            .when()
                .get("/moves")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getList("$")
                .size();

        assertEquals(moveCount, 1);    
    }

    @Test
    public void getMove() {
        Move result = given()
            .when()
            .get("/moves/" + move.id).then()
            .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getObject("$", Move.class);
        
        assertEquals(result.name, "razor leaf");
    }

    @Test
    public void getMoveNotFound() {
        given()
            .when()
            .get("/moves/" + -1).then()
            .statusCode(404);
    }
}