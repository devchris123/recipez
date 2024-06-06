package com.cwunder.recipe.ingredientquantity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.cwunder.recipe.ingredient.IngredientRepository;
import com.cwunder.recipe.recipe.RecipeRepository;
import com.cwunder.recipe.unit.UnitRepository;

@SpringBootTest
@ActiveProfiles("test")
public class IngredientQuantityControllerTest {
    @Autowired
    private IngredientQuantityModelAssembler assembler;
    @Autowired
    private IngredientQuantityRepository repo;
    private WebTestClient client;

    private final String CONTROLLER_URL = "/ingredientquantities";

    @Autowired
    private IngredientRepository ingrRepo;
    @Autowired
    private UnitRepository unitRepo;
    @Autowired
    private RecipeRepository recRepo;

    @Test
    void testCreateIngredientQuantity() {
        client = WebTestClient
                .bindToController(new IngredientQuantityController(repo, assembler))
                .build();
        var ingredQuant = new HashMap<String, Object>();
        var ingr = ingrRepo.findAll().getFirst();
        var ut = unitRepo.findAll().getFirst();
        var rec = recRepo.findAll().getFirst();
        ingredQuant.put("ingredient", ingr.getPublicId());
        ingredQuant.put("quantity", 10);
        ingredQuant.put("unit", ut.getPublicId());
        ingredQuant.put("recipe", rec.getPublicId());

        // execute / assert
        CollectionModel<EntityModel<IngredientQuantity>> rsp = client.get().uri(CONTROLLER_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<CollectionModel<EntityModel<IngredientQuantity>>>() {
                })
                .returnResult()
                .getResponseBody();
        assertNotNull(rsp);
        var cont = rsp.getContent();
        assertNotNull(cont);
    }
}
