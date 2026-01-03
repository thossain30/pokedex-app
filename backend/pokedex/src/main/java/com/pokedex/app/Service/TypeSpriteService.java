package com.pokedex.app.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pokedex.app.ApiEndpoints;

@Service
public class TypeSpriteService {
    private final RestTemplate restTemplate;
    private final Map<String, String> cache = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(TypeSpriteService.class.getName());

    public TypeSpriteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getTypeSprite(String typeName, String generation, String gameName) {
        String cacheKey = typeName + "-" + generation + "-" + gameName; 

        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        } 
        String url = ApiEndpoints.TYPE + typeName;
        Map<String, Object> response = Objects.requireNonNull(restTemplate.getForObject(url, Map.class));
        Map<String, Object> sprites = (Map<String, Object>) response.get("sprites");
        Map<String, Object> genData = (Map<String, Object>) sprites.get(generation);
        Map<String, String> gameData = (Map<String, String>) genData.get(gameName);

        String spriteUrl = gameData.get("name_icon");

        if (spriteUrl == null) {
            logger.warning("Sprite URL not found for type " + typeName + " in " + generation + " " + gameName);
            return null;
        }

        // Save in cache
        cache.put(cacheKey, spriteUrl);

        return spriteUrl;
    }
}
