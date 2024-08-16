package com.cwunder.recipe.recipe;

import java.util.HashSet;
import java.util.Set;

import com.cwunder.recipe._shared.AppEntity;
import com.cwunder.recipe.ingredientquantity.IngredientQuantity;
import com.cwunder.recipe.recipeinstruction.RecipeInstruction;
import com.cwunder.recipe.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Recipe")
public class Recipe extends AppEntity {
    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @Size(max = 65535)
    private String description = "";

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private String username;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user", nullable = true)
    private User user;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "recipe", targetEntity = IngredientQuantity.class, fetch = FetchType.EAGER)
    private Set<IngredientQuantity> ingredientQuantities = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "recipe", targetEntity = RecipeInstruction.class, fetch = FetchType.EAGER)
    private Set<RecipeInstruction> recipeInstructions = new HashSet<>();

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
