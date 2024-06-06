package com.cwunder.recipe.recipe;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mediatype.problem.Problem;

import com.cwunder.recipe._shared.ValidationAdvice;
import com.cwunder.recipe.ingredient.IngredientRepository;
import com.cwunder.recipe.ingredientquantity.IngredientQuantity;
import com.cwunder.recipe.ingredientquantity.IngredientQuantityModelAssembler;
import com.cwunder.recipe.ingredientquantity.IngredientQuantityRepository;
import com.cwunder.recipe.recipeinstruction.RecipeInstruction;
import com.cwunder.recipe.recipeinstruction.RecipeInstructionModelAssembler;
import com.cwunder.recipe.recipeinstruction.RecipeInstructionRepository;
import com.cwunder.recipe.unit.UnitRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class RecipeControllerTest {
    @Autowired
    private RecipeModelAssembler assembler;
    @Autowired
    private RecipeRepository repo;
    @Autowired
    private IngredientQuantityRepository ingrQRepo;
    @Autowired
    private IngredientQuantityModelAssembler ingrQAssembler;
    @Autowired
    private RecipeInstructionRepository recInstrRepo;
    @Autowired
    private RecipeInstructionModelAssembler recInstrAssembler;
    @Autowired
    private IngredientRepository ingrRepo;
    @Autowired
    private UnitRepository unitRepo;

    private WebTestClient client;

    private final String CONTROLLER_URL = "/recipes";

    @Test
    void testCreateRecipe() {
        client = buildWebTestClient();
        var recipe = new HashMap<String, Object>();
        String name = "myrecipe";
        recipe.put("name", name);

        // execute
        EntityModel<Recipe> rsp = postRecipe(client, recipe);

        // assert
        assertNotNull(rsp);
        Link link = rsp.getRequiredLink(IanaLinkRelations.SELF);
        var cont = rsp.getContent();
        assertNotNull(cont);
        assertTrue(link.getHref().equals(String.format(CONTROLLER_URL + "/%s", cont.getPublicId())));
    }

    @Test
    void testCreateRecipe400() throws Exception {
        // setup
        ObjectMapper om = new ObjectMapper();
        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(buildController())
                .setControllerAdvice(new ValidationAdvice())
                .build();
        client = buildWebTestClient();
        var recipe = new HashMap<String, Object>();
        String name = "";
        recipe.put("name", name);

        // execute
        MvcResult mvcres = mockMvc.perform(post(CONTROLLER_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(recipe)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // assert
        EntityModel<Problem> prob = om.readValue(mvcres.getResponse().getContentAsString(),
                new TypeReference<EntityModel<Problem>>() {
                });
        assertNotNull(prob);
        var cont = prob.getContent();
        assertNotNull(cont);
        var title = cont.getTitle();
        assertNotNull(title);
        assertTrue(title.equals("Validation error"));
    }

    @Test
    void testCreateIngredientQuantity() {
        // setup
        client = buildWebTestClient();
        var recipe = new HashMap<String, Object>();
        String name = "myrecipe";
        recipe.put("name", name);
        EntityModel<Recipe> recEM = postRecipe(client, recipe);
        assertNotNull(recEM);
        Link iqLink = recEM.getLink("ingredientquantities").orElseThrow(IllegalArgumentException::new);

        var ingredQuant = new HashMap<String, Object>();
        var ingr = ingrRepo.findAll().getFirst();
        var ut = unitRepo.findAll().getFirst();
        ingredQuant.put("ingredient", ingr.getPublicId());
        ingredQuant.put("quantity", 10);
        ingredQuant.put("unit", ut.getPublicId());

        // execute
        EntityModel<IngredientQuantity> iqEM = postSubEntity(client, iqLink.getHref(), ingredQuant)
                .expectBody(new ParameterizedTypeReference<EntityModel<IngredientQuantity>>() {
                })
                .returnResult()
                .getResponseBody();

        // assert
        assertNotNull(iqEM);
        var cont = iqEM.getContent();
        assertNotNull(cont);
    }

    @Test
    void testCreateRecipeInstruction() {
        // setup
        client = buildWebTestClient();
        var recipe = new HashMap<String, Object>();
        String name = "myrecipe";
        recipe.put("name", name);
        EntityModel<Recipe> recEM = postRecipe(client, recipe);
        assertNotNull(recEM);
        Link iqLink = recEM.getLink("recipeinstructions").orElseThrow(IllegalArgumentException::new);
        var recInstr = new HashMap<String, Object>();
        recInstr.put("description", "Do something");

        // execute
        EntityModel<RecipeInstruction> iqEM = postSubEntity(client, iqLink.getHref(), recInstr)
                .expectBody(new ParameterizedTypeReference<EntityModel<RecipeInstruction>>() {
                })
                .returnResult()
                .getResponseBody();

        // assert
        assertNotNull(iqEM);
        var cont = iqEM.getContent();
        assertNotNull(cont);
    }

    RecipeController buildController() {
        return new RecipeController(repo, assembler, ingrQRepo, ingrQAssembler, recInstrRepo, recInstrAssembler,
                ingrRepo, unitRepo);
    }

    WebTestClient buildWebTestClient() {
        return WebTestClient
                .bindToController(buildController())
                .build();
    }

    EntityModel<Recipe> postRecipe(WebTestClient client, HashMap<String, Object> recipe) {
        return client.post().uri(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(recipe))
                .exchange()
                .expectBody(new ParameterizedTypeReference<EntityModel<Recipe>>() {
                })
                .returnResult()
                .getResponseBody();
    }

    ResponseSpec postSubEntity(WebTestClient client, String url, HashMap<String, Object> data) {
        return client.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(data))
                .exchange()
                .expectStatus().isCreated();
    }
}
