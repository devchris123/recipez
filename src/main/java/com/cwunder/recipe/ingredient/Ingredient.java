package com.cwunder.recipe.ingredient;

import com.cwunder.recipe._shared.AppEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Ingredient")
public class Ingredient extends AppEntity {
    @Size(min = 1, max = 255)
    private String name;

    public Ingredient() {
        initialize();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
