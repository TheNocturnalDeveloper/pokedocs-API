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
        Pokemon result = Pokemon.findById(id);

        if(result == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok(result).build();
    }

    public Response deletePokemon(Integer id) {
        Pokemon result = Pokemon.findById(id);

        if(result == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        result.delete();

        return Response.noContent().build();
    }

    public Response addPokemon(Integer pokedexEntry, String name, Long typeId, Long secondTypeId, String image) {   

        if(Pokemon.exists(pokedexEntry, name)) {
            return Response.status(Status.CONFLICT).build();
        }

        if(Pokemon.getPokemonWithTypeCount(typeId, secondTypeId) > Pokemon.count() / 2) {
            //a pokemon may only be added if both types exist in less than 50% of pokemon
            return Response.status(Status.FORBIDDEN).build();
        }

        Type type = Type.findById(typeId);
        if(type == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }


        Type secondType = null;
        if(secondTypeId != null) {
            secondType = Type.findById(secondTypeId);

            if(secondType == null) {
                return Response.status(Status.BAD_REQUEST).build();
            }
        }
        


        Pokemon pokemon = new Pokemon();
        pokemon.pokeDexEntry = pokedexEntry;
        pokemon.name = name;
        pokemon.type = type;
        pokemon.secondType = secondType;
        pokemon.image = image;
        pokemon.persist();

        return Response.ok(pokemon).build();
    }


    public Response updatePokemon(Integer id, String name, Long typeId, Long secondTypeId, String image) {
        Pokemon pokemon = Pokemon.findById(id);
        
        if(pokemon == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        Type type = Type.findById(typeId);
        if(type == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }


        Type secondType = null;
        if(secondTypeId != null) {
            secondType = Type.findById(secondTypeId);

            if(secondType == null) {
                return Response.status(Status.BAD_REQUEST).build();
            }
        }

        pokemon.type = type;
        pokemon.secondType = secondType;
        pokemon.name = name;
        pokemon.image = image;

        return Response.ok(pokemon).build();
    }


    public Response assignMove(Integer pokedexEntry, Long moveId) {
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