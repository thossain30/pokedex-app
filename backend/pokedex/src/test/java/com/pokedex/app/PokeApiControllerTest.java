package com.pokedex.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.pokedex.app.Service.PokemonDetailsService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PokeApiController.class)
public class PokeApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonDetailsService pokemonDetailsService;

    @Test
    public void testGetAllPokemon() throws Exception {
        // Test logic for getAllPokemon method
        mockMvc.perform(get("/pokemon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("bulbasaur"))
                .andExpect(jsonPath("$[492].name").value("arceus"));
    }

    @Test
    public void testGetPokemonById() throws Exception {
        // Test logic for getPokemonById method
        mockMvc.perform(get("/pokemon/1"))
                .andExpect(status().isOk());
        verify(pokemonDetailsService).getPokemonDetails("1", "generation-iv", "platinum");
    }
}
