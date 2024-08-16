"use server";

import { ORIGIN } from "./url"
import { api } from "./api";

export const deleteIngredientQuantity = async (iqId: string): Promise<void> => {
    await api(ORIGIN + "/ingredientquantities" + "/" + iqId, {
        method: "delete",
    })
}