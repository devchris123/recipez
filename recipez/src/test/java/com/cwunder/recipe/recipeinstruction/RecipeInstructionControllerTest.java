package com.cwunder.recipe.recipeinstruction;

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
public class RecipeInstructionControllerTest {
    // Fixtures
    @Autowired
    private TestFixture testFixture;
    @Autowired
    private UserFixture userFxt;
    @Autowired
    private RecipeFixture recipeFixture;

    private WebTestClient recClient;
    private WebTestClient recInstrClient;

    @Autowired
    void setMockMvc(MockMvc mockMvc) {
        recClient = MockMvcWebTestClient.bindTo(mockMvc)
                .defaultHeader("Accept", MediaTypes.HAL_JSON_VALUE)
                .baseUrl("/recipes")
                .build();
        recInstrClient = MockMvcWebTestClient.bindTo(mockMvc)
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
    void testGetRecipeInstruction() {
        // setup
        ResponseSpec rsp = postRecipeRecipeInstruction();

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.self.href").value(v -> {
                    var self = (String) v;
                    // execute / assert
                    BodyContentSpec spec = recInstrClient.get().uri(self)
                            .exchange().expectStatus().isOk().expectBody();
                    assertRecipeInstructionJsonPath(spec);
                });
    }

    @Test
    void testGetRecipeInstruction404() {
        // setup
        ResponseSpec rsp = postRecipeRecipeInstruction();

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.self.href").value(v -> {
                    var self = (String) v;
                    // execute / assert
                    System.out.println(self);
                    recInstrClient.delete().uri(self)
                            .exchange().expectStatus().isNoContent();
                    recInstrClient.get().uri(self)
                            .exchange().expectStatus().isNotFound();
                });
    }

    @Test
    void testUpdateRecipeInstruction() {
        // setup
        ResponseSpec rsp = postRecipeRecipeInstruction();

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.self.href").value(v -> {
                    var self = (String) v;
                    // execute / assert
                    new String(recInstrClient.get().uri(self)
                            .exchange().expectStatus().isOk()
                            .expectBody().returnResult().getResponseBody());
                    var ingredQuant = new HashMap<String, Object>();
                    ingredQuant.put("description", "new description");
                    recInstrClient.put().uri(self).body(BodyInserters.fromValue(ingredQuant))
                            .exchange().expectStatus().isOk();
                });
    }

    @Test
    void testDeleteRecipeInstruction() {
        // setup
        ResponseSpec rsp = postRecipeRecipeInstruction();

        // execute
        rsp.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.self.href").value(v -> {
                    var link = (String) v;
                    // execute / assert
                    recInstrClient.delete().uri(link).exchange().expectStatus().isNoContent();
                });
    }

    ResponseSpec postRecipeRecipeInstruction() {
        // setup
        var recInstr = new HashMap<String, Object>();
        recInstr.put("description", "Do something");
        var recipe = createRecipeData();
        ResponseSpec rsp = postRecipe(recipe);

        // execute
        String res = new String(rsp.expectStatus().isCreated()
                .expectBody()
                .returnResult().getResponseBody());
        // .jsonPath("$._links.ex:ingredientquantities.href");
        String link = JsonPath.read(res, "$._links.ex:recipeinstructions.href");
        return recInstrClient.post().uri(link)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(recInstr))
                .exchange();
    }

    void assertRecipeInstructionJsonPath(BodyContentSpec spec) {
        spec.jsonPath("$.description").exists();
    }

    Map<String, Object> createRecipeData() {
        return RecipeControllerFixture.createRecipeData();
    }

    ResponseSpec postRecipe(Map<String, Object> recipe) {
        return RecipeControllerFixture.postRecipe(recClient, recipe);
    }
}
