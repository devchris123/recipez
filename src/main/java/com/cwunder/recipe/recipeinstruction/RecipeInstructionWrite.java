package com.cwunder.recipe.recipeinstruction;

import jakarta.validation.constraints.NotBlank;

public class RecipeInstructionWrite {
    @NotBlank
    private String description;

    public RecipeInstructionWrite() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
