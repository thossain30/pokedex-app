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

                console.log(data);

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
            console.log("Fetched selected Pokémon:", match);
            data = await match.json();
        } catch(err) {
            console.error("Error fetching selected Pokémon:", err);
            console.log("Value:", value);
            data = null;
        }
        setSelectedPokemon(data);
    }

    return (
        <div className="flex flex-col items-center w-full relative">
            <PokemonSearchOverlay 
                onSelectPokemon={ findSelectedPokemon } 
                pokemonList={pokemonList} 
            />
            { selectedPokemon && (
                <div className="absolute z-10 items-center mt-4 w-full h-full"
                    style={{
                        bottom: "-100%",
                        left: "-50%",
                        transform: "translateX(-50%)",
                    }}
                >
                    <PokemonSprite {...selectedPokemon}/>
                    <p className="absolute description bottom-[5%] left-[8%] w-[80%] text-[1rem]">
                        {selectedPokemon.description}
                    </p>
                </div>
            )}
        </div>
    )
}