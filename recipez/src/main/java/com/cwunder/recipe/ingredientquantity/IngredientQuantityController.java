package com.cwunder.recipe.ingredientquantity;

import java.util.*;
import java.util.stream.Collectors;

// Spring
import org.springframework.web.bind.annotation.*;

import com.cwunder.recipe._shared.NotFoundException;
import com.cwunder.recipe.ingredient.Ingredient;
import com.cwunder.recipe.ingredient.IngredientRepository;
import com.cwunder.recipe.unit.Unit;
import com.cwunder.recipe.unit.UnitRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private IngredientRepository ingrRepo;

    @Autowired
    private UnitRepository unitRepo;

    private final IngredientQuantityRepository repo;
    private final IngredientQuantityModelAssembler assembler;

    IngredientQuantityController(IngredientQuantityRepository repo, IngredientQuantityModelAssembler assembler,
            IngredientRepository ingrRepo, UnitRepository unitRepo) {
        this.repo = repo;
        this.assembler = assembler;
        this.ingrRepo = ingrRepo;
        this.unitRepo = unitRepo;
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
        Ingredient ingr = ingrRepo.findByPublicId(newIngredientQuantityData.getIngredient())
                .or(() -> ingrRepo.findByName(newIngredientQuantityData.getIngredient()))
                .orElseThrow(() -> generateNotFoundException("Ingredient"));
        Unit ut = unitRepo.findByPublicId(newIngredientQuantityData.getUnit())
                .or(() -> unitRepo.findByUnit(newIngredientQuantityData.getUnit()))
                .orElseThrow(() -> generateNotFoundException("Unit"));
        IngredientQuantity rec = repo.findByPublicId(id)
                .map(
                        recipe -> {
                            recipe.setIngredient(ingr);
                            recipe.setQuantity(newIngredientQuantityData.getQuantity());
                            recipe.setUnit(ut);
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

    private NotFoundException generateNotFoundException(String entity) {
        return new NotFoundException(entity);
    }
}