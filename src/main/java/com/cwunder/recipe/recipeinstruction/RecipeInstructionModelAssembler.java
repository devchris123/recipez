package com.cwunder.recipe.recipeinstruction;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RecipeInstructionModelAssembler
        implements RepresentationModelAssembler<RecipeInstruction, EntityModel<RecipeInstruction>> {

    @Override
    public @NonNull EntityModel<RecipeInstruction> toModel(@NonNull RecipeInstruction rec) {
        return EntityModel.of(rec,
                linkTo(methodOn(RecipeInstructionController.class)
                        .getRecipeInstruction(rec.getPublicId()))
                        .withSelfRel());
    }
}
