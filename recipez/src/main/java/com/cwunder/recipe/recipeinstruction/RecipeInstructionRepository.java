package com.cwunder.recipe.recipeinstruction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;

import com.cwunder.recipe._shared.AppEntityRepository;

public interface RecipeInstructionRepository
        extends JpaRepository<RecipeInstruction, Long>, AppEntityRepository<RecipeInstruction> {
    static String selectByUserAndRecipe = "select recInstr from RecipeInstruction recInstr where recInstr.recipe.user.username = ?#{ principal?.username } and recInstr.recipe.publicId = ?1";
    static String selectByUserAndPublicId = "select recInstr from RecipeInstruction recInstr where recInstr.recipe.user.username = ?#{ principal?.username } and recInstr.publicId = ?1";
    static String deleteByUserAndPublicId = "delete from RecipeInstruction recInstr where recInstr.recipe.user.username = ?#{ principal?.username } and recInstr.publicId = ?1";

    @Query(selectByUserAndRecipe)
    List<RecipeInstruction> findByRecipePublicId(String publicId);

    @Query(selectByUserAndPublicId)
    Optional<RecipeInstruction> findByPublicId(String publicId);

    @Modifying
    @Query(deleteByUserAndPublicId)
    void deleteByPublicId(String publicId);
}
