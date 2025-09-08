package com.pokedex.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(ApiEndpoints.BASE_API)
public class PokeApiController {
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping(ApiEndpoints.POKEMON + "/{name}")
    public ResponseEntity<String> getPokemon(@PathVariable String name) {
        // Call the PokeAPI
        String url = ApiEndpoints.POKEMON + name.toLowerCase();
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiEndpoints.TYPE + "{num}")
    public ResponseEntity<String> getMethodName(@PathVariable String num) {
        String url = ApiEndpoints.TYPE + num.toLowerCase();
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }
    
}
