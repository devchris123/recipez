package com.cwunder.recipe;

import java.net.URI;
import java.util.*;

import org.junit.jupiter.api.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.*;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.http.*;

public class RecipeIT {
    private static final String baseUrl = "http://recipe-app:8080";
    private static WebTestClient client;

    @BeforeAll
    static void setupWebCLient() {
        client = WebTestClient.bindToServer().baseUrl(baseUrl).build();
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
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .jsonPath(".title").isEqualTo("Validation error")
                .jsonPath(".errors").exists();
    }

    @Test
    void testPutRecipe() throws Exception {
        // setup
        URI link = createRecipe();
        Map<String, String> recipe = new HashMap<String, String>();
        String name = "newname";
        recipe.put("name", "newname");

        // execute / assert
        BodyContentSpec bodySpec = client.put().uri(link)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(recipe))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath(".name").isEqualTo(name);
        assertCollectionLinks(bodySpec);
    }

    @Test
    void testGetRecipe() throws Exception {
        // setup
        URI link = createRecipe();

        // execute / assert
        BodyContentSpec bodySpec = client.get().uri(link)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath(".name").isEqualTo("myrecipe");
        assertCollectionLinks(bodySpec);
    }

    @Test
    void testGetRecipe404() throws Exception {
        // execute / assert
        client.get().uri(new URI(String.format("%s/recipe/%s", baseUrl, 1000000000)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
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

    URI createRecipe() throws Exception {
        Map<String, String> recipe = new HashMap<String, String>();
        String name = "myrecipe";
        recipe.put("name", name);

        // execute / assert
        EntityModel<Recipe> rsp = client.post().uri("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(recipe))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(new ParameterizedTypeReference<EntityModel<Recipe>>() {
                })
                .returnResult()
                .getResponseBody();
        return new URI(String.format("%s/recipe/%s", baseUrl, rsp.getContent().getId()));
    }
}
