package nl.fhict.s4.pokedocs.presentation.controllers;


import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.eclipse.microprofile.jwt.JsonWebToken;
import nl.fhict.s4.pokedocs.presentation.services.UserService;

@Path("users")
@RequestScoped
public class UserResource {

    @Inject JsonWebToken jwt;

    @Inject UserService userService;


    @GET()
    @Path("roles")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles() {
        return userService.getRoles(jwt);
    }


    @POST
    @Path("logout")
    @RolesAllowed({"Admin", "User"})
    public Response logout() {
       return userService.logout();
    }


    @POST
    @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PermitAll
    public Response login(@FormParam("username") final String username, @FormParam("password") final String password) throws Exception {
       return userService.login(username, password);
    }


    @POST
    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PermitAll
    @Transactional
    public Response register(@FormParam("username") final String username, @FormParam("password") final String password) {
       return userService.Register(username, password);
    }

}