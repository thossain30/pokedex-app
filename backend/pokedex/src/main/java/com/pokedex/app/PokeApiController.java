package com.pokedex.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.pokedex.app.Service.PokemonDetailsService;
import com.pokedex.app.model.PokemonDto;
import com.pokedex.app.model.PokemonResponse;
import com.pokedex.app.model.PokemonResults;

@RestController
@RequestMapping()
@CrossOrigin(origins = "http://localhost:3000") // allow requests from React
public class PokeApiController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final PokemonDetailsService pokemonDetailsService;
    private final Map<String, List<String>> cache = new ConcurrentHashMap<>();

    public PokeApiController(PokemonDetailsService pokemonDetailsService) {
        this.pokemonDetailsService = pokemonDetailsService;
    }

    // Returns all pokemon based on limit (doing up to end of gen 4 for now)
    @SuppressWarnings("null")
    @GetMapping("/pokemon")
    public ResponseEntity<List<PokemonResults>> getAllPokemon(@RequestParam(defaultValue = "" + Constants.GEN_4_LIMIT) int limit) {
        String url = UriComponentsBuilder
            .fromHttpUrl(ApiEndpoints.POKEMON)
            .queryParam("limit", limit)
            .toUriString();
        PokemonResponse response = restTemplate.getForObject(url, PokemonResponse.class);
        List<PokemonResults> results = response.getResults();
        setTypesForResults(results);
        return ResponseEntity.ok(results);
    }

    // Returns 1 pokemonDto based on name or id
    @GetMapping("/pokemon/{nameOrId}")
    public ResponseEntity<PokemonDto> getPokemon(@PathVariable String nameOrId, 
                                             @RequestParam(defaultValue = "generation-iv") String generation,
                                             @RequestParam(defaultValue = "platinum") String gameName) {
        return ResponseEntity.ok(pokemonDetailsService.getPokemonDetails(nameOrId, generation, gameName));
    }


    public void setTypesForResults(List<PokemonResults> results) {
        for (PokemonResults result : results) {
            String cacheKey = "types_" + results.size();
            if (cache.containsKey(cacheKey)) {
                result.setTypes(cache.get(cacheKey).toArray(new String[0]));
                continue;
            }
            JsonNode pokemonData = restTemplate.getForObject(result.getUrl(), JsonNode.class);
            JsonNode selectedTypes = pokemonData.path("types");
            JsonNode pastTypes = pokemonData.path("past_types");

            if (pastTypes != null && pastTypes.isArray() && pastTypes.size() > 0) {
                selectedTypes = pastTypes.path("types");
            }

            List<String> typeNames = new ArrayList<>();
            for (JsonNode type : selectedTypes) {
                typeNames.add(type.path("type").path("name").asText());
            }
            result.setTypes(typeNames.toArray(new String[0]));
        }
    }
}
