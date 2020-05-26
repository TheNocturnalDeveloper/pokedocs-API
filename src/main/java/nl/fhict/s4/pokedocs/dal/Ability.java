package nl.fhict.s4.pokedocs.dal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name="pokemon_ability")
public class Ability extends PanacheEntity {
    
    @Column(name = "name", nullable = false, unique = true)
    public String name;
    
    @Column(name = "description", nullable = false)
    public String description;



    /**
     * Adds a new ability in the database
     * @param name the name of the ability
     * @param description a short description of what the ability does
     */
    public static Ability addAbility(final String name, final String description) {
        Ability ability = new Ability();
        ability.name = name;
        ability.description = description;

        ability.persist();

        return ability;
    }
}
