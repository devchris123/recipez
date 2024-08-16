"use server";

import { RECIPES_URL } from "./url"
import { FormActionResponse, IngredientQuantity } from "./types";
import { api } from "./api";

export const postIngredientQuantity = async (id: string, formData: FormData): Promise<FormActionResponse<IngredientQuantity>> => {
    const response = await api(RECIPES_URL + "/" + id + "/ingredientquantities", {
        method: "post",
        body: JSON.stringify({
            ingredient: formData.get("ingredient"),
            quantity: formData.get("quantity"),
            unit: formData.get("unit")
        }),
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