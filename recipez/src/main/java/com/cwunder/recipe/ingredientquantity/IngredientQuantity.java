package com.cwunder.recipe.ingredientquantity;

// Java SE
import java.math.BigDecimal;

// JSON
import com.fasterxml.jackson.annotation.JsonBackReference;

// Jakarta 
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// Recipe
import com.cwunder.recipe._shared.AppEntity;
import com.cwunder.recipe.recipe.Recipe;

@Entity
@Table(name = "IngredientQuantity")
public class IngredientQuantity extends AppEntity {
    private String ingredient;

    @NotNull
    @Positive
    private BigDecimal quantity;

    private String unit;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe", nullable = false)
    private Recipe recipe;

    public IngredientQuantity() {
        initialize();
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
