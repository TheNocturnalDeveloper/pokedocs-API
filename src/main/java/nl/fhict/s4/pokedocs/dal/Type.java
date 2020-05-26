package nl.fhict.s4.pokedocs.dal;

import java.util.HashSet;
import java.util.Set;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;



import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name="pokemon_type")
public class Type extends PanacheEntity {

    @Column(name = "name", nullable = false, unique = true)
    public String name;

    //TODO: HASHSETS AREN'T BEING FILLED
    
    @OneToMany
    @JsonbTransient
    public Set<Move> moves = new HashSet<>();


    @OneToMany
    @JsonbTransient
    public Set<Pokemon> pokemon = new HashSet<>();

    /**
     * Adds a new type in the database
     * @param name the name of the type
     */
    public static Type addType(final String name) {
        Type type = new Type();
        type.name = name;

        type.persist();

        return type;
    }

}