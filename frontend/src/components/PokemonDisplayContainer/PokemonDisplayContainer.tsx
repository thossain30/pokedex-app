import { useEffect, useState } from "react";
import { Pokemon, PokemonDetails } from "../../config/Helpers";
import { ENDPOINTS } from "../../config/endpoints";
import './PokemonDisplayContainer.css';
import PokemonSearchOverlay from "../PokemonSearchOverlay/PokemonSearchOverlay";
import NavArrows from "../NavArrows/NavArrows";
import PokemonSprite from "../PokemonSprite/pokemonSprite";
import TypeSprite from "../TypeSprite/typeSprite";
import CryButton from "../CryButton/CryButton";

export default function PokemonDisplayContainer() {
    const [selectedPokemon, setSelectedPokemon] = useState<PokemonDetails | null>(null);
    const [pokemonList, setPokemonList] = useState<Pokemon[]>([]);

    useEffect(() => {
        const fetchPokemonList = async () => {
            try {
                const res = await fetch(ENDPOINTS.ALL_POKEMON);
                const data = await res.json();

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
    fetchPokemonList();
  }, []);

    const findSelectedPokemon = async (value: string | number) => {
        let data;
        try {
            const match = await fetch(ENDPOINTS.POKEMON_BY_ID(value));
            data = await match.json();
        } catch(err) {
            console.error("Error fetching selected Pokémon:", err);
            data = null;
        }
        setSelectedPokemon(data);
    }

    const getTypeSprites = (pokemon: PokemonDetails | null) => {
        if (!pokemon) return null;
        else if (pokemon.types == null) return null;
        else {
            return pokemon.types.map(type => (
                <TypeSprite {...type} />
            ))
        }
    }

    return (
        <div className="w-full h-full relative">
            <div className="navArrows">
                <NavArrows 
                    onNavigate={ findSelectedPokemon } 
                    pokemonList={ pokemonList }
                    currentPokemon={ selectedPokemon } 
                />
            </div>

            <div className="searchOverlayContainer">
                <PokemonSearchOverlay 
                    onSelectPokemon={ findSelectedPokemon } 
                    pokemonList={ pokemonList }
                />
            </div>

            { selectedPokemon && (
                <div>
                    <div className="sprite">
                        <PokemonSprite {...selectedPokemon}/>
                    </div>

                    <p className="description leading-tight">
                        {selectedPokemon.description}
                    </p>

                    <div className="htwt">
                        <p>{selectedPokemon.height}</p>
                        <p>{selectedPokemon.weight}</p>
                    </div>
                    <div className="title">
                        <p>{selectedPokemon.title}</p>
                    </div>
                    <div className="typeSprites">
                        {getTypeSprites(selectedPokemon)}
                    </div>
                    <div className="CryBtn">
                        <CryButton {...selectedPokemon} />
                    </div>
                </div>
            )}
        </div>
    )
}