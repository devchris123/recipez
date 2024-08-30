import { RECIPES_URL } from "./url"
import { FormActionResponse, RecipeData } from "./types";
import { api } from "./api";

export const fetchRecipe = async (id: string): Promise<FormActionResponse<RecipeData>> => {
    const response = await api(RECIPES_URL + "/" + id, {
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