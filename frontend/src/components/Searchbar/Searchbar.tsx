import React, { useState } from "react";
import { Pokemon, capitalizeFirstLetter } from "../../config/Helpers";
import './Searchbar.css'

interface SearchBarProps {
    value: string;
    onChange: (value: string) => void;
    pokemon: Pokemon[];
}

const SearchBar: React.FC<SearchBarProps> = ({ value, onChange, pokemon }) => {
    const [isFocused, setIsFocused] = useState(false);

    const filtered = value
    ? pokemon.filter((p) =>
        p.name.toLowerCase().includes(value.toLowerCase())
      )
    : pokemon;

    return (
        <div className="searchbarContainer relative flex-grow">
            <input 
                type="text"
                value={value}
                placeholder="Search Pokemon..."
                onChange={(e) => onChange(e.target.value)}
                onFocus={() => {
                    setIsFocused(true);
                }}
                onBlur={() => setTimeout(() => setIsFocused(false), 200)}
                className="w-full p-2 px-2 py-1 flex-grow"
            />

            {isFocused && (
                <ul className="z-10 yellow-bg absolute w-full max-h-60 overflow-y-auto rounded shadow">
                    {filtered.map((poke) => (
                        <li 
                            key={ poke.id }
                            className="px-2 py-1 nameItem"
                            onClick={() => { onChange(capitalizeFirstLetter(poke.name)); }}
                        >
                            {capitalizeFirstLetter(poke.name)}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default SearchBar;