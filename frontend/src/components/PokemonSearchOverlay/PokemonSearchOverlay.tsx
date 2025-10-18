import React, { useEffect, useState } from "react";
import SearchBar from "../Searchbar/Searchbar";
import IdDropdown from "../IdDropdown/IdDropdown";
import { capitalizeFirstLetter } from "../../config/Helpers";
import './PokemonSearchOverlay.css';
import { Pokemon } from "../../config/Helpers";

interface PokemonSearchOverlayProps {
    pokemonList: Pokemon[];
    onSelectPokemon: (value: string | number) => void;
}


const PokemonSearchOverlay:React.FC<PokemonSearchOverlayProps> = ({ onSelectPokemon, pokemonList }) => {
    const [search, setSearch] = useState("");
    const [selectedId, setSelectedId] = useState<number | "">("");

    const handleSearchChange = (value: string) => {
        setSearch(value);
        const match = pokemonList.find(p => p.name.toLowerCase() === value.toLowerCase());
        setSelectedId(match ? match.id : "");
        onSelectPokemon(match ? match.name : value);
    }

    const handleDropDownChange = (id: number | "") => {
        setSelectedId(id);
        const match = pokemonList.find(p => p.id === id);
        setSearch(match ? capitalizeFirstLetter(match.name) : "");
        onSelectPokemon(match ? match.id : "");
    }
    
    return (
      <div className="flex flex-col gap-2 w-fit max-w-full overlayContainer mx-auto sm:flex-row">
        <IdDropdown value={selectedId} onChange={handleDropDownChange} pokemon={pokemonList} />
        <SearchBar value={search} onChange={handleSearchChange} pokemon={pokemonList} />
      </div>
    );
};

export default PokemonSearchOverlay;