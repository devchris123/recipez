package com.cwunder.recipe.ingredientquantity;

// Java SE
import java.util.*;

// Junit
import org.junit.jupiter.api.*;

// Annotation
import org.springframework.beans.factory.annotation.Autowired;

// Http
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;

// Spring Boot Test
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;

// Spring Test
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

// Recipe
import com.cwunder.recipe._test.RecipeFixture;
import com.cwunder.recipe._test.TestFixture;
import com.cwunder.recipe._test.UserFixture;
import com.cwunder.recipe._test.WithMockCustomUser;
import com.cwunder.recipe._test.RecipeControllerFixture;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockCustomUser(username = "testuser")
@ActiveProfiles("test")
public class IngredientQuantityControllerTest {
    // Fixtures
    @Autowired
    private TestFixture testFixture;
    @Autowired
    private UserFixture userFxt;
    @Autowired
    private RecipeFixture recipeFixture;

    private WebTestClient recClient;
    private WebTestClient ingrQuantClient;

    @Autowired
    void setMockMvc(MockMvc mockMvc) {
        recClient = MockMvcWebTestClient.bindTo(mockMvc)
                .defaultHeader("Accept", MediaTypes.HAL_JSON_VALUE)
                .baseUrl("/recipes")
                .build();
        ingrQuantClient = MockMvcWebTestClient.bindTo(mockMvc)
                .defaultHeader("Accept", MediaTypes.HAL_JSON_VALUE)
                .build();
    }

    @BeforeEach
    void setupEach() {
        testFixture.cleanDB();
        var newUser = userFxt.createUser("testuser", "testpw");
        recipeFixture.createRecipe(newUser);
    }

    @Test
    void testGetIngredientQuantity() {
        // setup
        ResponseSpec rsp = postRecipeIngredientQuantity();

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.self.href").value(v -> {
                    var self = (String) v;
                    // execute / assert
                    BodyContentSpec spec = ingrQuantClient.get().uri(self)
                            .exchange().expectStatus().isOk().expectBody();
                    assertIngredientQuantityJsonPath(spec);
                });
    }

    @Test
    void testGetIngredientQuantity404() {
        // setup
        ResponseSpec rsp = postRecipeIngredientQuantity();

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.self.href").value(v -> {
                    var self = (String) v;
                    // execute / assert
                    ingrQuantClient.delete().uri(self)
                            .exchange().expectStatus().isNoContent();
                    ingrQuantClient.get().uri(self)
                            .exchange().expectStatus().isNotFound();
                });
    }

    @Test
    void testUpdateIngredientQuantity() {
        // setup
        ResponseSpec rsp = postRecipeIngredientQuantity();

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.self.href").value(v -> {
                    var self = (String) v;
                    // execute / assert
                    String res = new String(ingrQuantClient.get().uri(self)
                            .exchange().expectStatus().isOk()
                            .expectBody().returnResult().getResponseBody());
                    var ingredQuant = new HashMap<String, Object>();
                    String ingr = JsonPath.read(res, "$.ingredient");
                    String unit = JsonPath.read(res, "$.unit");
                    ingredQuant.put("ingredient", ingr);
                    ingredQuant.put("quantity", 20);
                    ingredQuant.put("unit", unit);
                    ingrQuantClient.put().uri(self).body(BodyInserters.fromValue(ingredQuant))
                            .exchange().expectStatus().isOk();
                });
    }

    @Test
    void testDeleteIngredientQuantity() {
        // setup
        ResponseSpec rsp = postRecipeIngredientQuantity();

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.self.href").value(v -> {
                    var link = (String) v;
                    // execute / assert
                    ingrQuantClient.delete().uri(link).exchange().expectStatus().isNoContent();
                });
    }

    ResponseSpec postRecipeIngredientQuantity() {
        // setup
        var ingredQuant = new HashMap<String, Object>();
        ingredQuant.put("ingredient", "cheese");
        ingredQuant.put("quantity", 10);
        ingredQuant.put("unit", "kg");
        var recipe = createRecipeData();
        ResponseSpec rsp = postRecipe(recipe);

        // execute
        String res = new String(rsp.expectStatus().isCreated()
                .expectBody()
                .returnResult().getResponseBody());
        String link = JsonPath.read(res, "$._links.ex:ingredientquantities.href");
        return ingrQuantClient.post().uri(link)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredQuant))
                .exchange();
    }

    void assertIngredientQuantityJsonPath(BodyContentSpec spec) {
        RecipeControllerFixture.assertIngredientQuantityJsonPath(spec);
    }

    Map<String, Object> createRecipeData() {
        return RecipeControllerFixture.createRecipeData();
    }

    ResponseSpec postRecipe(Map<String, Object> recipe) {
        return RecipeControllerFixture.postRecipe(recClient, recipe);
    }
}
