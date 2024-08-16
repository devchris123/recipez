import { RECIPES_URL } from "./url"
import { ListResponse, ListRecipeResponse } from "./types";
import { api } from "./api";

export const fetchRecipes = async (): Promise<ListResponse<ListRecipeResponse>> => {
    const response = await api(RECIPES_URL, {
        method: "get",
    })
    if (response.status !== 200) {
        // Error case
        return {
            error: await response.json(),
            result: null
        }
    }
    return {
        error: null,
        result: await response.json()
    };
}