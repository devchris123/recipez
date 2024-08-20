package com.cwunder.recipe.ingredient;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cwunder.recipe._shared.NotFoundException;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientRepository repo;
    private final IngredientModelAssembler assembler;

    IngredientController(IngredientRepository repo, IngredientModelAssembler assembler) {
        this.repo = repo;
        this.assembler = assembler;
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE,
            MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE })
    public @ResponseBody CollectionModel<EntityModel<Ingredient>> listIngredients() {
        List<EntityModel<Ingredient>> recs = repo.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(recs, linkTo(methodOn(IngredientController.class).listIngredients()).withSelfRel());
    }

    @PostMapping()
    public @ResponseBody ResponseEntity<?> createIngredient(@Valid @RequestBody Ingredient newIngredient) {

        EntityModel<Ingredient> rec = assembler.toModel(repo.save(newIngredient));
        return ResponseEntity.created(rec.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(rec);
    }

    @GetMapping("/{id}")
    public @ResponseBody EntityModel<Ingredient> getIngredient(@PathVariable String id) {
        Ingredient rec = repo.findByPublicId(id).orElseThrow(this::generateNotFoundException);
        return assembler.toModel(rec);
    }

    @PutMapping("/{id}")
    public @ResponseBody ResponseEntity<?> updateIngredient(@Valid @RequestBody Ingredient newIngredient,
            @PathVariable String id) {
        Ingredient rec = repo.findByPublicId(id)
                .map(
                        ingred -> {
                            ingred.setName(newIngredient.getName());
                            return repo.save(ingred);
                        })
                .orElseThrow(this::generateNotFoundException);
        return ResponseEntity.ok().body(assembler.toModel(rec));
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<?> deleteIngredient(@PathVariable String id) {
        repo.deleteByPublicId(id);
        return ResponseEntity.noContent().build();
    }

    private NotFoundException generateNotFoundException() {
        return new NotFoundException("Ingredient");
    }
}
