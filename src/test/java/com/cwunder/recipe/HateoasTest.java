package com.cwunder.recipe;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;

public class HateoasTest {
    @Test
    void testEntityModelLink() {
        // setup
        EntityModel<Recipe> recEM = EntityModel.of(new Recipe(), Link.of("hey"));

        // execute
        Link link = recEM.getRequiredLink(IanaLinkRelations.SELF);

        // assert
        assertTrue(link.getHref().equals("hey"));
    }

    @Test
    void testEntityModelLinkToController() {
        // setup
        RecipeModelAssembler assembler = new RecipeModelAssembler();
        Recipe rec = new Recipe();
        rec.setId(0);
        EntityModel<Recipe> recEM = assembler.toModel(rec);

        // execute
        Link link = recEM.getRequiredLink(IanaLinkRelations.SELF);

        // assert
        assertTrue(link.getHref().equals("/recipe/0"));
    }

    @Test
    void testss() {
        // setup
        RecipeModelAssembler assembler = new RecipeModelAssembler();
        Recipe rec = new Recipe();
        rec.setId(0);
        EntityModel<Recipe> recEM = assembler.toModel(rec);
        ResponseEntity<EntityModel<Recipe>> rsp = ResponseEntity
                .created(recEM.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(recEM);

        // execute
        EntityModel<Recipe> recEMRsp = rsp.getBody();
        Link link = recEMRsp.getRequiredLink(IanaLinkRelations.SELF);

        // assert
        assertTrue(link.getHref().equals("/recipe/0"));
    }
}
