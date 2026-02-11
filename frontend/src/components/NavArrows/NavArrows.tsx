import React from 'react';
import './NavArrows.css';
import { Pokemon } from '../../config/Helpers';

interface NavArrowsProps {
    onNavigate: (value: string | number) => void;
    pokemonList: Pokemon[];
    currentPokemon: { id: number; name: string } | null;
}

const NavArrows: React.FC<NavArrowsProps> = ({onNavigate, pokemonList, currentPokemon}) => {
    const rightArrow = require('../../assets/right_arrow.png');
    const leftArrow = require('../../assets/left_arrow.png');
    const handleNavigate = (direction: 'prev' | 'next') => {
        let newId = currentPokemon ? currentPokemon.id : null;
        if (!currentPokemon) return;

        if (direction === 'prev' && currentPokemon.id > 1) {
            newId = currentPokemon.id - 1;
        } else if (direction === 'next' && currentPokemon.id < pokemonList.length) {
            newId = currentPokemon.id + 1;
        }

        const match = pokemonList.find(p => p.id === newId);
        onNavigate(match ? match.id : currentPokemon.id);
    }

    const handleArrowKeys = (e: React.KeyboardEvent<HTMLButtonElement>) => {
        if (e.key === 'ArrowLeft') {
            handleNavigate('prev');
        } else if (e.key === 'ArrowRight') {
            handleNavigate('next');
        }
    }

    return(
        <div className="navArrowsContainer">
            <button 
                className="navArrow leftArrow" 
                onClick={() => handleNavigate('prev')}
                onKeyDown={(e) => handleArrowKeys(e)}
                disabled={!currentPokemon || currentPokemon.id === 1}
            >
                <img src={leftArrow} alt="Previous" />
            </button>
            <button 
                className="navArrow rightArrow" 
                onClick={() => handleNavigate('next')}
                onKeyDown={(e) => handleArrowKeys(e)}
                disabled={!currentPokemon || currentPokemon.id === pokemonList.length}
            >
                <img src={rightArrow} alt="Next" />
            </button>
        </div>
    )
}

export default NavArrows;