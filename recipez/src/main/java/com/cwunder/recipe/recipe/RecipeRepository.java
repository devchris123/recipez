package com.cwunder.recipe.recipe;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.lang.NonNull;

import com.cwunder.recipe._shared.AppEntityRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, AppEntityRepository<Recipe> {
    static String selectByUser = "select rec from Recipe rec where rec.user.username = ?#{ principal?.username }";
    static String selectByPublicId = "select rec from Recipe rec left join fetch rec.ingredientQuantities left join fetch rec.recipeInstructions where rec.user.username = ?#{ principal?.username } and rec.publicId = ?1";
    static String deleteByPublicId = "delete from Recipe rec where rec.user.username = ?#{ principal?.username } and rec.publicId = ?1";

    @Query(selectByPublicId)
    Optional<Recipe> findByPublicId(String publicId);

    @Query(selectByPublicId)
    Recipe getByPublicId(String publicId);

    @Query(selectByUser)
    @NonNull
    List<Recipe> findAll();

    @Modifying
    @Query(deleteByPublicId)
    void deleteByPublicId(String publicId);
}
