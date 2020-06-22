package nl.fhict.s4.pokedocs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import io.quarkus.test.junit.QuarkusTest;
import nl.fhict.s4.pokedocs.dal.Pokemon;
import nl.fhict.s4.pokedocs.dal.Type;
import nl.fhict.s4.pokedocs.presentation.services.PokemonService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


@QuarkusTest
@Transactional
public class PokemonTest {


    @Inject PokemonService pokemonService;

    Type grassType;
    Type poisonType;
    Type fireType;

    Pokemon pokemon;


    @BeforeEach
    public void initPokemon() {

        grassType  = Type.addType("grass");
        poisonType = Type.addType("poison");
        fireType   = Type.addType("fire");

        pokemon = new Pokemon();
        pokemon.pokeDexEntry = 1;
        pokemon.name = "bulbasaur";
        pokemon.type = grassType;
        pokemon.secondType = poisonType;
        pokemon.image = "image";
        pokemon.persist();
    }


    @AfterEach
    public void resetDB() {
        Pokemon.deleteAll();
        Type.deleteAll();
    }


    @Test
    public void addPokemon() {
        Response result = pokemonService.addPokemon(2, "Ivysaur", grassType.id, fireType.id, "image");
        Pokemon resultValue = (Pokemon)result.getEntity();

        assertEquals(200, result.getStatus());
        assertEquals(resultValue.name, "Ivysaur");
    }

    @Test
    public void addPokemonTypePercentageTooHigh() {
        Response result = pokemonService.addPokemon(2, "Ivysaur", grassType.id, poisonType.id, "image");

        assertEquals(403, result.getStatus());
    }


    @Test
    public void addPokemonTypePercentageTooHighReverse() {
        Response result = pokemonService.addPokemon(2, "Ivysaur", poisonType.id,  grassType.id, "image");

        assertEquals(403, result.getStatus());
    }


    @Test
    public void addPokemonTypePercentageTooHighSingle() {
        Response result = pokemonService.addPokemon(2, "Ivysaur", grassType.id,  null, "image");

        assertEquals(403, result.getStatus());
    }


    @Test
    public void searchPokemonType() {
        Response result = pokemonService.searchPokemon(null, grassType.id, null);
        List<?> resultValue = (List<?>)result.getEntity();

        assertEquals(200, result.getStatus());
        assertEquals(resultValue.size(), 1);
    }


    @Test
    public void searchPokemonTypeNoResult() {

        Response result = pokemonService.searchPokemon(null, fireType.id, null);
        List<?> resultValue = (List<?>)result.getEntity();

        assertEquals(200, result.getStatus());
        assertEquals(resultValue.size(), 0);
    }


    @Test
    public void searchPokemonSecondaryType() {

        Response result = pokemonService.searchPokemon(null, null, poisonType.id);
        List<?> resultValue = (List<?>)result.getEntity();

        assertEquals(200, result.getStatus());
        assertEquals(resultValue.size(), 1);
    }


    @Test
    public void searchPokemonSecondaryTypeNoResult() {

        Response result = pokemonService.searchPokemon(null, fireType.id, null);
        List<?> resultValue = (List<?>)result.getEntity();

        assertEquals(200, result.getStatus());
        assertEquals(resultValue.size(), 0);
    }

    @Test
    public void searchPokemonName() {

        Response result = pokemonService.searchPokemon("bulb", null, null);
        List<?> resultValue = (List<?>)result.getEntity();

        assertEquals(200, result.getStatus());
        assertEquals(resultValue.size(), 1);
    }

    @Test
    public void searchPokemonNameNoResults() {
        Response result = pokemonService.searchPokemon("ivy", null, null);
        List<?> resultValue = (List<?>)result.getEntity();

        assertEquals(200, result.getStatus());
        assertEquals(resultValue.size(), 0);
    }

    @Test
    public void getPokemon() {
        Response result = pokemonService.getPokemon(pokemon.pokeDexEntry);
        Pokemon resultValue = (Pokemon)result.getEntity();

        assertEquals(200, result.getStatus());
        assertEquals(resultValue.name, "bulbasaur");
    }

    @Test
    public void updatePokemon() {
        Response result = pokemonService.updatePokemon(pokemon.pokeDexEntry, "ivysaur",  fireType.id, fireType.id, "image2");
        Pokemon resultValue = (Pokemon)result.getEntity();

        assertEquals(200, result.getStatus());
        assertEquals("ivysaur", resultValue.name);
        assertEquals(resultValue.type.id, fireType.id);
        assertEquals(resultValue.secondType.id, fireType.id);
        assertEquals("image2", resultValue.image);
    }

}