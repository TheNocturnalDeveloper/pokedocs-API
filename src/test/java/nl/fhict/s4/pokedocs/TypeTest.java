package nl.fhict.s4.pokedocs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import io.quarkus.test.junit.QuarkusTest;
import nl.fhict.s4.pokedocs.dal.Type;
import nl.fhict.s4.pokedocs.presentation.services.TypeService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Transactional
public class TypeTest {

    Type type;

    @Inject TypeService typeService;

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
        Response  result = typeService.addType("poison");
        Type resultValue = (Type)result.getEntity();

        assertEquals("poison", resultValue.name);
        assertEquals(200, result.getStatus());
    }


    @Test
    public void addTypeNameExists() {
        Response  result = typeService.addType(type.name);
   
        assertEquals(409, result.getStatus());
    }


    @Test
    public void getAllTypes() {
        Response  result = typeService.getAllTypes();
        List<?> resultValue = (List<?>)result.getEntity();

        assertEquals(1, resultValue.size());
        assertEquals(200, result.getStatus()); 
    }

    @Test
    public void deleteType() {
        Response  result = typeService.deleteType(type.id);
        assertEquals(204, result.getStatus());
    }

    @Test
    public void getType() {
        Response  result = typeService.getType(type.id);
        Type resultValue = (Type)result.getEntity();

        assertEquals(type.name, resultValue.name);
        assertEquals(200, result.getStatus());
    }

    @Test
    public void getTypeNotFound() {
        Response  result = typeService.getType(Long.valueOf(-1));
        assertEquals(404, result.getStatus());
    }
}