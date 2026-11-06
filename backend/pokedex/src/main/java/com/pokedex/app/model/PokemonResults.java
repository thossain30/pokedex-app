package com.pokedex.app.model;

public class PokemonResults {
    private String name;
    private String url;
    private String[] types;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String[] getTypes() { return types; }
    public void setTypes(String[] types) { this.types = types; }
}
