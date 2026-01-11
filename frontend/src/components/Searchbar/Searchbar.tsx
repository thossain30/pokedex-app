import React, { useState, useRef, useEffect } from "react";
import { Pokemon, capitalizeFirstLetter } from "../../config/Helpers";
import './Searchbar.css'

interface SearchBarProps {
    value: string;
    onChange: (value: string) => void;
    pokemon: Pokemon[];
}

const SearchBar: React.FC<SearchBarProps> = ({ value, onChange, pokemon }) => {
    const [isFocused, setIsFocused] = useState(false);
    const [activeIndex, setActiveIndex] = useState(-1);
    const itemRefs = useRef<(HTMLLIElement | null)[]>([]);

    
    useEffect(() => {
        if (activeIndex >= 0) {
            itemRefs.current[activeIndex]?.scrollIntoView({
                behavior: 'smooth',
                block: 'nearest'
            })
        }
    }, [activeIndex]);

    const filtered = value
    ? pokemon.filter((p) =>
        p.name.toLowerCase().includes(value.toLowerCase())
      )
    : pokemon;

    const handleSearchBarKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (filtered.length === 0) return;

        if (e.key === 'ArrowDown') {
            e.preventDefault();
            setActiveIndex((prev) => prev < filtered.length - 1 ? prev + 1 : 0);
        } 

        if (e.key === 'ArrowUp') {
            e.preventDefault();
            setActiveIndex((prev) => prev > 0 ? prev - 1 : filtered.length - 1);
        }

        if (e.key === 'Enter' && activeIndex >= 0) {
            e.preventDefault();
            onChange(capitalizeFirstLetter(filtered[activeIndex].name));
            setActiveIndex(-1);
            setIsFocused(false);
        }

        if (e.key === 'Backspace') {
            setIsFocused(true);
        }
    }

    return (
        <div className="searchbarContainer relative flex-grow">
            <input 
                type="text"
                value={value}
                placeholder="Search Pokemon..."
                onKeyDown={(e) => handleSearchBarKeyDown(e)}
                onChange={(e) => onChange(e.target.value)}
                onSelect={() => onChange(value)}
                onFocus={() => {
                    setIsFocused(true);
                }}
                onBlur={() => setTimeout(() => setIsFocused(false), 200)}
                className="w-full p-2 px-2 py-1 rounded-md flex-grow"
            />

            {isFocused && (
                <ul className="z-10 yellow-bg absolute w-full max-h-60 overflow-y-auto rounded shadow">
                    {filtered.map((poke, index) => (
                        <li 
                            key={ poke.id }
                            className={`px-2 py-1 nameItem ${activeIndex === index ? 'active' : ''}`}
                            ref={(el) => { itemRefs.current[index] = el; }}
                            onMouseEnter={() => setActiveIndex(index)}
                            onClick={() => { 
                                onChange(capitalizeFirstLetter(poke.name)); 
                                setActiveIndex(-1);
                            }}
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