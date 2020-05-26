package nl.fhict.s4.pokedocs.presentation.controllers;

import java.security.Principal;
import java.security.PrivateKey;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.smallrye.jwt.build.JwtClaimsBuilder;
import nl.fhict.s4.pokedocs.dal.User;
import nl.fhict.s4.pokedocs.presentation.utils.TokenUtils;
import io.quarkus.panache.common.Parameters;
import io.smallrye.jwt.build.Jwt;

@Path("users")
@RequestScoped
public class UserResource {

    @Inject
    JsonWebToken jwt;


    @GET()
    @Path("auth") 
    @RolesAllowed({"Admin", "User"}) 
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowed(@Context final SecurityContext ctx) {
        final Principal caller =  ctx.getUserPrincipal();
        final String name = caller == null ? "anonymous" : caller.getName();
        final boolean hasJWT = jwt.getClaimNames() != null;
        final String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s", name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT);
        return helloReply;
    }


    @GET()
    @Path("roles")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles() {
        var roles = Set.class.cast(jwt.claim("groups").orElseGet(() -> Set.of()));
        return Response.ok(roles).build();
    }


    @POST
    @Path("logout")
    @RolesAllowed({"Admin", "User"})
    public Response logout() {
        var cookie = new NewCookie(new Cookie("token", null), "", 0, true);
        return Response.ok().cookie(cookie).build();
    }



    @POST
    @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PermitAll
    public Response login(@FormParam("username") final String username, @FormParam("password") final String password) throws Exception {
        Optional<User> user = User.authenticate(username, password);

        if(user.isEmpty()) {
            return Response.status(Status.UNAUTHORIZED).build();
        }

        final JwtClaimsBuilder claims = Jwt.claims();
        final PrivateKey privateKey = TokenUtils.readPrivateKey("/META-INF/resources/privateKey.pem");
        final long issuedAt = TokenUtils.currentTimeInSecs();
        final int duration = 60 * 60;
        final long expiresAt = issuedAt + duration;
        
        claims.preferredUserName(username);
        claims.groups(user.get().role);
        claims.issuedAt(issuedAt);
        claims.expiresAt(expiresAt);

        String token = claims.jws().sign(privateKey);

        NewCookie cookie = new NewCookie(
            new Cookie("token", token), 
            "authentication token for the backend", 
            duration, 
            new Date(expiresAt), 
            true, 
            true
        );
        //csrf test data:text/html,<form method="post" action="https://localhost:8443/types"><input type="text" name="name" /><input type="submit" /></form>
        return Response.noContent().cookie(cookie).build();
    }


    @POST
    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PermitAll
    @Transactional
    public Response register(@FormParam("username") final String username, @FormParam("password") final String password) {

        if(User.count("username = :username", Parameters.with("username", username)) > 0) {
            //return a conflict response if the username is taken
            return Response.status(409).build();
        }

        final User user = User.add(username, password, "User");
        return Response.status(Status.CREATED).entity(user).build();
    }

}