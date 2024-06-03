package com.cwunder.recipe;

import java.util.*;
import java.util.stream.Collectors;

// Spring
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.hateoas.MediaTypes;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

// Jakarta
import jakarta.validation.Valid;

@Controller
public class RecipeController {

    private final RecipeRepository repo;
    private final RecipeModelAssembler assembler;

    RecipeController(RecipeRepository repo, RecipeModelAssembler assembler) {
        this.repo = repo;
        this.assembler = assembler;
    }

    @GetMapping(value = "/recipe", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE,
            MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE })
    public @ResponseBody CollectionModel<EntityModel<Recipe>> listRecipes() {
        List<EntityModel<Recipe>> recs = repo.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(recs, linkTo(methodOn(RecipeController.class).listRecipes()).withSelfRel());
    }

    @GetMapping(value = "/recipe")
    String listRecipesView() {
        return "recipe";
    }

    @PostMapping("/recipe")
    public @ResponseBody ResponseEntity<?> createRecipe(@Valid @RequestBody Recipe newRecipe) {

        EntityModel<Recipe> rec = assembler.toModel(repo.save(newRecipe));
        return ResponseEntity.created(rec.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(rec);
    }

    @GetMapping("/recipe/{id}")
    public @ResponseBody EntityModel<Recipe> getRecipe(@PathVariable Long id) {
        Recipe rec = repo.findById(id).orElseThrow(() -> new RecipeNotFoundException());
        return assembler.toModel(rec);
    }

    @PutMapping("/recipe/{id}")
    public @ResponseBody ResponseEntity<?> updateRecipe(@Valid @RequestBody Recipe newRecipe, @PathVariable Long id) {
        Recipe rec = repo.findById(id)
                .map(
                        recipe -> {
                            recipe.setName(newRecipe.getName());
                            return repo.save(recipe);
                        })
                .orElseThrow(
                        () -> new RecipeNotFoundException());
        return ResponseEntity.ok().body(assembler.toModel(rec));
    }

    @DeleteMapping("/recipe/{id}")
    public @ResponseBody ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
