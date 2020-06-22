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

import io.quarkus.security.Authenticated;
import nl.fhict.s4.pokedocs.presentation.services.PokemonService;



@Path("pokemon")
@RequestScoped
public class PokemonResource {

    
    @Inject PokemonService pokemonService;

    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchPokemon(
        @QueryParam("name") String name, 
        @QueryParam("typeId") Long typeId,
        @QueryParam("secondTypeId") Long secondTypeId
    ) {
       return pokemonService.searchPokemon(name, typeId, secondTypeId);
    }

    @GET
    @PermitAll
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPokemon(@PathParam("id") int  id) {
        return pokemonService.getPokemon(id);
    }


    @DELETE
    @Authenticated
    @Path("{id}")
    public Response deletePokemon(@PathParam("id") int id) {
        return pokemonService.deletePokemon(id);
    }


    @POST
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response addPokemon(
        @FormParam("pokedexEntry") int pokedexEntry,
        @FormParam("name") String name, 
        @FormParam("typeId") Long typeId, 
        @FormParam("secondTypeId") Long secondTypeId,
        @FormParam("image") String image
    ) {
       return pokemonService.addPokemon(pokedexEntry, name, typeId, secondTypeId, image);
    }

    @PUT
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{id}")
    @Transactional
    public Response updatePokemon(
        @PathParam("id") Integer id,
        @FormParam("name") String name, 
        @FormParam("typeId") Long typeId, 
        @FormParam("secondTypeId") Long secondTypeId,
        @FormParam("image") String image
    ) {
      return pokemonService.updatePokemon(id, name, typeId, secondTypeId, image);
    }



    @POST
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{id}/moves")
  
    public Response assignMove(
        @PathParam("id") int pokedexEntry,
        @FormParam("moveId") Long moveId
    ) {
       return pokemonService.assignMove(pokedexEntry, moveId);
    }


    @GET
    @PermitAll
    @Path("{id}/moves")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPokemonMoves(@PathParam("id") int  id) {
      return pokemonService.getPokemonMoves(id);
    }

}