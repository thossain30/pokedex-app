import React from 'react';
import './NavArrows.css';
import { Pokemon } from '../../config/Helpers';

interface NavArrowsProps {
    onNavigate: (value: string | number) => void;
    pokemonList: Pokemon[];
    currentPokemon: Pokemon | null;
}

const NavArrows: React.FC<NavArrowsProps> = ({onNavigate, pokemonList, currentPokemon}) => {
    const rightArrow = require('../../assets/right_arrow.png');
    const leftArrow = require('../../assets/left_arrow.png');

    const handleNavigate = (direction: 'prev' | 'next') => {
        const currentIndex = pokemonList.findIndex(p => p.id === currentPokemon?.id);
        let newId = currentPokemon ? currentPokemon.id : null;
        if (!currentPokemon) return;

        if (direction === 'prev' && currentIndex >= 0) {
            if (currentIndex > 0) {
                newId = pokemonList[currentIndex - 1].id;
            }
        } else if (direction === 'next' && currentIndex < pokemonList.length - 1) {
            if (currentIndex < pokemonList.length - 1) {
                newId = pokemonList[currentIndex + 1].id;
            }
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
                disabled={!currentPokemon || pokemonList[0].id === currentPokemon.id}
            >
                <img src={leftArrow} alt="Previous" />
            </button>
            <button 
                className="navArrow rightArrow" 
                onClick={() => handleNavigate('next')}
                onKeyDown={(e) => handleArrowKeys(e)}
                disabled={!currentPokemon || pokemonList[pokemonList.length - 1].id === currentPokemon.id}
            >
                <img src={rightArrow} alt="Next" />
            </button>
        </div>
    )
}

export default NavArrows;