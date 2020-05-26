package nl.fhict.s4.pokedocs.dal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name="pokemon_move")
public class Move extends PanacheEntity {
    
    @Column(name = "name", nullable = false, unique = true)
    public String name;
    
    @Column(name = "description", nullable = false)
    public String description;

    @JoinColumn(nullable = false)
    @ManyToOne //is one to one right, here?
    public Type type;


    /**
     * Adds a new move in the database
     * @param name the name of the move
     * @param description a short description of what the move does
     * @param type the type of the move
     */
    public static Move addMove(final String name, final String description, final Type type) {
        Move move = new Move();
        move.name = name;
        move.description = description;
        move.type = type;

        move.persist();

        return move;
    }

}

