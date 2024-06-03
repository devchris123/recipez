package com.cwunder.recipe;

import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec;
import org.springframework.web.reactive.function.BodyInserters;

public class RecipeIT {
    private static WebTestClient client;

    @BeforeAll
    static void setupWebCLient() {
        client = WebTestClient.bindToServer().baseUrl("http://recipe-app:8080").build();
    }

    @Test
    void testListRecipes() {
        // execute / assert
        BodyContentSpec bodySpec = client.get().uri("/recipe")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
        assertSelfLinks(bodySpec);
    }

    @Test
    void testPostRecipe() {
        // setup
        Map<String, String> recipe = new HashMap<String, String>();
        String name = "myrecipe";
        recipe.put("name", "myrecipe");

        // execute / assert
        BodyContentSpec bodySpec = client.post().uri("/recipe")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(recipe))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath(".name").isEqualTo(name);
        assertCollectionLinks(bodySpec);
    }

    @Test
    void testPostRecipe400() {
        // setup
        Map<String, String> recipe = new HashMap<String, String>();
        recipe.put("name", "");

        // execute / assert
        client.post().uri("/recipe")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(recipe))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath(".title").isEqualTo("Validation error")
                .jsonPath(".errors").exists();
    }

    @Test
    void testPutRecipe() {
        // setup
        createRecipe();
        Map<String, String> recipe = new HashMap<String, String>();
        String name = "newname";
        recipe.put("name", "newname");

        // execute / assert
        BodyContentSpec bodySpec = client.put().uri("/recipe")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(recipe))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath(".name").isEqualTo(name);
        assertCollectionLinks(bodySpec);
    }

    void assertCollectionLinks(BodyContentSpec bc) {
        assertSelfLinks(bc);
        bc.jsonPath("._links.recipes").exists()
                .jsonPath("._links.recipes.href").exists();
    }

    void assertSelfLinks(BodyContentSpec bc) {
        bc.jsonPath("._links").exists()
                .jsonPath("._links.self").exists()
                .jsonPath("._links.self.href").exists();
    }

    void createRecipe() {
        Map<String, String> recipe = new HashMap<String, String>();
        String name = "myrecipe";
        recipe.put("name", name);

        // execute / assert
        client.post().uri("/recipe")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(recipe))
                .exchange();
    }
}
