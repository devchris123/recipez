package com.cwunder.recipe.recipe;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;

import com.cwunder.recipe._shared.IdGenerator;

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
        rec.setId(1);
        rec.setPublicId(IdGenerator.genId());
        EntityModel<Recipe> recEM = assembler.toModel(rec);

        // execute
        Link link = recEM.getRequiredLink(IanaLinkRelations.SELF);

        // assert
        assertTrue(link.getHref().equals(String.format("/recipes/%s", rec.getPublicId())));
    }

    @Test
    void testss() {
        // setup
        RecipeModelAssembler assembler = new RecipeModelAssembler();
        Recipe rec = new Recipe();
        rec.setId(1);
        rec.setPublicId(IdGenerator.genId());
        EntityModel<Recipe> recEM = assembler.toModel(rec);
        ResponseEntity<EntityModel<Recipe>> rsp = ResponseEntity
                .created(recEM.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(recEM);

        // execute
        EntityModel<Recipe> recEMRsp = rsp.getBody();
        assertNotNull(recEMRsp);
        Link link = recEMRsp.getRequiredLink(IanaLinkRelations.SELF);

        // assert
        assertTrue(link.getHref().equals(String.format("/recipes/%s", rec.getPublicId())));
    }
}
