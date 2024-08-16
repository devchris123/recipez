
export interface RecipeInstruction {
    publicId: string;
    description: string;
}

export type Errors<T> = {
    [alias in keyof T]: string;
}

export interface ListResponse<T> {
    result: T | null;
    error: {
        title: string;
    } | null
}

export interface DeleteResponse {
    error?: string;
}

export interface FormActionResponse<T extends { publicId: string }> {
    result: T | null;
    error: {
        title: string;
        errors: Errors<Omit<T, "publicId">>
    } | null
}
// {"title":"Validation error","errors":{"description":"must not be blank"}}

export interface IngredientQuantity {
    publicId: string;
    ingredient: string;
    quantity: string;
    unit: string;
}

export interface Recipe {
    publicId: string;
    name: string;
    description: string;
}

export interface RecipeData {
    publicId: string;
    name: string;
    description: string;
    ingredientQuantities: IngredientQuantity[];
    recipeInstructions: RecipeInstruction[];
}

export interface ListRecipeResponse {
    _embedded: {
        "ex:recipeList"?: RecipeData[];
    };
}

export interface Response<T> { data?: T | null, loading: boolean, error: string | null }
