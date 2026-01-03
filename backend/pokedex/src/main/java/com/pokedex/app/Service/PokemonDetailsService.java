package com.pokedex.app.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import com.pokedex.app.ApiEndpoints;
import com.pokedex.app.Mathmethods;
import com.pokedex.app.model.PokemonDto;
import com.pokedex.app.model.TypeDto;

@Service
public class PokemonDetailsService {
    
    private final RestTemplate restTemplate;
    private final TypeSpriteService typeSpriteService;
    private static final Logger logger = LoggerFactory.getLogger(PokemonDetailsService.class);

    public PokemonDetailsService(RestTemplate restTemplate, TypeSpriteService typeSpriteService) {
        this.restTemplate = restTemplate;
        this.typeSpriteService = typeSpriteService;
    }

    public PokemonDto getPokemonDetails(String nameOrId, String generation, String gameName) {
        // nameOrId: name of pokemon
        // generation: generation-iv for now
        // gameName: platinum for now
        PokemonDto pokemonDto = new PokemonDto();
        Map<String, Object> pokemonData = null, speciesData = null;
        List<Map<String, Object>> types = null;
        List<Map<String, Object>> pastTypes = null;
        List<TypeDto> typeDtos = null;

        try {
            pokemonData = Objects.requireNonNull(restTemplate.getForObject(ApiEndpoints.POKEMON + nameOrId, Map.class));
            speciesData = Objects.requireNonNull(restTemplate.getForObject(ApiEndpoints.POKEMON_SPECIES + nameOrId, Map.class));

            logger.info("Fetched species data + details for Pokémon: {} ", pokemonData.get("name"));
            pokemonDto.setName((String) pokemonData.get("name"));
            pokemonDto.setId((int) pokemonData.get("id"));
            
            Map<String, String> cries = (Map<String, String>) pokemonData.get("cries");
            pokemonDto.setCry(cries.get("latest"));

            pokemonDto.setHeight(Mathmethods.createHeightString((int) pokemonData.get("height")));
            pokemonDto.setWeight(Mathmethods.createWeightString((int) pokemonData.get("weight")));
        } catch (NotFound e) {
            logger.warn("Pokemon not found: " + pokemonDto.getName());
        }

        try {
            // Only needed prior to gen 6 as Fairy type was introduced in gen 6
            pastTypes = (List<Map<String, Object>>) pokemonData.get("past_types");
            if (pastTypes != null && !pastTypes.isEmpty()) {
                types = (List<Map<String, Object>>) pastTypes.get(0).get("types");
            } else {
                types = (List<Map<String, Object>>) pokemonData.get("types");
            }
            
            typeDtos = types.stream()
                .map(t -> (Map<String, Object>) t.get("type"))
                .map(type -> {
                    String name = (String) type.get("name");
                    String url = typeSpriteService.getTypeSprite(name, generation, gameName);
                    return new TypeDto(name, url);
                })
                .toList();
            pokemonDto.setTypes(typeDtos);
        } catch (NotFound e) {
            logger.warn("TypeSprite not found for: {}",  types.toString());
            return null;
        } catch (NullPointerException e) {
            logger.error("Failed to fetch typeSprite for type: {}", types.toString());
            return null;
        }

        try {
            Map<String, Object> sprites = (Map<String, Object>) pokemonData.get("sprites");
            Map<String, Object> versionMapping = (Map<String, Object>) sprites.get("versions");
            Map<String, Object> generationSpriteMap = (Map<String, Object>) versionMapping.get(generation);
            Map<String, String> gameSpriteMap = (Map<String, String>) generationSpriteMap.get(gameName);
            pokemonDto.setSpriteUrl(gameSpriteMap.get("front_default"));

        } catch (NotFound e) {
            logger.error("Failed to fetch sprite for Pokémon: {}", nameOrId);
        }
        try {
            List<Map<String, Object>> generaMapping = (List<Map<String, Object>>) speciesData.get("genera");

            String englishTitle = generaMapping.stream()
                                .filter(g -> "en".equals(((Map<String, String>) g.get("language")).get("name")))
                                .map(g -> (String) g.get("genus"))
                                .findFirst()
                                .orElse("???????");
            pokemonDto.setTitle(englishTitle);

            List<Map<String, Object>> flavorTextEntries = (List<Map<String, Object>>) speciesData.get("flavor_text_entries");
            String gameDescription = flavorTextEntries.stream()
                                                    .filter(entry -> gameName.equals(((Map<String, String>) entry.get("version")).get("name")))
                                                    .map(entry -> (String) entry.get("flavor_text"))
                                                    .findFirst()
                                                    .orElse("?????????");
            pokemonDto.setDescription(gameDescription);
        } catch (NotFound e) {
            logger.warn("Pokemon not found: " + nameOrId);
            return null;
        } catch (Exception e) {
            logger.error("Failed to build Pokemon DTO for idOrName={}, generation={}, game={}", nameOrId, generation, gameName, e);            
            return null;
        }
        return pokemonDto;
    }
}
