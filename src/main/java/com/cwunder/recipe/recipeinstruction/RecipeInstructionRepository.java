package com.cwunder.recipe.recipeinstruction;

import java.util.List;

import org.springframework.data.jpa.repository.*;

import com.cwunder.recipe._shared.AppEntityRepository;

public interface RecipeInstructionRepository
        extends JpaRepository<RecipeInstruction, Long>, AppEntityRepository<RecipeInstruction> {
    List<RecipeInstruction> findByRecipePublicId(String publicId);
}
