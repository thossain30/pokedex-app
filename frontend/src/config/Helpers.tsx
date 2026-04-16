export interface Pokemon {
    name: string,
    id: number
}

export interface TypeDto {
    name: string;
    url: string;
}

export interface PokemonDetails {
    id: number;
    name: string;
    title: string;
    height: string;
    weight: string;
    cry: string;
    generationsAvailableIn: number[];
}

export interface GenerationDetails {
    name: string;
    genNumId: number;
    spriteUrls: string[];
    gameNames: string[];
    typeSpriteUrls: TypeDto[];
}

export interface DescriptionDto {
    key: string;
    description: string;
}

export function capitalizeFirstLetter(str: string) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}