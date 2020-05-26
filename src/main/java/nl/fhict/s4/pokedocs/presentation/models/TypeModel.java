package nl.fhict.s4.pokedocs.presentation.models;

public class TypeModel {
    private long id;
    private String name;

    public TypeModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

}