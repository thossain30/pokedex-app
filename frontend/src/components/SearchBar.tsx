import React, { useState } from "react";
import { ENDPOINTS } from "../config/endpoints";
import './Searchbar.css'

function SearchBar() {
    const [query, setQuery] = useState("");
    const [options, setOptions] = useState<Pokemon[]>([]);
    const [showDropdown, setShowDropdown] = useState(false);
    const [loaded, setLoaded] = useState(false); // track if we’ve already fetched

    interface Pokemon {
        name: string,
        id: number
    }

    function capitalizeFirstLetter(str: string) {
        return str.charAt(0).toUpperCase() + str.slice(1);
    }
    
    const fetchAllPokemon = async () => {
        if (loaded) return;

        try {
            const res = await fetch(ENDPOINTS.ALL_POKEMON);
            const data = await res.json();

            const pokemonList = data.map((poke: {name: string, url: string}) => {
                const id = parseInt(poke.url.split("/").filter(Boolean).pop()!)
                return { id, name: capitalizeFirstLetter(poke.name) };
            })
            setOptions(pokemonList);
            setLoaded(true);
        } catch (err) {
            console.error("Error fetching Pokémon:", err);
        }
    }

    return (
        <div className="searchbarContainer">
            <input 
                type="text"
                value={query}
                placeholder="Search Pokemon..."
                onChange={(e) => setQuery(e.target.value)}
                onFocus={() => {
                    fetchAllPokemon();
                    setShowDropdown(true);
                }}
                onBlur={() => setTimeout(() => setShowDropdown(false), 200)}
                className="w-full p-2"
            />

            {showDropdown && options.length > 0 && (
                <ul className="z-10 yellow-bg w-full max-h-60 overflow-y-auto rounded shadow">
                    {options.filter((poke) => 
                        poke.name.toLowerCase().includes(query.toLowerCase())
                    )
                    .map((poke) => (
                        <li 
                            key={ poke.id }
                            onClick={() => {
                                setQuery(poke.name)
                                setShowDropdown(false)
                            }}
                        >
                            {poke.name}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default SearchBar;