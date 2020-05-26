package nl.fhict.s4.pokedocs.presentation.controllers;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nl.fhict.s4.pokedocs.dal.Move;
import nl.fhict.s4.pokedocs.dal.Pokemon;
import nl.fhict.s4.pokedocs.dal.Type;

import org.eclipse.microprofile.jwt.JsonWebToken;

// import org.eclipse.microprofile.openapi.annotations.Operation;
// import org.eclipse.microprofile.openapi.annotations.media.Content;
// import org.eclipse.microprofile.openapi.annotations.media.Schema;
// import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
// import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;



@Path("pokemon")
@RequestScoped
public class PokemonResource {
    @Inject
    JsonWebToken jwt;

    //TODO: MOVE OPENAPI DATA TO YAML FILES
    //TODO: SWITCH TO TYPE ID INSTEAD OF NAME
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchPokemon(
        @QueryParam("name") String name, 
        @QueryParam("typeId") Long typeId,
        @QueryParam("secondTypeId") Long secondTypeId
    ) {
        return Response.ok(Pokemon.search(name, typeId, secondTypeId)).build();
    }

    @GET
    @PermitAll
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPokemon(@PathParam("id") int  id) {
        //TODO: HANDLE NULL?
  
        return Response.ok(Pokemon.findById(id)).build();
    }


    @DELETE
    @PermitAll
    @Path("{id}")
    public Response deletePokemon(@PathParam("id") int id) {
        //TODO: HANDLE NULL?
        //TODO: STATUS AND RETURN VALUE OF DELETE?
        Pokemon.findById(id).delete();
        return Response.noContent().build();
    }


    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response addPokemon(
        @FormParam("pokedexEntry") int pokedexEntry,
        @FormParam("name") String name, 
        @FormParam("typeId") Long typeId, 
        @FormParam("secondTypeId") Long secondTypeId
    ) {
        Pokemon pokemon = new Pokemon();
        pokemon.pokeDexEntry = pokedexEntry; //check if unique
        pokemon.name = name;
        pokemon.type = Type.findById(typeId); //check null
        pokemon.secondType = secondTypeId != null ? Type.findById(secondTypeId) : null;
        
        pokemon.persist();

        return Response.ok(pokemon).build();
    }

    @PUT
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{id}")
    public Response updatePokemon(
        @PathParam("id") int id,
        @FormParam("type") long typeId, 
        @FormParam("secondType") long secondTypeId
    ) {
        //TODO: UPDATE MOVES?
        Pokemon pokemon = Pokemon.findById(id);
        pokemon.type= Type.findById(typeId);
        pokemon.secondType = Type.findById(secondTypeId);

        return Response.ok(pokemon).build();
    }



    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{id}/moves")
  
    public Response assignMove(
        @PathParam("id") int pokedexEntry,
        @FormParam("moveId") Long moveId
    ) {
        //TODO: FACTOR OUT INTO DAL METHOD
        //TODO: NULL CHECKING
        //TODO: MOVES ARENT BEING SAVED
        Pokemon pokemon = Pokemon.findById(pokedexEntry);
        Move move = Move.findById(moveId);

        pokemon.moves.add(move);

        return Response.noContent().build();
    }


    @GET
    @PermitAll
    @Path("{id}/moves")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPokemonMoves(@PathParam("id") int  id) {
        //TODO: HANDLE NULL?
        Pokemon pokemon = Pokemon.findById(id);
        return Response.ok(pokemon.moves).build();
    }

}