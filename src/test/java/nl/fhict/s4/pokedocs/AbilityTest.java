package nl.fhict.s4.pokedocs;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import io.quarkus.test.junit.QuarkusTest;
import nl.fhict.s4.pokedocs.dal.Ability;
import nl.fhict.s4.pokedocs.presentation.services.AbilityService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Transactional
public class AbilityTest {

    @Inject AbilityService abilityService;

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
        Response result = abilityService.addAbility("Adaptability", "Powers up moves of the same type as the Pokémon.");
        Ability resultValue = (Ability)result.getEntity();

        assertEquals(resultValue.name, "Adaptability");
        assertEquals(200, result.getStatus());
    }

    @Test
    public void addAbilityNameExists() {
        Response result = abilityService.addAbility("sturdy", "pokemon does not go down after one hit");

        assertEquals(409, result.getStatus()); 
    }

    @Test
    public void getAllAbilities() {
        Response result = abilityService.getAllAbilities();
        List<?> resultValue = (List<?>)result.getEntity();
        
        assertEquals(resultValue.size(), 1);
    }

    @Test
    public void deleteAbility() {
        Response result = abilityService.deleteAbility(ability.id);

        assertEquals(204, result.getStatus()); 
    }

    @Test
    public void getAbility() {
        Response result = abilityService.getAbility(ability.id);
        Ability resultValue = (Ability)result.getEntity();

        assertEquals(resultValue.name, "sturdy");
        assertEquals(200, result.getStatus());
    }

    @Test
    public void getAbilityNotFound() {
        Response result = abilityService.getAbility((long) -1);

        assertEquals(404, result.getStatus());
    }
}