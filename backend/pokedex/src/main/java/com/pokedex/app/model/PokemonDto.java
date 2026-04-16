package com.pokedex.app.model;

import java.util.List;

public class PokemonDto {
    private String name;
    private int id;
    private String title;
    private String height;
    private String weight;
    private String cry;
    private List<Integer> generationsAvailableIn;

    public PokemonDto() { }

    public PokemonDto(String name, int id, String title, 
                      String height, String weight, String cry,
                      List<Integer> generationsAvailableIn) {
        this.name = name;
        this.id = id;
        this.title = title;
        this.height = height;
        this.weight = weight;
        this.cry = cry;
        this.generationsAvailableIn = generationsAvailableIn;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getCry() { return cry; }
    public void setCry(String cry) { this.cry = cry; }

    public List<Integer> getGenerationsAvailableIn() { return generationsAvailableIn; }
    public void setGenerationsAvailableIn(List<Integer> generationsAvailableIn) { this.generationsAvailableIn = generationsAvailableIn; }
}