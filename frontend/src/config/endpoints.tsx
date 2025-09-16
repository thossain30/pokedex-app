const BASE_API_URL = "http://localhost:8080";

export const ENDPOINTS = {
    ALL_POKEMON: `${BASE_API_URL}/pokemon`,
    POKEMON_BY_ID: (nameOrId: number | string) => `${BASE_API_URL}/pokemon/${nameOrId}` 
}