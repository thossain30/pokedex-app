import { GenerationDetails } from "../../config/Helpers";
import './pokemonSprite.css';

const PokemonSprite = ({generation, isShiny}: {generation: GenerationDetails | null, isShiny: boolean}) => {
    let spriteUrl = "";
    if (!generation || !generation.spriteUrls || generation.spriteUrls.length === 0) return null;
    if (generation.spriteUrls[1] === null || generation.spriteUrls[1] === undefined) {
        spriteUrl = generation.spriteUrls[0];
    } else {
        spriteUrl = isShiny ? generation.spriteUrls[1] : generation.spriteUrls[0];
    }
    return (
        <img src={spriteUrl} alt={generation.name} className="sprite object-contain mt-2"/>
    )
}

export default PokemonSprite;