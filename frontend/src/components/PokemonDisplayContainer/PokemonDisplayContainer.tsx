import { useEffect, useState } from "react";
import { Pokemon, PokemonDetails } from "../../config/Helpers";
import { ENDPOINTS } from "../../config/endpoints";
import './PokemonDisplayContainer.css';
import PokemonSearchOverlay from "../PokemonSearchOverlay/PokemonSearchOverlay";
import PokemonSprite from "../PokemonSprite/pokemonSprite";

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

    return (
        <div className="w-full h-full relative">
            <div className="searchOverlayContainer">
                <PokemonSearchOverlay 
                    onSelectPokemon={ findSelectedPokemon } 
                    pokemonList={pokemonList} 
                />
            </div>

            { selectedPokemon && (
                <div className="">
                    <div className="sprite">
                        <PokemonSprite {...selectedPokemon}/>
                    </div>

                    <p className="description leading-tight">
                        {selectedPokemon.description}
                    </p>
                </div>
            )}
        </div>
    )
}