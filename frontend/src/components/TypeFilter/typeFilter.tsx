import React, { useState } from "react";
import { Pokemon } from "../../config/Helpers";
import './typeFilter.css';

interface TypeFilterProps {
    value: string;
    onChange: (value: string) => void;
    pokemon: Pokemon[];
}

const types = ["normal", "fire", "water", "grass", "electric", "ice", "fighting", "poison", "ground", "flying", "psychic", "bug", "rock", "ghost", "dark", "dragon", "steel"];

const TypeFilter: React.FC<TypeFilterProps> = ({ value, onChange }) => {
    return (
        <select
            className="border rounded-md px-1 py-0.5 w-24 max-w-[6rem] shrink-0 typeFilterContainer"
            value={value}
            onChange={(e) => onChange(e.target.value)}
        >
            <option value="">Filter by Type</option>
            {types.map((type) => (
                <option key={type} value={type}>
                    {type}
                </option>
            ))}
        </select>
    )
}

export default TypeFilter;

