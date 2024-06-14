package com.cwunder.recipe.ingredientquantity;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class IngredientQuantityModelAssembler
        implements RepresentationModelAssembler<IngredientQuantity, EntityModel<IngredientQuantity>> {

    @Override
    public @NonNull EntityModel<IngredientQuantity> toModel(@NonNull IngredientQuantity rec) {
        return EntityModel.of(rec,
                linkTo(methodOn(IngredientQuantityController.class).getIngredientQuantity(rec.getPublicId()))
                        .withSelfRel());
    }
}