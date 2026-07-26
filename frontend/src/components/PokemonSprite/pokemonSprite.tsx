import { PokemonDetails } from "../../config/Helpers";
import './pokemonSprite.css';

const PokemonSprite = ({pokemon, isShiny}: {pokemon: PokemonDetails | null, isShiny: boolean}) => {
    if (!pokemon) return null;
    const spriteUrl = isShiny ? pokemon.spriteUrls[1] : pokemon.spriteUrls[0];
    return (
        <img src={spriteUrl} alt={pokemon.name} className="sprite object-contain mt-2"/>
    )
}

export default PokemonSprite;