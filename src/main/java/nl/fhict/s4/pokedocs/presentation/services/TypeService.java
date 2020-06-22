package nl.fhict.s4.pokedocs.presentation.services;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import io.quarkus.panache.common.Parameters;
import nl.fhict.s4.pokedocs.dal.Type;

@ApplicationScoped
public class TypeService {


    public Response addType(String name) {
        if(Type.count("name = :name", Parameters.with("name", name)) > 0) {
            //return a conflict response if the move already exists
            return Response.status(409).build();
        }
        Type type = new Type();
        type.name = name;
        type.persist();

        return Response.ok(type).build();
    }

    public Response getAllTypes() {
        return Response.ok(Type.listAll()).build();
    }

    public Response getType(Long id) {
        Type type = Type.findById(id);

        if(type == null) {
            return Response.status(404).build();
        }

        return Response.ok(type).build();
    }

    public Response deleteType(Long id) {
 
        //TODO: BLOCK IF USED BY ONE OR MORE POKEMON?
        Type type = Type.findById(id);

        if(type == null) {
            return Response.status(404).build();
        }

        type.delete();
        return Response.noContent().build();
    }

	public Response updateType(Long id, String name) {
        Type type = Type.findById(id);
        type.name = name;
        return Response.ok(type).build();
	}

    
}