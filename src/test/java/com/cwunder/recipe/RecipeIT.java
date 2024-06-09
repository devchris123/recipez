package com.cwunder.recipe;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import org.junit.jupiter.api.*;
import org.springframework.hateoas.*;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.*;

public class RecipeIT {
    private static final String baseUrl = "http://recipe-app:8080";
    private static WebTestClient testClient;
    private static WebClient webClient;

    private static String RECIPE_ENDPOINT = "/recipes";

    @BeforeAll
    static void setupWebCLient() {
        testClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
        webClient = WebClient.create(baseUrl);
    }

    @Test
    void testListRecipes() {
        // execute / assert
        BodyContentSpec bodySpec = testClient.get().uri(RECIPE_ENDPOINT)
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
        BodyContentSpec bodySpec = testClient.post().uri(RECIPE_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(recipe))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo(name);
        assertCollectionLinks(bodySpec);
    }

    @Test
    void testPostRecipe400() {
        // setup
        Map<String, String> recipe = new HashMap<String, String>();
        recipe.put("name", "");

        // execute / assert
        testClient.post().uri(RECIPE_ENDPOINT)
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
        BodyContentSpec bodySpec = testClient.put().uri(link)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(recipe))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo(name);
        assertCollectionLinks(bodySpec);
    }

    @Test
    void testGetRecipe() throws Exception {
        // setup
        URI link = createRecipe();

        // execute / assert
        BodyContentSpec bodySpec = testClient.get().uri(link)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo("myrecipe");
        assertCollectionLinks(bodySpec);
    }

    @Test
    void testGetRecipe404() throws Exception {
        // execute / assert
        testClient.get().uri(new URI(String.format("%s/recipes/%s", baseUrl, 1000000000)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    void assertCollectionLinks(BodyContentSpec bc) {
        assertSelfLinks(bc);
        bc.jsonPath("._links.ex:recipes").exists()
                .jsonPath("._links.ex:recipes.href").exists();
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
        String recEM = webClient.post().uri(RECIPE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .body(BodyInserters.fromValue(recipe))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(s -> s.path("_links"))
                .map(s -> s.path("self"))
                .map(s -> s.path("href"))
                .map(s -> {
                    try {
                        return (new ObjectMapper()).readValue(s.traverse(), String.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "";
                    }
                })
                .block();
        return new URI(recEM);
    }
}
