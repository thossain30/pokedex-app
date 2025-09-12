package com.pokedex.app.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pokedex.app.ApiEndpoints;
import com.pokedex.app.Mathmethods;
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
        // nameOrId: name of pokemon
        // generation: generation-iv for now
        // gameName: platinum for now
        Map<String, Object> pokemonData = Objects.requireNonNull(restTemplate.getForObject(ApiEndpoints.POKEMON + nameOrId, Map.class));
        Map<String, Object> speciesData = Objects.requireNonNull(restTemplate.getForObject(ApiEndpoints.POKEMON_SPECIES + nameOrId, Map.class));

        PokemonDto pokemonDto = new PokemonDto();
        pokemonDto.setName((String) pokemonData.get("name"));
        pokemonDto.setId((int) pokemonData.get("id"));
        
        Map<String, String> cries = (Map<String, String>) pokemonData.get("cries");
        pokemonDto.setCry(cries.get("latest"));

        pokemonDto.setHeight(Mathmethods.createHeightString((int) pokemonData.get("height")));
        pokemonDto.setWeight(Mathmethods.createWeightString((int) pokemonData.get("weight")));

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

        Map<String, Object> sprites = (Map<String, Object>) pokemonData.get("sprites");
        Map<String, Object> versionMapping = (Map<String, Object>) sprites.get("versions");
        Map<String, Object> generationSpriteMap = (Map<String, Object>) versionMapping.get(generation);
        Map<String, String> gameSpriteMap = (Map<String, String>) generationSpriteMap.get(gameName);
        pokemonDto.setSpriteUrl(gameSpriteMap.get("front_default"));

        List<Map<String, Object>> generaMapping = (List<Map<String, Object>>) speciesData.get("genera");

        String englishTitle = generaMapping.stream()
                              .filter(g -> "en".equals(((Map<String, String>) g.get("language")).get("name")))
                              .map(g -> (String) g.get("genus"))
                              .findFirst()
                              .orElse("Unknown Pokemon");
        pokemonDto.setTitle(englishTitle);

        List<Map<String, Object>> flavorTextEntries = (List<Map<String, Object>>) speciesData.get("flavor_text_entries");
        String gameDescription = flavorTextEntries.stream()
                                                  .filter(entry -> gameName.equals(((Map<String, String>) entry.get("version")).get("name")))
                                                  .map(entry -> (String) entry.get("flavor_text"))
                                                  .findFirst()
                                                  .orElse("?????????");
        pokemonDto.setDescription(gameDescription);
        return pokemonDto;
    }
}
