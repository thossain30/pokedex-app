package com.pokedex.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(ApiEndpoints.BASE_API)
public class PokeApiController {
    private final RestTemplate restTemplate = new RestTemplate();

    // Returns all pokemon based on limit (doing up to end of gen 4 for now)
    @GetMapping(ApiEndpoints.POKEMON)
    public ResponseEntity<String> getAllPokemon(@RequestParam(defaultValue = Constants.GEN_4_LIMIT) int limit) {
        String url = UriComponentsBuilder
            .fromHttpUrl(ApiEndpoints.POKEMON)
            .queryParam("limit", limit)
            .toUriString();
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    // Returns 1 pokemon based on name
    @GetMapping(ApiEndpoints.POKEMON + "/{name}")
    public ResponseEntity<String> getPokemon(@PathVariable String name) {
        String url = ApiEndpoints.POKEMON + name.toLowerCase();
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    // Returns type based on number
    @GetMapping(ApiEndpoints.TYPE + "{num}")
    public ResponseEntity<String> getMethodName(@PathVariable String num) {
        String url = ApiEndpoints.TYPE + num.toLowerCase();
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }
}
