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

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.panache.common.Parameters;
import nl.fhict.s4.pokedocs.dal.Ability;

@Path("abilities")
@RequestScoped
public class AbilityResource {
    @Inject
    JsonWebToken jwt;


    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTypes() {
        return Response.ok(Ability.listAll()).build();
    }

    @GET
    @PermitAll
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAbility(@PathParam("id") long id) {
        //TODO: HANDLE NULL?
        Ability ability = Ability.findById(id);

        if(ability == null) {
            return Response.status(404).build();
        }

        return Response.ok(ability).build();
    }

    @DELETE
    @PermitAll
    @Path("{id}")
    @Transactional
    public Response deleteAbility(@PathParam("id") long id) {
        //TODO: HANDLE NULL?
        //TODO: BLOCK IF USED BY ONE OR MORE POKEMON?
        //TODO: STATUS AND RETURN VALUE OF DELETE?
        Ability.findById(id).delete();
        return Response.noContent().build();
    }


    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response addAbility(@FormParam("name") String name, @FormParam("description") String description) {


        if(Ability.count("name = :name", Parameters.with("name", name)) > 0) {
            //return a conflict response if the ability already exists
            return Response.status(409).build();
        }

        Ability ability = Ability.addAbility(name, description);
        
        return Response.ok(ability).build();
    }


    @PUT
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{id}")
    public Response updateAbility(
        @PathParam("id") long id,
        @FormParam("description") String description
    ) {
        //TODO: also update name?
        Ability ability = Ability.findById(id);
        ability.description = description;

        return Response.ok(ability).build();
    }

}