import { PokemonDetails } from "../../config/Helpers";

const PokemonSprite = (pokemon: PokemonDetails | null) => {
    if (!pokemon) return null;
    return (
        <div>
            <img src={pokemon.spriteUrl} alt={pokemon.name} 
                className="object-contain mt-2"
                style={{
                    top: "40%",   // vertical position
                    left: "40%",  // horizontal position
                    transform: "translate(-17%, 22%)",
                    height: "20vw",
                    width: "auto"
                }}
            />
        </div>
    )
}

export default PokemonSprite;