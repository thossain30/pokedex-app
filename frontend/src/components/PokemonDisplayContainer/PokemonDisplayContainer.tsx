import { useEffect, useState } from "react";
import { DescriptionDto, GenerationDetails, Pokemon, PokemonDetails } from "../../config/Helpers";
import { ENDPOINTS } from "../../config/endpoints";
import './PokemonDisplayContainer.css';
import PokemonSearchOverlay from "../PokemonSearchOverlay/PokemonSearchOverlay";
import NavArrows from "../NavArrows/NavArrows";
import PokemonSprite from "../PokemonSprite/pokemonSprite";
import TypeSprite from "../TypeSprite/typeSprite";
import CryButton from "../CryButton/CryButton";

export default function PokemonDisplayContainer() {
    const [selectedPokemon, setSelectedPokemon] = useState<PokemonDetails | null>(null);
    const [genNum, setGenNum] = useState<number>(1);
    const [selectedGeneration, setSelectedGeneration] = useState<GenerationDetails>({} as GenerationDetails);
    const [selectedVersion, setSelectedVersion] = useState<string>('');
    const [description, setDescription] = useState<DescriptionDto>({} as DescriptionDto);
    const [pokemonList, setPokemonList] = useState<Pokemon[]>([]);
    const [isShiny, setIsShiny] = useState(false);

    const generations = [1, 2, 3, 4, 5, 6, 7, 8, 9];

    useEffect(() => {
        const fetchPokemonList = async () => {
            try {
                const res = await fetch(ENDPOINTS.ALL_POKEMON);
                if (!res.ok) {
                    throw new Error("Failed to fetch Pokémon list");
                }
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
    
    useEffect(() => {
        const fetchGenerationData = async () => {
            const generationData = await fetch(ENDPOINTS.GEN_BY_ID(genNum, selectedPokemon ? selectedPokemon.id : 1));
            let generationDto = await generationData.json();
            console.log('Fetched generation data: ', generationDto);
            setSelectedGeneration(generationDto);

            const gameVersionLocal = generationDto.gameNames[2];
            console.log('gameVersionLocal: ', gameVersionLocal);
            setSelectedVersion(gameVersionLocal);
            try {
                const descriptionData = await fetch(ENDPOINTS.DESCRIPTION_BY_ID(gameVersionLocal, selectedPokemon ? selectedPokemon.id : 1));
                if (!descriptionData.ok) {
                    const errorData = await descriptionData.json();
                    throw new Error(errorData.message || `Description not found with status ${errorData.status}`);
                }
                let descriptionDto = await descriptionData.json();
                setDescription(descriptionDto);
            } catch (err) {
                console.error("Error fetching description data:", err);
                return;
            }
        };
        fetchGenerationData();
    }, [genNum]);

    const findSelectedPokemon = async (value: string | number) => {
        let pokemonData;
        try {
            if (!value) return
            const pokemonMatch = await fetch(ENDPOINTS.POKEMON_BY_ID(value));
            if (!pokemonMatch.ok) {
                const errorData = await pokemonMatch.json();
                throw new Error(errorData.message || `Pokemon not found with status ${errorData.status}`);            
            }
            pokemonData = await pokemonMatch.json();
        } catch(err) {
            console.error("Error fetching selected Pokémon:", err);
            pokemonData = null;
        }
        setSelectedPokemon(pokemonData);
        let generation;
        try {
            const genNumLocal = pokemonData.generationsAvailableIn[0];
            setGenNum(genNumLocal);
            const generationData = await fetch(ENDPOINTS.GEN_BY_ID(genNumLocal, pokemonData.id));
            if (!generationData.ok) {
                const errorData = await generationData.json();
                throw new Error(errorData.message || `Generation not found with status ${errorData.status}`);            
            }
            generation = await generationData.json();
            setSelectedGeneration(generation);
        } catch (err) {
            console.error("Error fetching generation data:", err);
        }
        let description;
        try {
            const gameVersionLocal = generation.gameNames[0];
            setSelectedVersion(gameVersionLocal);
            const descriptionData = await fetch(ENDPOINTS.DESCRIPTION_BY_ID(gameVersionLocal, pokemonData.id));
            if (!descriptionData.ok) {
                const errorData = await descriptionData.json();
                throw new Error(errorData.message || `Description not found with status ${errorData.status}`);            
            }
            description = await descriptionData.json();
            setDescription(description);
        } catch (err) {
            console.error("Error fetching description data:", err);
        }
    }

    const getTypeSprites = (generationDto: GenerationDetails | null) => {
        if (!generationDto) return null;
        else if (generationDto.typeSpriteUrls == null) return null;
        else {
            return generationDto.typeSpriteUrls.map(type => (
                <TypeSprite {...type} />
            ))
        }
    }

    return (
        <div className="w-full h-full relative">
            <div className="generationToggles">
                {generations.map(gen => {
                    return (
                        <button
                            key={gen}
                            disabled={selectedPokemon ? !selectedPokemon.generationsAvailableIn.includes(gen) : true}
                            className={`genBtn ${gen === genNum ? 'active' : ''}`}
                            onClick={() => setGenNum(gen)}
                        >
                            {gen}
                        </button> 
                    )
                })}
            </div>
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
                    currentPokemon={ selectedPokemon }
                />
            </div>

            <button
                className={`shinyToggle ${isShiny ? 'shiny' : ''}`}
                onClick={() => setIsShiny(!isShiny)}
                >
                    <img src={require('../../assets/shiny_toggle.png')} alt="Shiny Toggle" />
            </button>

            { selectedPokemon && (
                <div>
                    <div className="sprite">
                        <PokemonSprite generation={selectedGeneration} isShiny={isShiny} />
                    </div>

                    <p className="description leading-tight">
                        {description.description}
                    </p>

                    <div className="htwt">
                        <p>{selectedPokemon.height}</p>
                        <p>{selectedPokemon.weight}</p>
                    </div>
                    <div className="title">
                        <p>{selectedPokemon.title}</p>
                    </div>
                    <div className="typeSprites">
                        {getTypeSprites(selectedGeneration)}
                    </div>
                    <div className="CryBtn">
                        <CryButton {...selectedPokemon} />
                    </div>
                </div>
            )}
        </div>
    )
}