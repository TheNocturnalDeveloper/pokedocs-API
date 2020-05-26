package nl.fhict.s4.pokedocs.presentation.models;

public class MoveModel {
    private long id;
    private String name;
    private String description;
    private TypeModel type;

    public MoveModel(long id, String name, String description, TypeModel type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
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

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the type
     */
    public TypeModel getType() {
        return type;
    }

}