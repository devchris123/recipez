package com.cwunder.recipe.ingredientquantity;

import java.util.*;
import java.util.stream.Collectors;

// Spring
import org.springframework.web.bind.annotation.*;

import com.cwunder.recipe._shared.NotFoundException;

import org.springframework.hateoas.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

// Jakarta
import jakarta.validation.Valid;

@Controller
@RequestMapping("/ingredientquantities")
public class IngredientQuantityController {

    private final IngredientQuantityRepository repo;
    private final IngredientQuantityModelAssembler assembler;

    IngredientQuantityController(IngredientQuantityRepository repo, IngredientQuantityModelAssembler assembler) {
        this.repo = repo;
        this.assembler = assembler;
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE,
            MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE })
    public @ResponseBody CollectionModel<EntityModel<IngredientQuantity>> listIngredientQuantitys() {
        List<EntityModel<IngredientQuantity>> recs = repo.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(recs,
                linkTo(methodOn(IngredientQuantityController.class).listIngredientQuantitys())
                        .withSelfRel());
    }

    @GetMapping("/{id}")
    public @ResponseBody EntityModel<IngredientQuantity> getIngredientQuantity(@PathVariable String id) {
        IngredientQuantity rec = repo.findByPublicId(id).orElseThrow(this::generateNotFoundException);
        return assembler.toModel(rec);
    }

    @PutMapping("/{id}")
    public @ResponseBody ResponseEntity<?> updateIngredientQuantity(
            @Valid @RequestBody IngredientQuantity newIngredientQuantity, @PathVariable String id) {
        IngredientQuantity rec = repo.findByPublicId(id)
                .map(
                        recipe -> {
                            recipe.setIngredient(newIngredientQuantity.getIngredient());
                            recipe.setQuantity(newIngredientQuantity.getQuantity());
                            recipe.setUnit(newIngredientQuantity.getUnit());
                            return repo.save(recipe);
                        })
                .orElseThrow(this::generateNotFoundException);
        return ResponseEntity.ok().body(assembler.toModel(rec));
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<?> deleteIngredientQuantity(@PathVariable String id) {
        repo.deleteByPublicId(id);
        return ResponseEntity.noContent().build();
    }

    private NotFoundException generateNotFoundException() {
        return new NotFoundException("IngredientQuantity");
    }
}