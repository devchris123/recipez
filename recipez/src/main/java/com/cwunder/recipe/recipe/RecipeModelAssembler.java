package com.cwunder.recipe.recipe;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RecipeModelAssembler implements RepresentationModelAssembler<Recipe, EntityModel<Recipe>> {
    @Override
    public @NonNull EntityModel<Recipe> toModel(@NonNull Recipe rec) {
        return EntityModel.of(rec,
                linkTo(methodOn(RecipeController.class).getRecipe(rec.getPublicId())).withSelfRel(),
                linkTo(methodOn(RecipeController.class).listRecipes()).withRel("recipes")
                        .andAffordance(afford(methodOn(RecipeController.class).createRecipe(null))),
                linkTo(methodOn(RecipeController.class).listIngredientQuantities(rec.getPublicId()))
                        .withRel("ingredientquantities")
                        .andAffordance(afford(
                                methodOn(RecipeController.class).createIngredientQuantity(null, rec.getPublicId()))),
                linkTo(methodOn(RecipeController.class).listRecipeInstructions(rec.getPublicId()))
                        .withRel("recipeinstructions")
                        .andAffordance(afford(
                                methodOn(RecipeController.class).createRecipeInstruction(null, rec.getPublicId()))));
    }
}
