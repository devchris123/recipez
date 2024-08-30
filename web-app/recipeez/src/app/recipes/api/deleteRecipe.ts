"use server";

import { RECIPES_URL } from "./url"
import { api } from "./api";
import { DeleteResponse } from "./types";


export const deleteRecipe = async (id: string): Promise<DeleteResponse> => {
    const response = await api(RECIPES_URL + "/" + id, {
        method: "delete",
    })
    if (response.status !== 204) {
        return {
            error: await response.json()
        }
    }
    return {};
}