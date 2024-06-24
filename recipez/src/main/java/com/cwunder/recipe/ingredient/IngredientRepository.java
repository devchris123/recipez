package com.cwunder.recipe.ingredient;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;

import com.cwunder.recipe._shared.AppEntityRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>, AppEntityRepository<Ingredient> {
    Optional<Ingredient> findByName(String name);
}
