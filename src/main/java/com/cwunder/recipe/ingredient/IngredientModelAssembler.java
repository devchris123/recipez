package com.cwunder.recipe.ingredient;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class IngredientModelAssembler implements RepresentationModelAssembler<Ingredient, EntityModel<Ingredient>> {

    @Override
    public @NonNull EntityModel<Ingredient> toModel(@NonNull Ingredient rec) {
        return EntityModel.of(rec,
                linkTo(methodOn(IngredientController.class).getIngredient(rec.getPublicId())).withSelfRel(),
                linkTo(methodOn(IngredientController.class).listIngredients()).withRel("Ingredients"));
    }
}
