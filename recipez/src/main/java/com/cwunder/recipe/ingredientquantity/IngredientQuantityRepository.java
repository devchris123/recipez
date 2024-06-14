package com.cwunder.recipe.ingredientquantity;

import java.util.List;

import org.springframework.data.jpa.repository.*;

import com.cwunder.recipe._shared.AppEntityRepository;

public interface IngredientQuantityRepository
        extends JpaRepository<IngredientQuantity, Long>, AppEntityRepository<IngredientQuantity> {
    List<IngredientQuantity> findByRecipePublicId(String publicId);
}
