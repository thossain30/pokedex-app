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
    description: string;
    title: string;
    height: string;
    weight: string;
    spriteUrl: string;
    cry: string;
    types: TypeDto[];
}

export function capitalizeFirstLetter(str: string) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}