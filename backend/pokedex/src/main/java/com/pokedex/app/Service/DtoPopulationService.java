package com.pokedex.app.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import com.pokedex.app.Helpers;
import com.pokedex.app.model.DescriptionDto;
import com.pokedex.app.model.GenerationDto;
import com.pokedex.app.model.PokemonDto;
import com.pokedex.app.model.TypeDto;

@Service
public class DtoPopulationService {
    
    private final TypeSpriteService typeSpriteService;
    private final PokeApiService pokeApiService;
    private static final Logger logger = LoggerFactory.getLogger(DtoPopulationService.class);
    Map<String, Object> pokemonData = null, speciesData = null;
    private final Map<String, DescriptionDto> descriptionCache = new ConcurrentHashMap<>();

    public DtoPopulationService(TypeSpriteService typeSpriteService, PokeApiService pokeApiService) {
        this.typeSpriteService = typeSpriteService;
        this.pokeApiService = pokeApiService;
    }

    public PokemonDto getPokemonDetails(String nameOrId) {
        // nameOrId: name of pokemon
        PokemonDto pokemonDto = new PokemonDto();
        List<Integer> generationsAvailableIn = null;

        try {
            pokemonData = Objects.requireNonNull(pokeApiService.fetchPokemonData(nameOrId));
            speciesData = Objects.requireNonNull(pokeApiService.fetchSpeciesData(nameOrId));

            logger.info("Fetched species data + details for Pokémon: {} ", pokemonData.get("name"));
            pokemonDto.setName((String) pokemonData.get("name"));
            pokemonDto.setId((int) pokemonData.get("id"));
            
            Map<String, String> cries = (Map<String, String>) pokemonData.get("cries");
            pokemonDto.setCry(cries.get("latest"));

            pokemonDto.setHeight(Helpers.createHeightString((int) pokemonData.get("height")));
            pokemonDto.setWeight(Helpers.createWeightString((int) pokemonData.get("weight")));
        } catch (NotFound e) {
            logger.warn("Pokemon not found: " + pokemonDto.getName());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon not found");
        }
        try {
            List<Map<String, Object>> generaMapping = (List<Map<String, Object>>) speciesData.get("genera");

            String englishTitle = generaMapping.stream()
                                .filter(g -> "en".equals(((Map<String, String>) g.get("language")).get("name")))
                                .map(g -> (String) g.get("genus"))
                                .findFirst()
                                .orElse("???????");
            pokemonDto.setTitle(englishTitle);
        } catch (NotFound e) {
            logger.warn("Pokemon not found: " + nameOrId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon not found: " + nameOrId);
        } catch (Exception e) {
            logger.error("Error occurred while fetching Pokémon title: ", e);
        }

        generationsAvailableIn = new java.util.ArrayList<>();
        Map<String, Object> sprites = (Map<String, Object>) pokemonData.get("sprites");
        Map<String, Object> versionMapping = (Map<String, Object>) sprites.get("versions");
        for (Entry<String, Object> generationSpriteMaps: versionMapping.entrySet()) {
            Map<String, Map<String, String>> versions = (Map<String,Map<String, String>>) generationSpriteMaps.getValue();
            boolean hasSpriteInGen = false;
            for (Map<String, String> spriteData : versions.values()) {
                if (spriteData.get("front_default") != null) {
                    hasSpriteInGen = true;
                    break;
                }
            }
            if (hasSpriteInGen) {
                generationsAvailableIn.add(
                    Helpers.getGenNumFromGenString(generationSpriteMaps.getKey())
                );
            }
            Collections.sort(generationsAvailableIn);
            pokemonDto.setGenerationsAvailableIn(generationsAvailableIn);
        }
        return pokemonDto;
    }

    public GenerationDto setUpGenerationDto(int genNum, String nameOrId, String typeString) {
        GenerationDto generationDto = new GenerationDto();
        List<Map<String, Object>> types = null, pastTypes = null;
        List<TypeDto> typeDtos = null;

        String genString = Helpers.getGenerationString(genNum);

        generationDto.setKey(nameOrId + " - " + genNum);
        generationDto.setGenNumId(genNum);

        try {
            pokemonData = pokeApiService.fetchPokemonData(nameOrId);
            generationDto.setGameNames(Helpers.getGameVersionsFromGen(genNum));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid generation number: {}", genNum);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid generation number: " + genNum);
        }

        try {
            // Only needed prior to gen 6 as Fairy type was introduced in gen 6
            pastTypes = (List<Map<String, Object>>) pokemonData.get("past_types");
            if (genNum < 6 && (pastTypes != null && !pastTypes.isEmpty())) {
                types = (List<Map<String, Object>>) pastTypes.get(0).get("types");
            } else {
                types = (List<Map<String, Object>>) pokemonData.get("types");
            }
            
            typeDtos = types.stream()
                .map(t -> (Map<String, Object>) t.get("type"))
                .map(type -> {
                    String name = (String) type.get("name");
                    String url = null;
                    if (genNum < 3) {
                        url = typeSpriteService.getTypeSprite(name, "generation-iii", "emerald");
                    }
                    else {
                        url = typeSpriteService.getTypeSprite(name, genString, typeString);
                    }
                    return new TypeDto(name, url);
                })
                .toList();
            generationDto.setTypeSpriteUrls(typeDtos);
        } catch (NotFound e) {
            logger.warn("TypeSprite not found for: {}", types.toString());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TypeSprite not found: " + genString + ", " + typeString);
        } catch (NullPointerException e) {
            logger.error("Failed to fetch typeSprite for type: {}", types.toString());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to build Generation DTO");
        }

        try {
            Map<String, Object> sprites = (Map<String, Object>) pokemonData.get("sprites");
            Map<String, Object> versionMapping = (Map<String, Object>) sprites.get("versions");
            Map<String, Object> generationSpriteMap = (Map<String, Object>) versionMapping.get(genString);
            Map<String, String> gameSpriteMap = (Map<String, String>) generationSpriteMap.get(typeString);
            generationDto.setSpriteUrls(new String[]{gameSpriteMap.get("front_default"), gameSpriteMap.get("front_shiny")});
        } catch (NotFound e) {
            logger.error("Failed to fetch sprite for Pokémon: {}", nameOrId, "in generation: {}", genString, "game: {}", typeString);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon not found: " + nameOrId);
        }

        return generationDto;
    }

    public DescriptionDto generateDescriptionDto(String versionId, String nameOrId) {
        DescriptionDto descriptionDto = new DescriptionDto();
        pokemonData = pokeApiService.fetchPokemonData(nameOrId);
        speciesData = pokeApiService.fetchSpeciesData(nameOrId); 
        String name = (String) pokemonData.get("name");
        String cacheKey = "";
        if (versionId != null && nameOrId != null) {
            cacheKey = name + "-" + versionId;
        } else {
            throw new IllegalArgumentException("versionId: " + versionId + " or nameOrId: " + nameOrId + " cannot be null");
        }

        if (descriptionCache.containsKey(cacheKey)) {
            return descriptionCache.get(cacheKey);
        } else {           
            List<Map<String, Object>> flavorTextEntries = (List<Map<String, Object>>) speciesData.get("flavor_text_entries");
            logger.info("gameName: {}", versionId);
            String gameDescription = flavorTextEntries.stream()
                                                    .filter(entry -> versionId.equals(((Map<String, String>) entry.get("version")).get("name")) 
                                                            && "en".equals(((Map<String, String>) entry.get("language")).get("name")))
                                                    .map(entry -> (String) entry.get("flavor_text"))
                                                    .findFirst()
                                                    .orElse("?????????");
            descriptionDto.setDescription(gameDescription);
            descriptionDto.setKey(cacheKey);
            descriptionCache.put(cacheKey, descriptionDto);
            return descriptionDto;
        }
    }
}
