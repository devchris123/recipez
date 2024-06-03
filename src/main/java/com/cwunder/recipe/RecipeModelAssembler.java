package com.cwunder.recipe;

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
                linkTo(methodOn(RecipeController.class).getRecipe(rec.getId())).withSelfRel(),
                linkTo(methodOn(RecipeController.class).listRecipes()).withRel("recipes"));
    }
}
