package nl.fhict.s4.pokedocs.presentation.services;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import nl.fhict.s4.pokedocs.dal.Move;
import nl.fhict.s4.pokedocs.dal.Pokemon;
import nl.fhict.s4.pokedocs.dal.Type;

@ApplicationScoped
public class PokemonService {

    public Response searchPokemon(String name, Long typeId, Long secondTypeId) {
        return Response.ok(Pokemon.search(name, typeId, secondTypeId)).build();
    }

    public Response getPokemon(Integer id) {
        //TODO: HANDLE NULL?
  
        return Response.ok(Pokemon.findById(id)).build();
    }

    public Response deletePokemon(Integer id) {
        Pokemon.findById(id).delete();
        return Response.noContent().build();
    }

    public Response addPokemon(Integer pokedexEntry, String name, Long typeId, Long secondTypeId) {   

        if(Pokemon.getPokemonWithTypeCount(typeId, secondTypeId) > Pokemon.count() / 2) {
            //a pokemon may only be added if both types exist in less than 50% of pokemon
            return Response.status(Status.FORBIDDEN).build();
        }

        Pokemon pokemon = new Pokemon();
        pokemon.pokeDexEntry = pokedexEntry;
        pokemon.name = name;
        pokemon.type = Type.findById(typeId);
        pokemon.secondType = secondTypeId != null ? Type.findById(secondTypeId) : null;
        
        pokemon.persist();

        return Response.ok(pokemon).build();
    }


    public Response updatePokemon(Integer id, Long typeId, Long secondTypeId) {
        Pokemon pokemon = Pokemon.findById(id);
        pokemon.type= Type.findById(typeId);

        if(secondTypeId != null) {
            pokemon.secondType = Type.findById(secondTypeId);
        }

        return Response.ok(pokemon).build();
    }


    public Response assignMove(Integer pokedexEntry, Long moveId) {
        //TODO: FACTOR OUT INTO DAL METHOD
        //TODO: NULL CHECKING
        //TODO: MOVES ARENT BEING SAVED
        Pokemon pokemon = Pokemon.findById(pokedexEntry);
        Move move = Move.findById(moveId);

        pokemon.moves.add(move);

        return Response.noContent().build();
    }

    public Response getPokemonMoves(Integer id) {
        //TODO: HANDLE NULL?
        Pokemon pokemon = Pokemon.findById(id);
        return Response.ok(pokemon.moves).build();
    }
}