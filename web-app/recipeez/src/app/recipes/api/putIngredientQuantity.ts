"use server";

import { ORIGIN } from "./url"
import { FormActionResponse, IngredientQuantity } from "./types";
import { api } from "./api";

export const putIngredientQuantity = async (id: string, formData: FormData): Promise<FormActionResponse<IngredientQuantity>> => {
    const response = await api(ORIGIN + "/ingredientquantities" + "/" + id, {
        method: "put",
        body: JSON.stringify({
            ingredient: formData.get("ingredient"),
            quantity: formData.get("quantity"),
            unit: formData.get("unit")
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