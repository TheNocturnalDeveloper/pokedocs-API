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
import nl.fhict.s4.pokedocs.presentation.services.AbilityService;

@Path("abilities")
@RequestScoped
public class AbilityResource {

    @Inject AbilityService abilityService;

    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAbilities() {
        return abilityService.getAllAbilities();
    }

    @GET
    @PermitAll
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAbility(@PathParam("id") long id) {
       return abilityService.getAbility(id);
    }

    @DELETE
    @Authenticated
    @Path("{id}")
    @Transactional
    public Response deleteAbility(@PathParam("id") long id) {
       return abilityService.deleteAbility(id);
    }


    @POST
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response addAbility(@FormParam("name") String name, @FormParam("description") String description) {
        return abilityService.addAbility(name, description);
    }


    @PUT
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{id}")
    public Response updateAbility(
        @PathParam("id") long id,
        @FormParam("description") String description
    ) {
        return abilityService.updateAbility(id, description);
    }

}