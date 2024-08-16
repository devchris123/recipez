package com.cwunder.recipe.recipeinstruction;

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