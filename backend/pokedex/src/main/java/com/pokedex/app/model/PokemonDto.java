package com.pokedex.app.model;

import java.util.List;

public class PokemonDto {
    private String name;
    private int id;
    private String description;
    private String title;
    private String height;
    private String weight;
    private String[] spriteUrls;
    private String cry;
    private List<TypeDto> types;

    public PokemonDto() { }

    public PokemonDto(String name, int id, String description, String title, 
                      String height, String weight, String[] spriteUrls, 
                      String cry, List<TypeDto> types) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.title = title;
        this.height = height;
        this.weight = weight;
        this.spriteUrls = spriteUrls;
        this.cry = cry;
        this.types = types;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String[] getSpriteUrls() { return spriteUrls; }
    public void setSpriteUrls(String[] spriteUrls) { this.spriteUrls = spriteUrls; }

    public String getCry() { return cry; }
    public void setCry(String cry) { this.cry = cry; }

    public List<TypeDto> getTypes() { return types; }
    public void setTypes(List<TypeDto> types) { this.types = types; }
    
}