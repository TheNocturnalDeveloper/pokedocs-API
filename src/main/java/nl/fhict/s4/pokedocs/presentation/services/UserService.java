package nl.fhict.s4.pokedocs.presentation.services;

import java.security.PrivateKey;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.panache.common.Parameters;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import nl.fhict.s4.pokedocs.dal.User;
import nl.fhict.s4.pokedocs.presentation.utils.TokenUtils;
import io.smallrye.jwt.build.Jwt;

@ApplicationScoped
public class UserService {

    public Response logout() {
        var cookie = new NewCookie(new Cookie("token", null), "", 0, true);
        return Response.ok().cookie(cookie).build();
    }

    public Response getRoles(JsonWebToken jwt) {
        var roles = Set.class.cast(jwt.claim("groups").orElseGet(() -> Set.of()));
        return Response.ok(roles).build();
    }

    public Response Register(String username, String password) {
        if(User.count("username = :username", Parameters.with("username", username)) > 0) {
            //return a conflict response if the username is taken
            return Response.status(409).build();
        }

        final User user = User.add(username, password, "User");
        return Response.status(Status.CREATED).entity(user).build();
    }

    public Response login(String username, String password) throws Exception {
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
}