package nl.fhict.s4.pokedocs;

import io.quarkus.test.junit.QuarkusTest;
import nl.fhict.s4.pokedocs.dal.Move;
import nl.fhict.s4.pokedocs.dal.Type;
import nl.fhict.s4.pokedocs.presentation.services.MoveService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Transactional
public class MoveTest {

    @Inject MoveService moveService;

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

        Response result = moveService.addMove("leafstorm", "shoots a storm of leaves at the opponent", grassType.id);
        Move resultValue = (Move)result.getEntity();

        assertEquals(200, result.getStatus());
        assertEquals("leafstorm", resultValue.name);
    }

    @Test
    public void addMoveNameExists() {
        Response result = moveService.addMove("razor leaf", "", grassType.id);

        assertEquals(409, result.getStatus());
    }
    

    @Test
    public void deleteMove() {
        Response result = moveService.deleteMove(move.id);

        assertEquals(204, result.getStatus());
    }

    @Test
    public void getAllMoves() {
        Response result = moveService.getAllMoves();
        List<?> resultValue = (List<?>)result.getEntity();

        assertEquals(200, result.getStatus());
        assertEquals(1, resultValue.size());
    }

    @Test
    public void getMove() {
        Response result = moveService.getMove(move.id);
        Move resultValue = (Move)result.getEntity();
        
        assertEquals(200, result.getStatus());
        assertEquals(resultValue.name, "razor leaf");
    }

    @Test
    public void getMoveNotFound() {
        Response result = moveService.getMove((long) -1);
        
        assertEquals(404, result.getStatus());
    }
}