package com.cwunder.recipe.ingredientquantity;

import java.math.BigDecimal;

import com.cwunder.recipe._shared.AppEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class IngredientQuantityWrite extends AppEntity {
    @NotBlank
    private String ingredient;

    @NotNull
    @Positive
    private BigDecimal quantity;

    @NotBlank
    private String unit;

    public IngredientQuantityWrite() {
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

}