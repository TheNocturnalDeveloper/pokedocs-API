package nl.fhict.s4.pokedocs.presentation.controllers;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.quarkus.security.Authenticated;
import nl.fhict.s4.pokedocs.presentation.services.TypeService;

@Path("types")
@RequestScoped
public class TypeResource {

    @Inject TypeService typeService;

    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTypes() {
        return typeService.getAllTypes();
    }

    @GET
    @PermitAll
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getType(@PathParam("id") long id) {
        return typeService.getType(id);
    }


    @DELETE
    @Authenticated
    @Path("{id}")
    @Transactional
    public Response deleteType(@PathParam("id") long id) {
        return typeService.deleteType(id);
    }


    @POST
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response addType(@FormParam("name") String name) {
      return typeService.addType(name);
    }


    @PUT
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{id}")
    @Transactional
    public Response updateType(@PathParam("id") long id, @FormParam("name") String name) {
      return typeService.updateType(id, name);
    }

}