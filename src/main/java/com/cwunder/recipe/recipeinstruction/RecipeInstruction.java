package com.cwunder.recipe.recipeinstruction;

import com.cwunder.recipe._shared.AppEntity;
import com.cwunder.recipe.recipe.Recipe;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "RecipeInstruction")
public class RecipeInstruction extends AppEntity {
    @NotBlank
    private String description;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe", nullable = false)
    private Recipe recipe;

    public RecipeInstruction() {
        initialize();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
