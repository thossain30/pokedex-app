package com.pokedex.app;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.pokedex.app.Service.DtoPopulationService;
import com.pokedex.app.model.DescriptionDto;
import com.pokedex.app.model.GenerationDto;
import com.pokedex.app.model.PokemonDto;
import com.pokedex.app.model.PokemonResponse;
import com.pokedex.app.model.PokemonResults;

@RestController
@RequestMapping()
@CrossOrigin(origins = "http://localhost:3000") // allow requests from React
public class PokeApiController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final DtoPopulationService dtoPopulationService;

    public PokeApiController(DtoPopulationService dtoPopulationService) {
        this.dtoPopulationService = dtoPopulationService;
    }

    // Returns all pokemon based on limit (doing up to end of gen 4 for now)
    @GetMapping("/pokemon")
    public ResponseEntity<List<PokemonResults>> getAllPokemon(@RequestParam(defaultValue = "1350") int limit) {
        String url = UriComponentsBuilder
            .fromHttpUrl(ApiEndpoints.POKEMON)
            .queryParam("limit", limit)
            .toUriString();
        PokemonResponse response = restTemplate.getForObject(url, PokemonResponse.class);
        return ResponseEntity.ok(response.getResults());
    }

    // Returns 1 pokemonDto based on name or id
    @GetMapping("/pokemon/{nameOrId}")
    public ResponseEntity<PokemonDto> getPokemon(@PathVariable String nameOrId) {
        return ResponseEntity.ok(dtoPopulationService.getPokemonDetails(nameOrId));
    }

    // Returns 1 generationDto based on name or id with changing sprites based on type and generation
    @GetMapping("/generation/{genNum}/{nameOrId}")
    public ResponseEntity<GenerationDto> getGenerationDto(@PathVariable int genNum, 
                                                          @PathVariable String nameOrId, 
                                                          @RequestParam(required = false) String typeGame) {
        if (typeGame == null || typeGame.isEmpty()) {
            // default to a game version string for type sprites, as they are the same between different game versions in the same generation
            typeGame = Helpers.getGameVersionForTypeString(genNum);
        }
        return ResponseEntity.ok(dtoPopulationService.setUpGenerationDto(genNum, nameOrId, typeGame));
    }

    // Returns description string based on game version and pokemon name or id
    @GetMapping("/description/{versionId}/{nameOrId}")
    public ResponseEntity<DescriptionDto> getDescription(@PathVariable String versionId, 
                                                         @PathVariable String nameOrId) {
        return ResponseEntity.ok(dtoPopulationService.generateDescriptionDto(versionId, nameOrId));
    }
}
