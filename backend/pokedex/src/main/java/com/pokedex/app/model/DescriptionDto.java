package com.pokedex.app.model;

public class DescriptionDto {
    String key;
    String description;
    public DescriptionDto() { }
    public DescriptionDto(String key, String description) {
        this.key = key;
        this.description = description;
    }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
