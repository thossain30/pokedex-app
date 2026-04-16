package com.pokedex.app.model;

import java.util.List;

public class GenerationDto {
    private String key;
    private int genNumId;
    private String[] spriteUrls;
    private String[] gameNames;
    private List<TypeDto> typeSpriteUrls;

    public GenerationDto() { }

    public GenerationDto(String key, int genNumId, String[] spriteUrls, 
                         String[] gameNames, List<TypeDto> typeSpriteUrls) {
        this.key = key;
        this.genNumId = genNumId;
        this.spriteUrls = spriteUrls;
        this.gameNames = gameNames;
        this.typeSpriteUrls = typeSpriteUrls;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public int getGenNumId() { return genNumId; }
    public void setGenNumId(int genNumId) { this.genNumId = genNumId; }
    
    public String[] getSpriteUrls() { return spriteUrls; }
    public void setSpriteUrls(String[] spriteUrls) { this.spriteUrls = spriteUrls; }
    
    public String[] getGameNames() { return gameNames; }
    public void setGameNames(String[] gameNames) { this.gameNames = gameNames; }
    
    public List<TypeDto> getTypeSpriteUrls() { return typeSpriteUrls; }
    public void setTypeSpriteUrls(List<TypeDto> typeSpriteUrls) { this.typeSpriteUrls = typeSpriteUrls; }
}
