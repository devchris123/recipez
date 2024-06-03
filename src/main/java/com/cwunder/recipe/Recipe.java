package com.cwunder.recipe;

import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Recipe {
    @Id
    private long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    private String description = "";

    Recipe() {
    }

    public Recipe(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long recipeId) {
        this.id = recipeId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        return Objects.equals(this.id, recipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Recipe{" + "id=" + this.id + ", name='" + this.name + '\'' + '}';
    }

}
