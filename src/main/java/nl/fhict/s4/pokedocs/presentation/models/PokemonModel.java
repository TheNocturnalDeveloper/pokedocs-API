package nl.fhict.s4.pokedocs.presentation.models;

public class PokemonModel {
    private long pokedexEntry;
    private String name;
    private TypeModel type;
    private TypeModel secondType;

    public  PokemonModel(long pokedexEntry, String name, TypeModel type, TypeModel secondType) {
        this.pokedexEntry = pokedexEntry;
        this.name = name;
        this.type = type;
        this.secondType = secondType;
    }

    /**
     * @return the pokedexEntry
     */
    public long getPokedexEntry() {
        return pokedexEntry;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public TypeModel getType() {
        return type;
    }

    /**
     * @return the secondType
     */
    public TypeModel getSecondType() {
        return secondType;
    }
}