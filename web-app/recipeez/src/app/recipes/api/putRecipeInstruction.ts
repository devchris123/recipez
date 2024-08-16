"use server";

import { ORIGIN } from "./url"
import { FormActionResponse, RecipeInstruction } from "./types";
import { api } from "./api";

export const putRecipeInstruction = async (id: string, formData: FormData): Promise<FormActionResponse<RecipeInstruction>> => {
    const response = await api(ORIGIN + "/recipeinstructions" + "/" + id, {
        method: "put",
        body: JSON.stringify({
            description: formData.get("description")
        }),
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