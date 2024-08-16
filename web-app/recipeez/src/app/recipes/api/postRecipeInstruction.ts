"use server";

import { RECIPES_URL } from "./url"
import { FormActionResponse, RecipeInstruction } from "./types";
import { api } from "./api";

export const postRecipeInstruction = async (id: string, formData: FormData): Promise<FormActionResponse<RecipeInstruction>> => {
    const response = await api(RECIPES_URL + "/" + id + "/recipeinstructions", {
        method: "post",
        body: JSON.stringify({ description: formData.get("description") }),
    })
    if (response.status !== 201) {
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