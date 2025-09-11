package com.pokedex.app.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pokedex.app.ApiEndpoints;
import com.pokedex.app.model.PokemonDto;
import com.pokedex.app.model.TypeDto;

@Service
public class PokemonDetailsService {
    
    private final RestTemplate restTemplate;
    private final TypeSpriteService typeSpriteService;

    public PokemonDetailsService(RestTemplate restTemplate, TypeSpriteService typeSpriteService) {
        this.restTemplate = restTemplate;
        this.typeSpriteService = typeSpriteService;
    }

    @SuppressWarnings("unchecked")
    public PokemonDto getPokemonDetails(String nameOrId, String generation, String gameName) {
        Map<String, Object> pokemonData = Objects.requireNonNull(restTemplate.getForObject(ApiEndpoints.POKEMON + nameOrId, Map.class));
        Map<String, Object> speciesData = Objects.requireNonNull(restTemplate.getForObject(ApiEndpoints.POKEMON_SPECIES + nameOrId, Map.class));

        PokemonDto pokemonDto = new PokemonDto();
        pokemonDto.setName((String) pokemonData.get("name"));
        pokemonDto.setId((int) pokemonData.get("id"));
        
        Map<String, String> cries = (Map<String, String>) pokemonData.get("cries");
        pokemonDto.setCry(cries.get("latest"));

        List<Map<String, Object>> types = (List<Map<String, Object>>) pokemonData.get("types");
        List<TypeDto> typeDtos = types.stream()
             .map(t -> (Map<String, Object>) t.get("type"))
             .map(type -> {
                String name = (String) type.get("name");
                String url = typeSpriteService.getTypeSprite(name, generation, gameName);
                return new TypeDto(name, url);
             })
             .toList();
        pokemonDto.setTypes(typeDtos);

        return pokemonDto;
    }
}
