package com.cwunder.recipe;

public class RecipeNotFoundException extends RuntimeException {
    RecipeNotFoundException() {
        super("Recipe not found");
    }
}
