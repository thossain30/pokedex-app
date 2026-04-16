const BASE_API_URL = "http://localhost:8080";

export const ENDPOINTS = {
    ALL_POKEMON: `${BASE_API_URL}/pokemon`,
    POKEMON_BY_ID: (nameOrId: number | string) => `${BASE_API_URL}/pokemon/${nameOrId}`,
    GEN_BY_ID: (genNum: number, nameOrId: number | string) => `${BASE_API_URL}/generation/${genNum}/${nameOrId}`,
    DESCRIPTION_BY_ID: (versionId: string, nameOrId: number | string) => `${BASE_API_URL}/description/${versionId}/${nameOrId}`, 
};