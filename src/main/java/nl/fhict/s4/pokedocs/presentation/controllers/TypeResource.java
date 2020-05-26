package nl.fhict.s4.pokedocs.presentation.controllers;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.panache.common.Parameters;
// import nl.fhict.s4.pokedocs.dal.Move;
// import nl.fhict.s4.pokedocs.dal.Pokemon;
import nl.fhict.s4.pokedocs.dal.Type;

@Path("types")
@RequestScoped
public class TypeResource {
    @Inject
    JsonWebToken jwt;


    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTypes() {
        return Response.ok(Type.listAll()).build();
    }

    @GET
    @PermitAll
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getType(@PathParam("id") long id) {
        Type type = Type.findById(id);

        if(type == null) {
            return Response.status(404).build();
        }

        return Response.ok(type).build();
    }


    @DELETE
    @PermitAll
    @Path("{id}")
    @Transactional
    public Response deleteType(@PathParam("id") long id) {
        //TODO: HANDLE NULL?
        //TODO: BLOCK IF USED BY ONE OR MORE POKEMON?
        //TODO: STATUS AND RETURN VALUE OF DELETE?
        Type.findById(id).delete();
        return Response.noContent().build();
    }


    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response addType(@FormParam("name") String name) {

        if(Type.count("name = :name", Parameters.with("name", name)) > 0) {
            //return a conflict response if the move already exists
            return Response.status(409).build();
        }

        Type type = new Type();
        type.name = name;
        type.persist();

        return Response.ok(type).build();
    }


    //TODO: IS THIS ONE NECESSARY? IT EMULATES THE POKEMON SEARCH
    @GET
    @PermitAll
    @Path("{id}/pokemon")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPokemon(@PathParam("id") long id) {
        Type type = Type.findById(id);
        return Response.ok(type.pokemon).build();
        //return Response.ok(Pokemon.search(null, id)).build();
    }

    //TODO: REPLACE WITH FILTER OPTIONS ON MOVE
    @GET
    @PermitAll
    @Path("{id}/moves")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoves(@PathParam("id") long id) {
        //TODO: HANDLE NULL?
        Type type = Type.findById(id);
        return Response.ok(type.moves).build();
        //return Response.ok(Move.list("type", type)).build();
    }

}