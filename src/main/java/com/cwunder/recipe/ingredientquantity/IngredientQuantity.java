package com.cwunder.recipe.ingredientquantity;

import java.math.BigDecimal;

import com.cwunder.recipe._shared.AppEntity;
import com.cwunder.recipe.ingredient.Ingredient;
import com.cwunder.recipe.recipe.Recipe;
import com.cwunder.recipe.unit.Unit;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "IngredientQuantity")
public class IngredientQuantity extends AppEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingredient", nullable = false)
    private Ingredient ingredient;

    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit", nullable = false)
    private Unit unit;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe", nullable = false)
    private Recipe recipe;

    public IngredientQuantity() {
        initialize();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
