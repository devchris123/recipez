package com.cwunder.recipe.unit;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UnitModelAssembler implements RepresentationModelAssembler<Unit, EntityModel<Unit>> {

    @Override
    public @NonNull EntityModel<Unit> toModel(@NonNull Unit rec) {
        return EntityModel.of(rec,
                linkTo(methodOn(UnitController.class).getUnit(rec.getPublicId())).withSelfRel(),
                linkTo(methodOn(UnitController.class).listUnits()).withRel("units"));
    }
}
