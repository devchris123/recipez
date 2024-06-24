package com.cwunder.recipe.user;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public @NonNull EntityModel<User> toModel(@NonNull User user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUser(user.getPublicId())).withSelfRel());
    }
}