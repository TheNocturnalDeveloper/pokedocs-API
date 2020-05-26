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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.panache.common.Parameters;
import nl.fhict.s4.pokedocs.dal.Move;
import nl.fhict.s4.pokedocs.dal.Type;

@Path("moves")
@RequestScoped
public class MoveResource {
    @Inject
    JsonWebToken jwt;

    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMoves() {
        return Response.ok(Move.listAll()).build();
    }

    @GET
    @PermitAll
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMove(@PathParam("id") long  id) {
        //TODO: HANDLE NULL?
        Move move = Move.findById(id);
        
        if(move == null) {
            return Response.status(404).build();
        }

        return Response.ok(move).build();
    }


    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response addMove(
        @FormParam("name") String name,
        @FormParam("description") String description,  
        @FormParam("type") Long typeId
    ) {

        if(Move.count("name = :name", Parameters.with("name", name)) > 0) {
            //return a conflict response if the move already exists
            return Response.status(409).build();
        }

        Move move = new Move();
        move.name = name;
        move.description = description;
        move.type = Type.findById(typeId);

        move.persist();

        return Response.ok(move).build();
    }


    @PUT
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{id}")
    public Response updateMove(
        @PathParam("id") long id,
        @FormParam("type") Long typeId,
        @FormParam("description") String description
    ) {
        
        Move move = Move.findById(id);
        move.type = Type.findById(typeId);
        move.description = description;

        return Response.ok(move).build();
    }


    @DELETE
    @PermitAll
    @Path("{id}")
    @Transactional
    public Response deleteMove(@PathParam("id") long id) {
        //TODO: HANDLE NULL?
        //TODO: BLOCK IF USED BY ONE OR MORE POKEMON?
     
        Move.findById(id).delete();
        return Response.noContent().build();
    }
}