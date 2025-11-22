import { PokemonDetails } from "../../config/Helpers";
import './pokemonSprite.css';

const PokemonSprite = (pokemon: PokemonDetails | null) => {
    if (!pokemon) return null;
    else {
        console.log("Rendering sprite for:", pokemon.spriteUrl);
    }
    return (
        <img src={pokemon.spriteUrl} alt={pokemon.name} className="sprite object-contain mt-2"/>
    )
}

export default PokemonSprite;