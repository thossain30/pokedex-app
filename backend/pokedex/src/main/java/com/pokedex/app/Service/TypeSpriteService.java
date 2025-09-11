package com.pokedex.app.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pokedex.app.ApiEndpoints;

@Service
public class TypeSpriteService {
    private final RestTemplate restTemplate;
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public TypeSpriteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getTypeSprite(String typeName, String generation, String gameName) {
        String cacheKey = typeName + "-" + generation + "-" + gameName; 

        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        } 
        String url = ApiEndpoints.TYPE + "/" + typeName;
        @SuppressWarnings("unchecked")
        Map<String, Object> response = Objects.requireNonNull(restTemplate.getForObject(url, Map.class));

        @SuppressWarnings("unchecked")
        Map<String, Object> sprites = (Map<String, Object>) response.get("sprites");
        @SuppressWarnings("unchecked")
        Map<String, Object> genData = (Map<String, Object>) sprites.get(generation);
        @SuppressWarnings("unchecked")
        Map<String, String> gameData = (Map<String, String>) genData.get(gameName);

        String spriteUrl = gameData.get("name_icon");

        // Save in cache
        cache.put(cacheKey, spriteUrl);

        return spriteUrl;
    }
}
