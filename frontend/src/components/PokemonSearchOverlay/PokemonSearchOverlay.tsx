import React, { useEffect, useState } from "react";
import SearchBar from "../Searchbar/Searchbar";
import IdDropdown from "../IdDropdown/IdDropdown";
import { capitalizeFirstLetter } from "../../config/Helpers";
import './PokemonSearchOverlay.css';
import { Pokemon } from "../../config/Helpers";
import TypeFilter from "../TypeFilter/typeFilter";

interface PokemonSearchOverlayProps {
    pokemonList: Pokemon[];
    allPokemon: Pokemon[];
    onSelectPokemon: (value: string | number) => void;
    currentPokemon?: { id: number; name: string; } | null;
    typeFilter: string;
    onTypeFilterChange: (value: string) => void;
}


const PokemonSearchOverlay:React.FC<PokemonSearchOverlayProps> = ({ onSelectPokemon, allPokemon, pokemonList, currentPokemon, typeFilter, onTypeFilterChange }) => {
    const [search, setSearch] = useState("");
    const [selectedId, setSelectedId] = useState<number | "">("");

    useEffect(() => {
        if (!currentPokemon || !currentPokemonExists) return;

        setSearch(capitalizeFirstLetter(currentPokemon.name));
        setSelectedId(currentPokemon.id);
    }, [currentPokemon]);

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

    const handleTypeFilterChange = (value: string) => {
        onTypeFilterChange(value);
        const newList = value ? allPokemon.filter(p => p.typesList?.includes(value.toLowerCase())) : allPokemon;

        if (newList.length === 0) return;

        setSearch(capitalizeFirstLetter(newList[0].name));
        setSelectedId(newList[0].id);
        onSelectPokemon(newList[0].name);
    }

    const currentPokemonExists = currentPokemon && pokemonList.some(p => p.name === currentPokemon.name);
    
    return (
      <div className="flex flex-col gap-2 w-fit max-w-full overlayContainer mx-auto sm:flex-row">
        <IdDropdown value={selectedId} onChange={handleDropDownChange} pokemon={pokemonList} />
        <SearchBar value={search} onChange={handleSearchChange} pokemon={pokemonList}   />
        <TypeFilter value={typeFilter} onChange={handleTypeFilterChange} pokemon={pokemonList} />
      </div>
    );
};

export default PokemonSearchOverlay;