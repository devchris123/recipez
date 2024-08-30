package com.cwunder.recipe.recipe;

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
import com.cwunder.recipe.ingredientquantity.IngredientQuantity;
import com.cwunder.recipe.ingredientquantity.IngredientQuantityModelAssembler;
import com.cwunder.recipe.ingredientquantity.IngredientQuantityRepository;
import com.cwunder.recipe.ingredientquantity.IngredientQuantityWrite;
import com.cwunder.recipe.recipeinstruction.RecipeInstruction;
import com.cwunder.recipe.recipeinstruction.RecipeInstructionModelAssembler;
import com.cwunder.recipe.recipeinstruction.RecipeInstructionRepository;
import com.cwunder.recipe.recipeinstruction.RecipeInstructionWrite;
import com.cwunder.recipe.user.User;
import com.cwunder.recipe.user.UserRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeRepository repo;
    private final RecipeModelAssembler assembler;

    private final IngredientQuantityRepository ingrQRepo;
    private final IngredientQuantityModelAssembler ingrQAssembler;

    private final RecipeInstructionRepository recInstrRepo;
    private final RecipeInstructionModelAssembler recInstrAssembler;

    private final UserRepository userRepo;

    RecipeController(RecipeRepository repo, RecipeModelAssembler assembler, IngredientQuantityRepository ingrQRepo,
            IngredientQuantityModelAssembler ingrQAssembler, RecipeInstructionRepository recInstrRepo,
            RecipeInstructionModelAssembler recInstrAssembler,
            UserRepository userRepo) {
        this.repo = repo;
        this.assembler = assembler;
        this.ingrQRepo = ingrQRepo;
        this.ingrQAssembler = ingrQAssembler;
        this.recInstrRepo = recInstrRepo;
        this.recInstrAssembler = recInstrAssembler;
        this.userRepo = userRepo;
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE,
            MediaTypes.HAL_FORMS_JSON_VALUE,
            MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE
    })

    public @ResponseBody CollectionModel<EntityModel<Recipe>> listRecipes() {
        List<EntityModel<Recipe>> recs = repo.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(recs, linkTo(methodOn(RecipeController.class).listRecipes()).withSelfRel());
    }

    @GetMapping()
    String listRecipesView() {
        return "recipes";
    }

    @PostMapping()
    public @ResponseBody ResponseEntity<?> createRecipe(@Valid @RequestBody Recipe newRecipe) {
        User user = userRepo.findByUsernameAuth(newRecipe.getUsername())
                .orElseThrow(this::generateUserNotFoundException);
        newRecipe.setUser(user);
        var newRec = repo.save(newRecipe);
        newRec = repo.getByPublicId(newRec.getPublicId());
        EntityModel<Recipe> rec = assembler.toModel(newRec);
        return ResponseEntity.created(rec.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(rec);
    }

    @GetMapping("/{id}")
    public @ResponseBody EntityModel<Recipe> getRecipe(@PathVariable String id) {
        Recipe rec = repo.findByPublicId(id).orElseThrow(this::generateNotFoundException);
        return assembler.toModel(rec);
    }

    @PutMapping("/{id}")
    public @ResponseBody ResponseEntity<?> updateRecipe(@Valid @RequestBody Recipe newRecipe, @PathVariable String id) {
        User user = userRepo.findByUsernameAuth(newRecipe.getUsername())
                .orElseThrow(this::generateUserNotFoundException);
        Recipe rec = repo.findByPublicId(id)
                .map(
                        recipe -> {
                            recipe.setName(newRecipe.getName());
                            recipe.setDescription(newRecipe.getDescription());
                            recipe.setUsername(user.getUsername());
                            return repo.save(recipe);
                        })
                .orElseThrow(this::generateNotFoundException);
        return ResponseEntity.ok().body(assembler.toModel(rec));
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<?> deleteRecipe(@PathVariable String id) {
        repo.deleteByPublicId(id);
        return ResponseEntity.noContent().build();
    }

    // Subresources accessible through this route
    @GetMapping("/{id}/ingredientquantities")
    public @ResponseBody CollectionModel<EntityModel<IngredientQuantity>> listIngredientQuantities(
            @PathVariable String id) {
        List<EntityModel<IngredientQuantity>> recs = ingrQRepo.findByRecipePublicId(id).stream()
                .map(ingrQAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(recs,
                linkTo(methodOn(RecipeController.class).listIngredientQuantities(id)).withSelfRel());
    }

    @GetMapping("/{id}/recipeinstructions")
    public @ResponseBody CollectionModel<EntityModel<RecipeInstruction>> listRecipeInstructions(
            @PathVariable String id) {
        List<EntityModel<RecipeInstruction>> recs = recInstrRepo.findByRecipePublicId(id).stream()
                .map(recInstrAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(recs,
                linkTo(methodOn(RecipeController.class).listRecipeInstructions(id)).withSelfRel());
    }

    @PostMapping("/{id}/ingredientquantities")
    public @ResponseBody ResponseEntity<?> createIngredientQuantity(
            @Valid @RequestBody IngredientQuantityWrite newIngredientQuantityData, @PathVariable String id) {
        Recipe rec = repo.findByPublicId(id).orElseThrow(this::generateNotFoundException);
        var newIngrQuant = new IngredientQuantity();
        newIngrQuant.setQuantity(newIngredientQuantityData.getQuantity());
        newIngrQuant.setIngredient(newIngredientQuantityData.getIngredient());
        newIngrQuant.setRecipe(rec);
        newIngrQuant.setUnit(newIngredientQuantityData.getUnit());
        EntityModel<IngredientQuantity> ingrQ = ingrQAssembler.toModel(ingrQRepo.save(newIngrQuant));
        return ResponseEntity.created(ingrQ.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(ingrQ);
    }

    @PostMapping("/{id}/recipeinstructions")
    public @ResponseBody ResponseEntity<?> createRecipeInstruction(
            @Valid @RequestBody RecipeInstructionWrite newRecipeInstructionData, @PathVariable String id) {
        var rec = repo.findByPublicId(id)
                .orElseThrow(this::generateNotFoundException);
        var newRecInstr = new RecipeInstruction();
        newRecInstr.setDescription(newRecipeInstructionData.getDescription());
        newRecInstr.setRecipe(rec);
        EntityModel<RecipeInstruction> recInstrEM = recInstrAssembler.toModel(recInstrRepo.save(newRecInstr));
        return ResponseEntity.created(recInstrEM.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(recInstrEM);
    }

    private NotFoundException generateNotFoundException() {
        return new NotFoundException("Recipe");
    }

    private NotFoundException generateUserNotFoundException() {
        return new NotFoundException("User");
    }
}
