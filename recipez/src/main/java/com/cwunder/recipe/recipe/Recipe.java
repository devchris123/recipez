package com.cwunder.recipe.recipe;

import java.util.*;

import com.cwunder.recipe._shared.AppEntity;
import com.cwunder.recipe.ingredientquantity.IngredientQuantity;
import com.cwunder.recipe.recipeinstruction.RecipeInstruction;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Recipe")
public class Recipe extends AppEntity {
    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    private String description = "";

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "recipe", targetEntity = IngredientQuantity.class)
    private Set<IngredientQuantity> ingredientQuantities;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "recipe", targetEntity = RecipeInstruction.class)
    private Set<RecipeInstruction> recipeInstructions;

    public Recipe() {
        initialize();
    }

    public Recipe(String name) {
        initialize();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<IngredientQuantity> getIngredientQuantities() {
        return ingredientQuantities;
    }

    public void setIngredientQuantities(Set<IngredientQuantity> ingredientQuantities) {
        this.ingredientQuantities = ingredientQuantities;
    }

    public Set<RecipeInstruction> getRecipeInstructions() {
        return recipeInstructions;
    }

    public void setRecipeInstructions(Set<RecipeInstruction> recipeInstructions) {
        this.recipeInstructions = recipeInstructions;
    }

    @Override
    public String toString() {
        return "Recipe{" + "id=" + this.getId() + ", name='" + this.getName() + '\'' + ", description="
                + this.getDescription() + "\'" + '}';
    }
}
