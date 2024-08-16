"use server";

import { ORIGIN } from "./url"
import { api } from "./api";

export const deleteRecipeInstruction = async (riId: string): Promise<void> => {
    await api(ORIGIN + "/recipeinstructions" + "/" + riId, {
        method: "delete",
    })
}