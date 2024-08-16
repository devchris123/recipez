package com.cwunder.recipe.recipe;

// Java SE
import java.util.*;

// Junit
import org.junit.jupiter.api.*;

// Web
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.http.MediaType;

// Transactions
import org.springframework.transaction.annotation.Transactional;

// Spring Boot Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

// Spring Test
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

// Hateoas
import org.springframework.hateoas.MediaTypes;

import com.cwunder.recipe._test.RecipeControllerFixture;
// Jackson
import com.cwunder.recipe._test.RecipeFixture;

// Recipe
import com.cwunder.recipe._test.TestFixture;
import com.cwunder.recipe._test.UserFixture;
import com.cwunder.recipe._test.WithMockCustomUser;

// Other
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockCustomUser(username = "testuser")
@ActiveProfiles("test")
public class RecipeControllerTest {
    private WebTestClient client;

    private final String CONTROLLER_URL = "/recipes";

    @Autowired
    private UserFixture userFxt;

    @Autowired
    private RecipeFixture recFxt;

    @Autowired
    private TestFixture testFixture;

    static String VALIDATION_ERROR_MESSAGE = "Validation error";

    @BeforeEach
    void setup() {
        testFixture.cleanDB();
        // Create a user that is used in subsequent authentications
        var user = userFxt.createUser("testuser", "testpw");
        // Create some random data to simulate a more complete environment
        var user2 = userFxt.createUser("testuser2", "testpw2");
        recFxt.createRecipe(user);
        recFxt.createRecipe(user2);
    }

    @Autowired
    void setMockMvc(MockMvc mockMvc) {
        client = MockMvcWebTestClient.bindTo(mockMvc)
                .defaultHeader("Accept", MediaTypes.HAL_JSON_VALUE)
                .baseUrl(CONTROLLER_URL)
                .build();
    }

    @Test
    void testCreateRecipe() {
        // setup
        var recipe = createRecipeData();

        // execute
        ResponseSpec rsp = postRecipe(client, recipe);

        // assert
        BodyContentSpec spec = rsp.expectStatus().isCreated()
                .expectBody();
        assertRecipeJsonPath(spec);
    }

    @Test
    void testCreateRecipe400() throws Exception {
        // setup
        var recipe = new HashMap<String, Object>();
        String name = "";
        recipe.put("name", name);

        // execute
        client.post().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(recipe))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.title").isEqualTo(VALIDATION_ERROR_MESSAGE)
                .jsonPath("$.errors").exists()
                .jsonPath("$.errors.name").isEqualTo("size must be between 1 and 255");
    }

    @Test
    void testGetRecipe() {
        // setup
        var recipe = createRecipeData();
        ResponseSpec rsp = postRecipe(client, recipe);

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.self.href").value(v -> {
                    var self = (String) v;
                    // execute / assert
                    BodyContentSpec spec = client.get().uri(self)
                            .exchange().expectStatus().isOk()
                            .expectBody();
                    assertRecipeJsonPath(spec);
                });
    }

    @Test
    void testListRecipe() {
        // setup
        var recipe = createRecipeData();
        ResponseSpec rsp = postRecipe(client, recipe);

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.ex:recipes.href").value(v -> {
                    var self = (String) v;
                    // execute / assert
                    BodyContentSpec spec = client.get().uri(self)
                            .exchange().expectStatus().isOk()
                            .expectBody();
                    spec.jsonPath("$._embedded").exists()
                            .jsonPath("_links").exists()
                            .jsonPath("$._links.self").exists()
                            .jsonPath("$._embedded").exists()
                            .jsonPath("$._embedded.ex:recipeList").isArray();
                });
    }

    @Test
    void testDeleteRecipe() {
        // setup
        var recipe = createRecipeData();
        ResponseSpec rsp = postRecipe(client, recipe);

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.self.href").value(v -> {
                    var self = (String) v;
                    // execute / assert
                    client.delete().uri(self)
                            .exchange().expectStatus().isNoContent();
                    client.get().uri(self)
                            .exchange().expectStatus().isNotFound();
                });
    }

    void assertRecipeJsonPath(BodyContentSpec spec) {
        spec.jsonPath("$.publicId").exists()
                .jsonPath("$.name").exists()
                .jsonPath("$.description").exists()
                .jsonPath("$.user").exists()
                .jsonPath("$.ingredientQuantities").isArray()
                .jsonPath("$.recipeInstructions").isArray()
                .jsonPath("$._links.self").exists()
                .jsonPath("$._links.ex:recipes").exists()
                .jsonPath("$._links.ex:ingredientquantities").exists()
                .jsonPath("$._links.ex:recipeinstructions").exists()
                .jsonPath("$._links.curies").exists();
    }

    @Test
    void testCreateIngredientQuantity() {
        // setup
        var ingredQuant = new HashMap<String, Object>();
        ingredQuant.put("ingredient", "cheese");
        ingredQuant.put("quantity", 10);
        ingredQuant.put("unit", "kg");
        var recipe = createRecipeData();
        ResponseSpec rsp = postRecipe(client, recipe);

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.ex:ingredientquantities.href").value(v -> {
                    var link = (String) v;
                    // execute / assert
                    BodyContentSpec spec = client.post().uri(link)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(ingredQuant))
                            .exchange().expectStatus().isCreated()
                            .expectBody();
                    assertIngredientQuantityJsonPath(spec);
                });
    }

    void assertIngredientQuantityJsonPath(BodyContentSpec spec) {
        RecipeControllerFixture.assertIngredientQuantityJsonPath(spec);
    }

    @Test
    void testCreateIngredientQuantity400() {
        // setup
        var ingredQuant = new HashMap<String, Object>();
        ingredQuant.put("ingredient", "");
        ingredQuant.put("quantity", null);
        ingredQuant.put("unit", "");
        var recipe = createRecipeData();
        ResponseSpec rsp = postRecipe(client, recipe);

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.ex:ingredientquantities.href").value(v -> {
                    var link = (String) v;
                    // execute / assert
                    client.post().uri(link)
                            .body(BodyInserters.fromValue(ingredQuant))
                            .exchange().expectStatus().is4xxClientError()
                            .expectBody()
                            .jsonPath("$.title").isEqualTo(VALIDATION_ERROR_MESSAGE)
                            .jsonPath("$.errors").exists()
                            .jsonPath("$.errors.ingredient").exists()
                            .jsonPath("$.errors.quantity").exists()
                            .jsonPath("$.errors.unit").exists();
                });
    }

    @Test
    void testCreateRecipeInstruction() {
        // setup
        var recipe = createRecipeData();
        ResponseSpec rsp = postRecipe(client, recipe);
        var recInstr = new HashMap<String, Object>();
        recInstr.put("description", "Do something");

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.ex:recipeinstructions.href").value(v -> {
                    var link = (String) v;
                    // execute / assert
                    client.post().uri(link)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(recInstr))
                            .exchange().expectStatus().isCreated()
                            .expectBody()
                            .jsonPath("$.publicId").exists()
                            .jsonPath("$.description").exists()
                            .jsonPath("$._links.self").exists();
                });
    }

    @Test
    void testCreateRecipeInstruction400() {
        // setup
        var recipe = createRecipeData();
        ResponseSpec rsp = postRecipe(client, recipe);
        var recInstr = new HashMap<String, Object>();
        recInstr.put("description", "");

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.ex:recipeinstructions.href").value(v -> {
                    var link = (String) v;
                    // execute / assert
                    client.post().uri(link)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(recInstr))
                            .exchange().expectStatus().is4xxClientError()
                            .expectBody()
                            .jsonPath("$.title").isEqualTo(VALIDATION_ERROR_MESSAGE)
                            .jsonPath("$.errors").exists()
                            .jsonPath("$.errors.description").exists();
                });
    }

    Map<String, Object> createRecipeData() {
        return RecipeControllerFixture.createRecipeData();
    }

    ResponseSpec postRecipe(WebTestClient client, Map<String, Object> recipe) {
        return RecipeControllerFixture.postRecipe(client, recipe);
    }
}
