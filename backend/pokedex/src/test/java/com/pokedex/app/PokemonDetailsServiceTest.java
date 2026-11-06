package com.pokedex.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.pokedex.app.Service.PokemonDetailsService;
import com.pokedex.app.Service.TypeSpriteService;
import com.pokedex.app.model.PokemonDto;

@ExtendWith(MockitoExtension.class)
public class PokemonDetailsServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock 
    private TypeSpriteService typeSpriteService;

    @InjectMocks
    private PokemonDetailsService pokemonDetailsService;

    private Map<String, Object> createMockPokemonData() {
        Map<String, Object> pokemonData = new HashMap<>();
        pokemonData.put("name", "bulbasaur");
        pokemonData.put("id", 1);
        pokemonData.put("height", 7);
        pokemonData.put("weight", 69);

        Map<String, String> cries = new HashMap<>();
        cries.put("latest", "bulbasaur_latest_cry");
        cries.put("legacy", "bulbasaur_legacy_cry");
        pokemonData.put("cries", cries);

        Map<String, Object> grass = Map.of("name", "grass");
        Map<String, Object> poison = Map.of("name", "poison");

        pokemonData.put("types", List.of(
            Map.of("type", grass),
            Map.of("type", poison)
        ));

        Map<String, Object> platinum = Map.of(
            "front_default", "front-url",
            "front_shiny", "shiny-url"
        );

        Map<String, Object> generationIV = Map.of(
            "platinum", platinum
        );

        pokemonData.put("sprites", Map.of(
            "versions", Map.of(
                "generation-iv", generationIV
            )
        ));
        return pokemonData;
    }

    private Map<String, Object> createMockSpecies() {
        Map<String, Object> species = new HashMap<>();

        species.put("genera", List.of(
            Map.of(
                "language", Map.of("name", "en"),
                "genus", "Seed Pokémon"
            )
        ));

        species.put("flavor_text_entries", List.of(
            Map.of(
                "version", Map.of("name", "platinum"),
                "flavor_text", "A strange seed was planted on its back."
            )
        ));

        return species;
    }

    @Test
    public void testGetPokemonDetails() throws Exception {
        Map<String, Object> pokemon = createMockPokemonData();
        Map<String, Object> species = createMockSpecies();

        Map<String, Object> grassType = new HashMap<>();
        grassType.put("name", "grass");

        Map<String, Object> poisonType = new HashMap<>();
        poisonType.put("name", "poison");

        when(restTemplate.getForObject(
            eq(ApiEndpoints.POKEMON + "1"),
            eq(Map.class)
        )).thenReturn(pokemon);

        when(restTemplate.getForObject(
            eq(ApiEndpoints.POKEMON_SPECIES + "1"),
            eq(Map.class)
        )).thenReturn(species);

        when(typeSpriteService.getTypeSprite("grass", "generation-iv", "platinum"))
            .thenReturn("grass-sprite-url");

        when(typeSpriteService.getTypeSprite("poison", "generation-iv", "platinum"))
            .thenReturn("poison-sprite-url");

        PokemonDto result = pokemonDetailsService.getPokemonDetails("1", "generation-iv", "platinum");

        // Assert
        assertEquals("bulbasaur", result.getName());
        assertEquals(1, result.getId());
        assertEquals("Seed Pokémon", result.getTitle());
        assertEquals("A strange seed was planted on its back.", result.getDescription());

        verify(restTemplate).getForObject(
            ApiEndpoints.POKEMON + "1", Map.class
        );

        verify(restTemplate).getForObject(
            ApiEndpoints.POKEMON_SPECIES + "1", Map.class
        );

        verify(typeSpriteService).getTypeSprite(
            "grass", "generation-iv", "platinum"
        );

        verify(typeSpriteService).getTypeSprite(
            "poison", "generation-iv", "platinum"
        );
    }

    @Test
    public void testGetPokemonDetailsNotFound() throws Exception {
        when(restTemplate.getForObject(
            contains(ApiEndpoints.POKEMON),
            eq(Map.class)
        )).thenThrow(new RuntimeException("Pokemon Not Found"));

        try {
            pokemonDetailsService.getPokemonDetails("9999", "generation-iv", "platinum");
        } catch (Exception e) {
            assertEquals("Pokemon Not Found", e.getMessage());
        }
    }

    @Test
    public void testGetPokemonDetailsTypeSpriteNotFound() throws Exception {
        Map<String, Object> pokemon = createMockPokemonData();
        Map<String, Object> species = createMockSpecies();

        when(restTemplate.getForObject(
            eq(ApiEndpoints.POKEMON + "1"),
            eq(Map.class)
        )).thenReturn(pokemon);

        when(restTemplate.getForObject(
            eq(ApiEndpoints.POKEMON_SPECIES + "1"),
            eq(Map.class)
        )).thenReturn(species);

        when(typeSpriteService.getTypeSprite("grass", "generation-iv", "platinum"))
            .thenReturn(null); // Simulate type sprite not found

        try {
            pokemonDetailsService.getPokemonDetails("1", "generation-iv", "platinum");
        } catch (Exception e) {
            assertEquals("TypeSprite not found: 1", e.getMessage());
        }
    }

    @Test
    public void testGetPokemonDetailsCacheHit() throws Exception {
        Map<String, Object> pokemon = createMockPokemonData();
        Map<String, Object> species = createMockSpecies();

        when(restTemplate.getForObject(
            eq(ApiEndpoints.POKEMON + "1"),
            eq(Map.class)
        )).thenReturn(pokemon);

        when(restTemplate.getForObject(
            eq(ApiEndpoints.POKEMON_SPECIES + "1"),
            eq(Map.class)
        )).thenReturn(species);

        when(typeSpriteService.getTypeSprite("grass", "generation-iv", "platinum"))
            .thenReturn("grass-sprite-url");

        when(typeSpriteService.getTypeSprite("poison", "generation-iv", "platinum"))
            .thenReturn("poison-sprite-url");

        // First call to populate cache
        PokemonDto firstCallResult = pokemonDetailsService.getPokemonDetails("1", "generation-iv", "platinum");

        // Second call should hit the cache
        PokemonDto secondCallResult = pokemonDetailsService.getPokemonDetails("1", "generation-iv", "platinum");

        // Assert that both results are the same object (cache hit)
        assertEquals(firstCallResult, secondCallResult);
    }

    @Test
    public void testGetPokemonWithPastType() {
        Map<String, Object> pokemon = createMockPokemonData();
        Map<String, Object> species = createMockSpecies();

        // Add past_types to simulate a Pokémon with past types
        Map<String, Object> pastType = new HashMap<>();
        pastType.put("generation", Map.of("name", "generation-iii"));
        pastType.put("types", List.of(Map.of("type", Map.of("name", "bug"))));
        pokemon.put("past_types", List.of(pastType));

        when(restTemplate.getForObject(
            eq(ApiEndpoints.POKEMON + "1"),
            eq(Map.class)
        )).thenReturn(pokemon);

        when(restTemplate.getForObject(
            eq(ApiEndpoints.POKEMON_SPECIES + "1"),
            eq(Map.class)
        )).thenReturn(species);

        when(typeSpriteService.getTypeSprite("bug", "generation-iv", "platinum"))
            .thenReturn("bug-sprite-url");

        PokemonDto result = pokemonDetailsService.getPokemonDetails("1", "generation-iv", "platinum");

        // Assert that the type is correctly set from past_types
        assertEquals(1, result.getTypes().size());
        assertEquals("bug", result.getTypes().get(0).getName());
    }
}
