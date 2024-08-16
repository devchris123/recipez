package com.cwunder.recipe.ingredientquantity;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cwunder.recipe._shared.NotFoundException;

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
            @Valid @RequestBody IngredientQuantityWrite newIngredientQuantityData, @PathVariable String id) {
        IngredientQuantity rec = repo.findByPublicId(id)
                .map(
                        recipe -> {
                            recipe.setIngredient(newIngredientQuantityData.getIngredient());
                            recipe.setQuantity(newIngredientQuantityData.getQuantity());
                            recipe.setUnit(newIngredientQuantityData.getUnit());
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