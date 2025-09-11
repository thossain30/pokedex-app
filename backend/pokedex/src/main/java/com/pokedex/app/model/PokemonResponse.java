package com.pokedex.app.model;

import java.util.List;

public class PokemonResponse {
    private List<PokemonResults> results;

    public List<PokemonResults> getResults() { return results; };
    public void setResults(List<PokemonResults> results) { this.results = results; }
}
