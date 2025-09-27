import React, { useState } from "react";
import { Pokemon } from "../../config/Helpers";
import './IdDropdown.css';

interface IdDropdownProps {
    value: number | "";
    onChange: (value: number | "") => void;
    pokemon: Pokemon[];
}

const IdDropdown: React.FC<IdDropdownProps> = ({ value, onChange, pokemon }) => {
    return (
        <select
            className="border rounded-md px-1 py-0.5 w-16 max-w-[4rem] shrink-0 dropdownContainer"
            value={value}
            onChange={(e) => {onChange(Number(e.target.value))}}
        >
            <option value="">#</option>
            {pokemon.map((p) => (
                <option key={p.id} value={p.id}>
                    {p.id}
                </option>
            ))}
        </select>
    );
};

export default IdDropdown;