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

import nl.fhict.s4.pokedocs.presentation.services.TypeService;

@Path("types")
@RequestScoped
public class TypeResource {

    @Inject JsonWebToken jwt;
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
    @PermitAll
    @Path("{id}")
    @Transactional
    public Response deleteType(@PathParam("id") long id) {
        return typeService.deleteType(id);
    }


    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response addType(@FormParam("name") String name) {
      return typeService.addType(name);
    }

}