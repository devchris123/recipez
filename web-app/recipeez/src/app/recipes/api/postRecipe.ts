"use server";

import { RECIPES_URL } from "./url"
import { FormActionResponse, RecipeData } from "./types";
import { api } from "./api";
import { getServerSessionWithConf } from "@/app/api/auth/[...nextauth]/conf";

export const postRecipe = async (formData: FormData): Promise<FormActionResponse<RecipeData>> => {
    const sess = await getServerSessionWithConf();
    const response = await api(RECIPES_URL, {
        method: "post",
        body: JSON.stringify({ name: formData.get("name"), description: formData.get("description"), username: sess?.user?.name })
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