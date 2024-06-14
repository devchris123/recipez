package com.cwunder.recipe.recipeinstruction;

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
@RequestMapping("/recipeinstructions")
public class RecipeInstructionController {

    private final RecipeInstructionRepository repo;
    private final RecipeInstructionModelAssembler assembler;

    RecipeInstructionController(RecipeInstructionRepository repo, RecipeInstructionModelAssembler assembler) {
        this.repo = repo;
        this.assembler = assembler;
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE,
            MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE })
    public @ResponseBody CollectionModel<EntityModel<RecipeInstruction>> listRecipeInstructions() {
        List<EntityModel<RecipeInstruction>> recs = repo.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(recs,
                linkTo(methodOn(RecipeInstructionController.class).listRecipeInstructions()).withSelfRel());
    }

    @GetMapping("/{id}")
    public @ResponseBody EntityModel<RecipeInstruction> getRecipeInstruction(@PathVariable String id) {
        RecipeInstruction rec = repo.findByPublicId(id).orElseThrow(this::generateNotFoundException);
        return assembler.toModel(rec);
    }

    @PutMapping("/{id}")
    public @ResponseBody ResponseEntity<?> updateRecipeInstruction(
            @Valid @RequestBody RecipeInstruction newRecipeInstruction, @PathVariable String id) {
        RecipeInstruction rec = repo.findByPublicId(id)
                .map(
                        recInstr -> {
                            recInstr.setDescription(newRecipeInstruction.getDescription());
                            recInstr.setRecipe(newRecipeInstruction.getRecipe());
                            return repo.save(recInstr);
                        })
                .orElseThrow(this::generateNotFoundException);
        return ResponseEntity.ok().body(assembler.toModel(rec));
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<?> deleteRecipeInstruction(@PathVariable String id) {
        repo.deleteByPublicId(id);
        return ResponseEntity.noContent().build();
    }

    private NotFoundException generateNotFoundException() {
        return new NotFoundException("RecipeInstruction");
    }
}