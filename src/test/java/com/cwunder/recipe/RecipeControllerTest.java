package com.cwunder.recipe;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mediatype.problem.Problem;

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
        private WebTestClient client;

        @Test
        void testCreateRecipe() {
                client = WebTestClient.bindToController(new RecipeController(repo, assembler)).build();
                Map<String, String> recipe = new HashMap<String, String>();
                String name = "myrecipe";
                recipe.put("name", name);

                // execute / assert
                EntityModel<Recipe> rsp = client.post().uri("/recipe")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(recipe))
                                .exchange()
                                .expectBody(new ParameterizedTypeReference<EntityModel<Recipe>>() {
                                })
                                .returnResult()
                                .getResponseBody();

                Link link = rsp.getRequiredLink(IanaLinkRelations.SELF);
                assertTrue(link.getHref().equals("/recipe/1"));
        }

        @Test
        void testCreateRecipe400() throws Exception {
                // setup
                ObjectMapper om = new ObjectMapper();
                MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new RecipeController(repo, assembler))
                                .setControllerAdvice(new RecipeValidationAdvice())
                                .build();
                client = WebTestClient.bindToController(new RecipeController(repo, assembler)).build();
                Map<String, String> recipe = new HashMap<String, String>();
                String name = "";
                recipe.put("name", name);

                // execute
                MvcResult mvcres = mockMvc.perform(post("/recipe")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(recipe)))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // assert
                EntityModel<Problem> prob = om.readValue(mvcres.getResponse().getContentAsString(),
                                new TypeReference<EntityModel<Problem>>() {
                                });
                assertTrue(prob.getContent().getTitle().equals("Validation error"));
        }
}
