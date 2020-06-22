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

import io.quarkus.security.Authenticated;
import nl.fhict.s4.pokedocs.presentation.services.MoveService;

@Path("moves")
@RequestScoped
public class MoveResource {

    @Inject MoveService moveService;

    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMoves() {
       return moveService.getAllMoves();
    }

    @GET
    @PermitAll
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMove(@PathParam("id") long  id) {
       return moveService.getMove(id);
    }


    @POST
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response addMove(
        @FormParam("name") String name,
        @FormParam("description") String description,  
        @FormParam("type") Long typeId
    ) {
        return moveService.addMove(name, description, typeId);
    }


    @PUT
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{id}")
    public Response updateMove(
        @PathParam("id") long id,
        @FormParam("type") Long typeId,
        @FormParam("description") String description
    ) {
        return moveService.updateMove(id, typeId, description);
    }


    @DELETE
    @Authenticated
    @Path("{id}")
    @Transactional
    public Response deleteMove(@PathParam("id") long id) {
        return moveService.deleteMove(id);
    }
}