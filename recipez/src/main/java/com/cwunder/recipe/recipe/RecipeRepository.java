package com.cwunder.recipe.recipe;

import org.springframework.data.jpa.repository.*;

import com.cwunder.recipe._shared.AppEntityRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, AppEntityRepository<Recipe> {

}
