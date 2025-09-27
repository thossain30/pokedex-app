import React, { useEffect, useState } from "react";
import SearchBar from "../Searchbar/Searchbar";
import { ENDPOINTS } from "../../config/endpoints";
import IdDropdown from "../IdDropdown/IdDropdown";
import { capitalizeFirstLetter } from "../../config/Helpers";
import './PokemonSearchOverlay.css';

interface Pokemon {
    name: string,
    id: number
}

const PokemonSearchOverlay = () => {
    const [search, setSearch] = useState("");
    const [selectedId, setSelectedId] = useState<number | "">("");
    const [pokemonList, setPokemonList] = useState<Pokemon[]>([]);

    useEffect(() => {
        const fetchPokemon = async () => {
            try {
                const res = await fetch(ENDPOINTS.ALL_POKEMON);
                const data = await res.json();

                console.log(data);

                const list: Pokemon[] = data.map(
                (poke: { name: string; url: string }) => {
                    const id = parseInt(poke.url.split("/").filter(Boolean).pop()!);
                    return { id, name: poke.name };
                }
                );

                setPokemonList(list);
            } catch (err) {
                console.error("Error fetching Pokémon:", err);
            }
        };
    fetchPokemon();
  }, []);

    const handleSearchChange = (value: string) => {
        setSearch(value);
        const match = pokemonList.find(p => p.name.toLowerCase() === value.toLowerCase());
        setSelectedId(match ? match.id : "");
    }

    const handleDropDownChange = (id: number | "") => {
        setSelectedId(id);
        const match = pokemonList.find(p => p.id === id);
        setSearch(match ? capitalizeFirstLetter(match.name) : "");
    }

    return (
      <div className="flex flex-col gap-2 w-fit max-w-full overlayContainer mx-auto sm:flex-row">
        <IdDropdown value={selectedId} onChange={handleDropDownChange} pokemon={pokemonList} />
        <SearchBar value={search} onChange={handleSearchChange} pokemon={pokemonList} />
      </div>
    );
};

export default PokemonSearchOverlay;