package com.pokedex.app.Service;

import java.util.Map;
import java.util.Objects;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.pokedex.app.ApiEndpoints;

@Service
public class PokeApiService {

    private final RestTemplate restTemplate;

    public PokeApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "pokemon", key = "#nameOrId")
    public Map<String, Object> fetchPokemonData(String nameOrId) {
        try {
            Map<String, Object> response =
                restTemplate.getForObject(ApiEndpoints.POKEMON + nameOrId, Map.class);

            if (response == null) {
                throw new RuntimeException("Pokemon data is null for: " + nameOrId);
            }

            return response;

        } catch (HttpClientErrorException.NotFound e) {
            // 404 from API
            throw new RuntimeException("Pokemon not found: " + nameOrId);

        } catch (HttpServerErrorException e) {
            // 5xx errors
            throw new RuntimeException("Pokemon API is down. Try again later.", e);

        } catch (RestClientException e) {
            // Other errors (e.g. network issues)
            throw new RuntimeException("Error fetching Pokemon data: " + e.getMessage(), e);
        }
    }

    @Cacheable(value = "pokemonSpecies", key = "#nameOrId")
    public Map<String, Object> fetchSpeciesData(String nameOrId) {
        try {
            Map<String, Object> response =
                restTemplate.getForObject(ApiEndpoints.POKEMON_SPECIES + nameOrId, Map.class);

            if (response == null) {
                throw new RuntimeException("Species data is null for: " + nameOrId);
            }

            return response;

        } catch (HttpClientErrorException.NotFound e) {
            // 404 from API
            throw new RuntimeException("Pokemon Species not found: " + nameOrId);

        } catch (HttpServerErrorException e) {
            // 5xx errors
            throw new RuntimeException("Pokemon API is down. Try again later.", e);

        } catch (RestClientException e) {
            // Other errors (e.g. network issues)
            throw new RuntimeException("Error fetching Pokemon species data: " + e.getMessage(), e);
        }
    }
}
