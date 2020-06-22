package nl.fhict.s4.pokedocs.presentation.services;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.quarkus.panache.common.Parameters;
import nl.fhict.s4.pokedocs.dal.Ability;

@ApplicationScoped
public class AbilityService {

    public Response deleteAbility(Long id) {
        Ability ability = Ability.findById(id);

        if(ability == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        ability.delete();

        return Response.noContent().build();
    }

    public Response getAllAbilities() {
        return Response.ok(Ability.listAll()).build();
    }

    public Response getAbility(Long id) {
         Ability ability = Ability.findById(id);

         if(ability == null) {
             return Response.status(Status.NOT_FOUND).build();
         }
 
         return Response.ok(ability).build();
    }

    public Response addAbility(String name, String description) {
        if(Ability.count("name = :name", Parameters.with("name", name)) > 0) {
            //return a conflict response if the ability already exists
            return Response.status(Status.CONFLICT).build();
        }

        Ability ability = Ability.addAbility(name, description);
        
        return Response.ok(ability).build();
    }

    public Response updateAbility(Long id, String description) {
        Ability ability = Ability.findById(id);
        ability.description = description;

        return Response.ok(ability).build();
    }

}