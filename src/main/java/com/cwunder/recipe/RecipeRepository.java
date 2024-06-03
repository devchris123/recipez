package com.cwunder.recipe;

import org.springframework.data.jpa.repository.*;

interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
