package com.cwunder.recipe.ingredientquantity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.cwunder.recipe._shared.AppEntityRepository;

public interface IngredientQuantityRepository
        extends JpaRepository<IngredientQuantity, Long>, AppEntityRepository<IngredientQuantity> {
    static String selectByUserAndRecipe = "select ingrQuant from IngredientQuantity ingrQuant where ingrQuant.recipe.user.username = ?#{ principal?.username } and ingrQuant.recipe.publicId = ?1";
    static String selectByUserAndPublicId = "select ingrQuant from IngredientQuantity ingrQuant where ingrQuant.recipe.user.username = ?#{ principal?.username } and ingrQuant.publicId = ?1";
    static String deleteByUserAndPublicId = "delete from IngredientQuantity ingrQuant where ingrQuant.recipe.user.username = ?#{ principal?.username } and ingrQuant.publicId = ?1";

    @Query(selectByUserAndRecipe)
    List<IngredientQuantity> findByRecipePublicId(String publicId);

    @Query(selectByUserAndPublicId)
    Optional<IngredientQuantity> findByPublicId(String publicId);

    @Transactional
    @Modifying
    @Query(deleteByUserAndPublicId)
    void deleteByPublicId(String publicId);
}
