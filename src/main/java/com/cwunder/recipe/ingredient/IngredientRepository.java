package com.cwunder.recipe.ingredient;

import org.springframework.data.jpa.repository.*;

import com.cwunder.recipe._shared.AppEntityRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>, AppEntityRepository<Ingredient> {

}
