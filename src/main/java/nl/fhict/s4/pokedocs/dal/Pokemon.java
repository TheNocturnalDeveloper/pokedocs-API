package nl.fhict.s4.pokedocs.dal;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;


@Entity
@Table(name = "pokemon")
public class Pokemon extends PanacheEntityBase  implements Serializable {

	private static final long serialVersionUID = -254654171295989299L;

    @Id
    @Column(name = "pokedex_entry", nullable = false, unique = true)
    public int pokeDexEntry;

    @Column(name = "name", nullable = false, unique = true)
    public String name;

 
    @JoinColumn(nullable = false)
    @ManyToOne
    public Type type;

   
    @JoinColumn(nullable = true)
    @ManyToOne
    public Type secondType;

    @Column(name = "image", nullable = false)
    public String image;

    @ManyToMany
    @JsonbTransient
    public Set<Move> moves = new HashSet<>();

    public static Boolean exists(Integer pokedexEntry, String name) {
        return Pokemon.count("name = :name or pokeDexEntry = :pokedexEntry", Map.of("name", name, "pokedexEntry", pokedexEntry)) >= 1;
    }

    public static List<Pokemon> search(String name,  Long typeId, Long secondTypeId) {
        //use criteria
        //JpaOperations.getEntityManager()
        Map<String, Object> params = new HashMap<>();
        StringBuilder queryBuffer = new StringBuilder("from Pokemon p");
        Boolean first = true;

        if(name != null) {
            queryBuffer.append(first ? " where " : " and ");

            queryBuffer.append("p.name like CONCAT('%', :name, '%')");
            params.put("name", name);

            if(first) first = false;
        }


        if(typeId != null) {
            queryBuffer.append(first ? " where " : " and ");
            queryBuffer.append("p.type.id = :typeId");
            params.put("typeId", typeId);

            if(first) first = false;
        }

        if(secondTypeId != null) {
            queryBuffer.append(first ? " where " : " and ");
            queryBuffer.append("p.secondType.id = :secondTypeId");
            params.put("secondTypeId", secondTypeId);

            if(first) first = false;
        }
        
        return  Pokemon.find(queryBuffer.toString(), params).list();
    }

    public static long getPokemonWithTypeCount(Long typeId, Long secondTypeId) {

        if(typeId == null) {
            //the primary type may not be null
            throw new IllegalArgumentException("typeId cannot be null");
        }

        Map<String, Object> params =  new HashMap<>();
        params.put("typeId", typeId);
        
        StringBuilder queryBuffer = new StringBuilder("from Pokemon p");

       
        if(secondTypeId == null) {
            queryBuffer.append(" where p.type.id = :typeId");
        }
        else {
            //check both combinations of type + second type
            queryBuffer.append(" where (p.type.id = :typeId and p.secondType.id = :secondTypeId) or (p.type.id = :secondTypeId and p.secondType.id = :typeId)");
            params.put("secondTypeId", secondTypeId);
        }

        return Pokemon.count(queryBuffer.toString(), params);
    }


    //for safety reasons deserialization is not allowed
    private final void readObject(ObjectInputStream in) throws java.io.IOException {
        throw new java.io.IOException("Cannot be deserialized");
    }
}