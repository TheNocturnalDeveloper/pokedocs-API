package nl.fhict.s4.pokedocs.presentation.services;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import io.quarkus.panache.common.Parameters;
import nl.fhict.s4.pokedocs.dal.Move;
import nl.fhict.s4.pokedocs.dal.Type;

@ApplicationScoped
public class MoveService {
  
    public Response getAllMoves() {
        return Response.ok(Move.listAll()).build();
    }

    public Response getMove(Long  id) {
        //TODO: HANDLE NULL?
        Move move = Move.findById(id);
        
        if(move == null) {
            return Response.status(404).build();
        }

        return Response.ok(move).build();
    }

    public Response addMove(String name, String description, Long typeId) {

        if(Move.count("name = :name", Parameters.with("name", name)) > 0) {
            //return a conflict response if the move already exists
            return Response.status(409).build();
        }

        Move move = new Move();
        move.name = name;
        move.description = description;
        move.type = Type.findById(typeId);

        move.persist();

        return Response.ok(move).build();
    }

    public Response updateMove(Long id, Long typeId, String description) {
        
        Move move = Move.findById(id);
        move.type = Type.findById(typeId);
        move.description = description;

        return Response.ok(move).build();
    }

    public Response deleteMove(Long id) {
        //TODO: HANDLE NULL?
        //TODO: BLOCK IF USED BY ONE OR MORE POKEMON?
     
        Move.findById(id).delete();
        return Response.noContent().build();
    }
}